package jScrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jScrap.JsonHandler;

public abstract class Scraper {
	private ArrayList<String> errorMessages = new ArrayList<>(); 
	private HashMap<String, String> settings = new HashMap<>();
	private JSONObject jsonObject;
	// queue of RawScrap items prepared before scraping
	private Queue<RawScrap> scrapeQueue = new LinkedList<>();
	// HashMap of the scrapped results (to be saved into JSON file)
	private HashMap<String, ArrayList<Scrap>> scrapedDataset = new HashMap<>();
	
	
	public Scraper() {
		// load input JSON file
		JsonHandler json = new JsonHandler(jScrap.inputJSON);
		json.loadFromFile();
		jsonObject = json.getJSON();
		if (jsonObject == null) {
			addErrorMessage("Input JSON file was not loaded correctly.");			
			return;
		}
		// parse settings from input file
		JSONObject JSONsettings = jsonObject.getJSONObject("Settings");
		if (JSONsettings != null) {
			Iterator<String> keys = JSONsettings.keys();
			while(keys.hasNext()) {
			    String key = keys.next();
			    setSetting(key, JSONsettings.get(key).toString());
			    jScrap.logger.log("Setting " + key + "=" + JSONsettings.get(key).toString());
			}
		}
		
		LoadScraperInput();
		Scrape();
		saveJSON();
	}
	
	
	abstract void LoadScraperInput();
	
	public void Scrape() {
		WebParser webParser = new WebParser(this);
	}
	
	public void setSetting(String key, String value) {
		this.settings.put(key, value);
	}
	
	public String getSetting(String key) {
		return this.settings.get(key);
	}
	
	public ArrayList<String> getErrorMessages() {
		return errorMessages;
	}

	public void addErrorMessage(String errorMessage) {
		jScrap.logger.log("Error: " + errorMessage);
		this.errorMessages.add(errorMessage);
	}
	
	public void addToQueue(RawScrap rs) {
		scrapeQueue.add(rs);
	}
	
	public RawScrap takeFromQueue() {
		return scrapeQueue.poll();
	}
	
	public boolean isQueueEmpty() {
		return scrapeQueue.isEmpty();
	}
	
	public JSONObject getJsonObject() {
		return jsonObject;
	}
	
	public void addResultData(String id, Scrap value) {
		ArrayList<Scrap> tempList = scrapedDataset.get(id);
		if (tempList == null) {
			tempList = new ArrayList<>();
		}
		tempList.add(value);
		this.scrapedDataset.put(id, tempList);
		jScrap.logger.log("Adding new result data: " + id + " - " + value.toString() + "\n");
	}
	
	public class Scrap {
		private String url;
		private String dataItemId;
		private HashMap<String, List<String>> scrapedData = new HashMap<>();
		private float scrapingTime;
		
		public Scrap(String dataItemId, String url) {
			this.dataItemId = dataItemId;
			this.url = url;
		}
		public void addData(String id, String data) {
			List<String> tempList = scrapedData.get(id);
			if (tempList == null) {
				tempList = new ArrayList<>();
			}
			tempList.add(data);
			this.scrapedData.put(id, tempList);
		}
		public void setData(String id, List<String> list) {
			this.scrapedData.put(id, list);
		}
		public HashMap<String, List<String>> getData() {
			return scrapedData;
		}
		
		@Override
		public String toString() {
			String str = "\n ------------------------------------------------- \n url:" + this.url + 
					"\n DataItem name: " + this.dataItemId + "\n";
			Iterator<String> keysDataItems = this.getData().keySet().iterator();
			while(keysDataItems.hasNext()) {					
				String keyDataItems = keysDataItems.next();
				str += "\n " + keyDataItems + ": " + this.getData().get(keyDataItems);
			}
			str += "\n -------------------------------------------------";
			return str;
		}
	}
	
	private void saveJSON() {
		JsonHandler json = new JsonHandler(jScrap.outputJSON);
		json.buildJSONfromMap(scrapedDataset);
		json.saveToFile();
	}
	
	// scrap data before html parsing is done
	public class RawScrap {
		// url that we are going to scrap
		private String url;
		// in HashMap we have dataItem name and xpath string for scraping
		private HashMap<String, HashMap<String, String>> dataItems = new HashMap<>();
		
		private boolean runJavaScript;
		private ArrayList<String> clickBeforeScrape;
		private int waitForJavaScript;
		
		public RawScrap(String url, HashMap<String, HashMap<String, String>> dataItems, boolean runJavaScript,
				ArrayList<String> clickBeforeScrape, int waitForJavaScript) {
			this.url = url;
			this.dataItems = dataItems;
			this.runJavaScript = runJavaScript;
			this.clickBeforeScrape = clickBeforeScrape;
			this.waitForJavaScript = waitForJavaScript;
		}

		public String getUrl() {
			return url;
		}

		public HashMap<String, HashMap<String, String>> getDataItems() {
			return dataItems;
		}

		public boolean isRunJavaScript() {
			return runJavaScript;
		}

		public ArrayList<String> getClickBeforeScrape() {
			return clickBeforeScrape;
		}

		public int getWaitForJavaScript() {
			return waitForJavaScript;
		}
		
	}	
}