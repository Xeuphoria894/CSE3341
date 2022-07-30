
class decl_int {

	private id_list list;

	public void parse(Scanner S) {

		try {

			if (S.currentToken() != Core.INT) {
				throw new Exception("ERROR: INT Keyword expected");
			}

			S.nextToken();

			this.list = new id_list();

			this.list.parse(S);

			if (S.currentToken() != Core.SEMICOLON) {
				throw new Exception("ERROR : SEMICOLON EXPECTED");
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

		System.out.print("int ");
		this.list.print();
		System.out.println(";");

	}

	public void semanticCheck() {
		this.list.semanticIntegerCheck();
	}

	public void execute(executor ext) {

		this.list.executeInt(ext);
	}

}
