package players;

import utils.NamedObject;

public interface BasePlayerInterface extends NamedObject {
	BasePlayerInterface reset();
	void setId(int id);
	double utility();
	public BasePlayerInterface clone();
}
