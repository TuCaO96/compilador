public class Token {
    private int Id;

    private String Token;

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

    public Token(int id, String token, String classe){
        setId(id);
        setToken(token);
        setClasse(classe);
    }
}
