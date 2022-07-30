
class Main {

	public static void main(String[] args) {

		Scanner S = new Scanner(args[0]);

		Parser.S = S;

		prog start = new prog();

		try {
			start.parse(S);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		start.sanCheck(); //check semantic
		
		start.print();
	}
}