package utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class AgentsConfig{

	public AgentDescription[] agents;

	public AgentsConfig(AgentDescription[] agents){
		this.agents = agents;
	}

	public AgentsConfig(AgentsConfig a, AgentsConfig b){
		this.agents = new AgentDescription[a.agents.length+b.agents.length];
		System.arraycopy(a.agents, 0,agents,0, a.agents.length);
		System.arraycopy(b.agents, 0,agents,a.agents.length, b.agents.length);
	}

	public static boolean saveJson(AgentsConfig o, String path){
		try (Writer w = new FileWriter(path)){
			Gson writer = new GsonBuilder().setPrettyPrinting().create();
			writer.toJson(o, w);
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static AgentsConfig readJson(String path){
		AgentsConfig playerDataRead = null;
		try (Reader r = new FileReader(path)) {
			Gson parser = new Gson();
			playerDataRead = parser.fromJson(r, AgentsConfig.class);
		}catch (Exception e){
			e.printStackTrace();
		}
		return playerDataRead;
	}

	public static void main(String[] args){
		String fileName = "roundtwo";
//		AgentsConfig c = new AgentsConfig(
//				new AgentDescription[]{
//						new AgentDescription("RND",null),
//						new AgentDescription("RND",null),
//						new AgentDescription("RND",null),
//						new AgentDescription("META-RHEA",new int[]{2,1,3,3,1})
//				}
//		);
		AgentsConfig c = new AgentsConfig(
				new AgentDescription[]{
						new AgentDescription("RHEA",new int[]{1,1,3,0,1,0,2,1,1,0})
				}
		);
		AgentsConfig.saveJson(c, "agents/"+fileName+".json");
	}

}
