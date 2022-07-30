import java.util.HashMap;

class assign {

	private expression expr;
	private String id1;
	private String id2;
	private int cases;

	public void parse(Scanner S) {
		try {

			if (S.currentToken() != Core.ID) {
				throw new Exception("ERROR: ID KEYWORD EXPECTED");
			}

			this.id1 = S.getID();

			S.nextToken();

			if (S.currentToken() != Core.ASSIGN) {
				throw new Exception("ERROR: ASSIGN EXPECTED");
			}

			S.nextToken();

			/* Handle different cases */
			if (S.currentToken() == Core.INPUT) {

				this.cases = 1;

				S.nextToken();

				if (S.currentToken() == Core.LPAREN) {

					S.nextToken();
				} else {
					throw new Exception("ERROR: LPAREN EXPECTED");
				}

				if (S.currentToken() == Core.RPAREN) {

					S.nextToken();
				} else {
					throw new Exception("ERROR: RPAREN EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR : SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.NEW) {
				this.cases = 3;
				S.nextToken();

				if (S.currentToken() == Core.CLASS) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: CLASS EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.SHARE) {
				this.cases = 4;
				S.nextToken();

				if (S.currentToken() == Core.ID) {
					this.id2 = S.getID();
					S.nextToken();
				} else {
					throw new Exception("ERROR: ID EXPECTED");
				}

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else if (S.currentToken() == Core.ID || S.currentToken() == Core.CONST
					|| S.currentToken() == Core.LPAREN) {

				this.cases = 2;
				expr = new expression();
				expr.parse(S);

				if (S.currentToken() == Core.SEMICOLON) {
					S.nextToken();
				} else {
					throw new Exception("ERROR: SEMICOLON EXPECTED");
				}

			} else {
				throw new Exception("ERROR: GRAMMAR VIOLATION");
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void print(int ind) {
		for (int i = 0; i < ind; i++) {
			System.out.print("\t");
		}

		switch (this.cases) {

		case 1:
			System.out.print(id1 + "=input()");
			System.out.println(";");

			break;

		case 2:
			System.out.print(id1 + "=");
			expr.print();
			System.out.println(";");

			break;

		case 3:
			System.out.print(id1 + "=new class");
			System.out.println(";");

			break;

		case 4:
			System.out.print(id1 + "=share " + id2);
			System.out.println(";");

			break;

		default: {

			break;
		}
		}
	}

	public void semanticCheck() {
		if (!Parser.checkHistory(id1)) {
			System.out.println(id1 + " IS NOT DECLARED");
			System.exit(1);
		}

		if (cases == 2) {
			this.expr.semanticCheck();
		} else if (cases == 3) {
			if (Parser.checkIdCore(id1) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
		} else if (cases == 4) {
			if (Parser.checkIdCore(id1) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
			if (!Parser.checkHistory(id2)) {
				System.out.println(id2 + " IS NOT DECLARED");
				System.exit(1);
			}
			if (Parser.checkIdCore(id2) != Core.REF) {
				System.out.println("ERROR: TYPE MISMATCH");
				System.exit(1);
			}
		}
	}

	public void execute(executor ext) {

		switch (this.cases) {

		case 1:

			/* Assign the value from input() to id */
			boolean inStack = false;
			Core type = null;
			int pointer = ext.getStack().size() - 1;
			HashMap<String, pairedVal> temp = null;

			while (pointer >= 0) {
				temp = ext.getStack().get(pointer);
				if (temp.containsKey(this.id1)) { // loop to find the position of the map
					inStack = true;
					break;
				}
				pointer--;
			}

			if (inStack) {

				type = ext.getStack().get(pointer).get(this.id1).getType();
				int val = ext.getStack().get(pointer).get(this.id1).getInt();

				if (type == Core.INT) {
					ext.replaceStackScope(this.id1, new pairedVal(ext.readIn(), Core.INT), pointer);
				} else if (type == Core.REF) {
					ext.setHeap(val, ext.readIn());
				}
			} else {
				type = ext.getStatic().get(this.id1).getType();
				int val = ext.getStatic().get(this.id1).getInt();

				if (type == Core.INT) {
					ext.replaceStaticScope(this.id1, new pairedVal(ext.readIn(), Core.INT));
				} else if (type == Core.REF) {
					ext.setHeap(val, ext.readIn());
				}
			}

			break;

		case 2:

			/* Assign the id with value from expression */
			int value = this.expr.execute(ext);
			boolean inStack1 = false;
			Core type1 = null;
			int pointer1 = ext.getStack().size() - 1;
			HashMap<String, pairedVal> temp1 = null;

			while (pointer1 >= 0) {
				temp1 = ext.getStack().get(pointer1);
				if (temp1.containsKey(this.id1)) { // loop stack to find the position of id
					inStack1 = true;
					break;
				}
				pointer1--;
			}

			if (inStack1) {

				type1 = ext.getStack().get(pointer1).get(this.id1).getType();
				int val = ext.getStack().get(pointer1).get(this.id1).getInt();

				if (type1 == Core.INT) {
					ext.replaceStackScope(this.id1, new pairedVal(value, Core.INT), pointer1);
				} else if (type1 == Core.REF) {
					ext.setHeap(val, value);
				}
			} else {
				type1 = ext.getStatic().get(this.id1).getType();
				int val = ext.getStatic().get(this.id1).getInt();

				if (type1 == Core.INT) {
					ext.replaceStaticScope(this.id1, new pairedVal(value, Core.INT));
				} else if (type1 == Core.REF) {
					ext.setHeap(val, value);
				}
			}

			break;

		case 3:

			/* allocation space for ref id */
			boolean inStack2 = false;
			int pointer2 = ext.getStack().size() - 1;
			Core type2 = null;
			HashMap<String, pairedVal> temp2 = null;

			while (pointer2 >= 0) {
				temp2 = ext.getStack().get(pointer2);
				if (temp2.containsKey(this.id1)) {
					inStack2 = true;
					break;
				}
				pointer2--;
			}

			if (inStack2) {

				type2 = ext.getStack().get(pointer2).get(this.id1).getType();
				if (type2 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}

				int position = 0;
				while (ext.checkHeapPosOccupied(position)) {
					position++;
				}
				ext.setHeapStatas(position, true);
				ext.replaceStackScope(this.id1, new pairedVal(position, Core.REF), pointer2);

			} else {

				type2 = ext.getStatic().get(this.id1).getType();
				if (type2 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}

				int position = 0;
				while (ext.checkHeapPosOccupied(position)) {
					position++;
				}
				ext.setHeapStatas(position, true);
				ext.replaceStaticScope(this.id1, new pairedVal(position, Core.REF));
			}

			break;

		case 4:

			/* change the reference value of id1 with id2 */
			boolean inStackId1 = false;
			boolean inStackId2 = false;
			int pointerId1 = ext.getStack().size() - 1;
			int pointerId2 = ext.getStack().size() - 1;
			int valId2 = 0;
			Core typeId1 = null;
			Core typeId2 = null;
			HashMap<String, pairedVal> tempId1 = null;
			HashMap<String, pairedVal> tempId2 = null;

			while (pointerId1 >= 0) {
				tempId1 = ext.getStack().get(pointerId1);
				if (tempId1.containsKey(this.id1)) { // loop to find id1
					inStackId1 = true;
					break;
				}
				pointerId1--;
			}

			while (pointerId2 >= 0) {
				tempId2 = ext.getStack().get(pointerId2);
				if (tempId2.containsKey(this.id2)) { // loop to find id2
					inStackId2 = true;
					break;
				}
				pointerId2--;
			}

			if (inStackId2) {

				typeId2 = ext.getStack().get(pointerId2).get(this.id2).getType();
				if (typeId2 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}

				valId2 = ext.getStack().get(pointerId2).get(this.id2).getInt();
			} else {

				typeId2 = ext.getStatic().get(this.id2).getType();
				if (typeId2 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}
				valId2 = ext.getStatic().get(this.id2).getInt();
			}

			if (inStackId1) {

				typeId1 = ext.getStack().get(pointerId1).get(this.id1).getType();
				if (typeId1 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}

				ext.replaceStackScope(this.id1, new pairedVal(valId2, Core.REF), pointerId1);

			} else {

				typeId1 = ext.getStatic().get(this.id1).getType();
				if (typeId1 != Core.REF) {
					System.out.println("ERROR: CANNOT ASSIGN NEW CLASS TO A NON-REF VARIABLE");
					System.exit(1);
				}
				ext.replaceStaticScope(this.id1, new pairedVal(valId2, Core.REF));
			}

			break;

		default:
			// should never reach here
			break;

		}
	}

}
