import com.sun.prism.shader.Solid_TextureYV12_Loader;

import javax.smartcardio.ATR;
import javax.swing.*;

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
                token.getId() == lex.ABRE_BLOCO || token.getId() == lex.CASO || 
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
    private void DECLAR(){
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
            if(tipo.getId() == lex.T_LINHA && token.getId() == lex.STRING){
                EXPR();
            }
            else if(tipo.getId() == lex.T_CARACTERE && token.getId() == lex.CHAR){
                EXPR();
            }
            else if(tipo.getId() == lex.T_REAL && token.getId() == lex.NUM_REAL){
                EXPR();
            }
            else if(tipo.getId() == lex.T_INTEIRO && token.getId() == lex.NUM_INTEIRO){
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
    }

    //ATRIB -> id ':=' EXP ';' | id ':=' ARRAY ';'
    private void ATRIB(){
        Token id = token;
        casaToken(lex.ID);
        casaToken(lex.ATRIB);
        if(token.getTipo() == lex.T_LINHA && token.getId() == lex.STRING){
            EXPR();
        }
        else if(token.getTipo() == lex.T_CARACTERE && token.getId() == lex.CHAR){
            EXPR();
        }
        else if((token.getTipo() == lex.T_REAL || token.getTipo() == lex.T_INTEIRO) &&
                (token.getId() != lex.STRING && token.getId() != lex.CHAR && token.getId() != lex.ABRE_ARRAY)){
            EXPR();
        }
        else if(token.getTipo() == lex.T_ARRAY && token.getId() == lex.ABRE_ARRAY){
            ARRAY();
        }
        else{
            erro("\nErro semântico: Variável " + id.getLexema() + " é de tipo diferente do atribuído " +
                    "na linha " + lex.linhaAtual);
        }
        casaToken(lex.TERM);
    }

    //ARRAY -> '['LITERAL {',' LITERAL}*']'
    private void ARRAY(){
        casaToken(lex.ABRE_ARRAY);
        LITERAL();
        if(token.getId() == lex.SEPARADOR){
            while (token.getId() == lex.SEPARADOR){
                casaToken(lex.SEPARADOR);
                LITERAL();
            }
        }
        casaToken(lex.FECHA_ARRAY);
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

    //FATOR -> '(' EXP ')' | !FATOR | id | LITERAL | READ
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
        else if(token.getId() == lex.CHAR){
            casaToken(lex.CHAR);
        }
        else if(token.getId() == lex.TRUE){
            casaToken(lex.TRUE);
        }
        else if(token.getId() == lex.FALSE){
            casaToken(lex.FALSE);
        }
        else if(token.getId() == lex.INPUT){
            READ();
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": regra inválida");
        }
    }
    //READ -> ler()
    private void READ(){
        casaToken(lex.INPUT);
        casaToken(lex.ABRE_EXPR);
        casaToken(lex.FECHA_EXPR);
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
        else if(token.getId() == lex.OP_IGUAL){
            casaToken(lex.OP_IGUAL);
        }
        else if(token.getId() == lex.DIFERENTE){
            casaToken(lex.DIFERENTE);
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador relacional inválido");
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
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador de adição inválido");
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
            erro("\nErro sintático na linha " + lex.linhaAtual + ": operador de multiplicação inválido");
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
        if(token.getId() == lex.NUM_REAL){
            casaToken(lex.NUM_REAL);
        }
        else if(token.getId() == lex.NUM_INTEIRO){
            casaToken(lex.NUM_INTEIRO);
        }
        else if(token.getId() == lex.STRING){
            casaToken(lex.STRING);
        }
        else if(token.getId() == lex.CHAR){
            casaToken(lex.CHAR);
        }
        else if(token.getId() == lex.TRUE){
            casaToken(lex.TRUE);
        }
        else if(token.getId() == lex.FALSE){
            casaToken(lex.FALSE);
        }
        else{
            erro("\nErro sintático na linha " + lex.linhaAtual + ": literal não reconhecido");
        }
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
