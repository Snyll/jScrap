package jScrap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomText;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import jScrap.Scraper;


public class WebParser {
	
	private WebClient webClient;
	
	public WebParser(Scraper scraper) {
		WebClient webClient = new WebClient(BrowserVersion.BEST_SUPPORTED);
		webClient.getOptions().setCssEnabled(false);
		
		while (scraper.isQueueEmpty() == false) {
			
			Scraper.RawScrap rawScrap = scraper.takeFromQueue();			
			jScrap.logger.log("Starting to parse url " + rawScrap.getUrl());
		    webClient.getOptions().setJavaScriptEnabled(rawScrap.isRunJavaScript());
		    webClient.waitForBackgroundJavaScript(rawScrap.getWaitForJavaScript());
		    webClient.getOptions().setThrowExceptionOnScriptError(false);

		    jScrap.logger.log("Javascript running: " + rawScrap.isRunJavaScript() + " - waiting " + rawScrap.getWaitForJavaScript() + "ms");
		    try {
		    	jScrap.logger.log("Loading page...");
				final HtmlPage page = webClient.getPage(rawScrap.getUrl());
				
				if (rawScrap.getClickBeforeScrape() != null && !rawScrap.getClickBeforeScrape().isEmpty() && rawScrap.isRunJavaScript()) {
					for (String clickElement : rawScrap.getClickBeforeScrape()) {
						HtmlElement htmlElement = page.getFirstByXPath(clickElement);
						htmlElement.click();
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
			    }
				
				jScrap.logger.log("Page loaded...");
				Iterator<String> keysRawScraps = rawScrap.getDataItems().keySet().iterator();
				while(keysRawScraps.hasNext()) {					
					String keyRawScrap = keysRawScraps.next(); // dataItemId
					jScrap.logger.log("Parsing defined dataItem from input file: " + keyRawScrap);
					Scraper.Scrap scrap = scraper.new Scrap(keyRawScrap, rawScrap.getUrl());
					
					// go trough dataitems records
					Iterator<String> keysDataItems = rawScrap.getDataItems().get(keyRawScrap).keySet().iterator();
					while(keysDataItems.hasNext()) {					
						String keyDataItems = keysDataItems.next();
						final List<DomNode> parseResult = page.getByXPath(rawScrap.getDataItems().get(keyRawScrap).get(keyDataItems));
						
						for (DomNode e : parseResult) {
							scrap.addData(keyDataItems, e.getTextContent().trim());
							jScrap.logger.log("New data pair saved: " + keyDataItems + " - " +  e.getTextContent().trim());						
						}
					}					
					jScrap.logger.log(scrap.toString());
					
				}				
		    } catch (FailingHttpStatusCodeException e) {
				e.printStackTrace();
			} catch (MalformedURLException e) {	
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		    
		}
		
	}
}
