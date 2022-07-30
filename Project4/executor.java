import java.util.*;

public class executor {

	private Map<String, pairedVal> staticField;

	private Stack<Stack<HashMap<String, pairedVal>>> stackLayer;

	private int[] heap;

	private Map<Integer, Boolean> pairedTable;
	
	private Map<String, func_decl> funcPointer;

	public Scanner S;

	public executor(int size, Scanner S) {

		this.initializeHeap(size);
		this.initializeStack();
		this.initializeStatic();
		this.S = S;
		this.funcPointer = new HashMap<String, func_decl>();
	}

	public void initializeStatic() {

		this.staticField = new HashMap<String, pairedVal>();
	}

	public void initializeStack() {

		this.stackLayer = new Stack<Stack<HashMap<String, pairedVal>>>();
	}

	public void initializeHeap(int scope) {

		this.heap = new int[scope];
		this.pairedTable = new HashMap<Integer, Boolean>();
		for (int i = 0; i < scope; i++) {
			this.pairedTable.put(i, false);
		}
	}

	public Map<String, pairedVal> getStatic() {

		return this.staticField;
	}

	public Stack<Stack<HashMap<String, pairedVal>>> getStack() {

		return this.stackLayer;
	}
	
	public Stack<HashMap<String, pairedVal>> getTopStackOfMapForFunc(){
		
		return this.stackLayer.peek();
	}

	public int[] getHeap() {

		return this.heap;
	}

	public int getHeap(int pos) {

		return this.heap[pos];
	}

	public void setHeap(int pos, int value) { // set new value to heap spaces

		if (pos > this.heap.length - 1) {
			System.out.println("ERROR: Overflow Max Heap Size");
			System.exit(1);
		}
		this.heap[pos] = value;
		this.pairedTable.replace(pos, true);
	}

	public boolean checkHeapPosOccupied(int pos) {

		return this.pairedTable.get(pos);
	}

	public void setHeapStatas(int pos, boolean flag) {

		this.pairedTable.replace(pos, flag);
	}

	public void addToStaticField(String s, pairedVal p) {
		this.staticField.put(s, p);
	}

	public void deleteStaticField(String s) {
		this.staticField.remove(s);
	}

	public void replaceStaticScope(String s, pairedVal p) {

		this.staticField.replace(s, p);
	}

	public void addToCurrentStackScope(String s, pairedVal p) {

		Stack<HashMap<String, pairedVal>> top = this.stackLayer.pop();
		HashMap<String, pairedVal> temp = top.pop();
		temp.put(s, p);
		top.push(temp);
		this.stackLayer.push(top);
	}

	/*
	 * Assign the new pairedVal of Integer and Core to the map.
	 * 
	 * @param layerNum the position of the destination layer of map
	 */
	public void replaceStackScope(String s, pairedVal p, int layerNum) {

		Stack<HashMap<String, pairedVal>> backup = new Stack<HashMap<String, pairedVal>>();
		Stack<HashMap<String, pairedVal>> currentTop = this.stackLayer.pop();
		int count = currentTop.size() - 1 - layerNum;

		for (int i = 0; i < count; i++) {
			backup.push(currentTop.pop());
		}

		HashMap<String, pairedVal> topMap = currentTop.pop();
		topMap.replace(s, p);
		currentTop.push(topMap);

		while (!backup.empty()) {
			currentTop.push(backup.pop());
		}
		
		this.stackLayer.push(currentTop);

	}

	public void addEmptyStackScope() {
		
		Stack<HashMap<String, pairedVal>> currentTop = this.stackLayer.pop();
		currentTop.push(new HashMap<String, pairedVal>());
		this.stackLayer.push(currentTop);
	}
	
	public void addEmptyStackOfMapForFunc() {
		
		Stack<HashMap<String, pairedVal>> funcLayer = new Stack<HashMap<String, pairedVal>>();
		this.stackLayer.push(funcLayer);
	}
	
	public void addFuncStack(Stack<HashMap<String, pairedVal>> funcMap) {
		
		this.stackLayer.push(funcMap);
		
	}

	public HashMap<String, pairedVal> popCurrentStackLayer() {
		
		Stack<HashMap<String, pairedVal>> temp = this.stackLayer.pop();
		HashMap<String, pairedVal> result = temp.pop();
		this.stackLayer.push(temp);
		
		return result;
	}
	
	public void popCurrentStackOfMapForFunc() {
		
		this.stackLayer.pop();
	}

	public void pushCurrentStackLayer(HashMap<String, pairedVal> map) {
		
		Stack<HashMap<String, pairedVal>> temp = this.stackLayer.pop();
		temp.push(map);
		this.stackLayer.push(temp);
	}

	public boolean isCurrentStackOfMapEmpty() {

		return this.stackLayer.peek().empty();
	}
	
	public boolean isStackEmpty() {
		
		return this.stackLayer.empty();
	}

	public boolean isStaticEmpty() {

		return this.staticField.isEmpty();
	}

	public boolean isHeapEmpty() {

		return this.heap.length == 0;
	}

	public int readIn() {

		int result = 0;

		try {
			if (S.currentToken() != Core.EOS) {
				result = S.getCONST();
				S.nextToken();
			} else {
				System.out.println(
						"ERROR: END OF FILE; ALL INPUTS RUN OUT. INPUT() SHOULD NOT BE CALLED\nEXISTING......");
				System.exit(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

		return result;
	}
	
	public void addFunction(String name, func_decl fc) {
		
		this.funcPointer.put(name, fc);
	}
	
	public func_decl getFunction(String name) {
		
		return this.funcPointer.get(name);
	}

}
