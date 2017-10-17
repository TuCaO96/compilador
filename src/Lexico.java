import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Lexico {
    //tokens
    public final int TERM = 0;
    public final int ID = 1;
    public final int NUM_INTEIRO = 2;
    public final int NUM_REAL = 3;
    public final int OP_SOMA = 4;
    public final int OP_SUBTRAI = 5;
    public final int OP_MULTIPLICA = 6;
    public final int OP_POTENCIA = 7;
    public final int OP_DIVISAO = 8;
    public final int OP_IGUAL = 9;
    public final int OP_OR = 10;
    public final int OP_AND = 11;
    public final int ABRE_ARRAY = 12;
    public final int FECHA_ARRAY = 13;
    public final int SEPARADOR = 14;
    public final int ATRIB = 15;
    public final int ABRE_BLOCO = 16;
    public final int FECHA_BLOCO = 17;
    public final int OP_MAIOR = 18;
    public final int OP_MAIOR_IGUAL = 19;
    public final int OP_MENOR = 20;
    public final int OP_MENOR_IGUAL = 21;
    public final int ABRE_EXPR = 22;
    public final int FECHA_EXPR = 23;
    public final int MOD = 24;
    public final int DIFERENTE = 25;
    public final int NEGA = 26;
    public final int CHAR = 27;
    public final int STRING = 28;
    public final int INPUT = 29;
    public final int OUTPUT = 30;
    public final int EOF = 31;

    //array de simbolos
    ArrayList<Simbolo> simbolos;
    //array de tokens
    public String[] TOKEN  = {"TERM", "ID", "NUM_INTEIRO", "NUM_REAL", "OP_SOMA", "OP_SUBTRAI", "OP_MULTIPLICA",
            "OP_POTENCIA", "OP_DIVISAO", "OP_IGUAL", "OP_OR", "OP_AND", "ABRE_ARRAY", "FECHA_ARRAY", "SEPARADOR",
            "ATRIB", "ABRE_BLOCO", "FECHA_BLOCO", "OP_MAIOR", "OP_MAIOR_IGUAL", "OP_MENOR", "OP_MENOR_IGUAL",
            "ABRE_EXPR", "FECHA_EXPR", "MOD", "DIFERENTE", "NEGA", "CHAR" ,"STRING", "INPUT", "OUTPUT", "EOF"
    };

	int pos = 0, contLinhas = 0;

    String entrada;

	public int anaLex() {
        int estado = 0;

		while (pos < entrada.length()) {
			char c = entrada.charAt(pos++);
                        
			System.out.println("[DEBUG] Caractere lido: " + c);

			//INICIO SWITCH DE ESTADOS
            case 0:
            	if(c == '\n'){
            	    contLinhas++;
                }

                //se for digito
                if(Character.isDigit(c)){
            	    estado = 1;
                }

                //se for letra
                if(Character.isLetter(c)){
                    estado = 2;
                }

                //vai pro estado nega, se vier depois um = vai pro diferente
                if(c == '!'){
                    estado = 3;
                }

                if(c == ':'){
                    estado = 4;
                }

                if(c == '>'){
                    estado = 5;
                }

                if(c == '<'){
                    estado = 6;
                }

                if(c == '+'){
                    estado = 7;
                }

                if(c == '-'){
                    estado = 8;
                }

                if(c == '&'){
                    estado = 9;
                }

                if(c == '|'){
                    estado = 10;
                }

                break;
			//FIM SWITCH DE ESTADOS

			String lex = entrada.substring(0, pos);
		}

		//fim do arquivo
		return -1;
	}

	public void erro(char c) {
		System.out.println("Erro léxico na coluna " + pos + ", no caractere " + c + ".");
	}
	
	public Lexico() {
		int token;
		token = anaLex();
        while(token != EOF){
            token = anaLex();
        }

	}

	public ArrayList<Simbolo> initTabelaSimbolos(){
		ArrayList<Simbolo> simbolos = new ArrayList<>();

		try {

			File f = new File("tokens_and_symbols/simbolos.txt");

			Scanner scan = new Scanner(f);

			BufferedReader b = new BufferedReader(new FileReader(f));

			String linha, posicao, token, lexema;

			//delimitador para nova linha é o ;
			scan.useDelimiter(Pattern.compile(";"));
			while (scan.hasNext()) {
				//proxima linha
				linha = scan.next();
				//dividimos a linha por cada virgula encontrada
				String[] partes = linha.split(",");
				//e entao salvamos cada informacao encontrada no objeto
				Simbolo simbolo = new Simbolo();
				simbolo.setPosicao(Integer.parseInt(partes[0]));
				simbolo.setToken(partes[1]);
				simbolo.setLexema(partes[2]);
				//adiciona novo simbolo na tabela
				simbolos.add(simbolo);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return simbolos;
	}

	public static void main(String[] args) {
		Lexico lex = new Lexico();
		lex.initTabelaSimbolos();
	}

}