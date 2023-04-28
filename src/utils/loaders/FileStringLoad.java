package utils.loaders;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileStringLoad {

	public static String fromPath(String path){
		StringBuilder contentBuilder = new StringBuilder();

		try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {

			stream.forEach(s -> contentBuilder.append(s).append("\n"));
		} catch (IOException e) {
			//handle exception
		}

		return contentBuilder.toString();
	}

	public static void main(String[] args) {
		System.out.println(fromPath("assets/RinascimentoParams.json"));
	}

}
