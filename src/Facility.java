import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Facility {

	Machine[][] ground;

	Map<PositionlessPair<Machine, Machine>,Closeness> closenesses;

	//	private int xLen;
	//	private int ylen;

	Deque<Machine> placeds = new LinkedList<>();
	Queue<Machine> remainings = new PriorityQueue<>(Collections.reverseOrder());

	public Facility(int xLen, int yLen) {
		ground = new Machine[yLen][xLen];
	}

	public void configureMachines(Map<PositionlessPair<Machine, Machine>,Closeness> closenessesInput){
		closenesses = new HashMap<>(closenessesInput);

		Set<Machine> machineSet = new HashSet<>();
		for (PositionlessPair<Machine, Machine> machines : closenesses.keySet()) {
			machineSet.add(machines.getS());
			machineSet.add(machines.getT());
		}

		for (Machine machine : machineSet) {
			machine.setTcr(calculateTcrForMachine(machine));
		}


		for (Machine machine : machineSet) {
			remainings.add(machine);
		}

	}

	public void placeAll(){ // Priority according to TCR


		Machine currentMachine = remainings.poll(); // First Highest TCR
		Point coordinate = getInitialPoint(currentMachine);

		int testFailCount = 0;
		while(!testPlaceable(coordinate, currentMachine)){
			switch (testFailCount++ % 3) {
			case 1:
				coordinate.x--;
				break;
			case 2:
				coordinate.y--;
				break;

			default:
				currentMachine.rotate();
				break;
			}
		}
		place(coordinate, currentMachine);


		while (!remainings.isEmpty()) {
			currentMachine = remainings.poll();
			Queue<PlacementCandidate> currentCandidates = currentMachine.getCandidatePlacements();
			for (int i = 0; i < ground.length; i++) {
				for (int j = 0; j < ground[i].length; j++) {
					for (byte doTwice = 0; doTwice < 2; doTwice++) { // For rotating
						if (testPlaceable(j, i, currentMachine)) {
							Point targetCoor = new Point(j, i);
							//The line below is for calculations. Remember to set it to null afterwards!
							currentMachine.setLocation(targetCoor);
							currentCandidates.add(new PlacementCandidate(
									calculateScore(currentMachine),
									targetCoor,
									currentMachine.isRotated()));
							currentMachine.setLocation(null);
						}
						currentMachine.rotate();
					}
				}
			}
			if (!currentCandidates.isEmpty()) {
				PlacementCandidate firstCandidate = currentCandidates.poll();
				if(currentMachine.isRotated() != firstCandidate.isRotated()){
					currentMachine.rotate();
				}
				place(firstCandidate.getLocation(), currentMachine);
			}else{
				//TODO
			}
		}


	}

	private double calculateScore(Machine currentMachine) {

		double score = 0;
		for (Machine placed : placeds) {
			double cr = calculateClosenessRating(currentMachine, placed);
			double dist = calculateMachineDistance(currentMachine, placed);
			score += cr / dist;
		}
		return score;
	}

	/**
	 * 
	 * @param m1
	 * @param m2
	 * @return Manhattan distance between two machines, returns 1 if they are adjacent.
	 */
	private int calculateMachineDistance(Machine m1, Machine m2) {
		Machine[] machines = {m1, m2};
		Point[] centers = new Point[2];
		for (int i = 0; i < machines.length; i++) {
			Machine current = machines[i];
			Point base = current.getLocation();
			centers[i] = new Point(base.x + current.getXLength()/2, base.y + current.getYLength()/2);
		}
		Point distVector = new Point(
				Math.abs(centers[0].x - centers[1].x), 
				Math.abs(centers[0].y - centers[1].y));
		distVector.x = Math.max(distVector.x - (m1.getXLength() + m2.getXLength())/2 , 0);
		distVector.y = Math.max(distVector.y - (m1.getYLength() + m2.getYLength())/2 , 0);
		return Math.max(distVector.x + distVector.y, 1);
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
				if(i >= ground.length || j >= ground[i].length || ground[i][j] != null){
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

		placeds.remove(m);
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

	protected int calculateTcrForMachine(Machine m){
		int total = 0;
		for (PositionlessPair<Machine, Machine> machines : closenesses.keySet()) {
			if(machines.contains(m)){
				total = closenesses.get(machines).getValue();
			}
		}

		return total;
	}

	public int calculateClosenessRating(Machine m1, Machine m2){
		return closenesses.get(new PositionlessPair<>(m1, m2)).getValue();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String nl = System.lineSeparator();
		for (int i = 0; i < ground.length; i++) {
			for (int j = 0; j < ground[i].length; j++) {
				sb.append(ground[i][j]).append("\t");
			}
			sb.append(nl);
		}
		return sb.toString();
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
	//			
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
