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
    public final int ELSEIF = 34;
    public final int EOF = 35;

    public String[] TOKENS  = {"TERMINADOR", "ID", "NUM_INTEIRO", "NUM_REAL", "OP_SOMA", "OP_SUBTRAI", "OP_MULTIPLICA",
            "OP_POTENCIA", "OP_DIVISAO", "OP_IGUAL", "OP_OR", "OP_AND", "ABRE_ARRAY", "FECHA_ARRAY", "SEPARADOR",
            "ATRIB", "ABRE_BLOCO", "FECHA_BLOCO", "OP_MAIOR", "OP_MAIOR_IGUAL", "OP_MENOR", "OP_MENOR_IGUAL",
            "ABRE_EXPR", "FECHA_EXPR", "MOD", "DIFERENTE", "NEGA", "CHAR" ,"STRING", "INPUT", "OUTPUT", "WHILE",
            "IF", "ELSE", "ELSEIF","EOF"
    };

	int posIni = 0, posFim = 0, pos = 0, linhaAtual = 1;

    String entrada;

    public String lex;

	public int anaLex() {
        int estado = 0;


		while (pos < entrada.length()) {
            char c = entrada.charAt(pos++);
//            this.msg.setText(this.msg.getText() + "\n[DEBUG] Caractere lido: " + c);
//            this.msg.setText(this.msg.getText() + "\n[DEBUG] Estado final: " + estado);
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
                        return NUM_INTEIRO;
                    }
                    break;
                case 2:
                    posFim = pos;
                    if(Character.isLetter(c) || Character.isDigit(c) || c == '_'){
                        estado = 2;
                    }
                    else{
                        pos--;
                        return ID;
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
                        return NEGA;
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
                        return OP_MAIOR;
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
                        return OP_MENOR;
                    }
                    break;
                //reconhece o :
                case 4:
                    posFim = pos - 1;
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
                    return OP_SOMA;
                //reconhece o -
                case 8:
                    posFim = pos - 1;
                    pos--;
                    return OP_SUBTRAI;
                //reconhece o &
                case 9:
                    posFim = pos - 1;
                    pos--;
                    return OP_AND;
                //reconhece o |
                case 10:
                    posFim = pos - 1;
                    pos--;
                    return OP_OR;
                //reconhece o terminador
                case 11:
                    posFim = pos - 1;
                    pos--;
                    return TERM;
                //se vier um ), ele vai pro estado final de fecha expr
                case 12:
                    posFim = pos - 1;
                    pos--;
                    return ABRE_EXPR;
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
                    return STRING;
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
                    return DIFERENTE;
                case 18:
                    pos--;
                    return OP_MAIOR_IGUAL;
                case 19:
                    pos--;
                    return OP_MENOR_IGUAL;
                case 20:
                    pos--;
                    return ATRIB;
                case 21:
                    posFim = pos - 1;
                    if(c == '*'){
                        estado = 22;
                    }
                    else{
                        pos--;
                        return OP_MULTIPLICA;
                    }
                    break;
                case 22:
                    pos--;
                    return OP_POTENCIA;
                //reconhece o =
                case 23:
                    posFim = pos - 1;
                    pos--;
                    return OP_IGUAL;
                //reconhece o /
                case 24:
                    posFim = pos - 1;
                    pos--;
                    return OP_DIVISAO;
                //reconhece o ,
                case 25:
                    posFim = pos - 1;
                    pos--;
                    return SEPARADOR;
                //reconhece o %
                case 26:
                    posFim = pos - 1;
                    pos--;
                    return MOD;
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
                        return NUM_REAL;
                    }
                    else{
                        estado = 28;
                    }
                    break;
                case 29:
                    posFim = pos - 1;
                    pos--;
                    return ABRE_BLOCO;
                case 30:
                    posFim = pos - 1;
                    pos--;
                    return ABRE_ARRAY;
                case 31:
                    posFim = pos - 1;
                    pos--;
                    return FECHA_EXPR;
                case 32:
                    posFim = pos - 1;
                    pos--;
                    return FECHA_BLOCO;
                case 33:
                    posFim = pos - 1;
                    pos--;
                    return FECHA_ARRAY;
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
                        return CHAR;
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
            this.msg.setText(this.msg.getText() + "\nErro: Faltou fechar string na coluna " + posFim + ", na linha " + linhaAtual + ".");
        }

        if(estado == 15){
            this.msg.setText(this.msg.getText() + "\nErro: Faltou fechar caractere na coluna " + posFim + ", na linha " + linhaAtual + ".");
        }

		//fim do arquivo
		return EOF;
	}

	public void erro(char c) {
        this.msg.setText(this.msg.getText() + "\nErro léxico na coluna " + posFim + ", no caractere " + c + ", na linha " + linhaAtual + ".");
	}

    public Lexico(JTextPane editor, JTextPane msg) {
        this.editor = editor;
        this.msg = msg;

        entrada = editor.getText() + "$";
    }
}
