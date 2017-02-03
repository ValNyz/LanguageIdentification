package optimize.parameter;

import java.util.HashMap;
import java.util.Iterator;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;

public class ADN extends HashMap<String, Class<?>>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8392450232637694377L;
	
	public HashMap<String, Parameter<Class<?>>> mapNameParam = new HashMap<String, Parameter<Class<?>>>();
	
	public ADN(HashMap<String, Class<?>> listParam) {
		super(listParam);
		for(String s : this.keySet())
			mapNameParam.put(s, null);
	}
	
	public Class<?> getParameterClass(String parameterName) {
		return get(parameterName);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getParameter(Class<T> key, String parameterName) {
		if(key == null) {
			throw new NullPointerException("No null keys are allowed in this code");
		}
		Parameter<T> p = (Parameter<T>) mapNameParam.get(parameterName);
		return p.getValue();
	}
	
	@SuppressWarnings("unchecked")
	public <T> void putParameter(Parameter<T> p) {
		String parameterName = p.getParameterName();
		if (!(get(parameterName) == p.getParameterClass()))
			throw new ClassFormatException("T class of Parameter<T> " + parameterName + " isn't good, it should be : " + get(parameterName));
		mapNameParam.put(parameterName, (Parameter<Class<?>>) p);
	}
	
	public boolean isAdnCorrect(HashMap<String, Class<?>> adn) {
		boolean correct = true;
		ADN temp = this;
		Iterator<String> adnIt = adn.keySet().iterator();
		while(adnIt.hasNext()) {
			String adnParam = adnIt.next();
			if (temp.get(adnParam) == adn.get(adnParam))
				temp.remove(adnParam);
		}
		if (temp.size() != 0)
			correct = false;
		return correct;
	}
}
