import java.io.IOException;
import java.util.Set;

public class TM {
	public static void main(String[] args) throws IOException {
		
		ITMModel tmModel = new TMModel();
		String cmd;
		String taskName;
		String data1;
		
		try {
			cmd = args[0];
			if (args.length < 2)
				taskName = null;
			else 
				taskName = args[1];
			if (args[0].equals("describe") || args[0].equals("size") || args[0].equals("rename")) {
				data1 = args[2];				
			}
			else 
				data1 = null;		
		}
		catch (ArrayIndexOutOfBoundsException ex) {
		
			System.out.println("Usage:");
			System.out.println("\tTM start <task name>");
			System.out.println("\tTM stop <task name>");
			System.out.println("\tTM describe <task name> <task description in quotes>");
			System.out.println("\tTM size <task name> <task size XS-XXL>");
			System.out.println("\tTM delete <task name>");
			System.out.println("\tTM rename <task name> <new name>");
			System.out.println("\tTM summary <task name>");
			System.out.println("\tTM summary");
			return;
		}
		
		switch (cmd) {
			case "stop": tmModel.stopTask(taskName);
						break;
			case "start": tmModel.startTask(taskName);
						break;
			case "describe": tmModel.describeTask(taskName, data1);
						break;
			case "size": tmModel.sizeTask(taskName, data1);
						break;
			case "delete": tmModel.deleteTask(taskName);
						break;
			case "rename": tmModel.renameTask(taskName, data1);
						break;			
			case "summary": if (taskName == null)
								summary(tmModel);
							else {
								summary(tmModel, taskName);
								break;
							}	
		}
	}
	
	public static void summary(ITMModel tmModel, String taskName) {
		String str = ("\nSummary for task:\t| " + taskName +
				"\nSize:\t\t\t| " + tmModel.taskSize(taskName) +
				"\nDuration\t\t| " + tmModel.taskElapsedTime(taskName) +
				"\nDescription:\n" + tmModel.taskDescription(taskName) + "\n");
		System.out.println(str);
	}
	
	public static void summary(ITMModel tmModel) {
		Set<String> taskNames = tmModel.taskNames();
		Set<String> taskSizes = tmModel.taskSizes();
		System.out.println("\n--------------------------------------| TASK LOG |--------------------------------------");
		for (String name : taskNames) {
			// Ignores deleted tasks
			if (name.contains("#PURGED#"))
				continue;
			
			summary(tmModel, name);
		}
		System.out.println("-----------------------------------");
		String str = ("Total Time:\t\t| " + tmModel.elapsedTimeForAllTasks() +
					"\n");
		System.out.println(str);
		System.out.println("\n--------------------------------| TASK SIZE BREAKDOWN |---------------------------------");
		for (String size : taskSizes) {
			Set<String> taskNamesForSize = tmModel.taskNamesForSize(size);
			if (taskNamesForSize.size() > 1) {
				System.out.println("Size:\t\t\t| " + size);
				System.out.println("Task Names:\t\t| " + taskNamesForSize);
				System.out.println("Min time:\t\t| " + tmModel.minTimeForSize(size));
				System.out.println("Avg time:\t\t| " + tmModel.avgTimeForSize(size));
				System.out.println("Max time:\t\t| " + tmModel.maxTimeForSize(size) + "\n");
			}
		}
		System.out.println("------------------------------------| END TASK LOG |------------------------------------");
	}
}
	









