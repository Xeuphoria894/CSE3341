
class decl_ref {

	private id_list list;

	public void parse(Scanner S) {

		try {
			if (S.currentToken() != Core.REF) {

				throw new Exception("ERROR: REF keyword expected");
			}
			S.nextToken();

			this.list = new id_list();

			this.list.parse(S);

			if (S.currentToken() != Core.SEMICOLON) {
				throw new Exception("ERROR: SEMICOLON EXPECTED");
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
		System.out.print("ref ");
		this.list.print();
		System.out.println(";");

	}

	public void semanticCheck() {

		this.list.semanticRefCheck();
	}

	public void execute(executor ext) {

		this.list.executeRef(ext);
	}

}
