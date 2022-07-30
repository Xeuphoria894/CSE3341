
class cmpr {

	private expression left;
	private expression right;
	private String sign;

	public void parse(Scanner S) {

		try {
			this.left = new expression(); // handle three cases
			this.left.parse(S);

			if (S.currentToken() == Core.EQUAL) {
				this.sign = "==";
				S.nextToken();
			} else if (S.currentToken() == Core.LESS) {
				this.sign = "<";
				S.nextToken();
			} else if (S.currentToken() == Core.LESSEQUAL) {
				this.sign = "<=";
				S.nextToken();
			} else {
				throw new Exception("ERROR: " + S.currentToken() + " is UNEXPECTED");

			}

			this.right = new expression();
			this.right.parse(S);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {

		this.left.print();

		System.out.print(sign);

		this.right.print();
	}

	public void semanticCheck() {
		this.left.semanticCheck();
		this.right.semanticCheck();
	}

	public boolean execute(executor ext) {

		boolean compare = false;

		int left = this.left.execute(ext);
		int right = this.right.execute(ext);

		switch (this.sign) {

		case "==":

			compare = left == right;

			break;

		case "<":

			compare = left < right;

			break;

		case "<=":

			compare = left <= right;

			break;

		default:
			// should not be reached

			break;

		}

		return compare;
	}

}
