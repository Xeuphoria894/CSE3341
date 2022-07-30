
class assign {

	private expression expr;
	private String id1;
	private String id2;
	private int cases;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR: ID KEYWORD EXPECTED");
			}

			this.id1 = S.getID();

			S.nextToken();

			if (S.currentToken() != Core.ASSIGN) {
				throw new Exception("ERROR: ASSIGN EXPECTED");
			}

			S.nextToken();

			/* Handle different cases */
			if (S.currentToken() == Core.INPUT) {

				this.cases = 1;

				S.nextToken();

				if (S.currentToken() == Core.LPAREN) {

					S.nextToken();
				} else {
					throw new Exception("ERROR: LPAREN EXPECTED");
				}

				if (S.currentToken() == Core.RPAREN) {

					S.nextToken();
				} else {
					throw new Exception("ERROR: RPAREN EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR : SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.NEW) {
				this.cases = 3;
				S.nextToken();

				if (S.currentToken() == Core.CLASS) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: CLASS EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.SHARE) {
				this.cases = 4;
				S.nextToken();

				if (S.currentToken() == Core.ID) {
					this.id2 = S.getID();
					S.nextToken();
				} else {
					throw new Exception("ERROR: ID EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.ID || S.currentToken() == Core.CONST
					|| S.currentToken() == Core.LPAREN) {

				this.cases = 2;
				expr = new expression();
				expr.parse(S);

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else {
				throw new Exception("ERROR: GRAMMAR VIOLATION");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {
		for (int i = 0; i < ind; i++) {
			System.out.print("\t");
		}

		switch (this.cases) {

		case 1:
			System.out.print(id1 + "=input()");
			System.out.println(";");

			break;

		case 2:
			System.out.print(id1 + "=");
			expr.print();
			System.out.println(";");

			break;

		case 3:
			System.out.print(id1 + "=new class");
			System.out.println(";");

			break;

		case 4:
			System.out.print(id1 + "=share " + id2);
			System.out.println(";");

			break;

		default: {

			break;
		}
		}
	}

	public void semanticCheck() {
		if (!Parser.checkHistory(id1)) {
			System.out.println(id1+" IS NOT DECLARED");
			System.exit(1);
		}

		if (cases == 2) {
			this.expr.semanticCheck();
		} else if (cases == 3) {
			if (Parser.checkIdCore(id1) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
		} else if (cases == 4) {
			if (Parser.checkIdCore(id1) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
			if (!Parser.checkHistory(id2)) {
				System.out.println(id2+" IS NOT DECLARED");
				System.exit(1);
			}
			if (Parser.checkIdCore(id2) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
		}
	}

}
