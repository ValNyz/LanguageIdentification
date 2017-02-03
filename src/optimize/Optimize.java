package optimize;

import java.util.HashMap;

import optimize.parameter.ADN;

public abstract class Optimize implements Comparable<Optimize> {

	private static HashMap<String, Class<?>> supportADN;
	private ADN adn;
	
	public abstract void optimize();
	
	public abstract double getScore();
	
	public ADN getADN() {
		return adn;
	}
	
	public boolean setADN(ADN adn) {
		if (adn.isAdnCorrect(supportADN)) {
			this.adn = adn;
			return true;
		} else
			return false;
	}
	
	public HashMap<String,Class<?>> getSupportADN() {
		return supportADN;
	}
}
