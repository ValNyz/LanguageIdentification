package main;

import languageDetector.LanguageDetector;
import optimize.parameter.Constant.TypeClassifier;
import reader_writer.Writer;

public class Test {

	public static void main(String[] args) {
		String language = "Italian";
		int nbCharacter = 5;
		boolean bNGram = true;
		int debutNGram = 2;
		int finalNGram = 5;
		TypeClassifier typeClassifier = TypeClassifier.BAYES;
		boolean bGramWords = true;
		int paramGramWords = 1;
		boolean bPPM = true;
		double paramPPM = 7.5;
		double limite = 0.25;
		boolean bPPMChoosingMethode = true;
					
		String result = "";
		
		String languagesDirectoryPath = "G:\\Altran\\LanguageDetector\\Language";
		String testFichierPath = "G:\\Altran\\Corpus de test\\" + language + "\\Test_" + nbCharacter + "characters.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\TestEn_2words.txt";//Proverbe_Anglais_Français.txt";//"C:\\Users\\vnyzam\\Downloads\\fr-en\\europarl-v7.it.it";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//
		LanguageDetector ld = new LanguageDetector(nbCharacter, languagesDirectoryPath, testFichierPath, true, bNGram, debutNGram, finalNGram, typeClassifier, bGramWords, paramGramWords, bPPM, paramPPM, limite, bPPMChoosingMethode);
		
		long debut = System.currentTimeMillis();
		result += ld.detectLanguage(true, language);
		System.out.println(System.currentTimeMillis()-debut);
		
		/*for (int i = nbCharacter; i <18; i++) {
			String languagesDirectoryPath = "G:\\Altran\\LanguageDetector\\Language";
			String testFichierPath = "G:\\Altran\\Corpus de test\\" + language + "\\Test_" + i + "characters.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\TestEn_2words.txt";//Proverbe_Anglais_Français.txt";//"C:\\Users\\vnyzam\\Downloads\\fr-en\\europarl-v7.it.it";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//
			long debut = System.currentTimeMillis();
			//LanguageDetector ld = new LanguageDetector(languagesDirectoryPath, true, true, true, false, "- Étude de l'état de l'art");
			//LanguageDetector ld = new LanguageDetector(languagesDirectoryPath, false, 4, false, true, false, "de et s'élança hors");
			LanguageDetector ld = new LanguageDetector(i, languagesDirectoryPath, testFichierPath, true, Ngram, 4, false, true, false);
			System.out.println(System.currentTimeMillis()-debut);
			debut = System.currentTimeMillis();
			result += ld.detectLanguage(true, language);
		}*/
		
		System.out.println("\r\n" + result.replace(".", ","));
		//result = ld2.detectLanguage();
		Writer writer = new Writer("G:\\Altran\\result.txt");
		writer.open();
		writer.write(result);
		writer.close();
		/*/ProfileGenerator pg = new ProfileGenerator(testFichierPath1, languagesDirectoryPath, "Dutch", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg1 = new ProfileGenerator(testFichierPath3, languagesDirectoryPath, "Finnish", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg1.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg3 = new ProfileGenerator(testFichierPath2, languagesDirectoryPath, "Italian", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg3.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg4 = new ProfileGenerator(testFichierPath4, languagesDirectoryPath, "Portuguese", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg4.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg5 = new ProfileGenerator(testFichierPath5, languagesDirectoryPath, "Spanish", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg5.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg6 = new ProfileGenerator(testFichierPath6, languagesDirectoryPath, "English", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg6.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		ProfileGenerator pg7 = new ProfileGenerator(testFichierPath7, languagesDirectoryPath, "French", true, 2, 6, false, false, true, pourcentageApprentissage);
		System.out.println(System.currentTimeMillis()-debut);
		debut = System.currentTimeMillis();
		pg7.generateProfile();
		System.out.println(System.currentTimeMillis()-debut);
		/*String langue = "Portuguese";
		String path = languagesDirectoryPath + "\\" + langue + "\\NGram\\" + langue;
		TreeNGramProfileGenerator m2TreeGramPG = new TreeNGramProfileGenerator(path + "_2Gram.txt", path + "_2TreeGram.txt", langue,2,true);
		TreeNGramProfileGenerator m3TreeGramPG = new TreeNGramProfileGenerator(path + "_3Gram.txt", path + "_3TreeGram.txt", langue,3,true);
		TreeNGramProfileGenerator m4TreeGramPG = new TreeNGramProfileGenerator(path + "_4Gram.txt", path + "_4TreeGram.txt", langue,4,true);
		m2TreeGramPG.generateProfile();
		m3TreeGramPG.generateProfile();
		m4TreeGramPG.generateProfile();
		m2TreeGramPG.writeProfile();
		m3TreeGramPG.writeProfile();
		m4TreeGramPG.writeProfile();*/
	}
}