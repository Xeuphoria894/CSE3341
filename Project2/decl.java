
class decl {

	private decl_int dInt;
	private decl_ref dRef;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() == Core.INT) {

				this.dInt = new decl_int();
				this.dInt.parse(S);
			} else if (S.currentToken() == Core.REF) {

				this.dRef = new decl_ref();
				this.dRef.parse(S);
			} else {
				throw new Exception("ERROR: MISSING CORRECT KEYWORD INT or REF");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {

		if (this.dInt != null) {
			this.dInt.print(ind);
		} else if (this.dRef != null) {
			this.dRef.print(ind);
		}

	}

	public void semanticCheck() {

		if (this.dInt != null) {
			this.dInt.semanticCheck();
		} else if (this.dRef != null) {
			this.dRef.semanticCheck();
		}
	}

}
