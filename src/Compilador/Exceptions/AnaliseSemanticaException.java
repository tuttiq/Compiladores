
package Compilador.Exceptions;

public class AnaliseSemanticaException extends Exception {
    
    public AnaliseSemanticaException (int line, String msg) {
        super("Analisador Semantico: Linha " + (line+1) + ", " + msg);
    }
    
}
