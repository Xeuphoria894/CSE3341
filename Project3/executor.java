import java.util.*;

public class executor {

	private Map<String, pairedVal> staticField;

	private Stack<HashMap<String, pairedVal>> stackLayer;

	private int[] heap;

	private Map<Integer, Boolean> pairedTable;

	public Scanner S;

	public executor(int size, Scanner S) {

		this.initializeHeap(size);
		this.initializeStack();
		this.initializeStatic();
		this.S = S;
	}

	public void initializeStatic() {

		this.staticField = new HashMap<String, pairedVal>();
	}

	public void initializeStack() {

		this.stackLayer = new Stack<HashMap<String, pairedVal>>();
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

	public Stack<HashMap<String, pairedVal>> getStack() {

		return this.stackLayer;
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

		HashMap<String, pairedVal> top = this.stackLayer.pop();
		top.put(s, p);
		this.stackLayer.push(top);
	}

	/*
	 * Assign the new pairedVal of Integer and Core to the map.
	 * 
	 * @param layerNum the position of the destination layer of map
	 */
	public void replaceStackScope(String s, pairedVal p, int layerNum) {

		Stack<HashMap<String, pairedVal>> backup = new Stack<HashMap<String, pairedVal>>();
		int count = this.stackLayer.size() - 1 - layerNum;

		for (int i = 0; i < count; i++) {
			backup.push(this.stackLayer.pop());
		}

		HashMap<String, pairedVal> top = this.stackLayer.pop();
		top.replace(s, p);
		this.stackLayer.push(top);

		while (!backup.empty()) {
			this.stackLayer.push(backup.pop());
		}

	}

	public void addEmptyStackScope() {
		this.stackLayer.push(new HashMap<String, pairedVal>());
	}

	public HashMap<String, pairedVal> popCurrentStackLayer() {

		return this.stackLayer.pop();
	}

	public void pushCurrentStackLayer(HashMap<String, pairedVal> map) {

		this.stackLayer.push(map);
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

}
