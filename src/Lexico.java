import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  @author Arthur Mendonça Ribeiro
 */

public class Lexico {

    private JTextPane editor;

    private JTextPane msg;

    public boolean temErro = false;

    //classes
    public final int CLASS_NULL = -1;
    public final int CLASS_VARIAVEL = 1;
    public final int CLASS_ARRANJO = 2;
    
    //caso token nao tenha tipo
    public final int TIPO_NULL = -1;
    
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
    public final int WHILE = 31;
    public final int IF = 32;
    public final int ELSE = 33;
    public final int EOF = 34;
    public final int INICIO = 35;
    public final int T_INTEIRO = 36;
    public final int T_LINHA = 37;
    public final int T_REAL = 38;
    public final int T_CARACTERE = 39;
    public final int T_BOOLEANO = 40;
    public final int RETORNAR = 42;
    public final int CASO = 43;
    public final int PARAR = 44;
    public final int PADRAO = 45;
    public final int SWITCH = 46;
    public final int TRUE = 49;
    public final int FALSE = 50;
    public final int T_ARRAY = 51;

    public String[] TOKENS  = {"TERMINADOR", "ID", "NUM_INTEIRO", "NUM_REAL", "OP_SOMA", "OP_SUBTRAI", "OP_MULTIPLICA",
            "OP_POTENCIA", "OP_DIVISAO", "OP_IGUAL", "OP_OR", "OP_AND", "ABRE_ARRAY", "FECHA_ARRAY", "SEPARADOR",
            "ATRIB", "ABRE_BLOCO", "FECHA_BLOCO", "OP_MAIOR", "OP_MAIOR_IGUAL", "OP_MENOR", "OP_MENOR_IGUAL",
            "ABRE_EXPR", "FECHA_EXPR", "MOD", "DIFERENTE", "NEGA", "CHAR" ,"STRING", "INPUT", "OUTPUT", "WHILE",
            "IF", "ELSE","EOF", "INICIO", "T_INTEIRO", "T_LINHA", "T_REAL", "T_CARACTERE", "T_BOOLEANO", "ENQUANTO",
            "RETORNAR", "TRUE", "FALSE", "T_ARRAY"
    };

    public ArrayList<Token> SYMBOLS = new ArrayList<>();

	int posIni = 0, posFim = 0, pos = 0, linhaAtual = 1;

    String entrada;

    public String lex;

	public Token anaLex() {
        int estado = 0;

		while (pos < entrada.length()) {
            char c = entrada.charAt(pos++);
            //INICIO SWITCH DE ESTADOS
            switch (estado) {
                case 0:
                    if (c == '\n') {
                        linhaAtual++;
                        estado = 0;
                    }
                    //ignora tabulação, o \r e espaço
                    else if(c == '\t' || c == '\r' || c == ' '){
                        estado = 0;
                    }
                    //se for numero
                    else if (Character.isDigit(c)) {
                        estado = 1;
                        posIni = pos - 1;
                        posFim = pos;
                    }
                    //se for letra
                    else if (Character.isLetter(c)) {
                        estado = 2;
                        posIni = pos - 1;
                        posFim = pos;
                    }
                    //vai pro estado nega, se vier depois um = vai pro diferente
                    else if (c == '!') {
                        estado = 3;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == ':') {
                        estado = 4;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '>') {
                        estado = 5;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '<') {
                        estado = 6;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '+') {
                        estado = 7;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '-') {
                        estado = 8;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '&') {
                        estado = 9;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '|') {
                        estado = 10;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == ';') {
                        estado = 11;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '(') {
                        estado = 12;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '{') {
                        estado = 29;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '[') {
                        estado = 30;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '\"') {
                        estado = 13;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '\'') {
                        estado = 15;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '*') {
                        estado = 21;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '=') {
                        estado = 23;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '/') {
                        estado = 24;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == ',') {
                        estado = 25;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '%') {
                        estado = 26;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == ')') {
                        estado = 31;
                        posIni = pos - 1;
                        posFim = pos;
                    } else if (c == '}') {
                        estado = 32;
                        posIni = pos - 1;
                        posFim = pos;
                    }else if (c == ']') {
                        estado = 33;
                        posIni = pos - 1;
                        posFim = pos;
                    }
                    else if(c == '$'){
                        //define inicio do novo lexema
                        posIni = pos - 1;
                        posFim = pos;
                    }
                    else{
                        erro(c);
                    }

                    break;
                case 1:
                    posFim = pos;
                    if(Character.isDigit(c)){
                        estado = 1;
                    }
                    else if(c == '.'){
                        estado = 27;
                    }
                    else{
                        pos--;
                        return new Token(NUM_INTEIRO, "NUM_INTEIRO", lex, CLASS_NULL, T_INTEIRO);
                    }
                    break;
                case 2:
                    posFim = pos;
                    if(Character.isLetter(c) || Character.isDigit(c) || c == '_'){
                        estado = 2;
                    }
                    else{
                        pos--;
                        //procura se existe ID na tabela de simbolos
                        //se sim, retorna o simbolo encontrado
                        for(Token t : SYMBOLS){
                            if(t.getLexema().equals(lex)){
                                return t;
                            }
                        }
                        //se nao, adiciona o novo ID na tabela
                        Token t = new Token(ID, "ID", lex, CLASS_VARIAVEL, 0);
                        SYMBOLS.add(t);
                        //e o retorna
                        return t;
                    }
                    break;
                //reconhece o !
                case 3:
                    posFim = pos - 1;
                    if(c == '='){
                        estado = 17;
                    }
                    else{
                        pos--;
                        return new Token(NEGA, "NEGA", lex, CLASS_NULL, TIPO_NULL);
                    }
                    break;
                //reconhece o >
                case 5:
                    posFim = pos - 1;
                    if(c == '='){
                        estado = 18;
                    }
                    else{
                        pos--;
                        return new Token(OP_MAIOR, "OP_MAIOR", lex, CLASS_NULL, TIPO_NULL);
                    }
                    break;
                //reconhece o <
                case 6:
                    posFim = pos - 1;
                    if(c == '='){
                        estado = 19;
                    }
                    else if(c == '-'){
                        estado = 34;
                    }
                    else if(c == ':'){
                        estado = 35;
                    }
                    else{
                        pos--;
                        return new Token(OP_MENOR, "OP_MENOR", lex, CLASS_NULL, TIPO_NULL);
                    }
                    break;
                //reconhece o :
                case 4:
                    posFim = pos;
                    if(c == '='){
                        estado = 20;
                    }
                    else
                        erro(c);
                    break;
                //reconhece o +
                case 7:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_SOMA, "OP_SOMA", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o -
                case 8:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_SUBTRAI, "OP_SUBTRAI", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o &
                case 9:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_AND, "OP_AND", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o |
                case 10:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_OR, "OP_OR", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o terminador
                case 11:
                    posFim = pos - 1;
                    pos--;
                    return new Token(TERM, "TERM", lex, CLASS_NULL, TIPO_NULL);
                //se vier um ), ele vai pro estado final de fecha expr
                case 12:
                    posFim = pos - 1;
                    pos--;
                    return new Token(ABRE_EXPR, "ABRE_EXPR", lex, CLASS_NULL, TIPO_NULL);
                //se vier outro ", ele vai pro estado final de fecha string
                case 13:
                    posFim = pos;
                    if(c == '\"'){
                        estado = 14;
                    }
                    else{
                        estado = 13;
                    }
                    break;
                //estado final de fecha string
                case 14:
                    pos--;
                    return new Token(STRING, "STRING", lex, CLASS_NULL, T_LINHA);
                case 15:
                    posFim = pos;
                    if(c == '\''){
                        erro(c);
                    }
                    else{
                        estado = 37;
                    }
                    break;
                case 17:
                    pos--;
                    return new Token(DIFERENTE, "DIFERENTE", lex, CLASS_NULL, TIPO_NULL);
                case 18:
                    pos--;
                    return new Token(OP_MAIOR_IGUAL, "OP_MAIOR_IGUAL", lex, CLASS_NULL, TIPO_NULL);
                case 19:
                    pos--;
                    return new Token(OP_MENOR_IGUAL, "OP_MENOR_IGUAL", lex, CLASS_NULL, TIPO_NULL);
                case 20:
                    pos--;
                    return new Token(ATRIB, "ATRIB", lex, CLASS_NULL, TIPO_NULL);
                case 21:
                    posFim = pos - 1;
                    if(c == '*'){
                        estado = 22;
                    }
                    else{
                        pos--;
                        return new Token(OP_MULTIPLICA, "OP_MULTIPLICA", lex, CLASS_NULL, TIPO_NULL);
                    }
                    break;
                case 22:
                    pos--;
                    return new Token(OP_POTENCIA, "OP_POTENCIA", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o =
                case 23:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_IGUAL, "OP_IGUAL", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o /
                case 24:
                    posFim = pos - 1;
                    pos--;
                    return new Token(OP_DIVISAO, "OP_DIVISAO", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o ,
                case 25:
                    posFim = pos - 1;
                    pos--;
                    return new Token(SEPARADOR, "SEPARADOR", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o %
                case 26:
                    posFim = pos - 1;
                    pos--;
                    return new Token(MOD, "MOD", lex, CLASS_NULL, TIPO_NULL);
                //reconhece o numero real no prox estado
                case 27:
                    posFim = pos - 1;
                    if(Character.isDigit(c)){
                        estado = 28;
                    }
                    else{
                        erro(c);
                    }
                    break;
                case 28:
                    if(!Character.isDigit(c)){
                        pos--;
                        return new Token(NUM_REAL, "NUM_REAL", lex, CLASS_NULL, T_REAL);
                    }
                    else{
                        estado = 28;
                    }
                    break;
                case 29:
                    posFim = pos - 1;
                    pos--;
                    return new Token(ABRE_BLOCO, "ABRE_BLOCO", lex, CLASS_NULL, TIPO_NULL);
                case 30:
                    posFim = pos - 1;
                    pos--;
                    return new Token(ABRE_ARRAY, "ABRE_ARRAY", lex, CLASS_NULL, T_ARRAY);
                case 31:
                    posFim = pos - 1;
                    pos--;
                    return new Token(FECHA_EXPR, "FECHA_EXPR", lex, CLASS_NULL, TIPO_NULL);
                case 32:
                    posFim = pos - 1;
                    pos--;
                    return new Token(FECHA_BLOCO, "FECHA_BLOCO", lex, CLASS_NULL, TIPO_NULL);
                case 33:
                    posFim = pos - 1;
                    pos--;
                    return new Token(FECHA_ARRAY, "FECHA_ARRAY", lex, CLASS_NULL, TIPO_NULL);
                case 34:
                    posFim = pos - 1;
                    if(c == '\n'){
                        estado = 0;
                    }
                    else{
                        estado = 34;
                    }
                    break;
                case 35:
                    posFim = pos - 1;
                    if(c == ':'){
                        estado = 36;
                    }
                    else {
                        estado = 35;
                    }
                    break;
                case 36:
                    posFim = pos - 1;
                    if(c == ':'){
                        estado = 36;
                    }
                    else if(c == '>'){
                        estado = 0;
                    }
                    else {
                        estado = 35;
                    }
                    break;
                case 37:
                    posFim = pos - 1;
                    if(c == '\''){
                        return new Token(CHAR, "CHAR", lex, CLASS_NULL, T_CARACTERE);
                    }
                    else{
                        erro(c);
                    }
                    break;
                default:
                    erro(c);
                    break;
                //FIM SWITCH DE ESTADOS
            }

            lex = entrada.substring(posIni, posFim);
//            this.msg.setText(this.msg.getText() + "\n[DEBUG] Lexema atual: " + lex);
        }

        if(estado == 13){
            this.msg.setText(this.msg.getText() + "\nErro léxico na coluna " + posFim + ", na linha " + linhaAtual + ": " +
                    "faltou fechar linha");
        }

		//fim do arquivo
        return new Token(EOF, "EOF", lex, -1, -1);
	}

	public void erro(char c) {
	    temErro = true;
        this.msg.setText(this.msg.getText() + "\nErro léxico na coluna " + posFim + ", no caractere " + c + ", na linha " + linhaAtual + ".");
	}

    public Lexico(JTextPane editor, JTextPane msg) {
        this.editor = editor;
        this.msg = msg;

        entrada = editor.getText() + "$";

        SYMBOLS.add(new Token(29, "INPUT", "ler", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(30, "OUTPUT", "escrever", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(31, "ENQUANTO", "enquanto", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(32, "SE", "se", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(33, "SENAO", "senao", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(35, "INICIO", "programa", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(36, "T_INTEIRO", "inteiro", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(37, "T_LINHA", "linha", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(38, "T_REAL", "real", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(39, "T_CARACTERE", "caractere", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(40, "T_BOOLEANO", "booleano", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(42, "RETORNAR", "retornar", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(43, "CASO", "caso", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(44, "PARAR", "parar", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(45, "PADRAO", "padrao", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(46, "INTERRUPTOR", "interruptor", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(48, "FUNCAO", "funcao", CLASS_NULL, TIPO_NULL));
        SYMBOLS.add(new Token(49, "TRUE", "verdadeiro", CLASS_NULL, T_BOOLEANO));
        SYMBOLS.add(new Token(50, "FALSE", "falso", CLASS_NULL, T_BOOLEANO));
        SYMBOLS.add(new Token(51, "T_ARRAY", "arranjo", CLASS_NULL, T_ARRAY));
    }
}
