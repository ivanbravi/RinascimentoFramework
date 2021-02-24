package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.util.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;

public class GsonTest {

	public static void main(String[] args){

		String path = "agents.json";

		AgentsConfig playerData = new AgentsConfig(new AgentDescription[]{new AgentDescription("RHEA",new int[]{0,0,0,0,0,0}),
			new AgentDescription("MCTS",new int[]{0,0,0,0,0,0}),
			new AgentDescription("SRHEA", new int[]{1,3}),
			new AgentDescription("OSLA",null)});

		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(playerData, w);
		}catch (Exception e){
			e.printStackTrace();
		}

		AgentsConfig playerDataRead = null;
		try (Reader r = new FileReader(path)) {
			Gson parser = new Gson();
			playerDataRead = parser.fromJson(r, AgentsConfig.class);
		}catch (Exception e){
			e.printStackTrace();
		}

		System.out.println();

	}

}
