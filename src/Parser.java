import javax.swing.*;

public class Parser {
    private Lexico lex = new Lexico(this.editor, this.msg);
    private Token token = lex.anaLex();
    private JTextPane editor;

    private JTextPane msg;

    public void casaToken(int tokenEsperado) {
        if(token.getId() == tokenEsperado) {
            token = lex.anaLex();
        }
        else{
            //adicionar token, ja que nao estara na tabela
            erro();
        }
    }

    public void erro() {
        System.out.println("Erro de sintaxe...");
    }

    //S -> 'inicio' id BLOCO
    private void S(){
        casaToken(lex.INICIO);
        casaToken(lex.ID);
        BLOCO();
    }

    //BLOCO -> '{' CMD* '}'
    private void BLOCO(){
        casaToken(lex.ABRE_BLOCO);
        //enquanto id do token esperado for um dos primeiro esperados pelo cmd, continua nele
        while(token.getId() == lex.IF || token.getId() == lex.WHILE || token.getId() == lex.ID ||
                token.getId() == lex.T_INTEIRO || token.getId() == lex.T_BOOLEANO || 
                token.getId() == lex.T_LINHA || token.getId() == lex.T_CARACTERE ||
                token.getId() == lex.ABRE_BLOCO || token.getId() == lex.CASO || 
                token.getId() == lex.PARAR){
            CMD();
        }
        casaToken(lex.FECHA_BLOCO);
        casaToken(lex.TERM);
    }

    //CMD -> IF | WHILE | ATRIB | DECLAR | BLOCO | SWITCH | break ;
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
        else if(token.getId() == lex.T_INTEIRO || token.getId() == lex.T_BOOLEANO ||
                token.getId() == lex.T_LINHA || token.getId() == lex.T_CARACTERE){
            DECLAR();
        }
        else if(token.getId() == lex.ABRE_BLOCO){
            BLOCO();
        }
        else if(token.getId() == lex.CASO){
            CASO();
        }
        else if(token.getId() == lex.PARAR){
            casaToken(lex.PARAR);
        }
        casaToken(lex.TERM);
    }

    //IF -> se '(' EXP ')' CMD
    private void IF(){
        casaToken(lex.IF);
        casaToken(lex.ABRE_EXPR);
        EXPR();
        casaToken(lex.FECHA_EXPR);
        CMD();
    }

    //WHILE -> enquanto '(' EXP ')' CMD
    private void WHILE(){
        casaToken(lex.WHILE);
        casaToken(lex.ABRE_EXPR);
        EXPR();
        casaToken(lex.FECHA_EXPR);
        CMD();
    }

    //DECLAR -> TIPO id {',' id}* 
    private void DECLAR(){
        TIPO();
        casaToken(lex.ID);
        while (token.getId() == lex.SEPARADOR){
            casaToken(lex.SEPARADOR);
            casaToken(lex.ID);
        }
    }

    //ATRIB -> id ':=' EXP
    private void ATRIB(){
        casaToken(lex.ID);
        casaToken(lex.ATRIB);
        EXPR();
    }

    //EXPR -> EXPRS [ OP_REL EXPRS ]
    private void EXPR(){
        EXPRS();
        if(token.getId() == lex.OP_IGUAL || token.getId() == lex.DIFERENTE || token.getId() == lex.OP_MENOR_IGUAL ||
                token.getId() == lex.OP_MENOR || token.getId() == lex.OP_MAIOR_IGUAL || token.getId() == lex.OP_MAIOR){
            OP_REL();
            EXPRS();
        }
    }

    //EXPRS -> TERMO { OP_AD TERMO}*
    private void EXPRS(){
        TERMO();
        while (token.getId() == lex.OP_SOMA || token.getId() == lex.OP_SUBTRAI || token.getId() == lex.OP_OR){
            OP_AD();
            TERMO();
        }
    }

    //TERMO -> FATOR {OP_MULT FATOR}*
    private void TERMO(){

    }

    //FATOR -> '(' EXP ')' | !FATOR | id | num | true | false | string | read()
    private void FATOR(){

    }

    //OP_REL -> < | > | <= | >= | = | !=
    private void OP_REL(){

    }

    //OP_AD -> + | - | '|'
    private void OP_AD(){

    }

    //OP_MULT -> * | / | &
    private void OP_MULT(){}

    //SWITCH -> interruptor '('id')' BLOCO_SWITCH
    private void SWITCH(){}

    //BLOCO_SWITCH -> '{' CASO* [PADRAO]'}'
    private void BLOCO_SWITCH(){}

    //CASO -> caso LITERAL BLOCO
    private void CASO(){}

    //PADRAO -> padrao BLOCO
    private void PADRAO(){}

    //LITERAL -> num_real | num_int | string | char | booleano
    private void LITERAL(){}

    //CLASSE -> classe id BLOCO_CLASSE
    private void CLASSE(){}

    //BLOCO_CLASSE -> {ATRIB | DECLAR | FUNCAO}+
    private void BLOCO_CLASSE(){}

    //FUNCAO -> função id '('PARAMETRO [',' PARAMETRO]')' BLOCO
    private void FUNCAO(){}

    //PARAMETRO -> TIPO id
    private void PARAMETRO(){}

    //TIPO -> t_int, t_string, t_char, t_float, t_boolean
    private void TIPO(){}

    //PRINT -> imprimir EXP;
    private void PRINT(){}

    //READ -> ler()
    private void READ(){}

    public void main(String[] args){
        S();
    }

    public Parser(){
        this.editor = editor;
        this.msg = msg;
        this.S();
    }
}
