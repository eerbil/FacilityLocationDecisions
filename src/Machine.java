import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;


public abstract class Machine implements Comparable<Machine>{
	private String name;
	private int width;
	private int height;
	private boolean isRotated;
	private Point location;
	
	private Collection<PlacementCandidate> candidatePlacements = new ArrayList<>();
	
	public void rotate(){
		if(canRotate()){
		isRotated = !isRotated;
		}else{
			throw new IllegalStateException();
		}
	}
	
	public boolean canRotate(){
		return location == null;
	}
	
	public int getXLength(){
		return isRotated ? height : width;
	}
	
	public int getYLength(){
		return isRotated ? width : height;
	}
	
	public Machine getNeighbor(Direction dir){
		Direction.values();
		return null;
	}
	public void setNeighbor(Direction dir, Machine mach){
		
	}
	
	public List<Machine> getAllNeighbors(){
		return null;
	}
	
	/*public <T> T chain(Supplier<T> supp, Direction dir){
		return 
	}*/

	@Override
	public int compareTo(Machine m) {
		return Math.subtractExact(0,0);
	}

	public void setLocation(Point point) {
		location = point;
		
	}

	public Point getLocation() {
		return location;
	}

	protected Collection<PlacementCandidate> getCandidatePlacements() {
		return candidatePlacements;
	}

	protected boolean isRotated() {
		return isRotated;
	}
}
