//dir for CD in command line//  C:\Users\josep\Google Drive\School\SPRING 2018\Software Engineering CSC-131\Time Manag\src

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * The TM class allows a user to track time worked on multiple tasks, and print summaries of the time worked.
 * 
 * Joseph Donati - CSUS - CSC131-03 - Sprint 1 - 2/4/2018 - Professor D. Posnett
 */
public class TM {
	// main only used to pass args to appMain, gets rid of static issues
	public static void main(String [] args) throws IOException {
		TM tm = new TM();
		tm.appMain(args);
	}
	
	// appMain is where program actually runs, not static
	void appMain(String [] args) throws IOException {
		
		// Declare variables
		String cmd;
		String taskName;
		String description;
		
		// Create TaskLog to interact with persistent log
		TaskLog log = new TaskLog();
		
		// Create object to pull time codes
		LocalDateTime currentTime = LocalDateTime.now();
		
		// Rule out unacceptable usage, if incorrect display usage instructions
		try {		
			cmd = args[0];
			if (args.length < 2 && cmd.equals("summary"))
				taskName = null;
			else 
				taskName = args[1];
			if (args[0].equals("describe")) {
					description = args[2];
			}
			else 
				description = null;		
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			// Usage instructions
			System.out.println("Usage:");
			System.out.println("\tTM start <task name>");
			System.out.println("\tTM stop <task name>");
			System.out.println("\tTM describe <task name> <task description in quotes>");
			System.out.println("\tTM summary <task name>");
			System.out.println("\tTM summary");
			return;
		}
		
		// switch to direct operation
		switch (cmd) {
			case "stop": cmdStop(taskName, log, cmd, currentTime);
						break;
			case "start": cmdStart(taskName, log, cmd, currentTime);
						break;
			case "describe": cmdDescribe(taskName, log, cmd, currentTime, description);
						break;
			case "summary": if (taskName == null)
								cmdSummary(log);
							else
								cmdSummary(log, taskName);
						break;
		}
		
	}
	
	
	/**
	 * cmdStart method prints start entry to log
	 * @param taskName The name of the task
	 * @param log The TaskLog object wrapping the persistent log
	 * @param cmd The command supplied by the user
	 * @param currentTime The current time at log entry
	 * @throws IOException
	 */
	void cmdStart(String taskName, TaskLog log, String cmd, LocalDateTime currentTime) throws IOException {
		String line = (currentTime + "/" + taskName + "/" + cmd);
		log.writeLine(line);
	}
	
	/**
	 * cmdStop method prints stop entry to log
	 * @param taskName The name of the task
	 * @param log The TaskLog object wrapping the persistent log
	 * @param cmd The command supplied by the user
	 * @param currentTime The current time at log entry
	 * @throws IOException
	 */
	void cmdStop(String taskName, TaskLog log, String cmd, LocalDateTime currentTime) throws IOException {
		String line = (currentTime + "/" + taskName + "/" + cmd);
		log.writeLine(line);
	}
	
	/**
	 * cmdDescribe method prints describe entry to log
	 * @param taskName The name of the task
	 * @param log The TaskLog object wrapping the persistent log
	 * @param cmd The command supplied by the user
	 * @param currentTime The current time at log entry
	 * @param description The description of the task provided by the user
	 * @throws IOException
	 */
	void cmdDescribe(String taskName, TaskLog log, String cmd, LocalDateTime currentTime, String description) throws IOException {
		String line = (currentTime + "/" + taskName + "/" + cmd + "/" + description);
		log.writeLine(line);
	}
	
	//NOTE: THIS METHOD UNFINISHED
	/**
	 * The cmdSummary method prints a summary of all tasks in log
	 * @param log The TaskLog object wrapping the persistent log
	 * @throws FileNotFoundException
	 */
	void cmdSummary(TaskLog log) throws FileNotFoundException {
		//System.out.println("WHOLE SUMMARY");
		LinkedList<TaskLogEntry> allLines = log.readFile();
		Iterator<TaskLogEntry> it = allLines.iterator();
		while(it.hasNext())
			System.out.println(it.next());        // For testing   
		
	}
	
	/**
	 * The cmdSummary method prints a summary of a task from log
	 * @param log The TaskLog object wrapping the persistent log
	 * @param task The individual task to provide summary for
	 * @throws FileNotFoundException
	 */
	void cmdSummary(TaskLog log, String task) throws FileNotFoundException {

		// Read log file, gather entries
		LinkedList<TaskLogEntry> allLines = log.readFile();
		
		// Create Task object based on task name, with entries from log
		Task taskToSummarize = new Task(task, allLines);
		
		// Display 
		System.out.println(taskToSummarize.toString());
		
	}
	
	
	
	/**
	 * The TaskLog class has methods for writing and reading from a persistent log file
	 */
	public class TaskLog {
		
		/*Reference note* Needed a refresher on file I/O, used method from: Starting out with Java, 2nd Edition, Gaddis + Muganda, print*/
		
		// Create FileWriter and PrintWriter to write to log file
		public FileWriter fwriter;
		public PrintWriter outputFile;
		
		
		/**
		 * Constructor, creates file I/O objects
		 */
		public TaskLog() throws IOException{
			fwriter = new FileWriter("TM.txt", true);
			outputFile = new PrintWriter(fwriter);
		}
		
		/**
		 * The writeLine method writes a line to the persistent log file
		 * @param line The line to write to the log file
		 * @throws IOException
		 */
		void writeLine(String line) throws IOException{
			outputFile.println(line);
			/* System.out.println("TESTHERE, line: " + line); // Testing: Uncomment to see the line being written to log file in console */
			outputFile.close();
		}		
		
		/**
		 * The readFile method reads a persistent log file into the program
		 * @return LinkedList holding ArrayList corresponding to each line in the log, with each token in the line stored as an element of the ArrayList
		 * @throws FileNotFoundException
		 */
		LinkedList<TaskLogEntry> readFile() throws FileNotFoundException {
			
			// Create LinkedList of TaskLogEntry objects to hold each entry in log file
			LinkedList<TaskLogEntry> lineList = new LinkedList<TaskLogEntry>();
			
			// Open file for reading
			File logFile = new File("TM.txt");
			Scanner inputFile = new Scanner(logFile);
			
			// Process each line in the string
			String inLine;
			while(inputFile.hasNextLine()) {
				TaskLogEntry entry = new TaskLogEntry();
				inLine = inputFile.nextLine();
				StringTokenizer st = new StringTokenizer(inLine, "/");
				entry.timeStamp = LocalDateTime.parse(st.nextToken());
				entry.taskName = st.nextToken();
				entry.cmd = st.nextToken();
				// If there are more than 3 tokens, 4th token is description
				if (st.hasMoreTokens())
					entry.description = st.nextToken();
				
				// Add entry to LinkedList
				lineList.add(entry);
			}
			
			// Close Scanner and return LinkedList
			inputFile.close();
			return lineList;
		}
	}
	
	/**
	 * The TaskLogEntry class holds information for a single entry in a TaskLog
	 */
	class TaskLogEntry {
		String cmd;
		String taskName;
		String description;
		LocalDateTime timeStamp;
		
	}
	
	/**
	 * The TimeUtilities class contains utilities for working with time related data
	 */
	static class TimeUtilities {
		
		/**
		 * The secondsFormatter method converts seconds to the HH:MM:SS format
		 * @param secondsToFormat The number of seconds to convert
		 * @return String containing time in HH:MM:SS format
		 */
		 static String secondsFormatter(long secondsToFormat) {
			
			long hours = (secondsToFormat / 3600);
			long minutes = ((secondsToFormat % 3600) / 60);
			long seconds = (secondsToFormat % 60);
			String formattedTime = (String.format("%02d:%02d:%02d", hours, minutes, seconds));
			return formattedTime;
			
		}
	}
	
	class Task {
		// Each task can be identified by name
		private String name;
		private String description;
		private String formattedTime = null;
		
		/**
		 * Constructor runs and gathers all task data from log entries
		 * @param name The name of the task
		 * @param entries The list of entries in the log
		 */
		public Task(String name, LinkedList<TaskLogEntry> entries) {
			// Initialize necessary variables 
			this.name = name;
			this.description = null;
			LocalDateTime lastStart = null;
			long timeElapsed = 0;
			
			
			for (TaskLogEntry entry: entries){
				if (entry.taskName.equals(name)) {
					switch (entry.cmd) {
					case "start":
						lastStart = entry.timeStamp;
						break;
					case "stop":
						if (lastStart != null) 
							timeElapsed += calculateDuration(lastStart, entry.timeStamp);
						lastStart = null;
						break;
					case "describe": 
						description = entry.description;
					}
				}
			}
			// Format the elapsed time, parse to String
			this.formattedTime = TimeUtilities.secondsFormatter(timeElapsed);
		}
		
		public String toString() {
			String str = ("\nSummary for task:\t| " + this.name + "\nDescription:\t\t| " + this.description + "\nDuration\t\t| " + this.formattedTime);
			return str;
		}
		
		/**
		 * The calculateDuration method calculates the duration between to LocalDateTime time stamps
		 * @param startTime The start time
		 * @param stopTime The start time
		 * @return The number of seconds elapsed between start/stop stamps
		 */
		long calculateDuration(LocalDateTime startTime, LocalDateTime stopTime) {
			long duration = ChronoUnit.SECONDS.between(startTime, stopTime);
			return duration;
		}
	}
}

// Could add in summary: Ability to show if still recording task, e.g. Start read in with no Stop after (boolean isRunning on with start, off with stop?)

