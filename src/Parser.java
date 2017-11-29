import javax.swing.*;

public class Parser {
    private Lexico lex = new Lexico(this.editor, this.msg);
    private int token = lex.anaLex();
    private JTextPane editor;

    private JTextPane msg;

    public void casaToken(int tokenEsperado) {
        if(token == tokenEsperado) {
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
        casaToken(35);
        casaToken(1);
        BLOCO();
    }

    //BLOCO -> '{' CMD* '}'
    private void BLOCO(){
        casaToken(16);
        while(lex.anaLex() == 32){
            CMD();
        }
        casaToken(17);
    }

    //CMD -> IF | WHILE | ATRIB | DECLAR | BLOCO | SWITCH | break;
    private void CMD(){

    }

    //IF -> se '(' EXP ')' CMD
    private void IF(){}

    //WHILE -> enquanto '(' EXP ')' CMD
    private void WHILE(){}

    //DECLAR -> TIPO id {',' id}* ;
    private void DECLAR(){}

    //ATRIB -> id ':=' EXP;
    private void ATRIB(){}

    //EXPR -> EXPRS [ OP_REL EXPRS ]
    private void EXPR(){}

    //EXPRS -> TERMO { OP_AD TERMO}*
    private void EXPRS(){}

    //TERMO -> FATOR {OP_MULT FATOR}*
    private void TERMO(){}

    //FATOR -> '(' EXP ')' | !FATOR | id | num | true | false | string | read()
    private void FATOR(){}

    //OP_REL -> < | > | <= | >= | ++ | !=
    private void OP_REL(){}

    //OP_AD -> + | - | '|'
    private void OP_AD(){}

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
