package language;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import grammaticalWords.GrammaticalWordsMap;
import optimize.parameter.Constant.TypeClassifier;
import profileGenerator.PPMProfileDistanceMeasurer;
import profileGenerator.PrefixePPMProfileDistanceMeasurer;
import profileGenerator.SuffixePPMProfileDistanceMeasurer;
import tree.RootNodePPM;
import treeProfileGenerator.TreeNGramProfileDistanceMeasurer;

public class Language {
	public final static String fileSep = System.getProperty("file.separator");
	
	private double[] scoreNGram;
	private int tailleEchantillonNGram;
	private List<TreeNGramProfileDistanceMeasurer> listLearningProfile = new ArrayList<TreeNGramProfileDistanceMeasurer>();
	private int scoreGramWord;
	private GrammaticalWordsMap gramWordsMap;
	private double scorePPM;
	private PPMProfileDistanceMeasurer profilePPM;
	private double scorePrefixePPM;
	private RootNodePPM prefixeProfilePPM;
	private double scoreSuffixePPM;
	private RootNodePPM suffixeProfilePPM;
	
	private String language;
	private String languageDirectoryPath;
	
	private String pourcentageCorpus = "0.005";
	private boolean bGramWords;
	private boolean bNGram;
	
	private boolean withExclusion = true;
	
	private boolean affichage;
	
	public Language(String language, String languageDirectoryPath, boolean bNGram, int debutNGram, int finalNGram, boolean bGramWords, boolean bPPM, boolean bPrefixeSuffixe, int tailleEchantillonNGram, boolean affichage) {
		this.language = language;
		this.languageDirectoryPath = languageDirectoryPath;
		this.bGramWords = bGramWords;
		this.bNGram = bNGram;
		if(bGramWords)
			loadGramWords();
		this.tailleEchantillonNGram = tailleEchantillonNGram;
		if (bNGram)
			loadNGramProfile(debutNGram, finalNGram);
		scoreGramWord = -1;
		scorePPM = -1;
		if (bPPM)
			loadPPMProfile();
		if (bPrefixeSuffixe)
			loadPrefixeSuffixePPMProfile();
		this.affichage = affichage;
	}
	
	private void loadNGramProfile(int debutNGram, int finalNGram) {
		File file = new File(this.languageDirectoryPath + fileSep + "NGram" + fileSep +((bGramWords) ? "Sans GramWords" + fileSep : "Avec GramWords" + fileSep) + pourcentageCorpus);
	    if (file.exists()) {
	    	File[] files = file.listFiles();
	        for (int i=0;i<files.length;i++) {
	            if (!files[i].isDirectory()) {
	            	String nGram = files[i].getName().split("_")[1];
	            	String learningNGram = nGram.substring(0, 1);
	            	String tree = nGram.substring(1);
					if (tree.equals("TreeGram.txt") && Integer.parseInt(learningNGram)>=debutNGram && Integer.parseInt(learningNGram) <= finalNGram) {
						TreeNGramProfileDistanceMeasurer nGramPDM = new TreeNGramProfileDistanceMeasurer(files[i].getAbsolutePath());
						listLearningProfile.add(nGramPDM);
					}
	            }
	        }
	    }
	}
	
	private void loadGramWords() {
		File file = new File(languageDirectoryPath + fileSep + "GramWords" + fileSep + language + "_Grammatical_Words.txt");
	    if (file.exists()) {
	    	gramWordsMap = new GrammaticalWordsMap(language, languageDirectoryPath + fileSep + "GramWords" + fileSep + language + "_Grammatical_Words.txt");
	    }
	}
	
	private void loadPPMProfile() {
		File file = new File(languageDirectoryPath + fileSep + "PPM" + fileSep +((bGramWords && !bNGram) ? "Sans GramWords" + fileSep : "Avec GramWords" + fileSep) + pourcentageCorpus + fileSep + ""+ language + "_PPM.txt");
	    if (file.exists()) {
	    	profilePPM = new PPMProfileDistanceMeasurer(file.getAbsolutePath());
	    }
	}
	
	private void loadPrefixeSuffixePPMProfile() {
		File file = new File(languageDirectoryPath + fileSep + "PPM" + fileSep + language + "_PrefixePPM.txt");
	    if (file.exists()) {
	    	PrefixePPMProfileDistanceMeasurer pPrefixePMPDM = new PrefixePPMProfileDistanceMeasurer(file.getAbsolutePath());
	    	prefixeProfilePPM = pPrefixePMPDM.readLearningProfile();
	    }
	    file = new File(languageDirectoryPath + fileSep + "PPM" + fileSep + language + "_SuffixePPM.txt");
	    if (file.exists()) {
	    	SuffixePPMProfileDistanceMeasurer pSuffixePMPDM = new SuffixePPMProfileDistanceMeasurer(file.getAbsolutePath());
	    	suffixeProfilePPM = pSuffixePMPDM.readLearningProfile();
	    }
	}
	
	public double[] testNGram(String sentence, TypeClassifier typeClassifier) {
		scoreNGram = new double[listLearningProfile.size()];
		if (typeClassifier != TypeClassifier.OUT_OF_PLACE) {
			for (int i=0;i<listLearningProfile.size();i++)
				scoreNGram[i] = 1;
		}
    	String result = "";
		for (int i=0;i<listLearningProfile.size();i++) { //Boucle sur les NGrams
			String[] words = sentence.split(" ");
			for (int j=0;j<words.length;j++) {
				if (typeClassifier == TypeClassifier.OUT_OF_PLACE)
					scoreNGram[i] += listLearningProfile.get(i).calculDistanceOOP(words[j], tailleEchantillonNGram);
				else if (typeClassifier == TypeClassifier.BAYES) /** Méthode Naive Bayes : posterieur = anterieur*(vraisemblance/évidence)	*/
					scoreNGram[i] *= listLearningProfile.get(i).calculDistanceNaiveBayes(words[j]);
				else
					scoreNGram[i] *= (1/listLearningProfile.size())*listLearningProfile.get(i).calculDistanceNaiveBayes(words[j]);
			}
			result += language + " " + (i+2) + "Gram : " + scoreNGram[i] + "\r\n";
		}
		/*if (affichage)
			System.out.println(result);*/
		return scoreNGram;
	}
	
	public void testGramWords(String sentence) {
		if (gramWordsMap != null) {
			gramWordsMap.detectGramWords(sentence);
			scoreGramWord += gramWordsMap.getScoreGramWords();
		}
	}
	
	public String removeGramWords(String sentence) {
		if (gramWordsMap != null) {
			sentence = gramWordsMap.removeGramWords(sentence);
		}
		return sentence;
	}
	
	public double testPPM(String sentence) {
		scorePPM = 0;
		String[] tabWords = sentence.split(" ");
		for (int i=0;i<tabWords.length;i++) {
			scorePPM += profilePPM.calculCompressionRatio(tabWords[i], withExclusion);
		}
		return scorePPM;
	}
	
	public double testPrefixePPM(String sentence) {
		scorePrefixePPM = 0;
		String[] tabWords = sentence.split(" ");
		for (int i=0;i<tabWords.length;i++) {
			PrefixePPMProfileDistanceMeasurer pPrefixePMPDM = new PrefixePPMProfileDistanceMeasurer(prefixeProfilePPM);
			scorePrefixePPM += pPrefixePMPDM.calculCompressionRatio(tabWords[i], withExclusion);
		}
		/*if (affichage)
			System.out.println(language + " PrefixePPM : " + scorePrefixePPM + "\r\n");*/
		return scorePrefixePPM;
	}

	public double testSuffixePPM(String sentence) {
		scoreSuffixePPM = 0;
		String[] tabWords = sentence.split(" ");
		for (int i=0;i<tabWords.length;i++) {
			SuffixePPMProfileDistanceMeasurer pSuffixePMPDM = new SuffixePPMProfileDistanceMeasurer(suffixeProfilePPM);
			scoreSuffixePPM += pSuffixePMPDM.calculCompressionRatio(tabWords[i], withExclusion);
		}
		/*if (affichage)
			System.out.println(language + " SuffixePPM : " + scoreSuffixePPM + "\r\n");*/
		return scoreSuffixePPM;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getScoreGramWord() {
		return scoreGramWord;
	}

	public void setScoreGramWord(int scoreGramWord) {
		this.scoreGramWord = scoreGramWord;
	}

	public GrammaticalWordsMap getGramWordsMap() {
		return gramWordsMap;
	}
	
	public void resetScore() {
		this.scoreGramWord = 0;
		//this.scoreNGram = new double[scoreNGram.length];
	}

	public double[] getScoreNGram() {
		return scoreNGram;
	}

	public double getScorePPM() {
		return scorePPM;
	}

	public double getScorePrefixePPM() {
		return  scorePrefixePPM;
	}

	public double getScoreSuffixePPM() {
		return scoreSuffixePPM;
	}
}
