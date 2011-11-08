package Compilador;

import Compilador.Exceptions.AnaliseSemanticaException;
import Compilador.Models.Simbolo;
import Compilador.Models.TabelaDeSimbolos;

public class Semantico {
    
    private TabelaDeSimbolos tabela;
    
    public Semantico()
    {
        tabela = new TabelaDeSimbolos();
    }
    
    public void insereSimbolo(int tipo, String lexema, boolean escopo) {
    	tabela.insere(tipo, lexema, escopo, null);
    }
    
    public void alteraSimbolo(String lexema, int tipo) {
        tabela.alteraTipo(lexema, tipo);
    
    }
    
    public Simbolo buscaSimbolo(String lexema)
    {
        return tabela.busca(lexema);
    }
    
    
    public void erro(int line, String msg) throws Exception
    {
        throw new AnaliseSemanticaException(line,msg);
    }
    
    public boolean isIdentificadorDuplicado(String lexema)
    {
        return false;
    }
    
    public boolean isIdentificadorDeclarado(String lexema) {
        return false;
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
    //percorre ateh o primeiro operador
    //verifica se termos recebidos (anteriores ao op) são do tipo permitido pelo operador
    //troca tudo pelo tipo do resultado do operador
    //(ver analisaExpressao no sintatico)
    

}
