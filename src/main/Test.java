package main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import languageDetector.LanguageDetector;
import languageDetector.LanguageDetector.LDParameter;
import optimize.AlgoGenetique;
import optimize.parameter.ADN;
import optimize.parameter.Parameter;

public class Test {
	public static void main(String[] args) throws Exception {
		String language = "Italian";
		List<String> param = parseArgs(args);
		int nbCharacter = Integer.parseInt(param.get(0));
		String languagesDirectoryPath = param.get(2);
		String testFichierPath = param.get(1) + "TestFullLanguage_" + nbCharacter + "characters.txt";
		
		
		int debutNGram = 2;
		int finalNGram = 5;
		double paramPPM = 20;
		double limite = 0.5;
		boolean bPPMChoosingMethode = true;
					
		String result = "";
		
		//String languagesDirectoryPath = "..\\LanguageIdentificationData\\Language";
		//String testFichierPath = "..\\LanguageIdentificationData\\Corpus de test\\FullLanguage_SoloWord\\" /*+ language + "\\Test_"*/ + "TestFullLanguage_" + nbCharacter + "characters.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\TestEn_2words.txt";//Proverbe_Anglais_Fran�ais.txt";//"C:\\Users\\vnyzam\\Downloads\\fr-en\\europarl-v7.it.it";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//
		LanguageDetector ld = new LanguageDetector();
		ADN adn = new ADN(LanguageDetector.supportADN);
		Parameter<Double> p2 = new Parameter<Double>(LDParameter.Limite.getName(), limite);
		p2.setMinValue(0.0);
		p2.setMaxValue(5.0);
		adn.putParameter(p2);
		adn.putParameter(new Parameter<Boolean>(LDParameter.PPMChoosingMethode.getName(), bPPMChoosingMethode));
		Parameter<Integer> p3 = new Parameter<Integer>(LDParameter.NGramDebut.getName(), debutNGram);
		p3.setMinValue(1);
		p3.setMaxValue(5);
		adn.putParameter(p3);
		Parameter<Integer> p4 = new Parameter<Integer>(LDParameter.NGramFin.getName(), finalNGram);
		p4.setMinValue(1);
		p4.setMaxValue(5);
		adn.putParameter(p4);
		Parameter<Double> p5 = new Parameter<Double>(LDParameter.ParamPPM.getName(), paramPPM);
		p5.setMinValue(0.1);
		p5.setMaxValue(20.0);
		adn.putParameter(p5);
		ld.initConstructor(nbCharacter, languagesDirectoryPath, testFichierPath, true, adn);
		ld.setTest(true);
		
		AlgoGenetique ag = new AlgoGenetique(0.1, 0.25, 0.2, 100, 10000, 0.999, ld);
		ag.init();
		ag.run();
		/*long debut = System.currentTimeMillis();
		result += ld.detectLanguage(true, language);
		System.out.println(System.currentTimeMillis()-debut);*/
		
		/*for (int i = nbCharacter; i <18; i++) {
			String languagesDirectoryPath = "G:\\Altran\\LanguageDetector\\Language";
			String testFichierPath = "G:\\Altran\\Corpus de test\\" + language + "\\Test_" + i + "characters.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\TestEn_2words.txt";//Proverbe_Anglais_Fran�ais.txt";//"C:\\Users\\vnyzam\\Downloads\\fr-en\\europarl-v7.it.it";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//"C:\\Users\\vnyzam\\Downloads\\TestNGRam\\Test.txt";//
			long debut = System.currentTimeMillis();
			//LanguageDetector ld = new LanguageDetector(languagesDirectoryPath, true, true, true, false, "- �tude de l'�tat de l'art");
			//LanguageDetector ld = new LanguageDetector(languagesDirectoryPath, false, 4, false, true, false, "de et s'�lan�a hors");
			LanguageDetector ld = new LanguageDetector(i, languagesDirectoryPath, testFichierPath, true, Ngram, 4, false, true, false);
			System.out.println(System.currentTimeMillis()-debut);
			debut = System.currentTimeMillis();
			result += ld.detectLanguage(true, language);
		}*/
		
		//System.out.println("\r\n" + result.replace(".", ","));
		//result = ld2.detectLanguage();
		try {
			FileWriter writer = new FileWriter("result.txt",true);
			writer.write(ag.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	private static List<String> parseArgs(String[] args) throws Exception{
		if (args.length == 0){
			throw new NullPointerException("No argument !");
		}
		List<String> listReturn = new ArrayList<String>();
		List<String> listArg = new ArrayList<String>();
		for (String str : args)
			listArg.add(str);
		
		while (listArg.size() != 0) {
			if (listArg.contains("-nbc")) {
				listReturn.add(listArg.get(listArg.indexOf("-nbc")+1));
				listArg.remove(listArg.indexOf("-nbc")+1);
				listArg.remove(listArg.indexOf("-nbc"));
			} else if (listArg.contains("-test_path")) {
				listReturn.add(listArg.get(listArg.indexOf("-test_path")+1));
				listArg.remove(listArg.indexOf("-test_path")+1);
				listArg.remove(listArg.indexOf("-test_path"));
			} else if (listArg.contains("-language_path")) {
				listReturn.add(listArg.get(listArg.indexOf("-language_path")+1));
				listArg.remove(listArg.indexOf("-language_path")+1);
				listArg.remove(listArg.indexOf("-language_path"));
			} else
				throw new NullPointerException("Out of valid parameter !");
		}
		return listReturn;
	}
}