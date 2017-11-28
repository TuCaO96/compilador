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
        else
            erro();
    }

    public void erro() {
        System.out.println("Erro de sintaxe...");
    }
    private void S(){

    }

    public void main(String[] args){
        S();
    }

    public Parser(){
        this.editor = editor;
        this.msg = msg;
    }
}
