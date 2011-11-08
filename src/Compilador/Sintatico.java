package Compilador;

import Compilador.Models.Simbolo;
import Compilador.Models.Token;
import Compilador.Exceptions.AnaliseSintaticaException;
import Compilador.Constants.Simbolos;
import Compilador.Constants.Tipos;
import java.io.File;
import java.util.ArrayList;

public class Sintatico {

    private Lexico lexico;
    private Semantico semantico;
    private Token tk;
    private ArrayList<Token> expressao;

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
                           return;
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
            {   if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
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
		Lexico(token)
	fim*/
    	
    	if(tk.getSimbolo()!=Simbolos.Inteiro && tk.getSimbolo()!= Simbolos.Booleano)
            throw new AnaliseSintaticaException(lexico.getN_line(),"tipo de variavel invalido.");
        
        semantico.alteraSimbolo(tk.getLexema(), tk.getSimbolo());
        tk = lexico.token();
    	
    }

    private void analisaComandos() throws Exception {
    /*inicio
    se token.simbolo = sinicio
    entao inicio
        Lexico(token)
        Analisa_comando_simples
        enquanto (token.simbolo != sfim)
        faça inicio
            se token.simbolo = spontovirgula
            entao inicio
                Lexico(token)
                se token.simbolo != sfim
                entao Analisa_comando_simples
                fim
            senao ERRO
            fim
        Lexico(token)
        fim
    senao ERRO
    fim
    */
        
        
    }
    
    private void analisaComandoSimples() throws Exception {
  /*inicio
        se token.simbolo = sidentificador
        entao Analisa_atrib_chprocedimento
        senao
            se token.simbolo = sse
            entao Analisa_se
            senao
                se token.simbolo = senquanto
                entao Analisa_enquanto
                senao
                    se token.simbolo = sleia
                    entao Analisa_leia
                    senao
                        se token.simbolo = sescreva
                        entao Analisa_ escreva
                        senao
                            Analisa_comandos
    fim
    */
    }
    
    private void analisaAtribChamadaProcedimento() throws Exception{
    /*inicio
        Lexico(token)
        se token.simbolo = satribuiçao
        entao Analisa_atribuicao
        senao Chamada_procedimento
    fim*/
    }
    
    private void analisaLeia() throws Exception {
    /*inicio
        Lexico(token)
        se token.simbolo = sabre_parenteses
        entao inicio
            Lexico(token)
            se token.simbolo = sidentificador
            entao se semantico.pesquisa_declvar_tabela(token.lexema)
                  entao inicio
                      Lexico(token)
                      se token.simbolo = sfecha_parenteses
                      entao Lexico(token)
                      senao ERRO
                    fim
                  senao ERRO
            senao ERRO
          fim
        senao ERRO
     fim
    */
    }
    
    private void analisaEscreva() throws Exception {
    /*inicio
        Lexico(token)
        se token.simbolo = sabre_parenteses
        entao inicio
            Lexico(token)
            se token.simbolo = sidentificador
            entao se semantico.pesquisa_ declvarfunc_tabela(token.lexema)
                entao inicio
                    Lexico(token)
                    se token.simbolo = sfecha_parenteses
                    entao Lexico(token)
                    senao ERRO
                    fim
                senao ERRO
            senao ERRO
            fim
        senao ERRO
    fim
    */
    }
    
    private void analisaEnquanto() throws Exception {
    /*início
        Léxico(token)
        Analisa_expressão
        se token.simbolo = sfaça
        então início
            Léxico(token)
            Analisa_comando_simples
        fim
        senão ERRO
    fim*/
        
        tk = lexico.token();
        analisaExpressao();
        if(tk.getSimbolo()==Simbolos.Faca)
        {
            tk = lexico.token();
            analisaComandoSimples();
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "comando enquanto, 'faca' esperado.");
    }
    
    private void analisaSe() throws Exception {
        
    }
    
    private void analisaSubRotinas() throws Exception {
    /*início
        enquanto (token.simbolo = sprocedimento) ou (token.simbolo = sfunção)
        faça início
            se (token.simbolo = sprocedimento)
            então analisa_declaração_procedimento
            senão analisa_ declaração_função
            se token.símbolo = sponto-vírgula
            então Léxico(token)
            senão ERRO
        fim
    fim*/
        
        while(tk.getSimbolo()==Simbolos.Procedimento || tk.getSimbolo()==Simbolos.Funcao)
        {
            if(tk.getSimbolo()==Simbolos.Procedimento)
                analisaDeclaracaoProcedimento();
            else
                analisaDeclaracaoFuncao();
            
            if(tk.getSimbolo()==Simbolos.PontoVirgula)
                tk = lexico.token();
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "token ';' esperado.");
        }
        
    }

    private void analisaDeclaracaoProcedimento() throws Exception {
    /*início
        Léxico(token)
        nível := “L” (marca ou novo galho)
        se token.símbolo = sidentificador
        então início
            pesquisa_declproc_tabela(token.lexema)
            se não encontrou
            então início
                Insere_tabela(token.lexema,”procedimento”,nível, rótulo)
                Léxico(token)
                se token.simbolo = sponto_vírgula
                então Analisa_bloco
                senão ERRO
            fim
            senão ERRO
        fim
        senão ERRO
        DESEMPILHA OU VOLTA NÍVEL
    fim*/
        
        tk = lexico.token();
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
            {
                semantico.insereSimbolo(Tipos.Procedimento, tk.getLexema(), true);
                tk = lexico.token();
                if(tk.getSimbolo()==Simbolos.PontoVirgula)
                    analisaBloco();
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(), "token ';' esperado.");
            }
            else
                semantico.erro(lexico.getN_line(), "procedimento " + tk.getLexema() + " declarado mais de uma vez neste escopo.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "nome de procedimento, identificador esperado.");
        
    }
    
    private void analisaDeclaracaoFuncao() throws Exception {
    /*início
        Léxico(token)
        nível := “L” (marca ou novo galho)
        se token.símbolo = sidentificador
        então início
            pesquisa_declfunc_tabela(token.lexema)
            se não encontrou
            então início
                Insere_tabela(token.lexema,””,nível,rótulo)
                Léxico(token)
                se token.símbolo = sdoispontos
                então início
                    Léxico(token)
                    se (token.símbolo = Sinteiro) ou (token.símbolo = Sbooleano)
                    então início
                        se (token.símbolo = Sinteger)
                        então TABSIMB[pc].tipo:=“função inteiro”
                        senão TABSIMB[pc].tipo:=“função boolean”
                        Léxico(token)
                        se token.símbolo = sponto_vírgula
                        então Analisa_bloco
                    fim
                    senão ERRO
                fim
                senão ERRO
            fim
            senão ERRO
        fim
        senão ERRO
        DESEMPILHA OU VOLTA NÍVEL
    fim*/
        
        tk = lexico.token();
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
            {
                semantico.insereSimbolo(Tipos.Funcao, tk.getLexema(), true);
                tk = lexico.token();
                if(tk.getSimbolo()==Simbolos.DoisPontos)
                {
                    tk = lexico.token();
                    if(tk.getSimbolo()==Simbolos.Inteiro || tk.getSimbolo()==Simbolos.Booleano)
                    {   
                        if(tk.getSimbolo()==Simbolos.Inteiro)
                            semantico.alteraSimbolo(tk.getLexema(), Tipos.FuncaoInteiro);
                        else
                            semantico.alteraSimbolo(tk.getLexema(), Tipos.FuncaoBooleano);
                    
                        tk = lexico.token();
                        if(tk.getSimbolo()==Simbolos.PontoVirgula)
                            analisaBloco();
                    }
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(), "tipo de funcao, 'inteiro' ou 'booleano' esperado.");
                }
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(), "token ':' esperado apos nome da funcao.");
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "funcao " + tk.getLexema() + " declarada mais de uma vez neste escopo.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "nome de funcao, identificador esperado.");
          
    }
    
    private void analisaAtribuicao() throws Exception {
        
    }
    
    private void analisaChamadaFuncao() throws Exception {
        
    }
    
    private void analisaChamadaProcedimento() throws Exception {
        
    }
    private void analisaExpressao() throws Exception {
    /*início
        Analisa_expressão_simples
        se (token.simbolo = (smaior ou smaiorig ou sig ou smenor ou smenorig ou sdif))
        então inicio
            Léxico(token)
            Analisa_expressão_simples
        fim
    fim*/
        
        analisaExpressaoSimples();
        if(tk.getSimbolo()==Simbolos.Maior || tk.getSimbolo()==Simbolos.MaiorIgual || tk.getSimbolo()==Simbolos.Igual
                || tk.getSimbolo()==Simbolos.Menor || tk.getSimbolo()==Simbolos.MenorIgual || tk.getSimbolo()==Simbolos.Diferente)
        {
            tk = lexico.token();
            analisaExpressaoSimples();
        }
    }
    
    private void analisaExpressaoSimples() throws Exception {
    /*início
        se (token.simbolo = smais) ou (token.simbolo = smenos)
        então
            Léxico(token)
        Analisa_termo
        enquanto ((token.simbolo = smais) ou (token.simbolo = smenos) ou (token.simbolo = sou))
        faça inicio
            Léxico(token)
            Analisa_termo
        fim
    fim*/
        
        if(tk.getSimbolo()==Simbolos.Mais || tk.getSimbolo()==Simbolos.Menos)
            tk = lexico.token();
        analisaTermo();
        while(tk.getSimbolo()==Simbolos.Mais || tk.getSimbolo()==Simbolos.Menos || tk.getSimbolo()==Simbolos.Ou)
        {
            tk = lexico.token();
            analisaTermo();
        }
    }
    
    //Dentro de AnalisaExpressao, para expressao, expressao simples, termo e fator
    //ao pedir o prox token, joga numa lista da expressao (ter a expressao inteira ao final da analise)
    // jogar positivo e negativo com algum diferencial indicando que eh unario e nao + e - comum
    
    //Sintatico.AnalisaExpressao retorna a expressao completa na lista
    //Pos ordem recebe a expressao e retorna ela convertida
    //Semantico.AnalisaExpressao recebe a expressao convertida e retorna o tipo final dela
    //Sintatico.AnalisaX (resultados de expressoes) verifica o tipo retornado

    private void analisaTermo() throws Exception {
    /*início
        Analisa_fator
        enquanto ((token.simbolo = smult) ou (token.simbolo = sdiv) ou (token.simbolo = se))
        então início
            Léxico(token)
            Analisa_fator
        fim
    fim*/
        
        analisaFator();
        while(tk.getSimbolo()==Simbolos.Multiplicacao || tk.getSimbolo()==Simbolos.Divisao || tk.getSimbolo()==Simbolos.Se)
        {
            tk = lexico.token();
            analisaFator();
        }
    }
    
    private void analisaFator() throws Exception {
    /*Início
        Se token.simbolo = sidentificador (* Variável ou Função*)
        Então inicio
            Se pesquisa_tabela(token.lexema,nível,ind)
            Então Se (TabSimb[ind].tipo = “função inteiro”) ou (TabSimb[ind].tipo = “função booleano”)
                  Então Analisa_chamada_função
                  Senão Léxico(token)
            Senão ERRO
            Fim
        Senão Se (token.simbolo = snumero) (*Número*)
              Então Léxico(token)
              Senão Se token.símbolo = snao (*NAO*)
                    Então início
                        Léxico(token)
                        Analisa_fator
                        Fim
                    Senão Se token.simbolo = sabre_parenteses (* expressão entre parenteses *)
                          Então início
                              Léxico(token)
                              Analisa_expressão(token)
                              Se token.simbolo = sfecha_parenteses
                              Então Léxico(token)
                              Senão ERRO
                              Fim
                          Senão Se (token.lexema = verdadeiro) ou (token.lexema = falso)
                                Então Léxico(token)
                                Senão ERRO
      Fim*/
       
        
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            Simbolo s = semantico.buscaSimbolo(tk.getLexema());
            if(s!=null)
            {   if(s.getTipo()==Tipos.FuncaoInteiro || s.getTipo()==Tipos.FuncaoBooleano)
                    analisaChamadaFuncao();
                else
                    tk = lexico.token(); 
            }
            else
                semantico.erro(lexico.getN_line(),"funcao/variavel nao declarada.");
                
        }
        else if (tk.getSimbolo()==Simbolos.Numero)
            tk = lexico.token();
        else if (tk.getSimbolo()==Simbolos.Nao)
        {
            tk = lexico.token();
            analisaFator();
        }
        else if (tk.getSimbolo()==Simbolos.AbreParenteses)
        {
            tk = lexico.token();
            analisaExpressao();
            if(tk.getSimbolo()==Simbolos.FechaParenteses)
                tk = lexico.token();
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "token ')' esperado.");
        }
        else if (tk.getSimbolo()==Simbolos.Verdadeiro || tk.getSimbolo()==Simbolos.Falso)
            tk = lexico.token();
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"fator invalido na expressao.");
        
    }
}