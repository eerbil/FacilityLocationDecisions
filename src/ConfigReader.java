import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class ConfigReader implements Closeable{

	private static final String LINE_SEPARATOR = System.lineSeparator();

	private Scanner sc;
	private StringBuilder message = new StringBuilder();

	private Map<String, Machine> machines = new HashMap<>();
	private Facility facility;
	HashMap<PositionlessPair<Machine,Machine>, Closeness> closenesses = new HashMap<>();

	public ConfigReader(Scanner sc) {
		this.sc = sc;
	}

	public void readAllAndConfig(){
		readLayoutSize();
		readMachines();
		readClosenesses();
	}
	public void readLayoutSize(){
		String[] nextLine = sc.nextLine().split(" ");
		facility = new Facility(Integer.parseInt(nextLine[0]), Integer.parseInt(nextLine[1]));
		sc.nextLine(); // Just pass a line
	}

	public void readMachines(){
		String nextLine = "";
		while(!(nextLine = sc.nextLine()).isEmpty()){			
			try {
				String[] machineProps = nextLine.split(" ");
				Machine currentMach = new Machine(
						machineProps[0],
						Integer.parseInt(machineProps[1]),
						Integer.parseInt(machineProps[2]));

				if(machines.putIfAbsent(machineProps[0], currentMach) != null){
					messageAppendln("Machine Names must be unique.");
				}
			} catch (Exception e) {
				messageAppendln("The input could not be processed. (At Machine Creation)");
			}
		}
	}


	public void readClosenesses(){
		String nextLine = "";
		while(!(nextLine = sc.nextLine()).isEmpty()){			
			try {
				String[] clsConfigs = nextLine.split(" ");
				closenesses.put(new PositionlessPair<Machine, Machine>(
						machines.get(clsConfigs[0]),
						machines.get(clsConfigs[1])),
						Closeness.valueOf(clsConfigs[2]));
				if(!sc.hasNextLine()){
					break;
				}
			} catch (Exception e) {
				messageAppendln("The input could not be processed. (At Closeness Setting)");
			}
		}
	}

	public Facility getFacility() {
		return facility;
	}
	public HashMap<PositionlessPair<Machine, Machine>, Closeness> getClosenesses() {
		return closenesses;
	}

	public String getMessage(){
		return message.toString();
	}
	private void messageAppend(String str) {
		message.append(str);
	}
	private void messageAppendln(String str){
		messageAppend(str + LINE_SEPARATOR);
	}

	@Override
	public void close() throws IOException {
		if(sc != null){
			sc.close();
		}

	}
}
