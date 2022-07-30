import java.util.HashMap;
import java.util.Stack;

class Parser {
	
	public static Scanner S;
	public static Stack<Stack<HashMap<String, Core>>> scope = new Stack<Stack<HashMap<String, Core>>>();
	public static HashMap<String, func_decl> funcPointer;
	
	/*Recursively check all scope layers*/
	public static boolean checkHistory(String s) {
		boolean flag = false;
		
		if(!scope.peek().empty()) {
			HashMap<String, Core> temp = scope.peek().pop();
			
			if (temp.containsKey(s)) {
				flag = true;
			}else {
				flag = checkHistory(s);
			}
			
			scope.peek().push(temp);
		}
		return flag;
	}
	
	/*check id at the top of the stack*/
	public static boolean checkCurrent(String s) {
		boolean flag = false;
		
		if(!scope.peek().empty()) {
			HashMap<String, Core> temp = scope.peek().peek();
			flag = temp.containsKey(s);
			
		}
		
		return flag;
	}
	
	public static Core checkIdCore(String s) { //return the Core related to ID
		
		Core c = null;
		
		if(!scope.peek().empty()) {
			HashMap<String, Core> temp = scope.peek().pop();
			if(temp.containsKey(s)) {
				c = temp.get(s);
			}else {
				c = checkIdCore(s);
			}
			scope.peek().push(temp);
		}
		return c;
	}
	

}
