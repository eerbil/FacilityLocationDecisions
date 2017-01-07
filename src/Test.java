import java.util.HashMap;


public class Test {

	public static void main(String[] args) {
		Facility f = new Facility(15, 15);
		Machine[] s = {
				new Machine("1", 3, 5),
				new Machine("2", 2, 1),
				new Machine("3", 4, 4),
				new Machine("4", 1, 1),
		};
		HashMap<PositionlessPair<Machine,Machine>, Closeness> hm = new HashMap<>();
		hm.put(new PositionlessPair<Machine, Machine>(s[0], s[1]), Closeness.I);
		hm.put(new PositionlessPair<Machine, Machine>(s[0], s[2]), Closeness.O);
		hm.put(new PositionlessPair<Machine, Machine>(s[0], s[3]), Closeness.U);
		hm.put(new PositionlessPair<Machine, Machine>(s[1], s[2]), Closeness.E);
		hm.put(new PositionlessPair<Machine, Machine>(s[1], s[3]), Closeness.I);
		hm.put(new PositionlessPair<Machine, Machine>(s[2], s[3]), Closeness.A);
		f.configureMachines(hm);
		f.placeAll();
		System.out.println(f);
		
	}

}
