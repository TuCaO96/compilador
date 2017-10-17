import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

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

	int posIni = 0, posFim = 0, linhaAtual = 1;

    String entrada;

	public int anaLex() {
        int estado = 0;

		while (posFim < entrada.length()) {
            char c = entrada.charAt(posFim++);

            this.msg.setText(this.msg.getText() + "\n[DEBUG] Caractere lido: " + c);

            switch (estado) {
                //INICIO SWITCH DE ESTADOS
                case 0:
                    if (c == '\n') {
                        linhaAtual++;
                    }

                    else if(c == '\t'){
                        estado = 0;
                    }

                    else if(c == '\r'){
                        estado = 0;
                    }

                    //se for digito
                    else if (Character.isDigit(c)) {
                        estado = 1;
                    }
                    //se for letra
                    else if (Character.isLetter(c)) {
                        estado = 2;
                    }
                    //vai pro estado nega, se vier depois um = vai pro diferente
                    else if (c == '!') {
                        estado = 3;
                    } else if (c == ':') {
                        estado = 4;
                    } else if (c == '>') {
                        estado = 5;
                    } else if (c == '<') {
                        estado = 6;
                    } else if (c == '+') {
                        estado = 7;
                    } else if (c == '-') {
                        estado = 8;
                    } else if (c == '&') {
                        estado = 9;
                    } else if (c == '|') {
                        estado = 10;
                    } else if (c == ';') {
                        estado = 11;
                    } else if (c == '(') {
                        estado = 12;
                    } else if (c == '{') {
                        estado = 12;
                    } else if (c == '[') {
                        estado = 12;
                    } else if (c == '\"') {
                        posIni = posFim - 1;
                        estado = 13;
                    } else if (c == '\'') {
                        estado = 14;
                    }

                    break;
                case 1:

                    break;
                case 2:

                    break;
                case 3:

                    break;
                case 13:
                    if(c == '\"'){
                        return STRING;
                    }
                    else{
                        estado = 13;
                    }
                    break;
                case 14:
                    break;
                default:
                    erro(c);
                    break;
                //FIM SWITCH DE ESTADOS
            }

            String lex = entrada.substring(posIni, posFim);
            this.msg.setText(this.msg.getText() + "\n[DEBUG] Lexema atual: " + lex);
        }

		//fim do arquivo
		return 35;
	}

	public void erro(char c) {
        this.msg.setText(this.msg.getText() + "\nErro léxico na coluna " + posFim + ", no caractere " + c + ", na linha " + linhaAtual + ".");
	}

    public Lexico(JTextPane editor, JTextPane msg) {
        this.editor = editor;
        this.msg = msg;

        entrada = editor.getText();
    }
}