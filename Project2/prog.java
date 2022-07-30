import java.util.HashMap;

class prog {

	private decl_seq d_seq;
	private stmt_seq s_seq;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() != Core.PROGRAM) {
				throw new Exception("ERROR: PROGRAM EXPECTED");
			}
			S.nextToken();

			if (S.currentToken() == Core.BEGIN) {

				S.nextToken();

				this.s_seq = new stmt_seq();
				this.s_seq.parse(S);

				if (S.currentToken() != Core.END) {
					throw new Exception("ERROR: END EXPECTED");
				}
				S.nextToken();
			} else {

				if (S.currentToken() == Core.INT || S.currentToken() == Core.REF) {
					this.d_seq = new decl_seq();
					this.d_seq.parse(S);

					if (S.currentToken() != Core.BEGIN) {
						throw new Exception("ERROR: BEGIN EXPECTED");
					}
					S.nextToken();

					this.s_seq = new stmt_seq();

					this.s_seq.parse(S);

					if (S.currentToken() != Core.END) {
						throw new Exception("ERROR: END EXPECTED");
					}
					S.nextToken();

				} else {
					throw new Exception("ERROR: DECL-SEQ EXPECTED");
				}
			}

			if (S.currentToken() != Core.EOS) {

				throw new Exception("ERROR: \"" + S.currentToken() + "\" AFTER END");

			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {
		System.out.println("program");

		if (this.d_seq != null) {
			this.d_seq.print(1);
		}
		System.out.println();
		System.out.println("begin");
		this.s_seq.print(1);
		System.out.println();
		System.out.println("end");

	}

	public void sanCheck() {

		Parser.scope.push(new HashMap<String, Core>()); //Create the Global Scope
		if (this.d_seq != null) {
			this.d_seq.semanticCheck();
		}

		Parser.scope.push(new HashMap<String, Core>());

		this.s_seq.semanticCheck();

		Parser.scope.pop(); //leave global scope
	}

}
