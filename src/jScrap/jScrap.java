package jScrap;

import java.io.File;
import java.time.Instant;
import java.util.List;
import java.util.logging.Level;

import jScrap.Logger;
import jScrap.Scraper;
import jScrap.WebParser;

public class jScrap {
	static public String inputJSON;
	static public Logger logger;
	
	static {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}
	
	public static void main(String[] args) {
		jScrap.inputJSON = inputJSON;
		jScrap.inputJSON = "input.json";
		logger = new Logger("jScrapLogs" + File.separator + (new File(inputJSON).getName()) + "_scraper_" + Instant.now() + ".log", true);
		Scraper scraper = new SingleScraper();
		
        
	}

}
