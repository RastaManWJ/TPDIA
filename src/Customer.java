
public class Customer {
	private int _nozzleUsed;
	private double _fuelNeeded;
	private int _fuelingTime;
	
	public Customer(int _nozzleUsed, double _fuelNeeded, int _fuelingTime) {
		super();
		this._nozzleUsed = _nozzleUsed;
		this._fuelNeeded = _fuelNeeded;
		this._fuelingTime = _fuelingTime;
	}

	public int get_nozzleUsed() {
		return _nozzleUsed;
	}

	public void set_nozzleUsed(int _nozzleUsed) {
		this._nozzleUsed = _nozzleUsed;
	}

	public double get_fuelNeeded() {
		return _fuelNeeded;
	}

	public void set_fuelNeeded(double _fuelNeeded) {
		this._fuelNeeded = _fuelNeeded;
	}

	public int get_fuelingTime() {
		return _fuelingTime;
	}

	public void set_fuelingTime(int _fuelingTime) {
		this._fuelingTime = _fuelingTime;
	}
	
	
	
}
