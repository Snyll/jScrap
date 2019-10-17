package jScrap;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class Logger {
	private String fileName;
	private FileWriter fr;
	private DateTimeFormatter formatter;
	private boolean logsEnabled;
	public Logger(String fileNameArg, boolean logsEnabled) {
		this.formatter =
			    DateTimeFormatter.ofLocalizedDateTime( FormatStyle.SHORT )
			                     .withLocale( Locale.UK )
			                     .withZone( ZoneId.systemDefault() );
		this.logsEnabled = logsEnabled;
		
		if (logsEnabled == false) {
			return;
		}
		if (fileNameArg.equals("")) {
			this.fileName = "log.log";
		} else {
			this.fileName = fileNameArg;
		}
		
		File file = new File(this.fileName);
		file.getParentFile().mkdirs();
		
		if (file.delete()) {
			System.out.println("Old log file deleted. Creating new one.");
			try {
				this.fr = new FileWriter(file, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void log(String message) {
		if (this.logsEnabled == false) {
			return;
		}
		try  {
			String currTimeStamp = this.formatter.format(Instant.now());
			System.out.println(currTimeStamp + ": " + message);
			fr = new FileWriter(this.fileName, true);			
		    fr.write(currTimeStamp + ": " + message + "\n");
		    this.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void close() {
		try  {			
		    fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
