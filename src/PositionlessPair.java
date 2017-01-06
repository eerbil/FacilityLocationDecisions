
public class PositionlessPair<S, T> {
	private final S el1;
	private final T el2;
	
	public PositionlessPair(S el1, T el2){
		this.el1 = el1;
		this.el2 = el2;
	}
	
	public S getS(){
		return el1;
	}
	public T getT(){
		return el2;
	}
	
	public boolean contains(Object o){
		return o.equals(el1) || o.equals(el2);
	}
	
	@Override
	public int hashCode() {
		return el1.hashCode() + el2.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PositionlessPair){
			@SuppressWarnings("unchecked")
			PositionlessPair<S, T> otherPair = (PositionlessPair<S, T>) obj;
			return (el1.equals(otherPair.el1) && el2.equals(otherPair.el2))
					|| (el1.equals(otherPair.el2) && el2.equals(otherPair.el1));
		}
		return super.equals(obj);
	}

}
