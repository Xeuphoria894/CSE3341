
class func_decl {

	private String id;
	private formals formals;
	private stmt_seq s_seq;

	public void Parse(Scanner S) {

		try {

			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR: ID Expected Here");
			}

			this.id = S.getID();
			S.nextToken();

			if (S.currentToken() != Core.LPAREN) {
				throw new Exception("ERROR: LPAREN Expected here");
			}

			S.nextToken();

			if (S.currentToken() != Core.REF) {
				throw new Exception("ERROR: REF Declaration Expected here");
			}
			S.nextToken();

			this.formals = new formals();
			this.formals.Parse(S);

			if (S.currentToken() != Core.RPAREN) {
				throw new Exception("ERROR: RPAREN Expected here");
			}

			S.nextToken();

			if (S.currentToken() != Core.LBRACE) {
				throw new Exception("ERROR: LBRACE Expected here");
			}

			S.nextToken();

			this.s_seq = new stmt_seq();
			this.s_seq.parse(S);

			if (S.currentToken() != Core.RBRACE) {
				throw new Exception("ERROR: RBRACE Expected here");
			}

			S.nextToken();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public formals getFormals() {

		return this.formals;
	}

	public stmt_seq getStatementSeq() {

		return this.s_seq;
	}

	public void execute(executor ext) {

		ext.addFunction(this.id, this);
	}

	public void semanticCheck() {

		try {

			if (Parser.funcPointer.containsKey(this.id)) {
				throw new Exception("ERROR: function \" " + this.id + " \" Has Already been Declared");
			}
			
			Parser.funcPointer.put(this.id, this);
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
