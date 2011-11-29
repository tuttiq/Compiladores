
package Compilador.Exceptions;

public class AnaliseSintaticaException extends Exception {
    
    public AnaliseSintaticaException (int line, String msg) {
        super("Analisador Sintatico: Linha " + (line+1) + ", " + msg);
    }
    
}
