import java.awt.Point;


public class PlacementCandidate implements Comparable<PlacementCandidate>{

	private double score;
	private Point location;
	private boolean isRotated;
	
	public PlacementCandidate(double score, Point location, boolean isRotated) {
		this.score = score;
		this.location = location;
		this.isRotated = isRotated;
	}
	
	protected double getScore() {
		return score;
	}
	protected Point getLocation() {
		return location;
	}
	protected boolean isRotated() {
		return isRotated;
	}

	@Override
	public int compareTo(PlacementCandidate o) {
		return Double.compare(getScore(), o.getScore());
	}

	@Override
	public String toString() {
		return "PlacementCandidate [score=" + score + ", location=" + location
				+ ", isRotated=" + isRotated + "]";
	}
	
	
	
	
	
}
