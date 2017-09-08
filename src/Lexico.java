public class Lexico {
	public final int NUM_DEC = 0;
	public final int NUM_OCT = 1;
	public final int NUM_HEX = 2;
	public final int EOF = 3;
	public final int TERM = 4;
	public final int ID = 5;
	public String[] TOKEN  = {"DECIMAL", "OCTAL", "DECIMAL", "EOF", "TERM", "ID"};
	
	String entrada = "0x3f8;0724;23;abcd;" + "$";

	String lex = "";

	int pos = 0;

	public int anaLex() {
        int estado = 0;
		while (pos < entrada.length()) {
			char c = getCaracter();
                        
			System.out.println("[DEBUG] Caractere lido: " + c);

			switch (estado) {
			case 0:
				if (c == '0')
					estado = 2;
				else if (c >= '1' && c <= '9')
					estado = 1;
				else if(c == '$')
				    estado = 6;
				else if(c == ';')
				    estado = 7;
				else if(Character.isAlphabetic(c) || c == '_')
				    estado = 8;
				else
					erro(c);
				break;
			case 1:
				if (c >= '0' && c <= '9')
					estado = 1;
				else {
					pos--;
					return NUM_DEC;
				}
				break;
			case 2:
				if (c >= '0' && c <= '7')
					estado = 3;
				else if (c == 'x' || c == 'X')
					estado = 4;
				else {
					pos--;
					return NUM_OCT;
				}
				break;
			case 3:
				if (c >= '0' && c <= '7')
					estado = 3;
				else {
					pos--;
					return NUM_OCT;
				}
				break;
			case 4:
				if (Character.isDigit(c) || (c >= 'a' && c <= 'f'))
					estado = 5;
				else
					erro(c);
				break;
			case 5:
				if (Character.isDigit(c) || (c >= 'a' && c <= 'f'))
					estado = 5;
				else {
					pos--;
					return NUM_HEX;
				}
				break;
            case 6:
                pos--;
                return EOF;
            case 7:
                pos--;
                return TERM;
            case 8:
                if(Character.isAlphabetic(c) || Character.isDigit(c) || c == '_'){
                    estado = 8;
                }
                else{
                    pos--;
                    return ID;
                }
                break;
            default:
				erro(c);
			}

			lex = entrada.substring(0, pos);
		}
		return EOF;
	}

	public char getCaracter() {
		return entrada.charAt(pos++);
	}

	public void erro(char c) {
		System.out.println("Erro léxico na coluna " + pos + ", no caractere " + c + ".");
	}
	
	public Lexico() {
		int token;
		token = anaLex();
        while(token != EOF){
            System.out.println(TOKEN[token]);
            token = anaLex();
        }

	}
	
	public static void main(String[] args) {
		new Lexico();
	}

}