
class cond {

	private cmpr cmpr;
	private cond cond;
	private int cases;

	public void parse(Scanner S) {

		try {
			if (S.currentToken() == Core.NEGATION) {
				S.nextToken();
				if (S.currentToken() == Core.LPAREN) {
					this.cases = 1;
					S.nextToken();
					this.cond = new cond();
					this.cond.parse(S);
					if (S.currentToken() != Core.RPAREN) {
						throw new Exception("ERROR: RPAREN EXPECTED");
					}
					S.nextToken();
				} else {
					throw new Exception("ERROR: LPAREN EXPECTED");
				}
			} else {

				this.cases = 2;
				this.cmpr = new cmpr();
				this.cmpr.parse(S);

				if (S.currentToken() == Core.OR) {
					this.cases = 3;
					S.nextToken();
					this.cond = new cond();

					this.cond.parse(S);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {

		switch (this.cases) {

		case 1:
			System.out.print("!(");
			this.cond.print();
			System.out.print(")");
			break;

		case 2:
			this.cmpr.print();

			break;

		case 3:
			this.cmpr.print();
			System.out.print(" or ");
			this.cond.print();

			break;
		}
	}

	public void semanticCheck() {

		switch (this.cases) {

		case 1:
			this.cond.semanticCheck();

			break;

		case 2:
			this.cmpr.semanticCheck();

			break;

		case 3:
			this.cmpr.semanticCheck();
			this.cond.semanticCheck();
			break;

		default: {

			break;
		}
		}

	}

}
