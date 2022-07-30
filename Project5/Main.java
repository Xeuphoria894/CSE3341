
class Main {

	public static void main(String[] args) {

		Scanner S1 = new Scanner(args[0]);
		Scanner S2 = new Scanner(args[1]);
		//Scanner S1 = new Scanner("src/6.code");
		//Scanner S2 = new Scanner("src/6.data");
		Parser.S = S1;
		
		executor ext = new executor(1000, S2); //initialize the heap size to 1000, should be enough

		prog start = new prog();

		try {
			start.parse(S1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//start.sanCheck();
		
		try {
			start.execute(ext);
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		//start.sanCheck(); //check semantic
		
		//start.print();
	}
}