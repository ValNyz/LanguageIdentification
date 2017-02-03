package profileGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import grammaticalWords.GrammaticalWordsMap;
import reader_writer.Reader;
import treeProfileGenerator.TreeNGramProfileGenerator;

public class ProfileGenerator {	
	private String pathFichierRead;
	private String pathFichierWrite;
	String langue;
	
	private List<GrammaticalWordsMap> listGramWords;
	private boolean withGramWords;
	
	private float learningPercentSize = 1f;
	private int sizeFichierRead;

	private boolean bNGram;
	private int initNGram;
	private int finalNGram;
	private List<NGramProfileGenerator> listNGramPG = new ArrayList<NGramProfileGenerator>();
	/*private NGramProfileGenerator m2GramPG;
	private NGramProfileGenerator m3GramPG;
	private NGramProfileGenerator m4GramPG;*/
	private List<TreeNGramProfileGenerator> listTreeGramPG = new ArrayList<TreeNGramProfileGenerator>();
	/*private TreeNGramProfileGenerator m2TreeGramPG;
	private TreeNGramProfileGenerator m3TreeGramPG;
	private TreeNGramProfileGenerator m4TreeGramPG;*/
	private boolean bPPM;
	private PPMProfileGenerator mPPMPG;
	private boolean bPrefixeSuffixe;
	private PrefixePPMProfileGenerator mPrefixePPMPG;
	private SuffixePPMProfileGenerator mSuffixePPMPG;
	
	private Reader reader;
		
	boolean traitementMot = true;

	public ProfileGenerator(String pathFichierRead, String pathFichierWrite, String langue, boolean bNGram, boolean bPPM, boolean bPrefixeSuffixe) {
		new ProfileGenerator(pathFichierRead, pathFichierWrite, langue, bNGram, 2, 4, bPPM, bPrefixeSuffixe, true, 1f);
	}
	
	public ProfileGenerator(String pathFichierRead, String pathFichierWrite, String langue, boolean bNGram, int initNGram, int finalNGram, boolean bPPM, boolean bPrefixeSuffixe, boolean withGramWords, float learningPercentSize) {
		this.pathFichierRead = pathFichierRead;
		this.pathFichierWrite = pathFichierWrite;
		this.langue = langue;
		
		this.bNGram = bNGram;
		this.bPPM = bPPM;
		this.bPrefixeSuffixe = bPrefixeSuffixe;
		
		if (!this.pathFichierRead.isEmpty()) {
			reader = new Reader(pathFichierRead, true);
			reader.open();
			sizeFichierRead = reader.size();
		}
		
		this.withGramWords = withGramWords;
		if (!withGramWords) {
			listGramWords = new ArrayList<GrammaticalWordsMap>();
			loadGramWords();
		}
		
		this.learningPercentSize = learningPercentSize;
		
		this.initNGram = initNGram;
		this.finalNGram = finalNGram;
		if (this.bNGram) {
			for (int i=this.initNGram;i<=this.finalNGram;i++) {
				listNGramPG.add(new NGramProfileGenerator(langue,i,true));
			}
			/*m2GramPG = new NGramProfileGenerator(langue,2,true);
			m3GramPG = new NGramProfileGenerator(langue,3,true);
			m4GramPG = new NGramProfileGenerator(langue,4,true);*/
		}
		if (this.bPPM)
			mPPMPG = new PPMProfileGenerator(langue, 5);
		if (this.bPrefixeSuffixe) {
			mPrefixePPMPG = new PrefixePPMProfileGenerator(langue, 4);
			mSuffixePPMPG = new SuffixePPMProfileGenerator(langue, 4);
		}
	}
	
	private void loadGramWords() {
		File file = new File(pathFichierWrite);
    	File[] files = file.listFiles();
	    for (int k=0;k<files.length;k++) {
            if (files[k].isDirectory()) {
            	File file2 = new File(files[k].getAbsolutePath() + "\\GramWords\\" + files[k].getName() + "_Grammatical_Words.txt");
            	if (file2.exists())
            		listGramWords.add(new GrammaticalWordsMap(langue, file2.getAbsolutePath()));
            }
	    }
	}
	
	public void generateProfile() {
		if (reader != null) {
			int endSize = (int)(learningPercentSize*sizeFichierRead);
			String text = reader.read();
			int readSize = text.length();
	        while (text != null && readSize < endSize)
	        {
	        	if (!withGramWords) {
	    			Iterator<GrammaticalWordsMap> itGramWords = listGramWords.iterator();
	    			while(itGramWords.hasNext()) {
	    				GrammaticalWordsMap gramWords = itGramWords.next();
	    				text = gramWords.detectAndRemoveGramWords(text);
	    			}
	    		}
    			//generateProfile par méthode
	        	if (bNGram)
	        		generateNGramProfile(text);
	        	if (bPPM)
	        		generatePPMProfile(text);
	        	if (bPrefixeSuffixe)
	        		generatePrefixeSuffixePPMProfile(text);
	        	
	        	text = reader.read();
	        	if (text != null)
	        		readSize +=	text.length();
	        }
	        if (bPPM)
	        	mPPMPG.cleanTree();
	        if (bPrefixeSuffixe) {
				mPrefixePPMPG.cleanTree();
				mSuffixePPMPG.cleanTree();
			}
	        writeProfile();
		}		
	}
	
	public void generateNGramProfile(String sentence) {
		for (int i=0;i<listNGramPG.size();i++) {
			listNGramPG.get(i).generateProfile(sentence);
		}
		/*m2GramPG.generateProfile(sentence);
		m3GramPG.generateProfile(sentence);
		m4GramPG.generateProfile(sentence);*/
	}

	public void generatePPMProfile(String sentence) {
		mPPMPG.generateProfile(sentence);
	}

	public void generatePrefixeSuffixePPMProfile(String sentence) {
		mPrefixePPMPG.generateProfile(sentence);
		mSuffixePPMPG.generateProfile(sentence);
	}
	
	public void writeProfile() {
		if (bNGram) {
			String path = pathFichierWrite + "\\" + langue + "\\NGram\\" + ((withGramWords) ? "Avec GramWords\\" : "Sans GramWords\\") + learningPercentSize + "\\" + langue;
			for (int i=0;i<listNGramPG.size();i++) {
				listNGramPG.get(i).writeProfile(path + "_" + (i+initNGram) + "Gram.txt", false);
				listTreeGramPG.add(new TreeNGramProfileGenerator(path + "_" + (i+initNGram) + "Gram.txt", path + "_" + (i+initNGram) + "TreeGram.txt", langue,(i+initNGram),true));
				listTreeGramPG.get(i).generateProfile();
				listTreeGramPG.get(i).writeProfile();
			}
			/*m2GramPG.writeProfile(path + "_2Gram.txt", false);
			m3GramPG.writeProfile(path + "_3Gram.txt", false);
			m4GramPG.writeProfile(path + "_4Gram.txt", false);
			m2TreeGramPG = new TreeNGramProfileGenerator(path + "_2Gram.txt", path + "_2TreeGram.txt", langue,2,true);
			m3TreeGramPG = new TreeNGramProfileGenerator(path + "_3Gram.txt", path + "_3TreeGram.txt", langue,3,true);
			m4TreeGramPG = new TreeNGramProfileGenerator(path + "_4Gram.txt", path + "_4TreeGram.txt", langue,4,true);
			m2TreeGramPG.generateProfile();
			m3TreeGramPG.generateProfile();
			m4TreeGramPG.generateProfile();
			m2TreeGramPG.writeProfile();
			m3TreeGramPG.writeProfile();
			m4TreeGramPG.writeProfile();*/
		}
		if (bPPM)
			mPPMPG.writeProfile(pathFichierWrite + "\\" + langue + "\\PPM\\" + ((withGramWords) ? "Avec GramWords\\" : "Sans GramWords\\") + learningPercentSize + "\\" + langue + "_PPM.txt");
		if (bPrefixeSuffixe) {
			mPrefixePPMPG.writeProfile(pathFichierWrite + "\\" + langue + "\\PPM\\" + langue + "_PrefixePPM.txt");
			mSuffixePPMPG.writeProfile(pathFichierWrite + "\\" + langue + "\\PPM\\" + langue + "_SuffixePPM.txt");
		}
	}
}
