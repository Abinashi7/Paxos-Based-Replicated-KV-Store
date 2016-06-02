import java.io.Serializable;


public class KeyValueReturnWrapper implements Serializable {
	/**
	 * The serial number of this version
	 */
	public static final long serialVersionUID = 1L;
	/**
	 * The key in this return wrapper.
	 */
	private String myKey;
	/**
	 * The value in this return wrapper
	 */
	private String myValue;
	/**
	 * The code contained in this return wrapper
	 */
	private KeyValueCode myCode;
	
	/**
	 * Constructor for this return value wrapper.
	 * @param theKey The key for this wrapper to contain.
	 * @param theValue The value for this wrapper to contain.
	 * @param theCode the KeyValueCode for this wrapper to contain.
	 */
	public KeyValueReturnWrapper(final String theKey, final String theValue,
			final KeyValueCode theCode) {
		myKey = theKey;
		myValue = theValue;
		myCode = theCode;
	}
	/**
	 * Getter for the Key.
	 * @return the key held in the wrapper.
	 */	
	public String getKey() {
		return myKey;
	}
	/**
	 * Getter for the value.
	 * @return the value held in the wrapper.
	 */
	public String getValue() {
		return myValue;
	}
	/**
	 * Getter for the code.
	 * @return the code
	 */
	public KeyValueCode getCode() {
		return myCode;
	}
	@Override
	public String toString() {
		return "[KeyValueReturnWrapper key=" + myKey + " value=" 
				+ myValue + " code=" + myCode + "]";
	}

}
