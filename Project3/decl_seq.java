
class decl_seq {

	private decl decl;
	private decl_seq d_seq;

	public void parse(Scanner S) {

		try {

			this.decl = new decl();
			this.decl.parse(S);

			if (S.currentToken() == Core.INT || S.currentToken() == Core.REF) {

				this.d_seq = new decl_seq();
				this.d_seq.parse(S);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {

		this.decl.print(ind);
		if (this.d_seq != null) {
			this.d_seq.print(ind);
		}

	}

	public void semanticCheck() {

		this.decl.semanticCheck();

		if (this.d_seq != null) {
			this.d_seq.semanticCheck();
		}
	}

	public void execute(executor ext) {

		this.decl.execute(ext);

		if (this.d_seq != null) {
			this.d_seq.execute(ext);
		}
	}

}
