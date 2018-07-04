
public class Nozzle {
	private int _ID;
	private double _transactionCurrent;
	private double _transactionTotal;
	private boolean _isUsed;
	
	public Nozzle(int _ID) {
		super();
		this._ID = _ID;
		this._transactionCurrent = 0;
		this._transactionTotal = 0;
		this._isUsed = false;
	}

	public double get_transactionCurrent() {
		return _transactionCurrent;
	}

	public void set_transactionCurrent(double _transactionCurrent) {
		this._transactionCurrent = _transactionCurrent;
	}

	public double get_transactionTotal() {
		return _transactionTotal;
	}

	public void set_transactionTotal(double _transactionTotal) {
		this._transactionTotal = _transactionTotal;
	}

	public int get_ID() {
		return _ID;
	}
	
	public void set_isUsed(boolean _isUsed) {
		this._isUsed = _isUsed;
	}
	
}
