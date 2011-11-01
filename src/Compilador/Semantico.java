package Compilador;

import Compilador.Structures.TabelaDeSimbolos;
import Compilador.Structures.Token;

public class Semantico {
    
    private TabelaDeSimbolos tabela;
    
    public Semantico(TabelaDeSimbolos t)
    {
        tabela = t;
    }
    
    public boolean isVariavelDuplicada(Token tk)
    {
        return false;
    }

}
