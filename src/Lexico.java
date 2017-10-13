import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Lexico {
    //estados finais
    public final int EOF = -1;
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
	//array de simbolos
	ArrayList<Simbolo> simbolos;

	int pos;

    String entrada;

	public int anaLex() {
        int estado = 0;

		while (pos < entrada.length()) {
			char c = getCaracter(entrada);
                        
			System.out.println("[DEBUG] Caractere lido: " + c);

			//INICIO SWITCH DE ESTADOS

			//FIM SWITCH DE ESTADOS

			String lex = entrada.substring(0, pos);
		}

		//fim do arquivo
		return -1;
	}

	public char getCaracter(String entrada) {
		return entrada.charAt(pos++);
	}

	public void erro(char c) {
		System.out.println("Erro léxico na coluna " + pos + ", no caractere " + c + ".");
	}
	
	public Lexico() {
		int token;
		token = anaLex();
        while(token != -1){
            token = anaLex();
        }

	}

	public ArrayList<Simbolo> initTabelaSimbolos(){
		ArrayList<Simbolo> simbolos = new ArrayList<>();

		try {

			File f = new File("src/com/mkyong/data.txt");

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
		new Lexico();
	}

}