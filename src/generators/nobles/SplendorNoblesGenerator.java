package generators.nobles;

import game.Parameters;
import game.state.Noble;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SplendorNoblesGenerator implements NoblesGenerator{
	private static String commentSymbol = "#";
	private static String cvsSplitSymbol = ",";

	@Override
	public Noble[] getNobles(Parameters p) {
		return loadNobles(p.noblesFile());
	}

	private Noble[] loadNobles(String source){
		Noble[] nobles = null;

		BufferedReader br = null;
		String line;

		try {

			br = new BufferedReader(new FileReader(source));
			line = readSkippingComments(br);
			int noblesCount = Integer.parseInt(line.split(cvsSplitSymbol)[0]);
			int suitCount = Integer.parseInt(line.split(cvsSplitSymbol)[1]);

			nobles = new Noble[noblesCount];

			int nobleId=0, points;
			int[] cost = new int[suitCount];

			while ((line = readSkippingComments(br)) != null) {
				String[] slices = line.split(cvsSplitSymbol);

				points = Integer.parseInt(slices[0]);

				fillCost(slices,1,suitCount,cost);

				try {
					nobles[nobleId] = new Noble(nobleId,points,cost);
				}catch (Exception e){
					System.out.println("Invalid nobleId");
					break;
				}

				nobleId++;
			}

			if(nobleId!=noblesCount){
				System.out.println("Invalid number of nobles: "+nobleId+" vs "+noblesCount);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return nobles;
	}

	private String readSkippingComments(BufferedReader b) throws IOException{
		String s;

		do{
			s = b.readLine();
			if(s==null){
				break;
			}
		}while (commentSymbol.equals(s.substring(0,1)));

		return s;
	}

	private void fillCost(String[] values, int start, int end, int[] cost){
		for(int i=start;i<=end; i++){
			cost[i-start] = Integer.parseInt(values[i]);
		}
	}

	public static void main(String[] args){
		String gameVersion = "default";
		String path ="assets/"+gameVersion+"/";
		Parameters p = Parameters.load(path,"parameters.json");
		SplendorNoblesGenerator noblesGenerator = new SplendorNoblesGenerator();
		Noble[] nobles = noblesGenerator.getNobles(p);

		for(int i=0; i<nobles.length; i++)
			System.out.println(nobles[i].toString());

	}

}
