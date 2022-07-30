import java.util.HashMap;

class factor {

	private String id;
	private int constant;
	private expression expr;
	private int cases;

	public void parse(Scanner S) {

		try {

			if (S.currentToken() == Core.ID) { // handle three grammars
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

	public int execute(executor ext) {

		int result = 0;

		switch (this.cases) {
		case 1:

			Core type = null;
			boolean inStack = false;
			int count = ext.getTopStackOfMapForFunc().size() - 1;
			HashMap<String, pairedVal> temp = null;

			while (count >= 0) {

				temp = ext.getTopStackOfMapForFunc().get(count);
				if (temp.containsKey(this.id)) { // loop the stack to find the position of id
					inStack = true;
					break;
				}
				count--;
			}

			if (inStack) {

				type = ext.getTopStackOfMapForFunc().get(count).get(this.id).getType();
				int val = ext.getTopStackOfMapForFunc().get(count).get(this.id).getInt(); // assign value based on the id's type

				if (type == Core.INT) {

					result = val;
				} else if (type == Core.REF) {

					result = ext.getHeap(val);
				} else {

					System.out.println("ERROR: UNEXPECTED TYPE MISMATCH; INT or REF EXPECTED");
					System.exit(1);
				}
			} else {

				type = ext.getStatic().get(this.id).getType();
				int val = ext.getStatic().get(this.id).getInt();

				if (type == Core.INT) {

					result = val;
				} else if (type == Core.REF) {

					result = ext.getHeap(val);
				}
			}

			break;

		case 2:

			
			result = this.constant;
			

			break;

		case 3:

			result = this.expr.execute(ext);

			break;

		default:
			// nothing here

			break;

		}

		return result;
	}

}
