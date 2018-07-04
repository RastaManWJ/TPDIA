public class Cistern {
	private int _ID;
	private double _volumeMax;
	private double _volumeCurrent;
	private double _temperature;
	
	public Cistern(int _ID, double _volumeMax, double _volumeCurrent, double _temperature) {
		super();
		this._ID = _ID;
		this._volumeMax = _volumeMax;
		this._volumeCurrent = _volumeCurrent;
		this._temperature = _temperature;
	}
	public Cistern() {
		this._ID = 0;
		this._volumeMax = 0;
		this._volumeCurrent = 0;
		this._temperature = 0;
	}

	public int get_ID() {
		return _ID;
	}

	public void set_ID(int _ID) {
		this._ID = _ID;
	}

	public double get_volumeMax() {
		return _volumeMax;
	}

	public void set_volumeMax(double _volumeMax) {
		this._volumeMax = _volumeMax;
	}

	public double get_volumeCurrent() {
		return _volumeCurrent;
	}

	public void set_volumeCurrent(double _volumeCurrent) {
		this._volumeCurrent = _volumeCurrent;
	}

	public double get_temperature() {
		return _temperature;
	}

	public void set_temperature(double _temperature) {
		this._temperature = _temperature;
	}
	
	
}
