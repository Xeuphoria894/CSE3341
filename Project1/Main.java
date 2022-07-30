class Main {
	public static void main(String[] args) {
		// Initialize the scanner with the input file
		Scanner S = new Scanner(args[0]);
		//Scanner S = new Scanner("src/1.code");

		// Print the token stream
		while (S.currentToken() != Core.EOS && S.currentToken() != Core.ERROR) {
			// Pring the current token, with any extra data needed
			System.out.print(S.currentToken());
			if (S.currentToken() == Core.ID) {
				String value = S.getID();
				System.out.print("[" + value + "]");
			} else if (S.currentToken() == Core.CONST) {
				int value = S.getCONST();
				System.out.print("[" + value + "]");
			}
			System.out.print("\n");

			// Advance to the next token
			S.nextToken();
		}
	}
}