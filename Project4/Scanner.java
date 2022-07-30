import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

class Scanner {

	private Core c;
	private BufferedReader br;
	private StringBuilder sb;
	private final String specials = ";(),=!<+-*{}"; // contains all special characters
	private boolean indicator = false; // decide if stringbuilder shall be used
	private HashMap<String, Core> keys;

	// Constructor should open the file and find the first token
	Scanner(String filename) {

		try {
			this.br = new BufferedReader(new FileReader(filename));
			this.keys = new HashMap<String, Core>();
			this.buildMap();
		} catch (FileNotFoundException e) {

			System.out.println("Error: File Not Found");
			System.exit(1);
		}
		this.nextToken();
	}

	// nextToken should advance the scanner to the next token
	public void nextToken() {

		try {
			int next = this.br.read();
			int cases = 0;

			/*
			 * If the file is empty at the beginning, return EOS
			 */
			if (next == -1) {
				this.c = Core.EOS;
				return;
			}

			while (Character.isWhitespace(next) && next != -1) {
				next = this.br.read();
			}

			if (next == -1) {
				this.c = Core.EOS;
				return;
			}

			/**
			 * Handle different types of input
			 */
			if (specials.contains(Character.toString((char) next))) {
				cases = 1;
			} else if (Character.isDigit((char) next)) {
				cases = 2;
			} else if (Character.isLetter((char) next)) {
				cases = 3;
			} else {
				cases = 4;
			}

			switch (cases) {

			/* Case for specials */
			case 1:

				if ((char) next != '<' && (char) next != '=') {
					this.c = this.keys.get(Character.toString((char) next));
				} else {
					if ((char) next == '<') {
						this.br.mark(1);
						int nextTemp = this.br.read();
						if ((char) nextTemp == '=') {
							this.c = this.keys.get("<=");
						} else {
							this.c = this.keys.get("<");
							this.br.reset();
						}
					} else {
						this.br.mark(1);
						int nextTemp2 = this.br.read();
						if ((char) nextTemp2 == '=') {
							this.c = this.keys.get("==");
						} else {
							this.c = this.keys.get("=");
							this.br.reset();
						}
					}
				}

				break;

			/* Case for Constants */
			case 2:
				boolean flag = true;
				this.indicator = true;
				this.sb = new StringBuilder();
				while (flag) {
					this.sb.append((char) next);
					this.br.mark(1);
					next = this.br.read();
					if (next == -1 || !Character.isDigit((char) next)) {
						flag = false;
						this.br.reset();
					}
				}
				break;

			/* Cases for IDs/Keywords */
			case 3:
				boolean flag2 = true;
				this.indicator = true;
				this.sb = new StringBuilder();
				while (flag2) {
					this.sb.append((char) next);
					this.br.mark(1);
					next = this.br.read();
					if (next == -1 || !(Character.isDigit((char) next) || Character.isLetter((char) next))) {
						flag2 = false;
						this.br.reset();
					}

				}
				break;

			/* Cases for invalid input */
			case 4:
				this.c = Core.ERROR;
				throw new Exception("ERROR: Invalid Input: \"" + Character.toString((char) next) + "\"");

			default: {

			}
				break;
			}

			/* Build String for Constants/IDs */
			if (indicator) {
				this.indicator = false; // reset indicator for the next token
				String code = this.sb.toString();

				if (this.keys.containsKey(code)) {
					this.c = this.keys.get(code);

				} else {
					if (code.matches("[a-zA-Z][a-zA-Z0-9]*")) {
						this.c = Core.ID;
					} else if (code.matches("[0-9]|[1-9][0-9]*")) {
						if (Integer.parseInt(code) <= 255) {
							this.c = Core.CONST;
						} else {
							this.c = Core.ERROR;
							throw new Exception("ERROR: the Number \"" + code + "\" is OUT OF BOUND");
						}
					} else {
						this.c = Core.ERROR;
						throw new Exception("ERROR: UNEXPECTED VIOLATION OF GRAMMAR");
					}
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
			System.exit(1);
		}

	}

	// currentToken should return the current token
	public Core currentToken() {
		return this.c;
	}

	// If the current token is ID, return the string value of the identifier
	// Otherwise, return value does not matter
	public String getID() {
		return this.sb.toString();
	}

	// If the current token is CONST, return the numerical value of the constant
	// Otherwise, return value does not matter
	public int getCONST() {
		return Integer.parseInt(this.sb.toString());
	}

	/* Build the tokens map */
	private void buildMap() {
		this.keys.put(";", Core.SEMICOLON);
		this.keys.put("(", Core.LPAREN);
		this.keys.put(")", Core.RPAREN);
		this.keys.put(",", Core.COMMA);
		this.keys.put("=", Core.ASSIGN);
		this.keys.put("!", Core.NEGATION);
		this.keys.put("==", Core.EQUAL);
		this.keys.put("<", Core.LESS);
		this.keys.put("<=", Core.LESSEQUAL);
		this.keys.put("+", Core.ADD);
		this.keys.put("-", Core.SUB);
		this.keys.put("*", Core.MULT);
		this.keys.put("{", Core.LBRACE);
		this.keys.put("}", Core.RBRACE);

		this.keys.put("program", Core.PROGRAM);
		this.keys.put("begin", Core.BEGIN);
		this.keys.put("end", Core.END);
		this.keys.put("new", Core.NEW);
		this.keys.put("int", Core.INT);
		this.keys.put("define", Core.DEFINE);
		this.keys.put("class", Core.CLASS);
		this.keys.put("extends", Core.EXTENDS);
		this.keys.put("if", Core.IF);
		this.keys.put("then", Core.THEN);
		this.keys.put("else", Core.ELSE);
		this.keys.put("while", Core.WHILE);
		this.keys.put("or", Core.OR);
		this.keys.put("input", Core.INPUT);
		this.keys.put("output", Core.OUTPUT);
		this.keys.put("ref", Core.REF);
		this.keys.put("share", Core.SHARE);
	}

}