import java.util.List;
import java.util.ArrayList;

class formals {

	private String id;
	private formals formals;

	public void Parse(Scanner S) {

		try {

			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR: ID Expected Here");
			}

			this.id = S.getID();
			S.nextToken();
			if (S.currentToken() == Core.COMMA) {
				S.nextToken();

				this.formals = new formals();

				formals.Parse(S);
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public List<String> execute() {
		List<String> params;

		if (formals != null) {
			params = formals.execute();
		} else {
			params = new ArrayList<String>();
		}
		params.add(this.id);

		return params;
	}
	
	public void semanticCheck() {
		
		try {
			
			if(Parser.checkCurrent(this.id)) {
				throw new Exception("ERROR: \" "+this.id+"\" Has Already Declared");
			}
			
			Parser.scope.peek().peek().put(this.id, Core.REF);
			if (this.formals != null) {
				this.formals.semanticCheck();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
