package Compilador;

import Compilador.Structures.Token;
import Compilador.Exceptions.AnaliseSintaticaException;
import Compilador.Exceptions.CompiladoComSucesso;
import Compilador.Structures.Simbolos;
import Compilador.Structures.Tipos;
import java.io.File;

public class Sintatico {

    private Lexico lexico;
    private Semantico semantico;
    private Token tk;

    public Sintatico(File source) {
        lexico = new Lexico(source);
        semantico = new Semantico();
        
    }

    public void execute() throws Exception {

    /*inicio
    Lexico(token)
    se token.simbolo = sprograma
    entao inicio
        Lexico(token)
        se token.simbolo = sidentificador
        entao inicio
            insere_tabela(token.lexema,"nomedeprograma","","")
            Lexico(token)
            se token.simbolo = spontovirgula
            entao inicio
                analisa_bloco
                se token.simbolo = sponto
                entao se acabou arquivo ou e comentario
                      entao sucesso
                      senao ERRO
                senao ERRO
                fim
            senao ERRO
            fim
        senao ERRO
        fim
    senao ERRO
    fim.
    */
        tk = lexico.token();
        if(tk!=null && tk.getSimbolo()==Simbolos.Programa)
        {
            tk = lexico.token();
            if(tk!=null && tk.getSimbolo()==Simbolos.Identificador)
            {
                semantico.insereSimbolo(Tipos.NomeDoPrograma, tk.getLexema(), true);
                
                tk = lexico.token();
                if(tk!=null && tk.getSimbolo()==Simbolos.PontoVirgula)
                {
                    analisaBloco();
                    
                    if(tk!=null && tk.getSimbolo()==Simbolos.Ponto)
                    {
                        tk = lexico.token();
                        if(tk==null)
                           throw new CompiladoComSucesso();
                        else
                            throw new AnaliseSintaticaException(lexico.getN_line(),"codigo apos final do programa.");
                    }
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(),"final do programa, token '.' esperado.");
                }
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(),"token ';' esperado.");
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(),"nome do programa, token identificador esperado.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"programa deve iniciar com o token 'programa'.");


    }
    
    private void analisaBloco() throws Exception
    {
        /*Algoritmo Analisa_Bloco <bloco>
            inicio
            Lexico(token)
            Analisa_et_variaveis
            Analisa_subrotinas
            Analisa_comandos
        fim*/
        
        tk = lexico.token();
        analisaEtapaVariaveis();
        analisaSubRotinas();
        analisaComandos();

    }
    
    private void analisaEtapaVariaveis() throws Exception{
        /*inicio
            se token.simbolo = svar
            entao inicio
                Lexico(token)
                se token.simbolo = sidentificador
                entao enquanto(token.simbolo = sidentificador)
                      faça inicio
                           Analisa_Variaveis
                           se token.simbolo = spontvirg
                           entao Lexico (token)
                           senao ERRO
                      fim
                senao ERRO
         fim*/
        
        if(tk.getSimbolo()==Simbolos.Var)
        {
            tk = lexico.token();
            if(tk.getSimbolo()==Simbolos.Identificador)
            {
                while(tk.getSimbolo()==Simbolos.Identificador)
                {
                    analisaVariaveis();
                    if(tk.getSimbolo()==Simbolos.PontoVirgula)
                        tk = lexico.token();
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(), "token ';' esperado.");
                }
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "nome de variavel, token identificador esperado.");
        }
    }
    
    private void analisaVariaveis() throws Exception {
    /*inicio
        repita
            se token.simbolo = sidentificador
            entao
                inicio
                Pesquisa_duplicvar_ tabela(token.lexema)
                se nao encontrou duplicidade
                entao
                    inicio
                    insere_tabela(token.lexema, "variavel")
                    Lexico(token)
                    se (token.simbolo = Svirgula) ou (token.simbolo = Sdoispontos)
                    entao
                        inicio
                        se token.simbolo = Svirgula
                        entao
                            inicio
                            Lexico(token)
                            se token.simbolo = Sdoispontos
                            entao ERRO
                            fim
                        fim
                    senao ERRO
                    fim
                senao ERRO
                fim
            senao ERRO
        ate que (token.simbolo = sdoispontos)
        Lexico(token)
        Analisa_Tipo
      fim*/
        
        do
        {
            if(tk.getSimbolo()==Simbolos.Identificador)
            {   if(!semantico.isVariavelDuplicada(tk))
                {
                   semantico.insereSimbolo(Tipos.Variavel, tk.getLexema(), false);
                   
                   tk = lexico.token();
                   if(tk.getSimbolo()==Simbolos.Virgula || tk.getSimbolo()==Simbolos.DoisPontos)
                   {
                       if (tk.getSimbolo()==Simbolos.Virgula)
                       {
                    	   tk = lexico.token();
                           if(tk.getSimbolo()==Simbolos.DoisPontos)
                               throw new AnaliseSintaticaException(lexico.getN_line(), "token invalido ':' apos ','.");
                       }
                       
                   } else
                	   throw new AnaliseSintaticaException(lexico.getN_line(), "token ',' ou ':' esperado.");
                } else
                	throw new AnaliseSintaticaException(lexico.getN_line(), "variavel '" + tk.getLexema() + "' declarada mais de uma vez neste escopo.");
            } else
            	throw new AnaliseSintaticaException(lexico.getN_line(), "nome de variavel, token identificador esperado."); 	
        }
        while(tk.getSimbolo()!=Simbolos.DoisPontos);
        tk = lexico.token();
        analisaTipo();
        
    }
    
    private void analisaTipo() throws Exception {
    /*inicio
		se (token.simbolo != sinteiro e token.simbolo != sbooleano))
		entio ERRO
		senio coloca_tipo_tabela(token.lexema)
		Lixico(token)
	fim*/
    	
    	if(tk.getSimbolo()!=Simbolos.Inteiro && tk.getSimbolo()!= Simbolos.Booleano)
            throw new AnaliseSintaticaException(lexico.getN_line(),"tipo de variavel invalido.");
        
        semantico.alteraSimbolo(tk.getLexema(), tk.getSimbolo());
    	
    }

    private void analisaSubRotinas() throws Exception {
       throw new AnaliseSintaticaException(lexico.getN_line(), "Ainda nao implementada AnalisaSubRotinas");
       
    }

    private void analisaComandos() throws Exception {
       throw new AnaliseSintaticaException(lexico.getN_line(), "Ainda nao implementada AnalisaComandos");
        
    }
    
    //Dentro de AnalisaExpressao, para expressao, expressao simples, termo e fator
    //ao pedir o prox token, joga numa lista da expressao (ter a expressao inteira ao final da analise)
    // jogar positivo e negativo com algum diferencial indicando que eh unario e nao + e - comum
    
    //Sintatico.AnalisaExpressao retorna a expressao completa na lista
    //Pos ordem recebe a expressao e retorna ela convertida
    //Semantico.AnalisaExpressao recebe a expressao convertida e retorna o tipo final dela
    //Sintatico.AnalisaX (resultados de expressoes) verifica o tipo retornado

}