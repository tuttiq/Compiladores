
package Compilador.Exceptions;

public class AnaliseLexicaException extends Exception{
    
    public AnaliseLexicaException(int line, String msg) {
        super("Analisador lexico: Linha " + (line+1) + ", " + msg);
    }
}
