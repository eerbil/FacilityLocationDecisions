import java.awt.Point;


public class PlacementCandidate {

	private double score;
	private Point location;
	private boolean isRotated;
	
	public PlacementCandidate(double score, Point location, boolean isRotated) {
		this.score = score;
		this.location = location;
		this.isRotated = isRotated;
	}
	
	protected double getPoint() {
		return score;
	}
	protected Point getLocation() {
		return location;
	}
	protected boolean isRotated() {
		return isRotated;
	}
	
//	protected void setPoint(double point) {
//		this.point = point;
//	}
//	protected void setLocation(Point location) {
//		this.location = location;
//	}
//	protected void setRotated(boolean isRotated) {
//		this.isRotated = isRotated;
//	}
	
	
	
	
	
}
