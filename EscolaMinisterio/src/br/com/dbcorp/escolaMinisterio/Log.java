package br.com.dbcorp.escolaMinisterio;

import java.io.IOException;
import java.util.Calendar;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

public class Log {
	
	private static Log log;
	
	private Logger logger;
	
	private Log() {
		String type = "DEBUG";
		String path = null;
		
		try {
			type = IniTools.obterValor("logType");
			path = IniTools.obterValor("logPath");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		String PATTERN = "%d [%c{1}] %-5p %m%n";
		
		ConsoleAppender console = new ConsoleAppender(); //create appender
		console.setLayout(new PatternLayout(PATTERN)); 
		console.setThreshold(Level.ALL);
		console.activateOptions();
		
		//add appender to any Logger (here is root)
		Logger.getRootLogger().addAppender(console);
		
		if (!"DEBUG".equalsIgnoreCase(type) && path != null) {
			Calendar cl = Calendar.getInstance(); 
			
			path = path + "escola_" + cl.get(Calendar.YEAR) + (cl.get(Calendar.MONTH) + 1) + cl.get(Calendar.DAY_OF_MONTH) + ".log";
			
			FileAppender fa = new FileAppender();
			fa.setName("FileLogger");
			fa.setFile(path);
			fa.setLayout(new PatternLayout(PATTERN));
			fa.setThreshold(Level.ERROR);
			fa.setAppend(true);
			fa.activateOptions();
			
			//add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fa);
		}
		
		this.logger = Logger.getLogger("EscolaLog");
		this.logger.setLevel(Level.ALL);
	}
	
	public static Log getInstance() {
		if (log == null) {
			log = new Log();
		}
		
		return log;
	}
	
	public void debug(String message) {
		this.logger.debug(message);
	}
	
	public void debug(String message, Throwable throwable) {
		this.logger.debug(message, throwable);
	}
	
	public void error(String message) {
		this.logger.error(message);
	}
	
	public void error(String message, Throwable throwable) {
		this.logger.error(message, throwable);
	}
	
	public void info(String message) {
		this.logger.info(message);
	}
	
	public void info(String message, Throwable throwable) {
		this.logger.info(message, throwable);
	}
}
