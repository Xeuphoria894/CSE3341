import java.util.HashMap;

class id_list {

	private String id;
	private id_list list;

	public void parse(Scanner S) {
		try {
			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR : ID EXPECTED");
			}
			this.id = S.getID();
			S.nextToken();

			if (S.currentToken() == Core.COMMA) {
				S.nextToken();

				this.list = new id_list();
				this.list.parse(S);
			}
		} catch (Exception e) {

			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print() {

		System.out.print(this.id);

		if (this.list != null) {
			System.out.print(",");
			this.list.print();
		}

	}

	public void semanticRefCheck() { // check if the REF keyword matches

		if (Parser.checkCurrent(this.id)) {
			System.out.println("ERROR: " + id + " Already declared"); // check declaration
			System.exit(1);
		}

		HashMap<String, Core> temp = Parser.scope.pop(); // add to scope
		temp.put(this.id, Core.REF);
		Parser.scope.push(temp);

		if (this.list != null) {
			this.list.semanticRefCheck();
		}
	}

	public void semanticIntegerCheck() { // check the declaration of INT
		if (Parser.checkCurrent(this.id)) {
			System.out.println("ERROR: " + this.id + " Already declared");
			System.exit(1);
		}

		HashMap<String, Core> temp = Parser.scope.pop();

		temp.put(this.id, Core.INT);
		Parser.scope.push(temp);

		if (this.list != null) {
			this.list.semanticIntegerCheck();
		}
	}

	public void executeInt(executor ext) {

		pairedVal pair = new pairedVal(0, Core.INT);

		if (ext.isStackEmpty()) {

			ext.addToStaticField(this.id, pair);
		} else {

			ext.addToCurrentStackScope(this.id, pair);
		}

		if (this.list != null) {
			this.list.executeInt(ext);
		}
	}

	public void executeRef(executor ext) {

		pairedVal pair = new pairedVal(null, Core.REF);

		if (ext.isStackEmpty()) {

			ext.addToStaticField(this.id, pair);
		} else {

			ext.addToCurrentStackScope(this.id, pair);
		}
		if (this.list != null) {
			this.list.executeRef(ext);
		}
	}

}
