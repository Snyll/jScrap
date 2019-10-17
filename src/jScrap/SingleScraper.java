package jScrap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jScrap.Scraper.RawScrap;

public class SingleScraper extends Scraper {

	public SingleScraper() {
		super();
	}
	
	@Override
	public void LoadScraperInput() {
		JSONObject jsonObject = super.getJsonObject();
		
		String urlSingle = "";
		boolean runJavaScriptSingle = true;
		int waitForJavaScriptSingle = 3000;
		ArrayList<String> clickBeforeScrapeSingle = new ArrayList<>();
		
		HashMap<String, HashMap<String, String>> dataItems = new HashMap<>();
		
		JSONArray JSONsingle = jsonObject.getJSONArray("SingleScrapes");
		if (JSONsingle != null) {
			for (int i = 0; i < JSONsingle.length(); i++) {
				
				JSONObject o = JSONsingle.getJSONObject(i);
				Iterator<String> keys = ((JSONObject)o).keys();
				while(keys.hasNext()) {
				    String key = keys.next();
				    try {
					    if (key.equals("url")) {
					    	urlSingle = o.getString(key);
					    }
					    if (key.equals("runJavaScript")) {					    	
					    	runJavaScriptSingle = o.getBoolean(key);					    	
					    }
					    if (key.equals("waitForJavaScript")) {					    	
					    	waitForJavaScriptSingle = o.getInt(key);
					    }
					    if (key.equals("clickBeforeScrape")) {					    	
					    	JSONArray JSONclickBeforeScrapeSingle = o.getJSONArray(key);
					    	for (Object click : JSONclickBeforeScrapeSingle) {
					    		clickBeforeScrapeSingle.add((String)click);
					    	}
					    }
					    if (key.equals("dataItems")) {
					    	String dataItemId = "";
					    	HashMap<String, String> scrapData = new HashMap<>();
					    	JSONArray JSONdataItemsSingle = o.getJSONArray(key);
					    	for (Object dataItemSingle : JSONdataItemsSingle) {
					    		Iterator<String> keysDataItems = ((JSONObject)dataItemSingle).keys();
					    		while(keysDataItems.hasNext()) {
					    			String keyDataItems = keysDataItems.next();
					    			if (keyDataItems.equals("id")) {
					    				dataItemId = ((JSONObject)dataItemSingle).getString(keyDataItems);
					    			}
					    			if (keyDataItems.equals("scrapData")) {					    				
					    				JSONObject scrapDataJO = ((JSONObject)dataItemSingle).getJSONObject(keyDataItems);	
								    	Iterator<String> keysScrapData = scrapDataJO.keys();
										while(keysScrapData.hasNext()) {
											String keyScrapData = keysScrapData.next();
											scrapData.put(keyScrapData, scrapDataJO.getString(keyScrapData));
										}
					    			}
					    		}
					    	}
					    	dataItems.put(dataItemId, scrapData);
					    }					    
				    } catch (JSONException e) {
				    	addErrorMessage("Wrong format of input JSON parameters.");
			    		e.printStackTrace();
			    	}
				}
				// add new url for scraping into our queue
				super.addToQueue(new RawScrap(urlSingle, dataItems, runJavaScriptSingle, clickBeforeScrapeSingle,
						waitForJavaScriptSingle));
			    jScrap.logger.log("Adding url to queue for scraping: " + urlSingle);
			}
		}
	}

}
