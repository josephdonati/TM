import java.io.IOException;
import java.util.Set;


public class TM {
	public static void main(String[] args) throws IOException {
		
		ITMModel tmModel = new TMModel();
		String cmd;
		String taskName;
		String data1;
		
		try {
			// TODO DOUBLE CHECK THIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			cmd = args[0];
			if (args.length < 2)
				taskName = null;
			else 
				taskName = args[1];
			if (args[0].equals("describe") || args[0].equals("size")) {
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
		
		// switch to direct operation
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
			case "rename": tmModel.sizeTask(taskName, data1);
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
				"\nDescription:\t\t| " + tmModel.taskDescription(taskName) +
				"\nSize:\t\t\t| " + tmModel.taskSize(taskName) +
				"\nDuration\t\t| " + tmModel.taskElapsedTime(taskName));
		System.out.println(str);
	}
	
	public static void summary(ITMModel tmModel) {
		Set<String> taskNames = tmModel.taskNames();
		Set<String> taskSizes = tmModel.taskSizes();
		//Set<String> namesForSizes = tmModel.taskNamesForSize(size); 
		for (String name : taskNames) {
			summary(tmModel, name);
		}
		String str = ("\nTotal Time:\t\t|" + tmModel.elapsedTimeForAllTasks() +
					"\n");
		System.out.println(str);
	}
}
	









