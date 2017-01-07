import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Scanner;


public class Init {
	private static final String FACILITY_LOG_FILE_NAME = "facility_log.txt";

	public static void main(String[] args) {
		try(
				PrintStream ps = new PrintStream("facility_input_errors.txt");
				BufferedWriter bw = new BufferedWriter(new FileWriter(FACILITY_LOG_FILE_NAME));
				ConfigReader cr = new ConfigReader(new Scanner(new File("facility_input.txt")));){
			cr.readAllAndConfig();
			ps.print(cr.getMessage());
			Facility fc = cr.getFacility();
			fc.configureMachines(cr.getClosenesses());
			fc.placeAll();
			bw.write(fc.getLog());
			System.out.println("Done. Please check \""+ FACILITY_LOG_FILE_NAME +"\"");
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
}
