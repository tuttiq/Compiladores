package Compilador.Models;

import java.util.ArrayList;

public class TabelaDeSimbolos {

    private ArrayList<Simbolo> tabela;

    public TabelaDeSimbolos() {
        tabela = new ArrayList<Simbolo>();
    }

    public void insere(int tipo,String lexema,boolean escopo,String end_memoria) {
        tabela.add(new Simbolo(tipo,lexema,escopo,end_memoria));
    }

    private int buscaIndex(String lexema) {
       for(Simbolo s : tabela)
       {
           if(s.getLexema().equals(lexema))
               return tabela.indexOf(s);
       }
       return -1;
    }
    
    public Simbolo busca(String lexema) {
        Simbolo s = tabela.get(buscaIndex(lexema));
        if(s!=null)
            return s;
        else
            return null;
    }

    public void alteraTipo(String lexema, int tipo) {
        int i = buscaIndex(lexema);
        
        if(i!=-1)
        {   Simbolo s = tabela.get(i);
            s.setTipo(tipo);
        }
    }
    
    public int getNVars() {
        
        int n = 0;
        
        for(int i=tabela.size()-1; !tabela.get(i).isNovoEscopo(); i--)
            n++;
        
        return n;
    }
    
    public boolean isDeclaradoNoEscopo(String lexema) {
        
        for(int i=tabela.size()-1; !tabela.get(i).isNovoEscopo(); i--)
           if(tabela.get(i).getLexema().equals(lexema))
               return true;
               
        return false;
    }
    
    public boolean isDeclarado(String lexema) {
        
        for(Simbolo s : tabela)
           if(s.getLexema().equals(lexema))
               return true;
               
        return false;
    }
    


}
