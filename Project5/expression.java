
class expression {

	private term term;
	private expression expr;
	private char sign;

	public void parse(Scanner S) {

		try {

			this.term = new term();
			this.term.parse(S);

			if (S.currentToken() == Core.ADD) {
				this.sign = '+';
				S.nextToken();
				this.expr = new expression();
				this.expr.parse(S);
			} else if (S.currentToken() == Core.SUB) {
				this.sign = '-';
				S.nextToken();
				this.expr = new expression();
				this.expr.parse(S);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {

		this.term.print();

		if (this.sign == '+') {
			System.out.print("+");
			this.expr.print();
		} else if (this.sign == '-') {
			System.out.print("-");
			this.expr.print();
		}

	}

	public void semanticCheck() {
		this.term.semanticCheck();

		if (this.expr != null) {
			this.expr.semanticCheck();
		}
	}

	public int execute(executor ext) {

		int result = this.term.execute(ext);

		if (this.sign == '+') {

			result = result + this.expr.execute(ext);
		} else if (this.sign == '-') {

			result = result - this.expr.execute(ext);
		}

		return result;
	}

}
