import java.util.HashMap;

class loop {

	private cond cond;
	private stmt_seq s_seq;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() != Core.WHILE) {
				throw new Exception("ERROR: WHILE EXPECTED");
			}
			S.nextToken();

			this.cond = new cond();
			this.cond.parse(S);

			if (S.currentToken() != Core.LBRACE) {
				throw new Exception("ERROR: LBRACE EXPECTED");
			}
			S.nextToken();

			this.s_seq = new stmt_seq();
			this.s_seq.parse(S);

			if (S.currentToken() != Core.RBRACE) {
				throw new Exception("ERROR: RBRACE EXPECTED");
			}
			S.nextToken();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);

		}
	}

	public void print(int ind) {

		for (int i = 0; i < ind; i++) {
			System.out.print("\t");
		}

		System.out.print("while ");
		this.cond.print();
		System.out.println("{");
		this.s_seq.print(ind + 1);
		for (int j = 0; j < ind; j++) {
			System.out.print("\t");
		}
		System.out.println("}");
	}

	public void semanticCheck() {

		this.cond.semanticCheck();
		Parser.scope.push(new HashMap<String, Core>()); // create new scope for while

		this.s_seq.semanticCheck();
		Parser.scope.pop();
	}

	public void execute(executor ext) {

		while (this.cond.execute(ext)) {
			ext.addEmptyStackScope();
			this.s_seq.execute(ext);
			ext.popCurrentStackLayer();

		}
	}

}
