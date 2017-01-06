
public enum Closeness {
	A(1000),
	E(100),
	I(10),
	O(1),
	U(0),
	X(-1000);
	
	private final int value;
	private Closeness(int value){
		this.value = value;
	}
	public int getValue() {
		return value;
	}
	
}
