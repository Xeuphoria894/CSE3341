import java.util.HashMap;
import java.util.Stack;

class Parser {
	
	public static Scanner S;
	public static Stack<HashMap<String, Core>> scope = new Stack<HashMap<String, Core>>();
	
	/*Recursively check all scope layers*/
	public static boolean checkHistory(String s) {
		boolean flag = false;
		
		if(!scope.isEmpty()) {
			HashMap<String, Core> temp = scope.pop();
			
			if (temp.containsKey(s)) {
				flag = true;
			}else {
				flag = checkHistory(s);
			}
			scope.push(temp);
		}
		return flag;
	}
	
	/*check id at the top of the stack*/
	public static boolean checkCurrent(String s) {
		boolean flag = false;
		
		if(!scope.isEmpty()) {
			HashMap<String, Core> temp = scope.pop();
			flag = temp.containsKey(s);
			scope.push(temp);
		}
		
		return flag;
	}
	
	public static Core checkIdCore(String s) { //return the Core related to ID
		
		Core c = null;
		
		if(!scope.isEmpty()) {
			HashMap<String, Core> temp = scope.pop();
			if(temp.containsKey(s)) {
				c = temp.get(s);
			}else {
				c = checkIdCore(s);
			}
			scope.push(temp);
		}
		return c;
	}

}
