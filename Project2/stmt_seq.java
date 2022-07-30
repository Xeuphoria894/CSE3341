
class stmt_seq {

	private stmt stmt;
	private stmt_seq s_seq;

	public void parse(Scanner S) {

		try {

			this.stmt = new stmt();
			this.stmt.parse(S);

			if (S.currentToken() == Core.ID || S.currentToken() == Core.IF || S.currentToken() == Core.WHILE
					|| S.currentToken() == Core.OUTPUT || S.currentToken() == Core.INT
					|| S.currentToken() == Core.REF) {
				this.s_seq = new stmt_seq();
				this.s_seq.parse(S);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {
		this.stmt.print(ind);
		if (this.s_seq != null) {
			this.s_seq.print(ind);
		}

	}

	public void semanticCheck() {

		this.stmt.semanticCheck();
		if (this.s_seq != null) {
			this.s_seq.semanticCheck();
		}
	}

}
