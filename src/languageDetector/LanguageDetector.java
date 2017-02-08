package languageDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import language.Language;
import optimize.Optimize;
import optimize.parameter.ADN;
import optimize.parameter.Constant.TypeClassifier;
import optimize.parameter.Parameter;
import reader_writer.Reader;
import sentenceSplitter.SentenceSplitter;
import tools.TupleDouble;

public class LanguageDetector extends Optimize {
	private boolean isTest = false;

	static {
		supportADN = new HashMap<String, Class<?>>();
		//supportADN.put("NGram", Boolean.class);
		//supportADN.put("GramWords", Boolean.class);
		//supportADN.put("PPM", Boolean.class);
		//supportADN.put("ParamGramWords", Integer.class);
		supportADN.put("Limite", Double.class);
		supportADN.put("PPMChoosingMethode", Boolean.class);
		supportADN.put("NGramDebut", Integer.class);
		supportADN.put("NGramFin", Integer.class);
		supportADN.put("ParamPPM", Double.class);
		//supportADN.put("TypeClassifier", TypeClassifier.class);
	}

	public static enum LDParameter {
		//NGram("NGram"),
		//GramWords("GramWords"),
		//PPM("PPM"),
		//ParamGramWords("ParamGramWords"),
		Limite("Limite"),
		PPMChoosingMethode("PPMChoosingMethode"),
		NGramDebut("NGramDebut"),
		NGramFin("NGramFin"),
		ParamPPM("ParamPPM");//,
		//TypeClassifier("TypeClassifier");

		private String name;

		private LDParameter(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private String languagesDirectoryPath;

	private String testText;
	private List<String> listSentence;
	private List<Language> listLanguage = new ArrayList<Language>();
	private List<Map<String, Integer>> listTestProfile = new ArrayList<Map<String, Integer>>();
	private static boolean bPrefixeSuffixe = false;
	boolean affichage = false;
	String result = "";
	/*
	 * Paramètres
	 */
	private boolean bNGram = true;
	private boolean bGramWords = false;
	private boolean bPPM = true;
	private int paramGramWords = 1; // nbPoint par gramWord
	// private double limite = 0.25d; // limite indétermination PPM
	// private boolean bPPMChoosingMethode = false; //base méthode PPM = d'abord
	// PPM si en dessous limite alors NGram
	// private int debutNGram = 2; //Plus basse valeur de NGram
	// private int finalNGram;
	// private double paramPPM = 4.5; // pondération PPM
	int tailleEchantillonNGram = 700;
	private TypeClassifier typeClassifier = TypeClassifier.BAYES; // Type classificateur NGram : 1 = Naïve
									// Bayes, 0 = Out of Place

	public LanguageDetector() {
	}
	
	public LanguageDetector(int nbCharacter, String languagesDirectoryPath, boolean bNGram, int debutNGram,
			int finalNGram, TypeClassifier typeClassifier, boolean bGramWords, int paramGramWords, boolean bPPM,
			double paramPPM, double limite, boolean bPPMChoosingMethode, String testText) {
		super();
		//adn.putParameter(new Parameter<Boolean>(LDParameter.NGram.getName(), bNGram));
		//adn.putParameter(new Parameter<Boolean>(LDParameter.GramWords.getName(), bGramWords));
		//adn.putParameter(new Parameter<Boolean>(LDParameter.PPM.getName(), bPPM));
		//adn.putParameter(new Parameter<Integer>(LDParameter.ParamGramWords.getName(), paramGramWords));
		adn.putParameter(new Parameter<Double>(LDParameter.Limite.getName(), limite));
		adn.putParameter(new Parameter<Boolean>(LDParameter.PPMChoosingMethode.getName(), bPPMChoosingMethode));
		adn.putParameter(new Parameter<Integer>(LDParameter.NGramDebut.getName(), debutNGram));
		adn.putParameter(new Parameter<Integer>(LDParameter.NGramFin.getName(), finalNGram));
		adn.putParameter(new Parameter<Double>(LDParameter.ParamPPM.getName(), paramPPM));
		//adn.putParameter(new Parameter<TypeClassifier>(LDParameter.TypeClassifier.getName(), typeClassifier));

		this.languagesDirectoryPath = languagesDirectoryPath;

		this.testText = testText;
		listSentence = SentenceSplitter.splitTextIntoSentence(this.testText);
		init();
	}
	
	public LanguageDetector(int nbCharacter, String languagesDirectoryPath, String testFilePath, boolean sentenceByLine,
			ADN adn) {
	}

	public LanguageDetector(int nbCharacter, String languagesDirectoryPath, String testFilePath, boolean sentenceByLine,
			boolean bNGram, int debutNGram, int finalNGram, TypeClassifier typeClassifier, boolean bGramWords,
			int paramGramWords, boolean bPPM, double paramPPM, double limite, boolean bPPMChoosingMethode) {
		super();
		ADN adn = new ADN(supportADN);
		//adn.putParameter(new Parameter<Boolean>(LDParameter.NGram.getName(), bNGram));
		//adn.putParameter(new Parameter<Boolean>(LDParameter.GramWords.getName(), bGramWords));
		//adn.putParameter(new Parameter<Boolean>(LDParameter.PPM.getName(), bPPM));
		//adn.putParameter(new Parameter<Integer>(LDParameter.ParamGramWords.getName(), paramGramWords));
		adn.putParameter(new Parameter<Double>(LDParameter.Limite.getName(), limite));
		adn.putParameter(new Parameter<Boolean>(LDParameter.PPMChoosingMethode.getName(), bPPMChoosingMethode));
		adn.putParameter(new Parameter<Integer>(LDParameter.NGramDebut.getName(), debutNGram));
		adn.putParameter(new Parameter<Integer>(LDParameter.NGramFin.getName(), finalNGram));
		adn.putParameter(new Parameter<Double>(LDParameter.ParamPPM.getName(), paramPPM));
		//adn.putParameter(new Parameter<TypeClassifier>(LDParameter.TypeClassifier.getName(), typeClassifier));

		// this.bNGram = bNGram;
		// this.bGramWords = bGramWords;
		// this.bPPM = bPPM;
		// this.paramGramWords = paramGramWords;
		// this.limite = limite;
		// this.bPPMChoosingMethode = bPPMChoosingMethode;
		// this.debutNGram = debutNGram;
		// this.finalNGram = finalNGram;
		// this.paramPPM = paramPPM;

		/*
		 * this.bNGram = bNGram; this.finalNGram = finalNGram; this.bGramWords =
		 * bGramWords; this.bPPM = bPPM;
		 */
		// this.bPrefixeSuffixe = bPrefixeSuffixe;
		/*
		 * double param = 1.0; if (nbCharacter < 9) param *= 20/nbCharacter;
		 * this.paramPPM = (bPPMChoosingMethode) ? 7d : 13.5d*param;
		 * this.paramGramWords = (bPPMChoosingMethode) ? 1 : 2; this.limite =
		 * param*limite;
		 */
		
		initConstructor(nbCharacter, languagesDirectoryPath, testFilePath, sentenceByLine, adn);
	}
	
	public void initConstructor(int nbCharacter, String languagesDirectoryPath, String testFilePath, boolean sentenceByLine,
							ADN adn) {
		if (adn.isAdnCorrect(supportADN))
			this.adn = adn;
		else
			throw new ClassFormatError("ADN de LanguageDetector ne correspond à son supportADN.");
		
		this.languagesDirectoryPath = languagesDirectoryPath;
		
		this.testText = "";
		Reader reader = new Reader(testFilePath, sentenceByLine);
		reader.open();
		String text = reader.read();
		if (sentenceByLine) {
			listSentence = new ArrayList<String>();
			while (text != null) {
				testText += text;
				listSentence.add(text);
				text = reader.read();
			}
		} else {
			testText += text;
			listSentence = SentenceSplitter.splitTextIntoSentence(text);
		}
		reader.close();
		
		init();
	}
	
	@Override
	public void init() {
		loadLanguage();
	}

	private void loadLanguage() {
		listLanguage.clear();
		File file = new File(languagesDirectoryPath);
		File[] files = file.listFiles();
		if (files != null) {
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					listLanguage.add(new Language(files[i].getName(), files[i].getAbsolutePath(),
							bNGram,
							adn.getParameterValue(Integer.class, LDParameter.NGramDebut.getName()),
							adn.getParameterValue(Integer.class, LDParameter.NGramFin.getName()),
							bGramWords,
							bPPM, bPrefixeSuffixe,
							tailleEchantillonNGram, affichage));
				}
			}
		}
	}

	public String detectLanguage(boolean test/*, String language*/) {
		double[] numericResult = new double[4];

		String result = "";
		Iterator<String> iteratorSentence = listSentence.iterator();
		while (iteratorSentence.hasNext()) { // Boucle sur les phrases
			String[] s = iteratorSentence.next().trim().split("\t");
			String language = s[0];
			String sentence = s[1];
			String temp = sentence;
			// test sur la phrase
			if (bGramWords) {
				sentence = detectLanguageGramWords(sentence);
				sentence = detectLanguageGramWords(sentence);
			}
			// if (!sentence.isEmpty()) {
			if (bNGram)
				detectLanguageNGram(sentence); // On récupère le tableau des
												// distances par langue et par
												// NGram
			if (bPPM)
				detectLanguagePPM(sentence);
			if (bPrefixeSuffixe) {

			}
			// }
			double[][] tempScoresNGram = new double[listLanguage.size()][]; // score
																			// par
																			// langue
																			// et
																			// par
																			// NGram
																			// méthode
																			// NGram
			// int[] scoresNGram = new int[listLanguage.size()]; //score par
			// langue méthode NGram
			int[] scoresGramWords = new int[listLanguage.size()]; // score par
																	// langue
																	// méthode
																	// GramWords
			double[][] tempScoresPPM = new double[listLanguage.size()][]; // distance
																			// par
																			// langue
																			// méthode
																			// PPM
			// int[] scoresPPM = new int[listLanguage.size()]; //score par
			// langue méthode PPM
			for (int i = 0; i < listLanguage.size(); i++) {
				if (bPrefixeSuffixe)
					tempScoresPPM[i] = new double[3];
				else if (bPPM)
					tempScoresPPM[i] = new double[1];
				if (bNGram)
					if (typeClassifier == TypeClassifier.OUT_OF_PLACE)
						tempScoresNGram[i] = listLanguage.get(i).getScoreNGram();
					else if (typeClassifier == TypeClassifier.BAYES) {
						// tempScoresNGram[i] = new
						// double[listLanguage.get(i).getScoreNGram().length];
						// for (int
						// j=0;j<listLanguage.get(i).getScoreNGram().length;j++)
						tempScoresNGram[i]/* [j] */ = /*
														 * ((double)(1/
														 * listLanguage.size()))
														 **/listLanguage.get(i).getScoreNGram()/* [j] */;
					}
				if (bGramWords)
					scoresGramWords[i] = listLanguage.get(i).getScoreGramWord();
				if (bPPM)
					tempScoresPPM[i][0] = listLanguage.get(i).getScorePPM();
				if (bPrefixeSuffixe) {
					tempScoresPPM[i][1] = listLanguage.get(i).getScorePrefixePPM();
					tempScoresPPM[i][2] = listLanguage.get(i).getScoreSuffixePPM();
				}
			}

			Map<String, Double> scoreLanguesPPM_NGram = new HashMap<String, Double>();
			Map<String, Double> scoreLanguesGramWords = normaliserScoreGramWords(scoresGramWords);
			Map<String, Double> scoreLanguesNGram = normaliserScoreNGram(tempScoresNGram);
			Map<String, Double> scoreLanguesPPM = normaliserScorePPM(tempScoresPPM);

			String bestLanguage = "";

			if (adn.getParameterValue(Boolean.class, LDParameter.PPMChoosingMethode.getName())) {
				for (int i = 0; i < listLanguage.size(); i++) {
					String langue = listLanguage.get(i).getLanguage();
					scoreLanguesPPM_NGram.put(langue, scoreLanguesNGram.get(langue) + scoreLanguesGramWords.get(langue)
							+ scoreLanguesPPM.get(langue));
					scoreLanguesNGram.put(langue, scoreLanguesNGram.get(langue) + scoreLanguesGramWords.get(langue));
					scoreLanguesPPM.put(langue, scoreLanguesPPM.get(langue) + scoreLanguesGramWords.get(langue));
				}

				if (affichage) {
					System.out.println("\r\n");
					Iterator<String> it = scoreLanguesNGram.keySet().iterator();
					while (it.hasNext()) {
						String langue = it.next();
						double score = scoreLanguesNGram.get(langue);
						System.out.println(langue + "\t" + score);
					}
					Iterator<String> itPPM = scoreLanguesPPM.keySet().iterator();
					while (itPPM.hasNext()) {
						String langue = itPPM.next();
						double score = scoreLanguesPPM.get(langue);
						System.out.println(langue + "\t" + score);
					}
				}
				bestLanguage = chooseBestLanguagePPM(scoreLanguesNGram, scoreLanguesPPM, scoreLanguesPPM_NGram);
			} else {
				// if (scoreLanguesPPM != null) {
				for (int i = 0; i < listLanguage.size(); i++) {
					String langue = listLanguage.get(i).getLanguage();
					scoreLanguesPPM_NGram.put(langue, scoreLanguesNGram.get(langue) + scoreLanguesGramWords.get(langue)
							+ scoreLanguesPPM.get(langue));
				}
				if (affichage) {
					Iterator<String> it = scoreLanguesPPM_NGram.keySet().iterator();
					while (it.hasNext()) {
						String langue = it.next();
						double score = scoreLanguesPPM_NGram.get(langue);
						System.out.println(langue + "\t" + score);
					}
				}
				bestLanguage = chooseBestLanguageAll(scoreLanguesPPM_NGram);
				/*
				 * } else if (sentence.equals(" ")) { bestLanguage =
				 * chooseBestLanguageAll(scoreLanguesGramWords); } else {
				 * bestLanguage = "Indetermination\tUnknown"; }
				 */

			}
			if (test) {
				numericResult[0]++;
				if (bestLanguage.contains(language)) // Identification Correct
					numericResult[1]++; //Juste //True Positif
				else if (bestLanguage.contains("Indetermination"))
					numericResult[2]++; //Indet //FalseNegatif
				else { // Erreur totale
					numericResult[3]++; //Erreur //FalsePositif
				}
			} else {
				result += temp + "\t" + bestLanguage + "\r\n";
				System.out.println(temp + "\t" + bestLanguage);
			}

			listTestProfile.clear();
			for (int i = 0; i < listLanguage.size(); i++)
				listLanguage.get(i).resetScore();
		}
		if (test) {
			result += numericResult[0] + "\r\n";
			result += numericResult[1] + "\r\n";
			result += numericResult[2] + "\r\n";
			result += numericResult[3] + "\r\n";
		}
		//System.out.println("Détection terminée");
		return result;
	}

	private void detectLanguageNGram(String sentence) {
		for (int i = 0; i < listLanguage.size(); i++) {
			listLanguage.get(i).testNGram(sentence, typeClassifier);
		}
	}

	private String detectLanguageGramWords(String sentence) {
		for (int i = 0; i < listLanguage.size(); i++) {
			listLanguage.get(i).testGramWords(sentence);
		}
		for (int i = 0; i < listLanguage.size(); i++) {
			sentence = listLanguage.get(i).removeGramWords(sentence);
		}
		return sentence;
	}

	private void detectLanguagePPM(String sentence) {
		for (int i = 0; i < listLanguage.size(); i++) {
			listLanguage.get(i).testPPM(sentence);
		}
	}

	private Map<String, Double> normaliserScoreNGram(double[][] tempScoresNGram) {
		Map<String, Double> scoresLanguageNGram = new HashMap<String, Double>();
		for (int i = 0; i < listLanguage.size(); i++) {
			scoresLanguageNGram.put(listLanguage.get(i).getLanguage(), 0.0d);
		}

		// L'objectif final est de déterminer la langue la plus proche du profil
		if (bNGram && tempScoresNGram != null) {
			int i = adn.getParameterValue(Integer.class, LDParameter.NGramDebut.getName());
			List<List<TupleDouble>> sortedScoresNGrams = sortLanguageNGram(tempScoresNGram);
			Iterator<List<TupleDouble>> itList = sortedScoresNGrams.iterator();
			while (itList.hasNext()) { // Boucle sur les NGrams (n = i)
				if (affichage)
					System.out.println(i + "Gram :");
				List<TupleDouble> listLangue = itList.next();
				Iterator<TupleDouble> itScoreLangues = listLangue.iterator();
				Double meilleurScore;
				Double pireScore;
				if (listLangue.get(0).getValue() != 0) {
					if (typeClassifier == TypeClassifier.OUT_OF_PLACE) {
						meilleurScore = listLangue.get(0).getValue();
						pireScore = listLangue.get(listLangue.size() - 1).getValue();
					} else { // mettre en place comparaison des valeurs, si trop
								// proche, pas de point
						meilleurScore = -Math.log(listLangue.get(0).getValue());
						pireScore = -Math.log(listLangue.get(listLangue.size() - 1).getValue());
					}
					if (meilleurScore - pireScore != 0) {
						while (itScoreLangues.hasNext()) { // Boucle sur les
															// langues
							TupleDouble tupF = itScoreLangues.next();
							String langue = tupF.getKey();
							Double score = tupF.getValue();
							Double temp = 0.0d;
							if (typeClassifier == TypeClassifier.OUT_OF_PLACE)
								temp = (double) i
										/ adn.getParameterValue(Integer.class, LDParameter.NGramDebut.getName())
										* (score - pireScore) / (meilleurScore - pireScore);
							else
								temp = (double) i
										/ adn.getParameterValue(Integer.class, LDParameter.NGramDebut.getName())
										* (-Math.log(score) - pireScore) / (meilleurScore - pireScore);
							scoresLanguageNGram.put(langue, scoresLanguageNGram.get(langue) + temp);
							if (affichage) {
								System.out.println(langue + "\t" + score + "\t" + temp);
							}
						}
					}
				}
				i++;
			}
		}
		return scoresLanguageNGram;
	}

	private Map<String, Double> normaliserScoreGramWords(int[] scoresGramWords) {
		Map<String, Double> scoresLanguageGramWords = new HashMap<String, Double>();
		for (int i = 0; i < listLanguage.size(); i++) {
			scoresLanguageGramWords.put(listLanguage.get(i).getLanguage(), 0.0d);
		}

		if (bGramWords && scoresGramWords != null) {
			if (affichage)
				System.out.println("GramWords :");
			for (int i = 0; i < listLanguage.size(); i++) {
				scoresLanguageGramWords.put(listLanguage.get(i).getLanguage(),
						scoresLanguageGramWords.get(listLanguage.get(i).getLanguage())
								+ paramGramWords
										* scoresGramWords[i]);
				if (affichage) {
					System.out.println(listLanguage.get(i).getLanguage() + "\t" + scoresGramWords[i]);
				}
			}
		}
		return scoresLanguageGramWords;
	}

	private Map<String, Double> normaliserScorePPM(double[][] tempScoresPPM) {
		Map<String, Double> scoresLanguagePPM = new HashMap<String, Double>();
		for (int i = 0; i < listLanguage.size(); i++) {
			scoresLanguagePPM.put(listLanguage.get(i).getLanguage(), 0.0d);
		}

		if (bPPM && tempScoresPPM != null) {
			if (affichage)
				System.out.println("PPM : ");
			List<List<TupleDouble>> sortedScoresPPMs = sortLanguagePPM(tempScoresPPM);
			Iterator<List<TupleDouble>> itList = sortedScoresPPMs.iterator();
			while (itList.hasNext()) {
				List<TupleDouble> listLangue = itList.next();
				Iterator<TupleDouble> itScoreLangues = listLangue.iterator();
				Double meilleurScore = listLangue.get(0).getValue();
				/*
				 * Double pireScore =
				 * listLangue.get(listLangue.size()-1).getValue(); //Permet la
				 * détection de la langue inconnue. A pousser en prenant en
				 * compte les nGrams (qui doivent avoir des valeurs très faible
				 * pour être langue inconnu) if (meilleurScore > 60 &&
				 * 1-meilleurScore/pireScore<0.3) scoresLanguagePPM = null; else
				 */ if (meilleurScore != 0) {
					while (itScoreLangues.hasNext()) {
						TupleDouble tupF = itScoreLangues.next();
						String langue = tupF.getKey();
						Double score = tupF.getValue();
						if (!Double.isNaN(score) && !Double.isInfinite(score)) {
							Double temp = adn.getParameterValue(Double.class, LDParameter.ParamPPM.getName())
									* meilleurScore / score;
							scoresLanguagePPM.put(langue,
									/* scoresLanguagePPM.get(langue)+ */temp);
							if (affichage) {
								System.out.println(langue + "\t" + score + "\t" + temp);
							}
						}
					}
				}
			}
		}
		return scoresLanguagePPM;
	}

	private List<List<TupleDouble>> sortLanguageNGram(double[][] tempScoresNGram) {
		List<List<TupleDouble>> scoresByNGram = new ArrayList<List<TupleDouble>>(); // Tableau
																					// des
																					// 2
																					// meilleurs
																					// langues
																					// par
																					// NGram
		for (int i = 0; i < tempScoresNGram[0].length; i++) { // Boucle sur les
																// NGram (n=i-1)
			List<TupleDouble> listScoreByNGram = new ArrayList<TupleDouble>();
			for (int j = 0; j < tempScoresNGram.length; j++) { // Boucle sur les
																// langues
				listScoreByNGram.add(new TupleDouble(listLanguage.get(j).getLanguage(), tempScoresNGram[j][i]));
			}
			Collections.sort(listScoreByNGram);
			if (typeClassifier == TypeClassifier.OUT_OF_PLACE)
				Collections.reverse(listScoreByNGram);
			scoresByNGram.add(listScoreByNGram);
		}
		return scoresByNGram;
	}

	private List<List<TupleDouble>> sortLanguagePPM(double[][] tempScoresPPM) {
		List<List<TupleDouble>> scoresByPPM = new ArrayList<List<TupleDouble>>(); // Tableau
																					// des
																					// 2
																					// meilleurs
																					// langues
																					// par
																					// méthodes
																					// PPM
		for (int i = 0; i < tempScoresPPM[0].length; i++) { // Boucle sur les
															// méthodes PPM 0 =
															// PPM 1 = Prefixe 2
															// = Suffixe
			List<TupleDouble> listScoreByPPM = new ArrayList<TupleDouble>();
			for (int j = 0; j < tempScoresPPM.length; j++) { // Boucle sur les
																// langues
				listScoreByPPM.add(new TupleDouble(listLanguage.get(j).getLanguage(), tempScoresPPM[j][i]));
			}
			Collections.sort(listScoreByPPM);
			Collections.reverse(listScoreByPPM);
			scoresByPPM.add(listScoreByPPM);
		}
		return scoresByPPM;
	}

	private String chooseBestLanguagePPM(Map<String, Double> mapScoreLanguageNGram,
			Map<String, Double> mapScoreLanguagePPM, Map<String, Double> scoreLanguesPPM_NGram) {
		String meilleurNGram[] = chooseBestLanguage(mapScoreLanguageNGram);
		String meilleurPPM[] = chooseBestLanguage(mapScoreLanguagePPM);
		String meilleurNGram_PPM[] = chooseBestLanguage(scoreLanguesPPM_NGram);

		if (mapScoreLanguagePPM.get(meilleurPPM[0]) - mapScoreLanguagePPM.get(meilleurPPM[1]) < adn
				.getParameterValue(Double.class, LDParameter.Limite.getName())) {
			if (meilleurNGram[0].equals(meilleurPPM[0]))
				return meilleurNGram[0] + "\t" + meilleurNGram[0];
			else {
				double diffScore = Math.abs(scoreLanguesPPM_NGram.get(meilleurNGram_PPM[0])
						- scoreLanguesPPM_NGram.get(meilleurNGram_PPM[1]));
				if (diffScore < adn.getParameterValue(Double.class, LDParameter.Limite.getName()))
					return "Indetermination\t" + meilleurNGram_PPM[0];
				else
					return meilleurNGram_PPM[0] + "\t" + diffScore;
			}
		} else {
			return meilleurPPM[0] + "\t"
					+ (mapScoreLanguagePPM.get(meilleurPPM[0]) - mapScoreLanguagePPM.get(meilleurPPM[1]));
		}
	}

	/**
	 * Peu efficace, à refaire
	 * 
	 * @param mapScoreLanguage
	 * @return
	 */
	private String chooseBestLanguageAll(Map<String, Double> mapScoreLanguage) {
		String meilleurNGram_PPM[] = chooseBestLanguage(mapScoreLanguage);

		if (mapScoreLanguage.get(meilleurNGram_PPM[0]) - mapScoreLanguage.get(meilleurNGram_PPM[1]) > adn
				.getParameterValue(Double.class, LDParameter.Limite.getName())) {
			return meilleurNGram_PPM[0] + "\t"
					+ (mapScoreLanguage.get(meilleurNGram_PPM[0]) - mapScoreLanguage.get(meilleurNGram_PPM[1]));
		} else {
			return "Indetermination\t" + meilleurNGram_PPM[0];
		}
		/*
		 * String meilleur = "English"; String second = "English";
		 * Iterator<String> it = mapScoreLanguage.keySet().iterator(); while
		 * (it.hasNext()) { String langue = it.next(); double score =
		 * mapScoreLanguage.get(langue); if (score >
		 * mapScoreLanguage.get(meilleur)) { meilleur = langue; } } if
		 * (second.equals(meilleur)) second = "French"; it =
		 * mapScoreLanguage.keySet().iterator(); while (it.hasNext()) { String
		 * langue = it.next(); double score = mapScoreLanguage.get(langue); if
		 * (!langue.equals(meilleur) && score > mapScoreLanguage.get(second)) {
		 * second = langue; } } double score = mapScoreLanguage.get(meilleur) -
		 * mapScoreLanguage.get(second); if (score < limite) meilleur =
		 * "Indetermination\t" + meilleur;// + "\t\t\t\t" + score; else meilleur
		 * = meilleur + "\t" + score; return meilleur;
		 */
	}

	private String[] chooseBestLanguage(Map<String, Double> mapScoreLanguage) {
		String[] tabLanguage = new String[2];
		String meilleur = "English";
		String second = "English";
		Iterator<String> it = mapScoreLanguage.keySet().iterator();
		while (it.hasNext()) {
			String langue = it.next();
			double score = mapScoreLanguage.get(langue);
			if (score > mapScoreLanguage.get(meilleur)) {
				meilleur = langue;
			}
		}
		if (second.equals(meilleur))
			second = "French";
		it = mapScoreLanguage.keySet().iterator();
		while (it.hasNext()) {
			String langue = it.next();
			double score = mapScoreLanguage.get(langue);
			if (!langue.equals(meilleur) && score > mapScoreLanguage.get(second)) {
				second = langue;
			}
		}
		tabLanguage[0] = meilleur;
		tabLanguage[1] = second;
		return tabLanguage;
	}

	public void setTest(boolean test) {
		this.isTest = test;
	}
	
	@Override
	public double getScore() {
		String[] listScore = result.split("\n");
		double tp = Double.parseDouble(listScore[1]);
		double fn = Double.parseDouble(listScore[2])+Double.parseDouble(listScore[3]);
		double fp = Double.parseDouble(listScore[3]);
		double precision = tp/(tp+fn);
		double rappel = tp/(tp+fp);
		//F1Mesure
		return 2*precision*rappel/(precision+rappel);
	}

	@Override
	public void optimize() {
		result = detectLanguage(isTest);
		//adn.setScore(getScore());
	}
}
