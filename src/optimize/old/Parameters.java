package optimize.old;

import java.util.HashMap;

public class Parameters extends HashMap<Class<?>, HashMap<String, Object>>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4369531956155698825L;

	public <T> T getParameter(Class<T> key, String parameterName) {
		if(key == null) {
			throw new NullPointerException("No null keys are allowed in this code");
		}
		return key.cast(this.get(key).get(parameterName));
	}
	
	public <T> void putParameter(Class<T> key, String parameterName, T value) {
		if (!this.containsKey(key))
			this.put(key, new HashMap<String, Object>());
		this.get(key).put(parameterName, value);
	}
}
