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

    public int busca(String lexema) {
       for(Simbolo s : tabela)
       {
           if(s.getLexema().equals(lexema))
               return tabela.indexOf(s);
       }
       return -1;
    }

    public void alteraTipo(String lexema, int tipo) {
        int i = busca(lexema);
        
        if(i!=-1)
        {   Simbolo s = tabela.get(i);
            s.setTipo(tipo);
        }
    }


}
