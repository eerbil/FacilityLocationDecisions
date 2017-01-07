import java.awt.Point;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;



public class Facility {

	Machine[][] ground;

	Map<PositionlessPair<Machine, Machine>,Closeness> closenesses;

	Deque<Machine> placeds = new LinkedList<>();
	Queue<Machine> remainings = new PriorityQueue<>(Collections.reverseOrder());

	StringBuilder log = new StringBuilder();

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

	public void placeAll(){
		placeAll(0);
	}

	protected void placeAll(int count){ // Priority according to TCR

		logWriteln("***** Placement Attempt: "+ (count + 1) +"  *****");
		double totalScore = 0;
		Machine currentMachine = remainings.poll(); // First Highest TCR
		Point coordinate = calculateInitialPoint(currentMachine, count);

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
		logWriteln("Placing the First Machine: " + currentMachine
				+ " with Highest TCR (" + currentMachine.getTcr() + ")"
				+ " Initial Coordinates: (" + coordinate.x +"," + coordinate.y + ")" );
		place(coordinate, currentMachine);
		logWriteln(toString());
		logWriteln("***************");

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
				logWriteln("Placing Machine: " + currentMachine
						+ " with Score: " + firstCandidate.getScore());
				
				place(firstCandidate.getLocation(), currentMachine);
				
				totalScore += firstCandidate.getScore();
				logWriteln(toString());
				logWriteln("***************");
			}else{  // No place to put machine
				placeAllFailed(count);
				return;
			}
		}
		logWriteln("***** All machines are successfully placed to " 
				+ ground[0].length + "x" + ground.length 
				+ " facility with Total Score: " + totalScore + " *****");

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
		Point distVector = new Point(m1.getLocation().x - m2.getLocation().x, m1.getLocation().y - m2.getLocation().y);
		int xLenOfLowerX = (distVector.x < 0 ? m1 : m2).getXLength();
		int yLenOfLowerY = (distVector.y < 0 ? m1 : m2).getYLength();

		distVector.x = Math.abs(distVector.x) - xLenOfLowerX;
		distVector.y = Math.abs(distVector.y) - yLenOfLowerY;

		int cornerPenalty = (distVector.x == 0 && distVector.y == 0) ? 1 : 0;

		distVector.x = Math.max(distVector.x, 0);
		distVector.y = Math.max(distVector.y, 0);

		return distVector.x + distVector.y + cornerPenalty + 1;	

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

	private void rollBackPlacements(){
		for (Machine machine : placeds) {
			machine.setLocation(null);
			machine.deleteAllCandidatePlacements();
		}
		placeds = new LinkedList<>();
		remainings = new PriorityQueue<>(Collections.reverseOrder());
		ground = new Machine[ground.length][ground[0].length];
		configureMachines(closenesses);
	}

	private void placeAllFailed(int countBeforeFail){
		switch (countBeforeFail) {
		case 0:
			logWriteln("***** Attempt failed. Starting Placement from scratch. *****");
			rollBackPlacements();
			placeAll(countBeforeFail + 1);
			break;

		default:
			logWriteln("***** No suitable placement is found, terminating. *****");
			break;
		}


	}

	private Point pointForCentering(int x, int y, Machine m){
		return new Point(
				x - m.getXLength()/2,
				y - m.getYLength()/2);
	}

	protected Point calculateInitialPoint(Machine m, int strategy){

		switch (strategy) {
		case 0:
			int y = ground.length / 2;
			int x = ground[y].length / 2;
			return pointForCentering(x, y, m);
		default:
			return new Point(0,0);
		}
	}

	protected int calculateTcrForMachine(Machine m){
		int total = 0;
		for (PositionlessPair<Machine, Machine> machines : closenesses.keySet()) {
			if(machines.contains(m)){
				total += closenesses.get(machines).getValue();
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
				Machine current = ground[i][j];
				sb.append(current == null ? "-" : current).append("\t");
			}
			sb.append(nl);
		}
		return sb.toString();
	}

	public String getLog(){
		return log.toString();
	}

	private void logWrite(String str){
		log.append(str);
	}

	private void logWriteln(String str){
		logWrite(str + System.lineSeparator());
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
