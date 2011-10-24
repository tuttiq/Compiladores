package Compilador;

import java.util.ArrayList;

public class TabelaDeSimbolos {

    private ArrayList<Simbolo> tabela;

    public TabelaDeSimbolos() {
        tabela = new ArrayList<Simbolo>();
    }

    public void insere(String tipo,String lexema,boolean escopo,String end_memoria) {
        tabela.add(new Simbolo(tipo,lexema,escopo,end_memoria));
    }

    public Simbolo busca(String lexema) {
       for(Simbolo s : tabela)
       {
           if(s.getLexema().equals(lexema))
               return s;
       }
       return null;
    }

    public void alteraTipo(String tipo) {
        for(Simbolo s : tabela)
        {
           if(s.getTipo().equals("variavel"))
               s.setTipo(tipo);
        }
    }


}
