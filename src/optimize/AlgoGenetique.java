package optimize;

import java.util.HashMap;

import optimize.parameter.ADN;
import optimize.parameter.Parameter;

public class AlgoGenetique {

	private static HashMap<String, Class<?>> supportADN = new HashMap<String, Class<?>>();

	static {
		supportADN.put("CHANCE_MUTATION", Double.class);
		supportADN.put("POURCENTAGE_MEILLEURS", Double.class);
		supportADN.put("POURCENTAGE_RESTE", Double.class);
		supportADN.put("NB_POPULATION", Integer.class);
		supportADN.put("NB_GENERATION_MAX", Integer.class);
		supportADN.put("NB_ELITE_A_GARDER", Integer.class);
		supportADN.put("VAL_MIN", Double.class);
	}
	
	public static enum AGParameter {
		CHANCE_MUTATION("CHANCE_MUTATION"),
		POURCENTAGE_MEILLEURS("POURCENTAGE_MEILLEURS"),
		POURCENTAGE_RESTE("POURCENTAGE_RESTE"),
		NB_POPULATION("NB_POPULATION"), 	
		NB_GENERATION_MAX("NB_GENERATION_MAX"),
		NB_ELITE_A_GARDER("NB_ELITE_A_GARDER"),
		VAL_MIN("NGramDebut");

		private String name;
		
		private AGParameter(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
	}
	
	private ADN mapParameter = new ADN(supportADN);
	
	public AlgoGenetique(double chanceMutation, double pourcentageMeilleur, double pourcentageReste, int taillePopulation, int nbGenerationMax, double valFitnessMin) {
		mapParameter.putParameter(new Parameter<Double>("CHANCE_MUTATION", chanceMutation));
		mapParameter.putParameter(new Parameter<Double>("POURCENTAGE_MEILLEURS", pourcentageMeilleur));
		mapParameter.putParameter(new Parameter<Double>("POURCENTAGE_RESTE", pourcentageReste));
		mapParameter.putParameter(new Parameter<Integer>("NB_POPULATION", taillePopulation));
		mapParameter.putParameter(new Parameter<Integer>("NB_GENERATION_MAX", nbGenerationMax));
		mapParameter.putParameter(new Parameter<Integer>("NB_ELITE_A_GARDER", (int)(taillePopulation*pourcentageMeilleur)));
		mapParameter.putParameter(new Parameter<Double>("CHANCE_MUTATION", chanceMutation));
	}
	
	/**
	 * Construction de la population initiale
	 */
	public void init() {
		
	}
	
	public void mutation() {
		
	}
	
	public void croisement() {
		
	}
	
	public void evoluer() {
		
	}
}
