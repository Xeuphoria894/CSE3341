
class decl_seq {

	private decl decl;
	private decl_seq d_seq;
	private func_decl func_decl;

	public void parse(Scanner S) {

		try {

			if (S.currentToken() == Core.INT || S.currentToken() == Core.REF) {

				
				this.decl = new decl();
				this.decl.parse(S);

				if (S.currentToken() == Core.INT || S.currentToken() == Core.REF || S.currentToken() == Core.ID) {

					this.d_seq = new decl_seq();
					this.d_seq.parse(S);

				}
			} else if (S.currentToken() == Core.ID) {

				
				this.func_decl = new func_decl();
				this.func_decl.Parse(S);

				if (S.currentToken() == Core.INT || S.currentToken() == Core.REF || S.currentToken() == Core.ID) {
					this.d_seq = new decl_seq();
					this.d_seq.parse(S);
				}
			} else {
				throw new Exception(
						"ERROR: UNEXPECTED GRAMMAR: \"" + S.currentToken() + "\" ; ONLY func-decl or decl allowed");
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
		
		/*
		this.decl.semanticCheck();

		if (this.d_seq != null) {
			this.d_seq.semanticCheck();
		}*/
		
		if(this.func_decl != null) {
			this.func_decl.semanticCheck();
			
		}
		
		if(this.d_seq != null) {
			this.d_seq.semanticCheck();
		}
	}

	public void execute(executor ext) {
		
		if (this.func_decl!= null) {
			this.func_decl.execute(ext);
		}else {
			this.decl.execute(ext);
		}
		
		if(this.d_seq != null) {
			this.d_seq.execute(ext);
		}

	}

}
