import java.awt.Point;
import java.util.Collection;
import java.util.Deque;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Facility {

	Machine[][] ground;

	int xLen;
	int ylen;

	Deque<Machine> placeds;
	Queue<Machine> remainings;

	public void placeAll(){ // Priority according to TCR

		/*while(!remainings.isEmpty()){
			Machine current = remainings.poll();

			int totalX = current.getXLength();

			Machine last = placeds.peekLast();
			if(last != null){

			}
			placeds.addLast(current);


		}*/



		Machine currentMachine = remainings.poll(); // First Highest TCR
		Point coordinate = getInitialPoint(currentMachine);

		while(!testPlaceable(coordinate, currentMachine)){
			//TODO
			throw new RuntimeException("Now what?");
		}
		place(coordinate, currentMachine);


		currentMachine = remainings.poll();
		Collection<PlacementCandidate> currentCandidates = currentMachine.getCandidatePlacements();
		for (int i = 0; i < ground.length; i++) {
			for (int j = 0; j < ground[i].length; j++) {
				if(testPlaceable(j, i, currentMachine)){
					currentCandidates.add(new PlacementCandidate(
							calculateScore(currentMachine),
							new Point(j, i),
							currentMachine.isRotated()));
				}
			}
		}


	}

	private double calculateScore(Machine currentMachine) {
		
		return 0;
	}

	public int getTotalX(Machine m){
		int total = 0;
		for(Direction dir : EnumSet.of(Direction.EAST, Direction.WEST)){
			while(m != null){
				total += m.getXLength();
				m = m.getNeighbor(dir);
			}
		}

		return total;

	}

	public int getTotalY(Machine m){
		int total = 0;
		for(Direction dir : EnumSet.of(Direction.NORTH, Direction.SOUTH)){
			while(m != null){
				total += m.getYLength();
				m = m.getNeighbor(dir);
			}
		}

		return total;

	}

	public void place(int x, int y, Machine m){
		m.setLocation(new Point(x, y));
		int xBound = x + m.getXLength();
		int yBound = y + m.getYLength();
		for (int i = y; i < yBound; i++) {
			for (int j = x; j < xBound; j++) {
				ground[i][j] = m;
			}
		}
		placeds.add(m);
	}

	public void place(Point p, Machine m){
		place(p.x, p.y, m);
	}

	public boolean testPlaceable(int x, int y, Machine m){
		int xBound = x + m.getXLength();
		int yBound = y + m.getYLength();
		for (int i = y; i < yBound; i++) {
			for (int j = x; j < xBound; j++) {
				if(ground[i][j] != null){
					return false;
				}
			}
		}
		return true;
	}

	public boolean testPlaceable(Point p, Machine m){
		return testPlaceable(p.x, p.y, m);
	}

	public void remove(Machine m){

		Point p = m.getLocation();
		int x = p.x;
		int y = p.y;

		int xBound = x + m.getXLength();
		int yBound = y + m.getYLength();

		for (int i = y; i < yBound; i++) {
			for (int j = x; j < xBound; j++) {
				ground[i][j] = null;
			}
		}
		m.setLocation(null);

	}

	private Point pointForCentering(int x, int y, Machine m){
		return new Point(
				x - m.getXLength()/2,
				y - m.getYLength()/2);
	}

	protected Point getInitialPoint(Machine m){
		int y = ground.length / 2;
		int x = ground[y].length / 2;
		return pointForCentering(x, y, m);
	}

	//	public static class AroundAMachineIterator implements Iterator<Point>{
	//
	//		private Machine newMach;
	//		private Machine fixedMach;
	//		
	//		private int counter = 0;
	//		
	//		public AroundAMachineIterator(Machine newMach, Machine fixedMach) {
	//			this.newMach = newMach;
	//			this.fixedMach = fixedMach;
	//			
	//			
	//		}
	//
	//		@Override
	//		public boolean hasNext() {
	//			// TODO Auto-generated method stub
	//			return counter < 2 * (fixedMach.getXLength() + fixedMach.getYLength()) + 4;
	//		}
	//
	//		@Override
	//		public Point next() {
	//			Point result;
	//			Point basePoint = fixedMach.getLocation();
	//			
	//			result = new Point(basePoint.x + , basePoint.y);
	//			
	//			return result;
	//		}
	//		
	//	}

}
