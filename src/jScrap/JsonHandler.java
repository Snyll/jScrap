package jScrap;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.json.JSONObject;

public class JsonHandler {
	private String JSONFile;
	private JSONObject JSON;
	
	public JsonHandler(String jSONFile) {
		JSONFile = jSONFile;
	}
	
	public JSONObject getJSON() {
		return JSON;
	}

	public void buildJSONfromMap(Map<String, ArrayList<String>> JSONMap) {
		if (JSONMap.isEmpty()) { 
			return;
		}
		this.JSON = new JSONObject(JSONMap);
	}
	
	public void loadFromFile() {	
		if (JSONFile == null) { 
			return;
		}
		try (Stream<String> lines = Files.lines(Paths.get(JSONFile))) {
            // UNIX \n, WIndows \r\n
            String content = lines.collect(Collectors.joining(System.lineSeparator()));
            JSONObject jo = new JSONObject(content);
            this.JSON = jo;
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public void saveToFile() {
		PrintWriter out;
		if (!JSON.isEmpty()) {
			try {
				out = new PrintWriter(this.JSONFile);
				out.println(JSON.toString());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}
	}
	
}