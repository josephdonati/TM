import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TMModel implements ITMModel {
	private TaskLog log;
	private LocalDateTime currentTime;
	private LinkedList<TaskLogEntry> allLines;
	private TreeSet<String> allNames;
	private TreeSet<String> allSizes; 
	
	
	public TMModel() throws IOException{
		log = new TaskLog();
		currentTime = LocalDateTime.now();
		allLines = log.readFile();
		allNames = new TreeSet<String>();
		allSizes = new TreeSet<String>();
		for (TaskLogEntry entry : allLines){
			allNames.add(entry.taskName);
			if (entry.cmd.equals("size")) {
				allSizes.add(entry.data1);
			}
		}
	}
	
	
	
	@Override
	public boolean startTask(String name) {
		try {
			log.writeLine(currentTime + "/" + name + "/start");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean stopTask(String name) {
		try {
			log.writeLine(currentTime + "/" + name + "/stop");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean describeTask(String name, String description) {
		try {
			log.writeLine(currentTime + "/" + name + "/describe/" + description );
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean sizeTask(String name, String size) {
		try {
			log.writeLine(currentTime + "/" + name + "/size/" + size );
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean deleteTask(String name) {
		//Used method found at "https://stackoverflow.com/questions/8563294/modifying-existing-file-content-in-java"
				List<String> newLines = new ArrayList<>();
				try {
					for (String line : Files.readAllLines(Paths.get("TM.txt"), StandardCharsets.UTF_8)) {
					    if (line.contains("/"+ name + "/")) {
					       newLines.add(line.replace("/"+ name + "/", "/#PURGED#" + name + "/"));
					    } else {
					       newLines.add(line);
					    }
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Files.write(Paths.get("TM.txt"), newLines, StandardCharsets.UTF_8);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
				}
					
				return false;
	}

	@Override
	public boolean renameTask(String oldName, String newName) {
		//Used method found at "https://stackoverflow.com/questions/8563294/modifying-existing-file-content-in-java"
		List<String> newLines = new ArrayList<>();
		try {
			for (String line : Files.readAllLines(Paths.get("TM.txt"), StandardCharsets.UTF_8)) {
			    if (line.contains("/"+ oldName + "/")) {
			       newLines.add(line.replace("/"+ oldName + "/", "/"+ newName + "/"));
			    } else {
			       newLines.add(line);
			    }
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.write(Paths.get("TM.txt"), newLines, StandardCharsets.UTF_8);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		return false;
	}

	@Override
	public String taskElapsedTime(String name) {
		Task tasktToSummarize = new Task(name, allLines);
		return tasktToSummarize.totalTime();
	}

	@Override
	public String taskSize(String name) {
		Task tasktToSummarize = new Task(name, allLines);
		return tasktToSummarize.shirtSize();
	}

	@Override
	public String taskDescription(String name) {
		Task tasktToSummarize = new Task(name, allLines);
		return tasktToSummarize.description();
	}

	@Override
	public String minTimeForSize(String size) {
		long minTime = Long.MAX_VALUE;
		for (String taskName : allNames) {
			Task taskForSizes = new Task(taskName, allLines);
			if (taskForSizes.shirtSize.equals(size) && taskForSizes.totalTime < minTime)
				minTime = taskForSizes.totalTime;
		}
		return TimeUtilities.secondsFormatter(minTime);
	}

	@Override
	public String maxTimeForSize(String size) {
		long maxTime = 0;
		for (String taskName : allNames) {
			Task taskForSizes = new Task(taskName, allLines);
			if (taskForSizes.shirtSize.equals(size) && taskForSizes.totalTime > maxTime)
				maxTime = taskForSizes.totalTime;
		}
		return TimeUtilities.secondsFormatter(maxTime);
	}

	@Override
	public String avgTimeForSize(String size) {
		long sumTime = 0;
		int counter = 0;
		for (String taskName : allNames) {
			Task taskForSizes = new Task(taskName, allLines);
			if (taskForSizes.shirtSize.equals(size)) { 
				sumTime += taskForSizes.totalTime;
				counter++;
			}
		}
		sumTime /= counter;
		return TimeUtilities.secondsFormatter(sumTime);
	}

	@Override
	public Set<String> taskNamesForSize(String size) {
		Set<String> taskNamesForSize = new TreeSet<String>();
		for (String taskName : allNames) {
			
			//Deals with deleted entries
			if (taskName.contains("#PURGED#")) 
				continue;
			
			Task taskForSizes = new Task(taskName, allLines);
			if (taskForSizes.shirtSize.equals(size)) 
				taskNamesForSize.add(taskName);
			
		}
		return taskNamesForSize;
	}

	@Override
	public String elapsedTimeForAllTasks() {
		long timeForAllTasks = 0;
		for (String taskName : allNames){
			Task taskToSummarize = new Task(taskName, allLines);
			timeForAllTasks += taskToSummarize.totalTime;
		}	
		return TimeUtilities.secondsFormatter(timeForAllTasks);
	}

	@Override
	public Set<String> taskNames() {
		return allNames;
	}

	@Override
	public Set<String> taskSizes() {
		return allSizes;
	}
	
	/**
	 * The TaskLog class has methods for writing and reading from a persistent log file
	 */
	public class TaskLog {
		
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
		private StringBuilder description = new StringBuilder("");
		private String shirtSize = "Unspecified";
		private String formattedTime = null;
		private long totalTime = 0;
		
		/**
		 * Constructor runs and gathers all task data from log entries
		 * @param name The name of the task
		 * @param entries The list of entries in the log
		 */
		public Task(String name, LinkedList<TaskLogEntry> entries) { 
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
							description.append("--" + entry.data1);
						else
							description.append("\n--" + entry.data1);
						
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
				
		
		public String totalTime(){
			return this.formattedTime;
		}
		
		public String shirtSize() {
			return this.shirtSize;
		}
		
		public String description() {
			return this.description.toString();
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
