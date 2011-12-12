package Compilador;

import Compilador.Constants.Simbolos;
import Compilador.Constants.Tipos;
import Compilador.Exceptions.AnaliseSemanticaException;
import Compilador.Models.Simbolo;
import Compilador.Models.TabelaDeSimbolos;
import Compilador.Models.Token;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Hashtable;

public class Semantico {
    
    private TabelaDeSimbolos tabela;
    private Hashtable<Integer,Integer> prioridades = new Hashtable<Integer, Integer>();
    
    public Semantico()
    {
        tabela = new TabelaDeSimbolos();
        setOperadores();
    }
    
    private void setOperadores() {
        prioridades.put(Simbolos.Ou, 1);
        prioridades.put(Simbolos.E, 2);
        prioridades.put(Simbolos.Maior,3);
        prioridades.put(Simbolos.MaiorIgual,3);
        prioridades.put(Simbolos.Menor,3);
        prioridades.put(Simbolos.MenorIgual,3);
        prioridades.put(Simbolos.Igual,3);
        prioridades.put(Simbolos.Diferente,3);
        prioridades.put(Simbolos.Mais,4);
        prioridades.put(Simbolos.Menos,4);
        prioridades.put(Simbolos.Multiplicacao,5);
        prioridades.put(Simbolos.Divisao,5);
        prioridades.put(Simbolos.Nao, 6);
        prioridades.put(Simbolos.Positivo,6);
        prioridades.put(Simbolos.Negativo,6);
        prioridades.put(Simbolos.AbreParenteses,0);
        
    }
    
    public void insereSimbolo(int tipo, String lexema, boolean escopo) {
    	tabela.insere(tipo, lexema, escopo, null);
    }
    
    public void insereSimbolo(int tipo, String lexema, boolean escopo, int end_memoria) {
    	tabela.insere(tipo, lexema, escopo, String.valueOf(end_memoria));
    }
    
    public void alteraSimbolo(int tipo) {
        
        tabela.alteraTipo(tipo);      
    }
    
    public void limpaEscopo() {
        tabela.limpaEscopo();
    }
    
    public void removeSimbolos(ArrayList<Simbolo> vars) {
    	for(Simbolo s : vars)
            tabela.remove(s);
    }
    
    public Simbolo buscaSimbolo(String lexema)
    {
        return tabela.busca(lexema);
    }
    
    public void erro(int line, String msg) throws Exception
    {
        throw new AnaliseSemanticaException(line,msg);
    }
    
    public ArrayList<Simbolo> getVariaveis() {
        return tabela.getVars();
    }
    
    public boolean isIdentificadorDuplicado(String lexema)
    {
        return tabela.isDeclaradoNoEscopo(lexema);
    }
    
    public boolean isIdentificadorDeclarado(String lexema) {
        return tabela.isDeclarado(lexema);
    }
    
    public void analisaExpressao(Simbolo id, ArrayList<Token> expressao, int line) throws Exception
    {
        //percorre ateh o primeiro operador
        //verifica se termos recebidos (anteriores ao op) são do tipo permitido pelo operador
        //troca tudo pelo tipo do resultado do operador
        
        ArrayList<Integer> tipos = new ArrayList<Integer>();
        
        for(Token termo : expressao)
        {
            if(Simbolos.isOperador(termo.getSimbolo()))
            {
                if(termo.getSimbolo()==Simbolos.Nao ||
                        termo.getSimbolo()==Simbolos.Positivo ||
                        termo.getSimbolo()==Simbolos.Negativo)
                {
                    continue;
                }
                if(termo.getSimbolo()==Simbolos.E ||
                        termo.getSimbolo()==Simbolos.Ou )  //RECEBE BOOLEANOS
                {
                    if(tipos.get(tipos.size()-1)!=Tipos.Booleano &&
                            tipos.get(tipos.size()-2)!=Tipos.Booleano) //2 BOOLEANOS
                                erro(line, "operador '" + termo.getLexema() + "' deve ser aplicado a Booleano.");
                   
                    tipos.remove(tipos.size()-1);  //GERA BOOLEANO
                }
                
                if(termo.getSimbolo()==Simbolos.Maior ||
                        termo.getSimbolo()==Simbolos.MaiorIgual ||
                        termo.getSimbolo()==Simbolos.Menor ||
                        termo.getSimbolo()==Simbolos.MenorIgual ||
                        termo.getSimbolo()==Simbolos.Igual ||
                        termo.getSimbolo()==Simbolos.Diferente )  //RECEBE 2 INTEIROS
                {
                    if(tipos.get(tipos.size()-1)!=Tipos.Inteiro && tipos.get(tipos.size()-2)!=Tipos.Inteiro)
                           erro(line, "operador '" + termo.getLexema() + "' deve ser aplicado a Inteiro.");
                    
                    tipos.remove(tipos.size()-1);
                    tipos.remove(tipos.size()-1);
                    tipos.add(Tipos.Booleano);  //GERA BOOLEANO
                }
                
                if(termo.getSimbolo()==Simbolos.Mais ||
                        termo.getSimbolo()==Simbolos.Menos ||
                        termo.getSimbolo()==Simbolos.Multiplicacao ||
                        termo.getSimbolo()==Simbolos.Divisao)  //RECEBE INTEIROS
                {
                    if(tipos.get(tipos.size()-1)!=Tipos.Inteiro &&
                            tipos.get(tipos.size()-2)!=Tipos.Inteiro) //2 INTEIROS
                                erro(line, "operador '" + termo.getLexema() + "' deve ser aplicado a Inteiro.");
                             
                    
                    tipos.remove(tipos.size()-1); //GERA INTEIROS
                }
                
            }
            else
            {
                if( termo.getSimbolo()==Simbolos.Numero)
                    tipos.add(Tipos.Inteiro);
                if( termo.getSimbolo()==Simbolos.Identificador)
                {
                    Simbolo s = tabela.busca(termo.getLexema());
                    if(s.getTipo()==Tipos.FuncaoInteiro || s.getTipo()==Tipos.Inteiro)
                        tipos.add(Tipos.Inteiro);
                    else if(s.getTipo()==Tipos.FuncaoBooleano || s.getTipo()==Tipos.Booleano)
                        tipos.add(Tipos.Booleano);
                }
                if( termo.getSimbolo()==Simbolos.Verdadeiro || termo.getSimbolo()==Simbolos.Falso )
                    tipos.add(Tipos.Booleano);
            }
        }
        
        if(id.getTipo()==Tipos.FuncaoInteiro || id.getTipo()==Tipos.Inteiro)
            if(Tipos.Inteiro!=tipos.get(0))
                erro(line, "expressao de tipo incompativel com " + id.getLexema() + ".");
        if(id.getTipo()==Tipos.FuncaoBooleano || id.getTipo()==Tipos.Booleano)
            if(Tipos.Booleano!=tipos.get(0))
                erro(line, "expressao de tipo incompativel com " + id.getLexema() + ".");
    }
	
    public ArrayList<Token> posOrdem(ArrayList<Token> expressao)
    {
       /*function Posfixa(E:string) : string;
            var P : Pilha;
                S : string;
                i : integer;
                x : char;
            begin
                Init(P);
                S:=’’;
                
                for I:=1 to length(E)
                    do case E[i] of
                        ‘A’..‘Z’:
                            S := S + E[I];
                        ‘+’..‘-’,‘*’..‘/’:
                            begin
                                while not IsEmpty(P) and (Prio(Top(P))>=Prio(E[i]))
                                    do S := S + Pop(P);
                                Push(P, E[i]);
                            end;
                        ‘(’: Push(P, E[i]);
                        ‘)’ : 
                            begin
                                while Top(P)<>’(’ do S := S + Pop(P);
                                x:= Pop(P);
                            end;
                    end;
                while not IsEmpty(P) do S := S + Pop(P);
                Posfixa := S;
            end;
        */
        
        
           ArrayList<Token> novaExpressao = new ArrayList<Token>();
           Stack<Token> pilha = new Stack<Token>();
           
           for(int i=0; i<expressao.size();i++) {
               
               int termo = expressao.get(i).getSimbolo();
               
               if(Simbolos.isOperando(termo))
                   novaExpressao.add(expressao.get(i));
               
               else if (Simbolos.isOperador(termo))
               {                  
                     while(!pilha.empty() &&
                              prioridades.get(pilha.peek().getSimbolo())>=prioridades.get(termo) )
                      {
                          novaExpressao.add(pilha.pop());
                      }
                      pilha.push(expressao.get(i));
              }
              else if(termo==Simbolos.AbreParenteses)
                  pilha.push(expressao.get(i));
              
              else if(termo==Simbolos.FechaParenteses)
              {
                  while(pilha.peek().getSimbolo()!=Simbolos.AbreParenteses)
                  {
                      novaExpressao.add(pilha.pop());
                  }
                      pilha.pop();
              }
              
           }
           while(!pilha.empty())
              {
                  novaExpressao.add(pilha.pop());
              }
           
           return novaExpressao;
    }

    
    //Pela arvore de derivação (tabela de simbolo em pilha)
    //Usando expressão pós-ordem (mais simples)
    //Convertendo a expressão para pós-ordem usando uma pilha auxiliar
    //SEMANTICO funciona em cima dos identificadores (seus significados)
    //(<variavel> <chamada de funcao> <chamada de proc>)
    // *** Ver se a declaração estah duplicada (no escopo)
    // *** Ver se a declaracao foi feita (toda a pilha)
    // *** Ver se o uso está de acordo com o TIPO do identificador (quando achar o uso)
    // *** - Seguir regras de derivação do PDF para <variavel> <ch de f> e <ch de proc>
    // *** Analisar expressao por prioridade de operador
    // Nivel de prioridade de operadores (maior 1, menor 6):
    // 1) positivo negativo (recebe: 1 int -> gera: int) não (1 booleano -> booleano)
    // 2) * div (2 int -> inteiro)
    // 3) + - (2 int -> int)
    // 4) > < >= <= = != (2 int -> booleano)
    // 5) e (2 booleano -> booleano)
    // 6) ou (2 booleano -> booleano)
    //Analise da expressão: converte para pós ordem respeitando as prioridades de operadores
    //Analise semantica da pós orderm:
    
    //(ver analisaExpressao no sintatico)
    

}
