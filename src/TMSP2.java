import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeSet;

/**
 * The TM app allows a user to track time worked on multiple tasks, and print summaries of the time worked.
 * 
 * Joseph Donati - CSUS - CSC131-03 - Sprint 2 - 2/14/2018 - Professor D. Posnett
 */
public class TMSP2 {
	// main only used to pass args to appMain, gets rid of static issues
	public static void main(String [] args) throws IOException {
		TMSP2 tm = new TMSP2();
		tm.appMain(args);
	}
	
	// appMain is where program actually runs, not static
	void appMain(String [] args) throws IOException {
		
		// Declare variables
		String cmd;
		String taskName;
		String data1, data2 = null;   // Ignore data2, having problems...
		
		// Create TaskLog to interact with persistent log
		TaskLog log = new TaskLog();
		
		// Create object to pull time codes
		LocalDateTime currentTime = LocalDateTime.now();
		
		// Rule out unacceptable usage, if incorrect display usage instructions
		try {		
			cmd = args[0];
			if (args.length < 2)
				taskName = null;
			else 
				taskName = args[1];
			if (args[0].equals("describe") || args[0].equals("size")) {
				data1 = args[2];
				if (args.length == 4)
					data2 = args[3];				
			}
			else 
				data1 = null;		
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			// Usage instructions
			System.out.println("Usage:");
			System.out.println("\tTM start <task name>");
			System.out.println("\tTM stop <task name>");
			System.out.println("\tTM describe <task name> <task description in quotes> <(optional)task size XS-XXL>");
			System.out.println("\tTM size <task name> <task size XS-XXL>");
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
			case "describe": cmdDescribe(taskName, log, cmd, currentTime, data1, data2);
						break;
			case "size": cmdDescribe(taskName, log, cmd, currentTime, data1, data2);
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
	 * @param data2 
	 * @param description The description of the task provided by the user
	 * @throws IOException
	 */
	void cmdDescribe(String taskName, TaskLog log, String cmd, LocalDateTime currentTime, String data1, String data2) throws IOException {
		String line;
		
		// If data2 is provided, write all data
		if(data2 != null)
			line = (currentTime + "/" + taskName + "/" + cmd + "/" + data1 + "/" + data2);
		// If data2 not provided, only write data 1
		else
			line = (currentTime + "/" + taskName + "/" + cmd + "/" + data1);
		log.writeLine(line);
	}
	
	
	/**
	 * The cmdSummary method prints a summary of all tasks in log
	 * @param log The TaskLog object wrapping the persistent log
	 * @throws FileNotFoundException
	 */
	void cmdSummary(TaskLog log) throws FileNotFoundException {
		
		// Read log file, gather entries
		LinkedList<TaskLogEntry> allLines = log.readFile();
		
		// Gather all task names from log entries, store in Tree Set. Tree set will sort tasks and prevent duplicate summaries for a single task.
		TreeSet<String> names = new TreeSet<String>();
		long totalTime = 0;
		for (TaskLogEntry entry : allLines){
			names.add(entry.taskName);	
		}
		
		// Display each summary individually
		System.out.println("\n--------------------------------------| TASK LOG |--------------------------------------");
		for (String name : names) {
			totalTime += cmdSummary(log, name);
		}
		System.out.println("\n---------------------------------------------------------------------------------------- \nTotal time\t\t|" + TimeUtilities.secondsFormatter(totalTime));
	}
	
	/**
	 * The cmdSummary method prints a summary of a task from log
	 * @param log The TaskLog object wrapping the persistent log
	 * @param task The individual task to provide summary for
	 * @throws FileNotFoundException
	 */
	long cmdSummary(TaskLog log, String task) throws FileNotFoundException {

		// Read log file, gather entries
		LinkedList<TaskLogEntry> allLines = log.readFile();
		
		// Create Task object based on task name, with entries from log
		Task taskToSummarize = new Task(task, allLines);
		
		// Display 
		System.out.println(taskToSummarize.toString());
		return taskToSummarize.totalTime;
		
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
			fwriter = new FileWriter("TM2.txt", true);
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
			File logFile = new File("TM2.txt");
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
				// If cmd is describe, data is description, if cmd is size, data is size.
				if (st.hasMoreTokens())
					entry.data1 = st.nextToken();
				if (st.hasMoreTokens())
					entry.data2 = st.nextToken();
				
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
		String data1;
		String data2;
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
		private StringBuilder description = new StringBuilder("");
		//private String description;
		private String shirtSize;
		private String formattedTime = null;
		private long totalTime = 0;
		
		/**
		 * Constructor runs and gathers all task data from log entries
		 * @param name The name of the task
		 * @param entries The list of entries in the log
		 */
		public Task(String name, LinkedList<TaskLogEntry> entries) {
			// Initialize necessary variables 
			this.name = name;
			LocalDateTime lastStart = null;
			long timeElapsed = 0;
			
			
			for (TaskLogEntry entry : entries){
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
						if (description.toString().equals(""))
							description.append(entry.data1);
						else
							description.append("\n\t\t\t| " + entry.data1);
						
						// Only update shirtSize if not empty to prevent unwanted update
						if (entry.data2 != null)
							shirtSize = entry.data2;
						break;
					case "size": 
						shirtSize = entry.data1;
					}
				}
			}
			// Format the elapsed time, parse to String, store long time for cmdSummary
			this.formattedTime = TimeUtilities.secondsFormatter(timeElapsed);
			this.totalTime = timeElapsed;
		}
		
		
		
		/**
		 * Provides string of formatted output for Task
		 */
		public String toString() { 
			String str = ("\nSummary for task:\t| " + this.name + "\nDescription:\t\t| " + this.description + "\nSize:\t\t\t| " + this.shirtSize + "\nDuration\t\t| " + this.formattedTime);
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
