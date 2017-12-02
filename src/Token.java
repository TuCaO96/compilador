public class Token {
    private int Id;

    private String Token;

    private String Lexema;

    private String Classe;

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

    public String getClasse() {
        return Classe;
    }

    public void setClasse(String classe) {
        Classe = classe;
    }

    public String getLexema() {
        return Lexema;
    }

    public void setLexema(String lexema) {
        Lexema = lexema;
    }

    public Token(int id, String token, String lexema, String classe){
        setId(id);
        setToken(token);
        setLexema(lexema);
        setClasse(classe);
    }
}
