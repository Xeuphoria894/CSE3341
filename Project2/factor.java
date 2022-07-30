
class factor {

	private String id;
	private int constant;
	private expression expr;
	private int cases;

	public void parse(Scanner S) {

		try {

			if (S.currentToken() == Core.ID) { //handle three grammars
				this.cases = 1;
				this.id = S.getID();
				S.nextToken();
			} else if (S.currentToken() == Core.CONST) {
				this.cases = 2;
				this.constant = S.getCONST();
				S.nextToken();
			} else if (S.currentToken() == Core.LPAREN) {
				this.cases = 3;
				S.nextToken();
				this.expr = new expression();
				this.expr.parse(S);
				if (S.currentToken() != Core.RPAREN) {
					throw new Exception("ERROR: RPAREN expected but not detected");
				}
				S.nextToken();
			} else {
				throw new Exception("ERROR: \"" + S.currentToken() + "\" IS UNEXCEPTED GRAMMAR");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {
		if (this.cases == 1) {
			System.out.print(this.id);
		} else if (this.cases == 2) {
			System.out.print(this.constant);

		} else if (this.cases == 3) {
			System.out.print("(");
			this.expr.print();
			System.out.print(")");
		}
	}

	public void semanticCheck() {
		if (this.cases == 1) {
			if (!Parser.checkHistory(this.id)) {
				System.out.println("ERROR: \"" + this.id + "\" HAS NOT BEEN DECLARED");
				System.exit(1);
			}
		} else if (this.cases == 3) {
			this.expr.semanticCheck();
		}
	}

}
