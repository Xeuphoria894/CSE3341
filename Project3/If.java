import java.util.HashMap;

class If {

	private cond cond;
	private stmt_seq s_seq1;
	private stmt_seq s_seq2;

	public void parse(Scanner S) {

		try {

			if (S.currentToken() != Core.IF) {
				throw new Exception("ERROR: IF EXPECTED");
			}

			S.nextToken();

			this.cond = new cond();

			this.cond.parse(S);

			if (S.currentToken() != Core.THEN) {
				throw new Exception("ERROR: THEN EXPECTED");
			}

			S.nextToken();

			if (S.currentToken() != Core.LBRACE) {
				throw new Exception("ERROR: LBRACE EXPECTED");

			}
			S.nextToken();
			this.s_seq1 = new stmt_seq();
			this.s_seq1.parse(S);

			if (S.currentToken() != Core.RBRACE) {
				throw new Exception("ERROR: RBRACE EXPECTED");
			}
			S.nextToken();

			if (S.currentToken() == Core.ELSE) {

				S.nextToken();

				if (S.currentToken() != Core.LBRACE) {
					throw new Exception("ERROR: LBRACE EXPECTED");
				}
				S.nextToken();

				this.s_seq2 = new stmt_seq();
				this.s_seq2.parse(S);

				if (S.currentToken() != Core.RBRACE) {
					throw new Exception("ERROR: RBRACE EXPECTED");
				}
				S.nextToken();
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

		System.out.print("if ");
		this.cond.print();
		System.out.println(" then {");
		this.s_seq1.print(ind + 1);
		System.out.println("}");

		if (this.s_seq2 != null) {
			for (int j = 0; j < ind; j++) {
				System.out.print("\t");
			}
			System.out.println("else {");
			this.s_seq2.print(ind + 1);
			System.out.println("}");

		}

	}

	public void semanticCheck() {

		this.cond.semanticCheck();

		Parser.scope.push(new HashMap<String, Core>()); // push new scope

		this.s_seq1.semanticCheck();

		Parser.scope.pop(); // return back to the origin scope

		if (this.s_seq2 != null) {
			Parser.scope.push(new HashMap<String, Core>());
			this.s_seq2.semanticCheck();
			Parser.scope.pop();
		}
	}

	public void execute(executor ext) {

		try {
			boolean state = this.cond.execute(ext);
			ext.addEmptyStackScope();
			if (state) {
				this.s_seq1.execute(ext);
			} else if (this.s_seq2 != null) {
				this.s_seq2.execute(ext);
			}

			ext.popCurrentStackLayer();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
