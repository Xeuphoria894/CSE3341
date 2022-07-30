
class term {

	private factor factor;
	private term term;

	public void parse(Scanner S) {

		try {

			this.factor = new factor();
			this.factor.parse(S);

			if (S.currentToken() == Core.MULT) {
				S.nextToken();
				this.term = new term();
				this.term.parse(S);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {
		this.factor.print();

		if (this.term != null) {
			System.out.print("*");
			this.term.print();
		}
	}

	public void semanticCheck() {
		this.factor.semanticCheck();

		if (this.term != null) {
			this.term.semanticCheck();
		}
	}

}
