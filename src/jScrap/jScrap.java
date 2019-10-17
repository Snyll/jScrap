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
	static public String outputJSON;
	static {
		java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF); 
		System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
	}
	
	public static void main(String[] args) {
		if (args.length != 0) {
			if (args[0] != null) {
				jScrap.outputJSON = args[0];
			}
			if (args[1] != null) {
				jScrap.outputJSON = args[1];
			}
		}
		if (jScrap.inputJSON == null) {
			jScrap.inputJSON = "input.json";
		}
		if (jScrap.outputJSON == null) {
			jScrap.outputJSON = "output.json";
		}
		logger = new Logger("jScrapLogs" + File.separator + (new File(inputJSON).getName()) + "_scraper_" + Instant.now() + ".log", true);
		Scraper scraper = new SingleScraper();
	}

}
