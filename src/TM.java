
public class TM {
	public static void main(String[] args) {
		ITMModel tmModel = new TMModel();
		
		// Declare variables
		String cmd;
		String taskName;
		String data1;// data2 = null;
		
		// Create TaskLog to interact with persistent log
		//TaskLog log = new TaskLog();
		
		// Create object to pull time codes
		//LocalDateTime currentTime = LocalDateTime.now();
		
		// Rule out unacceptable usage, if incorrect display usage instructions
		try {		
			cmd = args[0];
			if (args.length < 2)
				taskName = null;
			else 
				taskName = args[1];
			if (args[0].equals("describe") || args[0].equals("size")) {
				data1 = args[2];
//MIGHT NEED TO REMOVE vvvvvvv NO LONGER HAVE DESCRIPTION/SIZE
//				if (args.length == 4)
//					data2 = args[3];				
			}
			else 
				data1 = null;		
		}
		catch (ArrayIndexOutOfBoundsException ex) {
			// Usage instructions
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
								summary();
							else
								summary(taskName);
						break;
		}	
	}

	private static void summary(String taskName) {
		// TODO Auto-generated method stub
		System.out.println("HEERE1");
	}

	private static void summary() {
		// TODO Auto-generated method stub
		System.out.println("HEERE2");
	}		
}








