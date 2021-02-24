package log.heuristics.nn.encog.creator;

import log.heuristics.nn.NNCreator;
import log.heuristics.nn.NNDecoder;
import log.heuristics.nn.NNInterface;
import org.encog.engine.network.activation.*;
import org.encog.persist.EncogDirectoryPersistence;
import org.encog.persist.PersistError;

import java.io.File;
import java.util.HashMap;

/**
 * Encog specific implementation of NNCreator
 */

public abstract class NNCreatorEncog extends NNCreator{

	/**
	 * Map containing the function objects matched with their NNCreator names
	 */
	transient private final static HashMap<String, Object> functions = new HashMap<String, Object>(){{
		put(TANH, new ActivationTANH());
		put(LIN, new ActivationLinear());
		put(LOG, new ActivationLOG());
		put(SIG, new ActivationSigmoid());
	}};

	public NNCreatorEncog(NNDecoder decoder) {
		super(decoder);
	}

	/**
	 * This method provides the mapping of NNCreator function names to function objects
	 * @return Encog-specific function name-object map
	 */
	@Override
	public HashMap<String, Object> getFunctions() {
		return functions;
	}

	/**
	 * This method saves Encog format netowrks at the specified path.
	 * @param path location and name of the file where the network will be saved
	 * @return flag of whether the file was saved or some error arose
	 */

	public boolean saveNetwork(String path){
		NNInterface network = create();
		try {
			EncogDirectoryPersistence.saveObject(new File(path), network);
		}catch (PersistError e){
			System.out.println("[Encog - Save Object]: FAILED");
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
