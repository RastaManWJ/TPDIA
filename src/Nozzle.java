
public class Nozzle {
	private int _ID;
	private double _transactionCurrent;
	private double _transactionTotal;
	
	public Nozzle(int _ID) {
		super();
		this._ID = _ID;
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
	
}
