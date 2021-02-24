package log.heuristics.nn;

import java.util.HashMap;

/**
 * This abstract class provides all the functionalities to create Neural Networks given a
 * network specification through a NNDecoder object
 */

public abstract class NNCreator {

	public static final String TANH = "TANH";
	public static final String LIN = "LIN";
	public static final String LOG = "LOG";
	public static final String SIG = "SIG";

	/**
	 * This function returns the map of function names to the actual function object
	 * used during the instantiation of the neural network
	 * @return the map of functions coupled with the actual function objects specific of the implementation
	 */
	public abstract HashMap<String, Object> getFunctions();

	/**
	 * Object containing the information about the architecture
	 */
	protected NNDecoder decoder;

	/**
	 * Constructor initialising the decoder used to extrapolate the information about the network topology.
	 * @param decoder instance of NNDecoder which will be specific for each architecture
	 */
	public NNCreator(NNDecoder decoder){
		this.decoder = decoder;
	}

	/**
	 * This function helps extracting the function object given its name
	 * @param functionType is the name as defined by the public static final String keys in this class
	 * @return the actual function object specific of the implementation of the neural network
	 */
	public Object decodeFunction(String functionType){
		HashMap<String, Object> functions = this.getFunctions();
		if(functions.containsKey(functionType))
			return functions.get(functionType);
		return functions.get(TANH);
	}

	/**
	 * Abstract method that will implement the library-specific rules for building
	 * the network specified by the encoding
	 * @return the network object
	 */
	public abstract NNInterface create();

	/**
	 * This method allows to save the network topology to a specified path
	 * @param path location and name of the file where the network will be saved
	 * @return a flag of whether the writing of the file was successful
	 */
	public abstract boolean saveNetwork(String path);

}
