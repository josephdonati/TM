import java.util.Set;

public interface ITMModel {

    // set information in our model
    //
    boolean startTask(String name); 							//X  //O
    boolean stopTask(String name); 								//X	 //O	
    boolean describeTask(String name, String description);		//X  //O
    boolean sizeTask(String name, String size);					//X  //O
    boolean deleteTask(String name);							//X
    boolean renameTask(String oldName, String newName);			//X

    // return information about our tasks
    //
    String taskElapsedTime(String name);						//X  //O
    String taskSize(String name);								//X  //O
    String taskDescription(String name);						//X  //O

    // return information about some tasks
    //
    String minTimeForSize(String size);
    String maxTimeForSize(String size);
    String avgTimeForSize(String size);

    Set<String> taskNamesForSize(String size);

    // return information about all tasks
    //
    String elapsedTimeForAllTasks();							//X
    Set<String> taskNames();									//X  //O
    Set<String> taskSizes();									//X  

}