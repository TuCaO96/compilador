import com.sun.org.apache.xpath.internal.operations.Neg;
import com.sun.prism.shader.Solid_TextureYV12_Loader;

import javax.smartcardio.ATR;
import javax.swing.*;
import java.util.Scanner;

public class Parser {
    private Lexico lex;
    private Token token;
    private JTextPane editor;
    private boolean erro = false;

    private JTextPane msg;

    public Token casaToken(int tokenEsperado) {
        if(token.getId() == tokenEsperado) {
            token = lex.anaLex();
            return token;
        }
        else{
            if(token.getId() != lex.EOF){
                erro("\nErro sintático na linha " + lex.linhaAtual + ": " +
                        "esperava token " + tokenEsperado + " e foi encontrado " + token.getId());
                token = lex.anaLex();
            }
            return null;
        }
    }

    public void erro(String msg) {
        erro = true;
        this.msg.setText(this.msg.getText() + msg);
    }

    //Define o tipo da variavel
    public Token atribuiTipo(Token token, Token tipo){
        if(tipo.getId() == lex.T_ARRAY){
            token.setClasse(lex.CLASS_ARRANJO);
            token.setTipo(lex.T_ARRAY);
        }
        else if(tipo.getId() == lex.T_BOOLEANO){
            token.setTipo(lex.T_BOOLEANO);
        }
        else if(tipo.getId() == lex.T_CARACTERE){
            token.setTipo(lex.T_CARACTERE);
        }
        else if(tipo.getId() == lex.T_LINHA){
            token.setTipo(lex.T_LINHA);
        }
        else if(tipo.getId() == lex.T_REAL){
            token.setTipo(lex.T_REAL);
        }
        else if(tipo.getId() == lex.T_INTEIRO){
            token.setTipo(lex.T_INTEIRO);
        }
        else{
            erro("\n Erro semântico: Tipo não reconhecido de variável na linha " + lex.linhaAtual);
        }

        return token;
    }

    //S -> 'programa' id BLOCO
    private void S(){
        casaToken(lex.INICIO);
        casaToken(lex.ID);
        BLOCO();
        if(!erro){
            this.msg.setText(this.msg.getText() + "\nCompilação executada com sucesso");
        }
    }

    //BLOCO -> '{' CMD* '}'
    private void BLOCO(){
        casaToken(lex.ABRE_BLOCO);
        //enquanto id do token esperado for um dos primeiro esperados pelo cmd, continua nele
        while(token.getId() == lex.IF || token.getId() == lex.WHILE || token.getId() == lex.ID ||
                token.getId() == lex.T_INTEIRO || token.getId() == lex.T_BOOLEANO || 
                token.getId() == lex.T_LINHA || token.getId() == lex.T_CARACTERE ||
                token.getId() == lex.ABRE_BLOCO || token.getId() == lex.SWITCH ||
                token.getId() == lex.PARAR || token.getTipo() == lex.T_ARRAY ||
                token.getId() == lex.T_REAL || token.getId() == lex.OUTPUT){
            CMD();
        }
        casaToken(lex.FECHA_BLOCO);
    }

    //CMD -> IF | WHILE | ATRIB | DECLAR | BLOCO | SWITCH | escrever | break; | retornar;
    private void CMD(){
        if(token.getId() == lex.IF){
            IF();
        }
        else if(token.getId() == lex.WHILE){
            WHILE();
        }
        else if(token.getId() == lex.ID){
            ATRIB();
        }
        else if(token.getId() == lex.T_INTEIRO || token.getId() == lex.T_BOOLEANO || token.getId() == lex.T_REAL ||
                token.getId() == lex.T_LINHA || token.getId() == lex.T_CARACTERE || token.getTipo() == lex.T_ARRAY){
            DECLAR();
        }
        else if(token.getId() == lex.ABRE_BLOCO){
            BLOCO();
        }
        else if(token.getId() == lex.SWITCH){
            SWITCH();
        }
        else if(token.getId() == lex.OUTPUT){
            PRINT();
        }
        else if(token.getId() == lex.PARAR){
            casaToken(lex.PARAR);
            casaToken(lex.TERM);
        }
        else if(token.getId() == lex.RETORNAR){
            casaToken(lex.RETORNAR);
            casaToken(lex.TERM);
        }
    }

    //IF -> se '(' EXP ')' CMD {senao CMD}*
    private void IF(){
        casaToken(lex.IF);
        casaToken(lex.ABRE_EXPR);
        EXPR();
        casaToken(lex.FECHA_EXPR);
        CMD();
        while(token.getId() == lex.ELSE){
            if(token.getId() == lex.ELSE){
                casaToken(lex.ELSE);
                CMD();
            }
        }

    }

    //WHILE -> enquanto '(' EXP ')' CMD
    private void WHILE(){
        casaToken(lex.WHILE);
        casaToken(lex.ABRE_EXPR);
        EXPR();
        casaToken(lex.FECHA_EXPR);
        CMD();
    }

    //DECLAR -> TIPO id [:= EXP] {',' id [:= EXP]}* ';'
    private Token DECLAR(){
        //pega tipo
        Token tipo = token;
        //define se tipo é valido
        TIPO();
        //pega id
        Token id = token;
        //define tipo pro id
        if (casaToken(lex.ID) != null){
            id = atribuiTipo(id, tipo);
        }
        //se declaração tambem houver atribuição
        if(token.getId() == lex.ATRIB){
            casaToken(lex.ATRIB);
            if(token.getTipo() == lex.T_LINHA && token.getId() == lex.STRING){
                Token tkn = EXPR();

                if(tkn == null){
                    erro("\nErro semântico: Não é possível realizar operações com token inválido");
                    return null;
                }

                if(tkn.getTipo() != token.getTipo()){
                    erro("\nErro semântico: Atribuição de tipo diferente da variável");
                }
            }
            else if(token.getTipo() == lex.T_CARACTERE && token.getId() == lex.CHAR){
                Token tkn = EXPR();

                if(tkn == null){
                    erro("\nErro semântico: Não é possível realizar operações com token inválido");
                    return null;
                }

                if(tkn.getTipo() != token.getTipo()){
                    erro("\nErro semântico: Atribuição de tipo diferente da variável");
                }
            }
            else if((token.getTipo() == lex.T_REAL || token.getTipo() == lex.T_INTEIRO) &&
                    (token.getId() != lex.STRING && token.getId() != lex.CHAR && token.getId() != lex.ABRE_ARRAY)){
                Token tkn = EXPR();

                if(tkn == null){
                    erro("\nErro semântico: Não é possível realizar operações com token inválido");
                    return null;
                }

                if(tkn.getTipo() != id.getTipo()){
                    erro("\nErro semântico: Atribuição de tipo diferente da variável");
                }
            }
            else if(token.getTipo() == lex.T_ARRAY && token.getId() == lex.ABRE_ARRAY){
                ARRAY();
            }
            else{
                erro("\nErro semântico: Variável " + id.getLexema() + " é de tipo diferente do atribuído " +
                        "na linha " + lex.linhaAtual);
                return null;
            }
        }

        while (token.getId() == lex.SEPARADOR){
            casaToken(lex.SEPARADOR);
            casaToken(lex.ID);
            //se declaração tambem houver atribuição
            if(token.getId() == lex.ATRIB){
                casaToken(lex.ATRIB);
                if(tipo.getId() == lex.T_LINHA && token.getId() == lex.STRING){
                    EXPR();
                }
                else if(tipo.getId() == lex.T_CARACTERE && token.getId() == lex.CHAR){
                    EXPR();
                }
                else if((tipo.getId() == lex.T_REAL || tipo.getId() == lex.T_INTEIRO) &&
                        (token.getId() != lex.STRING && token.getId() != lex.CHAR && token.getId() != lex.ABRE_ARRAY)){
                    EXPR();
                }
                else if(tipo.getId() == lex.T_ARRAY && token.getId() == lex.ABRE_ARRAY){
                    ARRAY();
                }
                else{
                    erro("\n Erro semântico: Variável "+id.getLexema()+" é de tipo diferente do atribuído " +
                            "na linha " + lex.linhaAtual);
                }
            }
        }
        //terminador
        casaToken(lex.TERM);

        return id;
    }

    //ATRIB -> id ':=' EXP ';' | id ':=' ARRAY ';'
    private Token ATRIB(){
        Token id = token;
        casaToken(lex.ID);
        casaToken(lex.ATRIB);
        if(token.getTipo() == lex.T_LINHA && token.getId() == lex.STRING){
            Token tkn = EXPR();

            if(tkn == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                return null;
            }

            if(tkn.getTipo() != token.getTipo()){
                erro("\nErro semântico: Atribuição de tipo diferente da variável");
            }
        }
        else if(token.getTipo() == lex.T_CARACTERE && token.getId() == lex.CHAR){
            Token tkn = EXPR();

            if(tkn == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                return null;
            }

            if(tkn.getTipo() != token.getTipo()){
                erro("\nErro semântico: Atribuição de tipo diferente da variável");
            }
        }
        else if((token.getTipo() == lex.T_REAL || token.getTipo() == lex.T_INTEIRO) &&
                (token.getId() != lex.STRING && token.getId() != lex.CHAR && token.getId() != lex.ABRE_ARRAY)){
            Token tkn = EXPR();

            if(tkn == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                return null;
            }

            if(tkn.getTipo() != token.getTipo()){
                erro("\nErro semântico: Atribuição de tipo diferente da variável");
            }
        }
        else if(token.getTipo() == lex.T_ARRAY && token.getId() == lex.ABRE_ARRAY){
            ARRAY();
        }
        else{
            erro("\nErro semântico: Variável " + id.getLexema() + " é de tipo diferente do atribuído " +
                    "na linha " + lex.linhaAtual);
            return null;
        }
        casaToken(lex.TERM);
        return id;
    }

    //ARRAY -> '['LITERAL {',' LITERAL}*']'
    private Token ARRAY(){
        casaToken(lex.ABRE_ARRAY);
        Token tkn = LITERAL();
        if(token.getId() == lex.SEPARADOR){
            while (token.getId() == lex.SEPARADOR){
                casaToken(lex.SEPARADOR);
                LITERAL();
            }
        }
        casaToken(lex.FECHA_ARRAY);
        return tkn;
    }

    //EXPR -> EXPRS [ OP_REL EXPRS ]
    private Token EXPR(){
        Token tkn = EXPRS();
        //verifica o tipo do token e cria variavel pra receber resultado de acordo com isso
        if(tkn == null){
            erro("\nErro semântico: Não é possível realizar operações com token inválido");
            return null;
        }
        
        if(token.getId() == lex.OP_IGUAL || token.getId() == lex.DIFERENTE || token.getId() == lex.OP_MENOR_IGUAL ||
                token.getId() == lex.OP_MENOR || token.getId() == lex.OP_MAIOR_IGUAL || token.getId() == lex.OP_MAIOR){
            Token operador = OP_REL();
            Token tkn2 = EXPRS();

            if(operador == null || tkn2 == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                return null;
            }
            else if(tkn2.getTipo() != tkn.getTipo()){
                erro("\nErro semântico: Não é possível mesclar valores de tokens de tipos diferentes");
                return null;
            }

            //pega de acordo com os tipos das variaveis o resultado
            //e realiza operações de acordo com qual for o operador
            if(tkn.getTipo() == lex.T_INTEIRO){
                boolean resultado = false;

                if(operador.getId() == lex.OP_MAIOR){
                    resultado = Integer.parseInt(tkn.getLexema()) > Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MAIOR_IGUAL){
                    resultado = Integer.parseInt(tkn.getLexema()) >= Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MENOR){
                    resultado = Integer.parseInt(tkn.getLexema()) < Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MENOR_IGUAL){
                    resultado = Integer.parseInt(tkn.getLexema()) <= Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_IGUAL){
                    resultado = Integer.parseInt(tkn.getLexema()) == Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.DIFERENTE){
                    resultado = Integer.parseInt(tkn.getLexema()) != Integer.parseInt(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                    return null;
                }
                
                if(resultado){
                    return new Token(lex.TRUE, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_BOOLEANO);
                }
                else{
                    return new Token(lex.FALSE, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_BOOLEANO);
                }
                
            }

            else if(tkn.getTipo() == lex.T_REAL){
                boolean resultado = false;

                if(operador.getId() == lex.OP_MAIOR){
                    resultado = Float.parseFloat(tkn.getLexema()) > Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MAIOR_IGUAL){
                    resultado = Float.parseFloat(tkn.getLexema()) >= Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MENOR){
                    resultado = Float.parseFloat(tkn.getLexema()) < Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_MENOR_IGUAL){
                    resultado = Float.parseFloat(tkn.getLexema()) <= Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_IGUAL){
                    resultado = Float.parseFloat(tkn.getLexema()) == Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.DIFERENTE){
                    resultado = Float.parseFloat(tkn.getLexema()) != Float.parseFloat(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }

                if(resultado){
                    return new Token(lex.TRUE, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_BOOLEANO);
                }
                else{
                    return new Token(lex.FALSE, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_BOOLEANO);
                }
            }
            else{
                erro("\nErro semântico: Operação não permitida com o tipo de variável passado");
            }
        }
        return tkn;
    }

    //EXPRS -> TERMO { OP_AD TERMO}*
    private Token EXPRS(){
        Token tkn = TERMO();
        //verifica o tipo do token e cria variavel pra receber resultado de acordo com isso
        if(tkn == null){
            System.out.println("1");
            erro("\nErro semântico: Não é possível realizar operações com token inválido");
            return null;
        }

        while (token.getId() == lex.OP_SOMA || token.getId() == lex.OP_SUBTRAI || token.getId() == lex.OP_OR){
            Token operador = OP_AD();
            Token tkn2 = TERMO();

            if(operador == null || tkn2 == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                System.out.println("2");
                return null;
            }
            else if(tkn2.getTipo() != tkn.getTipo()){
                erro("\nErro semântico: Não é possível mesclar valores de tokens de tipos diferentes");
            }

            //pega de acordo com os tipos das variaveis o resultado
            //e realiza operações de acordo com qual for o operador
            if(tkn.getTipo() == lex.T_INTEIRO){
                int resultado = Integer.parseInt(tkn.getLexema());

                if(operador.getId() == lex.OP_SOMA){
                    resultado = resultado + Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_SUBTRAI){
                    resultado = resultado - Integer.parseInt(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_INTEIRO, "INTEIRO", Integer.toString(resultado), -1, lex.T_INTEIRO);
            }

            else if(tkn.getTipo() == lex.T_REAL){
                float resultado = Float.parseFloat(tkn.getLexema());

                if(operador.getId() == lex.OP_SOMA){
                    resultado = resultado + Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_SUBTRAI){
                    resultado = resultado - Float.parseFloat(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_REAL, "REAL", Float.toString(resultado), -1, lex.T_REAL);
            }

            else if(tkn.getTipo() == lex.T_BOOLEANO){
                boolean resultado = Boolean.parseBoolean(tkn.getLexema());

                if(operador.getId() == lex.OP_OR){
                    resultado = resultado || Boolean.parseBoolean(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_REAL, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_REAL);
            }
            else{
                erro("\nErro semântico: Operação não permitida com o tipo de variável passado");
            }
        }

        return tkn;
    }

    //TERMO -> FATOR {OP_MULT FATOR}*
    private Token TERMO(){
        Token tkn = FATOR();

        //verifica o tipo do token e cria variavel pra receber resultado de acordo com isso
        if(tkn == null){
            erro("\nErro semântico: Não é possível realizar operações com token inválido");
            System.out.println("3");
            return null;
        }

        while (token.getId() == lex.OP_MULTIPLICA || token.getId() == lex.OP_DIVISAO || token.getId() == lex.OP_AND){
            Token operador = OP_MULT();
            Token tkn2 = FATOR();

            if(operador == null || tkn2 == null){
                erro("\nErro semântico: Não é possível realizar operações com token inválido");
                return null;
            }
            else if(tkn2.getTipo() != tkn.getTipo()){
                erro("\nErro semântico: Não é possível mesclar valores de tokens de tipos diferentes");
            }

            //pega de acordo com os tipos das variaveis o resultado
            //e realiza operações de acordo com qual for o operador
            if(tkn.getTipo() == lex.T_INTEIRO){
                int resultado = Integer.parseInt(tkn.getLexema());

                if(operador.getId() == lex.OP_MULTIPLICA){
                    resultado = resultado * Integer.parseInt(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_DIVISAO){
                    resultado = resultado / Integer.parseInt(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_INTEIRO, "INTEIRO", Integer.toString(resultado), -1, lex.T_INTEIRO);
            }

            else if(tkn.getTipo() == lex.T_REAL){
                float resultado = Float.parseFloat(tkn.getLexema());

                if(operador.getId() == lex.OP_MULTIPLICA){
                    resultado = resultado * Float.parseFloat(tkn2.getLexema());
                }
                else if(operador.getId() == lex.OP_DIVISAO){
                    resultado = resultado / Float.parseFloat(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_REAL, "REAL", Float.toString(resultado), -1, lex.T_REAL);
            }

            else if(tkn.getTipo() == lex.T_BOOLEANO){
                boolean resultado = Boolean.parseBoolean(tkn.getLexema());

                if(operador.getId() == lex.OP_AND){
                    resultado = resultado && Boolean.parseBoolean(tkn2.getLexema());
                }
                else{
                    erro("\nErro semântico: Operador inválido");
                }
                return new Token(lex.NUM_REAL, "BOOLEANO", Boolean.toString(resultado), -1, lex.T_REAL);
            }
            else{
                erro("\nErro semântico: Operação não permitida com o tipo de variável passado");
            }
        }
        return tkn;
    }

    //FATOR -> '(' EXP ')' | !FATOR | id | LITERAL | READ
    private Token FATOR(){
        if(token.getId() == lex.ABRE_EXPR){
            casaToken(lex.ABRE_EXPR);
            Token tk = EXPR();
            casaToken(lex.FECHA_EXPR);
            return tk;
        }
        else if(token.getId() == lex.NEGA){
            casaToken(lex.NEGA);
            Token tk = FATOR();
            return tk;
        }
        else if(token.getId() == lex.ID){
            Token tk = token;
            casaToken(lex.ID);
            return tk;
        }
        else if(token.getId() == lex.NUM_INTEIRO){
            Token tk = token;
            casaToken(lex.NUM_INTEIRO);
            return tk;
        }
        else if(token.getId() == lex.NUM_REAL){
            Token tk = token;
            casaToken(lex.NUM_REAL);
            return tk;
        }
        else if(token.getId() == lex.STRING){
            Token tk = token;
            casaToken(lex.STRING);
            return tk;
        }
        else if(token.getId() == lex.CHAR){
            Token tk = token;
            casaToken(lex.CHAR);
            return tk;
        }
        else if(token.getId() == lex.TRUE){
            Token tk = token;
            casaToken(lex.TRUE);
            return tk;
        }
        else if(token.getId() == lex.FALSE){
            Token tk = token;
            casaToken(lex.FALSE);
            return tk;
        }
        else if(token.getId() == lex.INPUT){
            return READ();
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": regra inválida");
        }
        return null;
    }
    //READ -> ler()
    private Token READ(){
        casaToken(lex.INPUT);
        casaToken(lex.ABRE_EXPR);
        casaToken(lex.FECHA_EXPR);
        Scanner s = new Scanner(System.in);
        String input = s.nextLine();
        return new Token(lex.INPUT,"INPUT", input, -1, -1);
    }

    //OP_REL -> < | > | <= | >= | = | !=
    private Token OP_REL(){
        if(token.getId() == lex.OP_MENOR){
            Token tk = token;
            casaToken(lex.OP_MENOR);
            return tk;
        }
        else if(token.getId() == lex.OP_MENOR_IGUAL){
            Token tk = token;
            casaToken(lex.OP_MENOR_IGUAL);
            return tk;
        }
        else if(token.getId() == lex.OP_MAIOR){
            Token tk = token;
            casaToken(lex.OP_MAIOR);
            return tk;
        }
        else if(token.getId() == lex.OP_MAIOR_IGUAL){
            Token tk = token;
            casaToken(lex.OP_MAIOR_IGUAL);
            return tk;
        }
        else if(token.getId() == lex.OP_IGUAL){
            Token tk = token;
            casaToken(lex.OP_IGUAL);
            return tk;
        }
        else if(token.getId() == lex.DIFERENTE){
            Token tk = token;
            casaToken(lex.DIFERENTE);
            return tk;
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador relacional inválido");
        }
        return null;
    }

    //OP_AD -> + | - | '|'
    private Token OP_AD(){
        if(token.getId() == lex.OP_SOMA){
            Token tk = token;
            casaToken(lex.OP_SOMA);
            return tk;
        }
        else if(token.getId() == lex.OP_SUBTRAI){
            Token tk = token;
            casaToken(lex.OP_SUBTRAI);
            return tk;
        }
        else if(token.getId() == lex.OP_OR){
            Token tk = token;
            casaToken(lex.OP_OR);
            return tk;
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador de adição inválido");
        }

        return null;
    }

    //OP_MULT -> * | / | &
    private Token OP_MULT(){
        if(token.getId() == lex.OP_MULTIPLICA){
            Token tk = token;
            casaToken(lex.OP_MULTIPLICA);
            return tk;
        }
        else if(token.getId() == lex.OP_DIVISAO){
            Token tk = token;
            casaToken(lex.OP_DIVISAO);
            return tk;
        }
        else if(token.getId() == lex.OP_AND){
            Token tk = token;
            casaToken(lex.OP_AND);
            return tk;
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador de multiplicação inválido");
        }
        return null;
    }

    //SWITCH -> interruptor '('id')' BLOCO_SWITCH
    private void SWITCH(){
        casaToken(lex.SWITCH);
        casaToken(lex.ABRE_EXPR);
        casaToken(lex.ID);
        casaToken(lex.FECHA_EXPR);
        BLOCO_SWITCH();
    }

    //BLOCO_SWITCH -> '{' CASO* [PADRAO]'}'
    private void BLOCO_SWITCH(){
        casaToken(lex.ABRE_BLOCO);
        while (token.getId() == lex.CASO){
            if(token.getId() == lex.CASO){
                CASO();
            }
        }
        if(token.getId() == lex.PADRAO){
            PADRAO();
        }
        casaToken(lex.FECHA_BLOCO);
    }

    //CASO -> caso LITERAL BLOCO
    private void CASO(){
        casaToken(lex.CASO);
        LITERAL();
        BLOCO();
    }

    //PADRAO -> padrao BLOCO
    private void PADRAO(){
        casaToken(lex.PADRAO);
        BLOCO();
    }

    //LITERAL -> num_real | num_int | string | char | booleano
    private Token LITERAL(){
        if(token.getId() == lex.NUM_REAL){
            return casaToken(lex.NUM_REAL);
        }
        else if(token.getId() == lex.NUM_INTEIRO){
            return casaToken(lex.NUM_INTEIRO);
        }
        else if(token.getId() == lex.STRING){
            return casaToken(lex.STRING);
        }
        else if(token.getId() == lex.CHAR){
            return casaToken(lex.CHAR);
        }
        else if(token.getId() == lex.TRUE){
            return casaToken(lex.TRUE);
        }
        else if(token.getId() == lex.FALSE){
            return casaToken(lex.FALSE);
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": literal não reconhecido");
        }
        return null;
    }

    //TIPO -> t_int, t_string, t_char, t_float, t_boolean
    private void TIPO(){
        if(token.getId() == lex.T_INTEIRO){
            casaToken(lex.T_INTEIRO);
        }
        else if(token.getId() == lex.T_LINHA){
            casaToken(lex.T_LINHA);
        }
        else if(token.getId() == lex.T_BOOLEANO){
            casaToken(lex.T_BOOLEANO);
        }
        else if(token.getId() == lex.T_CARACTERE){
            casaToken(lex.T_CARACTERE);
        }
        else if(token.getId() == lex.T_REAL){
            casaToken(lex.T_REAL);
        }
        else if(token.getId() == lex.T_ARRAY){
            casaToken(lex.T_ARRAY);
        }
    }

    //PRINT -> imprimir EXP;
    private void PRINT(){
        casaToken(lex.OUTPUT);
        EXPR();
        casaToken(lex.TERM);
    }

    public void execute(){
        S();
    }

    public Parser(JTextPane editor, JTextPane msg){
        this.editor = editor;
        this.msg = msg;
        lex = new Lexico(this.editor, this.msg);
        token = lex.anaLex();
        execute();
    }
}
