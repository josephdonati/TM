import java.util.Set;


public class TMModel implements ITMModel {
	public TMModel(){
		
	}
	
	@Override
	public boolean startTask(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopTask(String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean describeTask(String name, String description) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean sizeTask(String name, String size) {
		// TODO Auto-generated method stub
		return false;
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

}
