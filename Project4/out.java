
class out {

	private expression expr;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() != Core.OUTPUT) {
				throw new Exception("ERROR: OUTPUT keyword EXPECTED");
			}

			S.nextToken();
			if (S.currentToken() != Core.LPAREN) {
				throw new Exception("ERROR: LPAREN EXPECTED");
			}
			S.nextToken();

			this.expr = new expression();

			this.expr.parse(S);

			if (S.currentToken() != Core.RPAREN) {
				throw new Exception("ERROR : RPAREN EXPECTED");
			}

			S.nextToken();

			if (S.currentToken() != Core.SEMICOLON) {
				throw new Exception("ERROR : SEMICOLON EXPECTED");
			}

			S.nextToken();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int indent) {
		for (int i = 0; i < indent; i++) {
			System.out.print("\t");
		}

		System.out.print("output(");
		this.expr.print();
		System.out.println(");");
	}

	public void semanticCheck() {
		this.expr.semanticCheck();
	}

	public void execute(executor ext) {

		System.out.println(this.expr.execute(ext));
	}

}
