package optimize.old;

import java.util.ArrayList;
import java.util.HashMap;

import optimize.parameter.Parameter;

public class ADN extends HashMap<Class<?>, ArrayList<Parameter<Class<?>>>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4369531956155698825L;

	public <T> T getParameter(Class<T> key, String parameterName) {
		if(key == null) {
			throw new NullPointerException("No null keys are allowed in this code");
		}
		Parameter<T> p = findParameter(this.get(key), parameterName);
		if (p == null)
			return null;
		else
			return p.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> void putParameter(Class<T> key, String parameterName, T value) {
		if (!this.containsKey(key))
			this.put(key, new ArrayList<Parameter<Class<?>>>());
		Parameter<T> p = findParameter(this.get(key), parameterName);
		if (p == null) {
			p = new Parameter<T>(parameterName);
			this.get(key).add((Parameter<Class<?>>) p);
		}
		p.setValue(value);
	}
	
	@SuppressWarnings("unchecked")
	public <T> Parameter<T> findParameter(ArrayList<Parameter<Class<?>>> list, String parameterName) {
		int i = 0;
		Parameter<T> p = null;
		boolean notFind = true;
		
		while (notFind && i < list.size()) {
			if (list.get(i).getParameterName().equals(parameterName)) {
				p = (Parameter<T>) list.get(i);
				notFind = true;
			}
			i++;
		}
		return p;
	}
}
