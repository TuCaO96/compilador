import javax.smartcardio.ATR;
import javax.swing.*;

public class Parser {
    private Lexico lex;
    private Token token;
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
        this.msg.setText(this.msg.getText() + "\nErro sintático na coluna " + lex.posFim + ", na linha " + lex.linhaAtual + ".");
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
        FATOR();
        while (token.getId() == lex.OP_MULTIPLICA || token.getId() == lex.OP_DIVISAO || token.getId() == lex.OP_AND){
            OP_MULT();
            FATOR();
        }
    }

    //FATOR -> '(' EXP ')' | !FATOR | id | num | true | false | string | read()
    private void FATOR(){
        if(token.getId() == lex.ABRE_EXPR){
            casaToken(lex.ABRE_EXPR);
            EXPR();
            casaToken(lex.FECHA_EXPR);
        }
        else if(token.getId() == lex.NEGA){
            FATOR();
        }
        else if(token.getId() == lex.ID){
            casaToken(lex.ID);
        }
        else if(token.getId() == lex.NUM_INTEIRO){
            casaToken(lex.NUM_INTEIRO);
        }
        else if(token.getId() == lex.NUM_REAL){
            casaToken(lex.NUM_REAL);
        }
        else if(token.getId() == lex.STRING){
            casaToken(lex.STRING);
        }
        else{
            erro();
        }
        //falta implementar read e true or false
    }

    //OP_REL -> < | > | <= | >= | = | !=
    private void OP_REL(){
        if(token.getId() == lex.OP_MENOR){
            casaToken(lex.OP_MENOR);
        }
        else if(token.getId() == lex.OP_MENOR_IGUAL){
            casaToken(lex.OP_MENOR_IGUAL);
        }
        else if(token.getId() == lex.OP_MAIOR){
            casaToken(lex.OP_MAIOR);
        }
        else if(token.getId() == lex.OP_MAIOR_IGUAL){
            casaToken(lex.OP_MAIOR_IGUAL);
        }

        else{
            erro();
        }
    }

    //OP_AD -> + | - | '|'
    private void OP_AD(){
        if(token.getId() == lex.OP_SOMA){
            casaToken(lex.OP_SOMA);
        }
        else if(token.getId() == lex.OP_SUBTRAI){
            casaToken(lex.OP_SUBTRAI);
        }
        else if(token.getId() == lex.OP_OR){
            casaToken(lex.OP_OR);
        }
        else{
            erro();
        }
    }

    //OP_MULT -> * | / | &
    private void OP_MULT(){
        if(token.getId() == lex.OP_MULTIPLICA){
            casaToken(lex.OP_MULTIPLICA);
        }
        else if(token.getId() == lex.OP_DIVISAO){
            casaToken(lex.OP_DIVISAO);
        }
        else if(token.getId() == lex.OP_AND){
            casaToken(lex.OP_AND);
        }
        else{
            erro();
        }
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
            CASO();
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
    private void LITERAL(){
        if(token.getId() == lex.T_REAL){
            casaToken(lex.T_REAL);
        }
        else if(token.getId() == lex.T_INTEIRO){
            casaToken(lex.T_INTEIRO);
        }
        else if(token.getId() == lex.T_LINHA){
            casaToken(lex.T_LINHA);
        }
        else if(token.getId() == lex.T_CARACTERE){
            casaToken(lex.T_CARACTERE);
        }
        else if(token.getId() == lex.T_BOOLEANO){
            casaToken(lex.T_BOOLEANO);
        }
        else{
            erro();
        }
    }

    //CLASSE -> classe id BLOCO_CLASSE
    private void CLASSE(){
        casaToken(lex.CLASSE);
        casaToken(lex.ID);
        BLOCO_CLASSE();
    }

    //BLOCO_CLASSE -> {ATRIB | DECLAR | FUNCAO}+
    private void BLOCO_CLASSE(){
        casaToken(lex.ABRE_BLOCO);
        do{
            if(token.getId() == lex.ID ){
                ATRIB();
            }
            else if(token.getId() == lex.T_INTEIRO || token.getId() == lex.T_BOOLEANO ||
                    token.getId() == lex.T_LINHA || token.getId() == lex.T_CARACTERE){
                DECLAR();
            }
            else if(token.getId() == lex.FUNCAO){
                FUNCAO();
            }
            else{
                erro();
            }
        }
        while (token.getId() != lex.FECHA_BLOCO);
    }

    //FUNCAO -> funcao id '('PARAMETRO [',' PARAMETRO]')' BLOCO
    private void FUNCAO(){
        casaToken(lex.FUNCAO);
        casaToken(lex.ID);
        casaToken(lex.ABRE_EXPR);
        PARAMETRO();
        if(token.getId() == lex.SEPARADOR){
            while (token.getId() == lex.SEPARADOR){
                casaToken(lex.SEPARADOR);
                PARAMETRO();
            }
        }
        casaToken(lex.FECHA_EXPR);
        BLOCO();
    }

    //PARAMETRO -> TIPO id
    private void PARAMETRO(){
        TIPO();
        casaToken(lex.ID);
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
    }

    //PRINT -> imprimir EXP;
    private void PRINT(){
        casaToken(lex.OUTPUT);
        EXPR();
    }

    //READ -> ler()
    private void READ(){

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
