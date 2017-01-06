import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;


public class Machine implements Comparable<Machine>{
	private String name;
	private int width;
	private int height;
	private boolean isRotated;
	private Point location;
	private int tcr = 0;
	private Queue<PlacementCandidate> candidatePlacements = new PriorityQueue<PlacementCandidate>(Collections.reverseOrder());
	
	public Machine(String name, int width, int height) {
		this.name = name;
		this.width = width;
		this.height = height;
	}
	
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
		return Integer.compare(getTcr(), m.getTcr());
	}

	public void setLocation(Point point) {
		location = point;
		
	}

	public Point getLocation() {
		return location;
	}

	protected Queue<PlacementCandidate> getCandidatePlacements() {
		return candidatePlacements;
	}

	protected boolean isRotated() {
		return isRotated;
	}

	protected int getTcr() {
		return tcr;
	}

	protected void setTcr(int tcr) {
		this.tcr = tcr;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
