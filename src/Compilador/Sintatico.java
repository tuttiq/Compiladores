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
    
    private void analisaBloco() throws Exception {
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
                      faÃ§a inicio
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
                        throw new AnaliseSintaticaException(lexico.getN_line(), "declaracao de variaveis, token ';' esperado.");
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

        if(tk.getSimbolo() == Simbolos.Inicio){
            tk = lexico.token();
            analisaComandoSimples();
            while(tk.getSimbolo() != Simbolos.Fim){
                if(tk.getSimbolo() == Simbolos.PontoVirgula){
                    tk = lexico.token();
                    if(tk.getSimbolo() != Simbolos.Fim)
                        analisaComandoSimples();
                }
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(),"token ';' esperado");
            }//fim while

            tk = lexico.token();
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "token 'inicio' esperado");


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
        if(tk.getSimbolo() == Simbolos.Identificador)
            analisaAtribChamadaProcedimento();
        else{
            if(tk.getSimbolo() == Simbolos.Se)
                analisaSe();
            else if(tk.getSimbolo() == Simbolos.Enquanto)
                analisaEnquanto();
            else if(tk.getSimbolo() == Simbolos.Leia)
                analisaLeia();
            else if(tk.getSimbolo() == Simbolos.Escreva)
                analisaEscreva();
            else
                analisaComandos();
        }
    }
    
    private void analisaAtribChamadaProcedimento() throws Exception{
    /*inicio
        Lexico(token)
        se token.simbolo = satribuiçao
        entao Analisa_atribuicao
        senao Chamada_procedimento
    fim*/
        tk = lexico.token();
        if(tk.getSimbolo() == Simbolos.Atribuicao)
            analisaAtribuicao();
        else
            analisaChamadaProcedimento();
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
        tk = lexico.token();
        if(tk.getSimbolo() == Simbolos.AbreParenteses)
        {
            tk = lexico.token();
            if(tk.getSimbolo() == Simbolos.Identificador)
            {
                if( semantico.isIdentificadorDeclarado(tk.getLexema()) )
                {
                    tk = lexico.token();
                    if(tk.getSimbolo() == Simbolos.FechaParenteses)
                        tk = lexico.token();
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(),"comando leia, token ')' esperado ");
                }
                else
                    semantico.erro(lexico.getN_line(),"identificador " + tk.getLexema() + " não declarado.");
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(),"comando leia, token identificador esperado.");

        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"comando leia, token '(' esperado");

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
        tk = lexico.token();
        if(tk.getSimbolo() == Simbolos.AbreParenteses){
            tk = lexico.token();
            if(tk.getSimbolo()==Simbolos.Identificador)
            {
	            if( semantico.isIdentificadorDeclarado(tk.getLexema()) )
	            {
	                tk = lexico.token();
	                if(tk.getSimbolo() == Simbolos.FechaParenteses)
	                    tk = lexico.token();
	                else
	                    throw new AnaliseSintaticaException(lexico.getN_line(),"comando escreva, token ')' esperado");
	            }
	            else
	                semantico.erro(lexico.getN_line(),"identificador " + tk.getLexema() + " nao declarado.");
            }
            else
            	throw new AnaliseSintaticaException(lexico.getN_line(), "comando escreva, token identificador esperado.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"comando escreva, token '(' esperado");
    }
    
    private void analisaSe() throws Exception {
       /*   início
                Léxico(token)
                Analisa_expressão
                se token.símbolo = sentão
                então início
                    Léxico(token)
                    Analisa_comando_simples
                    se token.símbolo = Ssenão
                    então início
                        Léxico(token)
                        Analisa_comando_simples
                    fim
                fim
                senão ERRO
            fim*/
        tk = lexico.token();
        analisaExpressao();
        
        if(tk.getSimbolo() == Simbolos.Entao)
        {
            tk =lexico.token();
            analisaComandoSimples();
            
            if(tk.getSimbolo() == Simbolos.Senao)
            {
                tk = lexico.token();
                analisaComandoSimples();
            }
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"comando se, 'entao' esperado");
    }

    private void analisaEnquanto() throws Exception {
    /*inÃ­cio
        LÃ©xico(token)
        Analisa_expressÃ£o
        se token.simbolo = sfaÃ§a
        entÃ£o inÃ­cio
            LÃ©xico(token)
            Analisa_comando_simples
        fim
        senÃ£o ERRO
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
    
    private void analisaSubRotinas() throws Exception {
    /*inÃ­cio
        enquanto (token.simbolo = sprocedimento) ou (token.simbolo = sfunÃ§Ã£o)
        faÃ§a inÃ­cio
            se (token.simbolo = sprocedimento)
            entÃ£o analisa_declaraÃ§Ã£o_procedimento
            senÃ£o analisa_ declaraÃ§Ã£o_funÃ§Ã£o
            se token.sÃ­mbolo = sponto-vÃ­rgula
            entÃ£o LÃ©xico(token)
            senÃ£o ERRO
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
    /*inÃ­cio
        LÃ©xico(token)
        nÃ­vel := â€œLâ€� (marca ou novo galho)
        se token.sÃ­mbolo = sidentificador
        entÃ£o inÃ­cio
            pesquisa_declproc_tabela(token.lexema)
            se nÃ£o encontrou
            entÃ£o inÃ­cio
                Insere_tabela(token.lexema,â€�procedimentoâ€�,nÃ­vel, rÃ³tulo)
                LÃ©xico(token)
                se token.simbolo = sponto_vÃ­rgula
                entÃ£o Analisa_bloco
                senÃ£o ERRO
            fim
            senÃ£o ERRO
        fim
        senÃ£o ERRO
        DESEMPILHA OU VOLTA NÃ�VEL
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
    /*inÃ­cio
        LÃ©xico(token)
        nÃ­vel := â€œLâ€� (marca ou novo galho)
        se token.sÃ­mbolo = sidentificador
        entÃ£o inÃ­cio
            pesquisa_declfunc_tabela(token.lexema)
            se nÃ£o encontrou
            entÃ£o inÃ­cio
                Insere_tabela(token.lexema,â€�â€�,nÃ­vel,rÃ³tulo)
                LÃ©xico(token)
                se token.sÃ­mbolo = sdoispontos
                entÃ£o inÃ­cio
                    LÃ©xico(token)
                    se (token.sÃ­mbolo = Sinteiro) ou (token.sÃ­mbolo = Sbooleano)
                    entÃ£o inÃ­cio
                        se (token.sÃ­mbolo = Sinteger)
                        entÃ£o TABSIMB[pc].tipo:=â€œfunÃ§Ã£o inteiroâ€�
                        senÃ£o TABSIMB[pc].tipo:=â€œfunÃ§Ã£o booleanâ€�
                        LÃ©xico(token)
                        se token.sÃ­mbolo = sponto_vÃ­rgula
                        entÃ£o Analisa_bloco
                    fim
                    senÃ£o ERRO
                fim
                senÃ£o ERRO
            fim
            senÃ£o ERRO
        fim
        senÃ£o ERRO
        DESEMPILHA OU VOLTA NÃ�VEL
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
        tk = lexico.token();
        analisaExpressao();
    }
    
    private void analisaChamadaFuncao() throws Exception {
        
    }
    
    private void analisaChamadaProcedimento() throws Exception {
        
    }
    
    private void analisaExpressao() throws Exception {
    /*inÃ­cio
        Analisa_expressÃ£o_simples
        se (token.simbolo = (smaior ou smaiorig ou sig ou smenor ou smenorig ou sdif))
        entÃ£o inicio
            LÃ©xico(token)
            Analisa_expressÃ£o_simples
        fim
    fim*/
        expressao = new ArrayList<Token>();
        
        analisaExpressaoSimples();
        if(tk.getSimbolo()==Simbolos.Maior || tk.getSimbolo()==Simbolos.MaiorIgual || tk.getSimbolo()==Simbolos.Igual
                || tk.getSimbolo()==Simbolos.Menor || tk.getSimbolo()==Simbolos.MenorIgual || tk.getSimbolo()==Simbolos.Diferente)
        {
            expressao.add(tk);
            tk = lexico.token();
            analisaExpressaoSimples();
        }
    }
    
    private void analisaExpressaoSimples() throws Exception {
    /*inÃ­cio
        se (token.simbolo = smais) ou (token.simbolo = smenos)
        entÃ£o
            LÃ©xico(token)
        Analisa_termo
        enquanto ((token.simbolo = smais) ou (token.simbolo = smenos) ou (token.simbolo = sou))
        faÃ§a inicio
            LÃ©xico(token)
            Analisa_termo
        fim
    fim*/
        
        
        if(tk.getSimbolo()==Simbolos.Mais || tk.getSimbolo()==Simbolos.Menos)
        {
            expressao.add(tk);
            tk = lexico.token();
        }
        analisaTermo();
        while(tk.getSimbolo()==Simbolos.Mais || tk.getSimbolo()==Simbolos.Menos || tk.getSimbolo()==Simbolos.Ou)
        {
            expressao.add(tk);
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
    /*inÃ­cio
        Analisa_fator
        enquanto ((token.simbolo = smult) ou (token.simbolo = sdiv) ou (token.simbolo = se))
        entÃ£o inÃ­cio
            LÃ©xico(token)
            Analisa_fator
        fim
    fim*/
        
        analisaFator();
        while(tk.getSimbolo()==Simbolos.Multiplicacao || tk.getSimbolo()==Simbolos.Divisao || tk.getSimbolo()==Simbolos.Se)
        {
            expressao.add(tk);
            tk = lexico.token();
            analisaFator();
        }
    }
    
    private void analisaFator() throws Exception {
    /*InÃ­cio
        Se token.simbolo = sidentificador (* VariÃ¡vel ou FunÃ§Ã£o*)
        EntÃ£o inicio
            Se pesquisa_tabela(token.lexema,nÃ­vel,ind)
            EntÃ£o Se (TabSimb[ind].tipo = â€œfunÃ§Ã£o inteiroâ€�) ou (TabSimb[ind].tipo = â€œfunÃ§Ã£o booleanoâ€�)
                  EntÃ£o Analisa_chamada_funÃ§Ã£o
                  SenÃ£o LÃ©xico(token)
            SenÃ£o ERRO
            Fim
        SenÃ£o Se (token.simbolo = snumero) (*NÃºmero*)
              EntÃ£o LÃ©xico(token)
              SenÃ£o Se token.sÃ­mbolo = snao (*NAO*)
                    EntÃ£o inÃ­cio
                        LÃ©xico(token)
                        Analisa_fator
                        Fim
                    SenÃ£o Se token.simbolo = sabre_parenteses (* expressÃ£o entre parenteses *)
                          EntÃ£o inÃ­cio
                              LÃ©xico(token)
                              Analisa_expressÃ£o(token)
                              Se token.simbolo = sfecha_parenteses
                              EntÃ£o LÃ©xico(token)
                              SenÃ£o ERRO
                              Fim
                          SenÃ£o Se (token.lexema = verdadeiro) ou (token.lexema = falso)
                                EntÃ£o LÃ©xico(token)
                                SenÃ£o ERRO
      Fim*/
        
        expressao.add(tk);
        
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