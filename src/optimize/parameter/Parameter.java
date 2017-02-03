package optimize.parameter;

public class Parameter<T> {
	private String parameterName;
	private T parameterValue;

	public Parameter(String parameterName) {
		super();
		this.parameterName = parameterName;
	}

	public Parameter(String parameterName, T parameterValue) {
		super();
		this.parameterName = parameterName;
		this.parameterValue = parameterValue;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public T getValue() {
		return parameterValue;
	}

	public void setValue(T value) {
        this.parameterValue = value;
    }
	
	public Class<?> getParameterClass() {
		return parameterValue.getClass();
	}
}
