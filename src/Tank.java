import java.util.ArrayList;
import java.util.List;

public class Tank {
	private int _ID;
	private List<Nozzle> _nozzleList = new ArrayList<Nozzle>();
	private double _volumeMax;
	private double _volumeCurrent;
	private double _temperature;
	private double _height;

	public Tank(int _ID, List<Nozzle> _nozzleList, double _volumeMax, double _volumeCurrent, double _temperature, double _height) {
		super();
		this._ID = _ID;
		this._nozzleList = _nozzleList;
		this._volumeMax = _volumeMax;
		this._volumeCurrent = _volumeCurrent;
		this._temperature = _temperature;
		this._height = _height;
	}
	
	public Tank(int _ID, List<Nozzle> _nozzleList, double _volumeMax) {
		super();
		this._ID = _ID;
		this._nozzleList = _nozzleList;
		this._volumeMax = _volumeMax;
		this._volumeCurrent = _volumeMax;
		this._temperature = 25;
		this._height = 0;
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

	public int get_ID() {
		return _ID;
	}
	
	public List<Nozzle> get_nozzleList() {
		return _nozzleList;
	}

	public double get_volumeMax() {
		return _volumeMax;
	}

	public double get_height() {
		return _height;
	}
		
}
