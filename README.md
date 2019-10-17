# jScrap - Web scraper written in Java

Dependencies: htmlunit-2.36.0; json-20190722

This scraper takes input json file with configuration and produces json output with scraped result. 
Runs JavaScript and is able to click buttons before scraping the page (read more links etc.)

Currently it can scrape only from given url adresses and does not navigate trought the website.

usage: java -jar jScrap.jar input.json output.json

## input.json example
```
{
"SingleScrapes": [{
	"url": "https://www.kurzy.cz/kurzy-men/",
	"runJavaScript": "true",
	"waitForJavaScript": "3000",
	"dataItems": [
			{
			"id": "kurzyMen",
			"scrapData": {
				"mena": "//table[@class=\"pd pdw\"]/tbody[1]/tr[@class=\"pl\" or @class=\"ps\"]/td[3]/a/text()",
				"hodnotaVcera": "//table[@class=\"pd pdw\"]/tbody[1]/tr[@class=\"pl\" or @class=\"ps\"]/td[5]/text()",
				"hodnotaDnes": "//table[@class=\"pd pdw\"]/tbody[1]/tr[@class=\"pl\" or @class=\"ps\"]/td[7]/text()"
			}
			}				
		]
	}
	],

"Settings": {
	"logging": "false",
	"timeout": "5000",
	"delay": "1000"		
	}
}
}
```
