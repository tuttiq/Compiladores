package Compilador.Models;

import Compilador.Constants.Tipos;
import java.util.ArrayList;

public class TabelaDeSimbolos {

    private ArrayList<Simbolo> tabela;

    public TabelaDeSimbolos() {
        tabela = new ArrayList<Simbolo>();
    }

    public void insere(int tipo,String lexema,boolean escopo,String end_memoria) {
        tabela.add(new Simbolo(tipo,lexema,escopo,end_memoria));
    }

    public void remove(Simbolo s)
    {
        tabela.remove(s);
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

    public void alteraTipo(int tipo) {
        
        for(Simbolo s : tabela)
        {   if(s.getTipo()== Tipos.Variavel && (tipo == Tipos.Inteiro || tipo == Tipos.Booleano) )
                s.setTipo(tipo);
            if(s.getTipo()== Tipos.Funcao && (tipo == Tipos.FuncaoInteiro || tipo == Tipos.FuncaoBooleano))
                s.setTipo(tipo);
        }
    }
    
    public void limpaEscopo() {
        
       
        int i = tabela.size()-1;
        
        while(i>0 && !(tabela.get(i).isNovoEscopo()))
            i--;
        
        
            tabela.get(i).setNovoEscopo(false);
        
    }
    
    public ArrayList<Simbolo> getVars() {
        
        ArrayList<Simbolo> vars = new ArrayList<Simbolo>();
        
        for(int i=tabela.size()-1; i>0 && !(tabela.get(i).isNovoEscopo()); i--)
        {    
            if(tabela.get(i).getTipo()==Tipos.Inteiro ||tabela.get(i).getTipo()==Tipos.Booleano)
                vars.add(tabela.get(i));
        }
        return vars;
    }
    
    public boolean isDeclaradoNoEscopo(String lexema) {
        
        for(int i=tabela.size()-1; i>0 && !(tabela.get(i).isNovoEscopo()); i--)
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
