package Compilador.Models;

public class Token {

    private String lexema;
    private int simbolo;

    public Token() {
    }



    public Token(String lexema, int simbolo) {
        this.lexema = lexema;
        this.simbolo = simbolo;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(int simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return "Token{" + "lexema=" + lexema + ", simbolo=" + simbolo + '}';
    }

    

}
