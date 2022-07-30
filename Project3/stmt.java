
class stmt {

	private assign assign;
	private If If;
	private loop lp;
	private out out;
	private decl decl;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() == Core.ID) {

				this.assign = new assign();
				this.assign.parse(S);
			} else if (S.currentToken() == Core.IF) {
				this.If = new If();
				this.If.parse(S);
			} else if (S.currentToken() == Core.WHILE) {
				this.lp = new loop();
				this.lp.parse(S);
			} else if (S.currentToken() == Core.OUTPUT) {
				this.out = new out();
				this.out.parse(S);
			} else if (S.currentToken() == Core.INT || S.currentToken() == Core.REF) {
				this.decl = new decl();
				this.decl.parse(S);
			} else {
				throw new Exception("ERROR : UNEXPECTED KEYWORD \"" + S.currentToken() + "\"");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {

		if (this.assign != null) {
			this.assign.print(ind);
		} else if (If != null) {
			this.If.print(ind);
		} else if (lp != null) {
			this.lp.print(ind);
		} else if (out != null) {
			this.out.print(ind);
		} else if (decl != null) {
			this.decl.print(ind);
		}
	}

	public void semanticCheck() {

		if (this.assign != null) {
			this.assign.semanticCheck();
		} else if (this.If != null) {
			this.If.semanticCheck();
		} else if (this.lp != null) {
			this.lp.semanticCheck();
		} else if (this.out != null) {
			this.out.semanticCheck();
		} else if (this.decl != null) {
			this.decl.semanticCheck();
		}
	}

	public void execute(executor ext) {

		if (this.assign != null) {
			this.assign.execute(ext);
		} else if (this.If != null) {
			this.If.execute(ext);
		} else if (this.lp != null) {
			this.lp.execute(ext);
		} else if (this.out != null) {
			this.out.execute(ext);
		} else if (this.decl != null) {
			this.decl.execute(ext);
		}
	}

}
