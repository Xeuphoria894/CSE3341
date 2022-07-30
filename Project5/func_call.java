import java.util.*;

class func_call {

	private String id;
	private formals formals;

	public void Parse(Scanner S) {

		try {

			if (S.currentToken() != Core.BEGIN) {

				throw new Exception("ERROR: BEGIN KEYWORD EXPECTED");
			}

			S.nextToken();

			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR: ID EXPECTED HERE");
			}

			this.id = S.getID();

			S.nextToken();

			if (S.currentToken() != Core.LPAREN) {
				throw new Exception("ERROR: LPAREN Expected");
			}
			S.nextToken();

			this.formals = new formals();

			this.formals.Parse(S);

			if (S.currentToken() != Core.RPAREN) {
				throw new Exception("ERROR: RPAREN EXPECTED");
			}

			S.nextToken();

			if (S.currentToken() != Core.SEMICOLON) {
				throw new Exception("ERROR: SEMICOLON Expected");
			}
			S.nextToken();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void execute(executor ext) {

		Stack<HashMap<String, pairedVal>> funcScope = new Stack<HashMap<String, pairedVal>>(); // create new frame

		funcScope.push(new HashMap<String, pairedVal>());

		func_decl call = ext.getFunction(this.id);

		formals callParam = call.getFormals();

		stmt_seq callStmtSeq = call.getStatementSeq();

		List<String> passParam = callParam.execute(); // get formal and actual parameters
		List<String> oldParam = this.formals.execute();

		HashMap<String, pairedVal> temp = null;

		for (int i = 0; i < passParam.size(); i++) {

			pairedVal pair = new pairedVal(null, Core.REF);

			boolean inStack = false;
			int count = ext.getTopStackOfMapForFunc().size() - 1; // copy and pass the parameters

			while (count >= 0) {
				temp = ext.getTopStackOfMapForFunc().get(count);
				if (temp.containsKey(oldParam.get(i))) {
					inStack = true;
					break;
				}
				count--;
			}

			if (inStack) {
				pair.setInt(ext.getTopStackOfMapForFunc().get(count).get(oldParam.get(i)).getInt());
				ext.updateNumOfReferenceByOne(ext.getTopStackOfMapForFunc().get(count).get(oldParam.get(i)).getInt()); // update reference
																														
			} else {
				pair.setInt(ext.getStatic().get(oldParam.get(i)).getInt());
				ext.updateNumOfReferenceByOne(ext.getStatic().get(oldParam.get(i)).getInt()); // reference + 1
																														
			}

			HashMap<String, pairedVal> topMap = funcScope.pop();
			topMap.put(passParam.get(i), pair);
			funcScope.push(topMap);
		}

		ext.addFuncStack(funcScope);

		ext.addEmptyStackScope();

		callStmtSeq.execute(ext);

		Stack<HashMap<String, pairedVal>> deadFunc = ext.popCurrentStackOfMapForFunc();
		int before = 0;

		for (int i = 0; i < deadFunc.size(); i++) {

			for (Map.Entry<String, pairedVal> entry : deadFunc.get(i).entrySet()) { //iterate over the dead function and remove references

				before = ext.numOfReference();

				if (entry.getValue().getType() == Core.REF) {

					if (entry.getValue().getInt() != null) {
						ext.removeNumOfReferenceByOne(entry.getValue().getInt());
					}
				}

				if (before != ext.numOfReference()) {
					System.out.println("gc:" + ext.numOfReference());
				}
			}
		}

	}

	public void semanticCheck() {

		try {

			if (!Parser.funcPointer.containsKey(this.id)) {
				throw new Exception("ERROR: \" " + this.id + " \" has not been declared");
			}

			func_decl call = Parser.funcPointer.get(this.id);

			// call.getFormals().semanticCheck();

			List<String> passedParam = call.getFormals().execute();
			List<String> oldParam = this.formals.execute();

			Set<String> passedParamSet = new HashSet<String>(passedParam);

			if (passedParamSet.size() < passedParam.size()) {
				throw new Exception("ERROR : Duplicate declaration in formal parameters");
			}

			if (passedParam.size() != oldParam.size()) {
				throw new Exception("ERROR: the number of formal parameters does not match the passed parameters");
			}

			Stack<HashMap<String, Core>> topStack = new Stack<HashMap<String, Core>>();
			topStack.push(new HashMap<String, Core>());

			for (int j = 0; j < passedParam.size(); j++) {

				HashMap<String, Core> temp = topStack.pop();
				temp.put(passedParam.get(j), Core.REF);
				topStack.push(temp);
			}

			Parser.scope.push(topStack);
			Stack<HashMap<String, Core>> top = Parser.scope.pop();
			top.push(new HashMap<String, Core>());
			Parser.scope.push(top);

			this.formals.semanticCheck();

			Parser.scope.pop();

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

}
