public class Token {
    private int Id;

    private String Token;

    private String Lexema;

    private int Classe;

    private int Tipo;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public int getClasse() {
        return Classe;
    }

    public void setClasse(int classe) {
        Classe = classe;
    }

    public String getLexema() {
        return Lexema;
    }

    public void setLexema(String lexema) {
        Lexema = lexema;
    }

    public int getTipo() {
        return Tipo;
    }

    public void setTipo(int tipo) {
        Tipo = tipo;
    }

    public Token(int id, String token, String lexema, int classe, int tipo){
        setId(id);
        setToken(token);
        setLexema(lexema);
        setClasse(classe);
        setTipo(tipo);
    }
}
