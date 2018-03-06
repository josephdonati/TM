import java.io.*;
import java.time.LocalDateTime;
import java.util.*;

	

public class TMModel implements ITMModel {
	private TaskLog log;
	private LocalDateTime currentTime; 
	
	public TMModel() throws IOException{
		log = new TaskLog();
		currentTime = LocalDateTime.now();
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
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean deleteTask(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean renameTask(String oldName, String newName) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String taskElapsedTime(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String taskSize(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String taskDescription(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String minTimeForSize(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String maxTimeForSize(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String avgTimeForSize(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> taskNamesForSize(String size) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String elapsedTimeForAllTasks() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> taskNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> taskSizes() {
		// TODO Auto-generated method stub
		return null;
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
}
