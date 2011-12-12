package Compilador;

import Compilador.Models.Simbolo;
import Compilador.Models.Token;
import Compilador.Exceptions.AnaliseSintaticaException;
import Compilador.Constants.Comandos;
import Compilador.Constants.Simbolos;
import Compilador.Constants.Tipos;
import java.io.File;
import java.util.ArrayList;

public class Sintatico {

    private Lexico lexico;
    private Semantico semantico;
    private GeradorDeCodigo codigo;
    private Token tk;
    private ArrayList<Token> expressao;
    private Integer proxEndereco = 0;
    private Integer proxRotulo = 1;

    public Sintatico(File source)  throws Exception{
        lexico = new Lexico(source);
        semantico = new Semantico();
        codigo = new GeradorDeCodigo(source);
        
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
            codigo.gera(Comandos.Start);
            
            tk = lexico.token();
            if(tk!=null && tk.getSimbolo()==Simbolos.Identificador)
            {
                semantico.insereSimbolo(Tipos.NomeDoPrograma, tk.getLexema(), true);
                
                tk = lexico.token();
                if(tk!=null && tk.getSimbolo()==Simbolos.PontoVirgula)
                {
                    analisaBloco();
                    ArrayList<Simbolo> vars = semantico.getVariaveis();
        codigo.gera(Comandos.Deallocate, proxEndereco-vars.size(), vars.size());
        semantico.removeSimbolos(vars);
        semantico.limpaEscopo();
        proxEndereco-=vars.size();
                    if(tk!=null && tk.getSimbolo()==Simbolos.Ponto)
                    {
                        codigo.gera(Comandos.Halt);
                        codigo.close();
                        
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
        int nVars=0; //numero de variaveis a alocar
        
        do
        {
            if(tk.getSimbolo()==Simbolos.Identificador)
            {   if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
                {
                    
                   semantico.insereSimbolo(Tipos.Variavel, tk.getLexema(), false, proxEndereco+nVars);
                   nVars++;
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
                    semantico.erro(lexico.getN_line(), "variavel '" + tk.getLexema() + "' declarada mais de uma vez neste escopo.");
            } else
            	throw new AnaliseSintaticaException(lexico.getN_line(), "nome de variavel, token identificador esperado."); 	
        }
        while(tk.getSimbolo()!=Simbolos.DoisPontos);
        
        codigo.gera(Comandos.Allocate, proxEndereco, nVars);
        proxEndereco+=nVars;
        
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
        
        
        switch(tk.getSimbolo())
        {
            case Simbolos.Inteiro: semantico.alteraSimbolo(Tipos.Inteiro);
                break;
            case Simbolos.Booleano: semantico.alteraSimbolo(Tipos.Booleano);
                break;
        }
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
                else if(tk.getSimbolo() != Simbolos.Fim)
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
        {
        	if(semantico.isIdentificadorDeclarado(tk.getLexema()))
        		analisaAtribChamadaProcedimento();
        	else
        		semantico.erro(lexico.getN_line(), "identificador " + tk.getLexema() + " nao declarado.");
        }
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
        Simbolo identificador = semantico.buscaSimbolo(tk.getLexema());
        tk = lexico.token();
        if(tk.getSimbolo() == Simbolos.Atribuicao)
            analisaAtribuicao(identificador);
        else
            analisaChamadaProcedimento(identificador);
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
                    Simbolo id = semantico.buscaSimbolo(tk.getLexema());
                    codigo.gera(Comandos.Read);
                    codigo.gera(Comandos.Store, id.getEndereco());
                    
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
                        Simbolo s = semantico.buscaSimbolo(tk.getLexema());
                        if(s.getTipo()==Tipos.FuncaoInteiro)
                        {
                            analisaChamadaFuncao(s);
                            codigo.gera(Comandos.LoadValue, proxEndereco-1);
                        }
                        else
                            codigo.gera(Comandos.LoadValue, s.getEndereco());
                        
                        codigo.gera(Comandos.Print);
                        
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
        
        expressao = new ArrayList<Token>();
        
        analisaExpressao();
        substituiUnarios();
        imprimeExpressao();
        expressao = semantico.posOrdem(expressao);
        imprimeExpressao();
        geraExpressao();
        semantico.analisaExpressao(new Simbolo(Tipos.Booleano, "operador SE", false, null), expressao, lexico.getN_line()); //analise semantica da expressao deve dar booleano
        codigo.gera(Comandos.JumpIfFalse, Comandos.Label + proxRotulo);
        int aux = proxRotulo;
        proxRotulo++;
        
        if(tk.getSimbolo() == Simbolos.Entao)
        {
            tk =lexico.token();
            analisaComandoSimples();
            
            codigo.gera(Comandos.Jump, Comandos.Label + proxRotulo);
            int aux2 = proxRotulo;
            proxRotulo++;
            codigo.geraLabel(aux);
            
            if(tk.getSimbolo() == Simbolos.Senao)
            {
                tk = lexico.token();
                analisaComandoSimples();
            }
            codigo.geraLabel(aux2);
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"comando se, 'entao' esperado");
    }

    private void imprimeExpressao() {
        for(Token x : expressao)
            System.out.println(x);
        
        System.out.println("----------------");
    }
    
    private void substituiUnarios()
    {
        for(int i=0;i<expressao.size();i++)
        {
            if(expressao.get(i).getSimbolo()==Simbolos.Mais || expressao.get(i).getSimbolo()==Simbolos.Menos)
            {
                if(i-1==-1 || expressao.get(i-1).getSimbolo()==Simbolos.AbreParenteses
                        || Simbolos.isOperador(expressao.get(i-1).getSimbolo()))
                {
                   if(expressao.get(i).getSimbolo()==Simbolos.Mais)
                       expressao.get(i).setSimbolo(Simbolos.Positivo);
                   else
                       expressao.get(i).setSimbolo(Simbolos.Negativo);
                }
            }
        }
    }
    
    private void analisaEnquanto() throws Exception {
    /*inÃ­cio
        LÃ©xico(token)
        Analisa_expressao
        se token.simbolo = sfaÃ§a
        entao inÃ­cio
            LÃ©xico(token)
            Analisa_comando_simples
        fim
        senao ERRO
    fim*/
        codigo.geraLabel(proxRotulo);
        int aux = proxRotulo;
        proxRotulo++;
        
        tk = lexico.token();
        
        expressao = new ArrayList<Token>();
        analisaExpressao();
        substituiUnarios();
        imprimeExpressao();
        expressao = semantico.posOrdem(expressao);
        imprimeExpressao();
        geraExpressao();
        semantico.analisaExpressao(new Simbolo(Tipos.Booleano, "operador ENQUANTO", false, null), expressao, lexico.getN_line()); //analise semantica da expressao deve dar booleano
        codigo.gera(Comandos.JumpIfFalse, Comandos.Label + proxRotulo);
        int aux2 = proxRotulo;
        proxRotulo++;
        
        if(tk.getSimbolo()==Simbolos.Faca)
        {
            tk = lexico.token();
            analisaComandoSimples();
            codigo.gera(Comandos.Jump, Comandos.Label + aux);
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "comando enquanto, 'faca' esperado.");
    
        codigo.geraLabel(aux2);
    }
    
    private void analisaSubRotinas() throws Exception {
    /*inÃ­cio
        enquanto (token.simbolo = sprocedimento) ou (token.simbolo = sfunÃ§ao)
        faÃ§a inÃ­cio
            se (token.simbolo = sprocedimento)
            entao analisa_declaraÃ§ao_procedimento
            senao analisa_ declaraÃ§ao_funÃ§ao
            se token.sÃ­mbolo = sponto-vÃ­rgula
            entao LÃ©xico(token)
            senao ERRO
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
        entao inÃ­cio
            pesquisa_declproc_tabela(token.lexema)
            se nao encontrou
            entao inÃ­cio
                Insere_tabela(token.lexema,â€�procedimentoâ€�,nÃ­vel, rÃ³tulo)
                LÃ©xico(token)
                se token.simbolo = sponto_vÃ­rgula
                entao Analisa_bloco
                senao ERRO
            fim
            senao ERRO
        fim
        senao ERRO
        DESEMPILHA OU VOLTA NÍVEL
    fim*/
        
        tk = lexico.token();
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
            {
                semantico.insereSimbolo(Tipos.Procedimento, tk.getLexema(), true);
                codigo.gera(Comandos.Jump, Comandos.Label + proxRotulo);
                int aux = proxRotulo;
                proxRotulo++;
                codigo.geraLabel(proxRotulo);
                proxEndereco++;//alocado para end de retorno
                Simbolo s = semantico.buscaSimbolo(tk.getLexema());
                s.setEndereco(proxRotulo);
                proxRotulo++;
                
                tk = lexico.token();
                
                if(tk.getSimbolo()==Simbolos.PontoVirgula)
                {   analisaBloco();
                    ArrayList<Simbolo> vars = semantico.getVariaveis();
                    codigo.gera(Comandos.Deallocate, proxEndereco-vars.size(), vars.size());
                    semantico.removeSimbolos(vars);
                    semantico.limpaEscopo();
                    proxEndereco-=vars.size();
                    codigo.gera(Comandos.Return);
                    proxEndereco--; //do endereço de retorno.
                    codigo.geraLabel(aux);
                }
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
        entao inÃ­cio
            pesquisa_declfunc_tabela(token.lexema)
            se nao encontrou
            entao inÃ­cio
                Insere_tabela(token.lexema,â€�â€�,nÃ­vel,rÃ³tulo)
                LÃ©xico(token)
                se token.sÃ­mbolo = sdoispontos
                entao inÃ­cio
                    LÃ©xico(token)
                    se (token.sÃ­mbolo = Sinteiro) ou (token.sÃ­mbolo = Sbooleano)
                    entao inÃ­cio
                        se (token.sÃ­mbolo = Sinteger)
                        entao TABSIMB[pc].tipo:=â€œfunÃ§ao inteiroâ€�
                        senao TABSIMB[pc].tipo:=â€œfunÃ§ao booleanâ€�
                        LÃ©xico(token)
                        se token.sÃ­mbolo = sponto_vÃ­rgula
                        entao Analisa_bloco
                    fim
                    senao ERRO
                fim
                senao ERRO
            fim
            senao ERRO
        fim
        senao ERRO
        DESEMPILHA OU VOLTA NÍVEL
    fim*/
        
        tk = lexico.token();
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            if(!semantico.isIdentificadorDuplicado(tk.getLexema()))
            {
                semantico.insereSimbolo(Tipos.Funcao, tk.getLexema(), true);
                codigo.gera(Comandos.Jump, Comandos.Label + proxRotulo);
                int aux = proxRotulo;
                proxRotulo++;
                codigo.geraLabel(proxRotulo);
                proxEndereco+=2; //um para o LDC e um que eh alocado para end de retorno
                Simbolo s = semantico.buscaSimbolo(tk.getLexema());
                s.setEndereco(proxRotulo);
                proxRotulo++;
                
                tk = lexico.token();
                if(tk.getSimbolo()==Simbolos.DoisPontos)
                {
                    tk = lexico.token();
                    if(tk.getSimbolo()==Simbolos.Inteiro || tk.getSimbolo()==Simbolos.Booleano)
                    {   
                        if(tk.getSimbolo()==Simbolos.Inteiro)
                            semantico.alteraSimbolo(Tipos.FuncaoInteiro);
                        else
                            semantico.alteraSimbolo(Tipos.FuncaoBooleano);
                    
                        tk = lexico.token();
                        if(tk.getSimbolo()==Simbolos.PontoVirgula)
                        {   analisaBloco();
                            ArrayList<Simbolo> vars = semantico.getVariaveis();
                            codigo.gera(Comandos.Deallocate, proxEndereco-vars.size(), vars.size());
                            semantico.removeSimbolos(vars);
                            semantico.limpaEscopo();
                            proxEndereco-=vars.size();
                            codigo.gera(Comandos.Return);
                            proxEndereco-=2;// um do valor de retorno e um do end de retorno
                            codigo.geraLabel(aux);
                        }
                    }
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(), "tipo de funcao, 'inteiro' ou 'booleano' esperado.");
                }
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(), "token ':' esperado apos nome da funcao.");
            }
            else
                semantico.erro(lexico.getN_line(), "funcao " + tk.getLexema() + " declarada mais de uma vez neste escopo.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(), "nome de funcao, identificador esperado.");
          
    }
   
    private void analisaAtribuicao(Simbolo id) throws Exception {
        
        tk = lexico.token(); 
        
        expressao = new ArrayList<Token>();
        analisaExpressao();
        substituiUnarios();
        imprimeExpressao();
        expressao = semantico.posOrdem(expressao);
        imprimeExpressao();
        geraExpressao();
        semantico.analisaExpressao(id,expressao, lexico.getN_line());
        int nVars = semantico.getVariaveis().size();
        
        if(id.getTipo()==Tipos.FuncaoBooleano || id.getTipo()==Tipos.FuncaoInteiro)
            codigo.gera(Comandos.Store, proxEndereco-nVars-2);
        else
            codigo.gera(Comandos.Store, id.getEndereco());
        
    }
    
    private void analisaChamadaFuncao(Simbolo id) throws Exception {
       codigo.gera(Comandos.LoadConst, 0); //espaço para o valor de retorno na pilha
                
      codigo.gera(Comandos.Call,id.getEndereco());
      
    }
    
    private void analisaChamadaProcedimento(Simbolo id) throws Exception {
        codigo.gera(Comandos.Call, id.getEndereco());
        
    }
    
    private void analisaExpressao() throws Exception {
    /*inÃ­cio
        Analisa_expressao_simples
        se (token.simbolo = (smaior ou smaiorig ou sig ou smenor ou smenorig ou sdif))
        entao inicio
            LÃ©xico(token)
            Analisa_expressao_simples
        fim
    fim*/
        
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
        entao
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
        entao inÃ­cio
            LÃ©xico(token)
            Analisa_fator
        fim
    fim*/
        
        analisaFator();
        while(tk.getSimbolo()==Simbolos.Multiplicacao || tk.getSimbolo()==Simbolos.Divisao || tk.getSimbolo()==Simbolos.E)
        {
            expressao.add(tk);
            tk = lexico.token();
            analisaFator();
        }
    }
    
    private void analisaFator() throws Exception {
    /*InÃ­cio
        Se token.simbolo = sidentificador (* VariÃ¡vel ou FunÃ§ao*)
        Entao inicio
            Se pesquisa_tabela(token.lexema,nÃ­vel,ind)
            Entao Se (TabSimb[ind].tipo = â€œfunÃ§ao inteiroâ€�) ou (TabSimb[ind].tipo = â€œfunÃ§ao booleanoâ€�)
                  Entao Analisa_chamada_funÃ§ao
                  Senao LÃ©xico(token)
            Senao ERRO
            Fim
        Senao Se (token.simbolo = snumero) (*NÃºmero*)
              Entao LÃ©xico(token)
              Senao Se token.sÃ­mbolo = snao (*NAO*)
                    Entao inÃ­cio
                        LÃ©xico(token)
                        Analisa_fator
                        Fim
                    Senao Se token.simbolo = sabre_parenteses (* expressao entre parenteses *)
                          Entao inÃ­cio
                              LÃ©xico(token)
                              Analisa_expressao(token)
                              Se token.simbolo = sfecha_parenteses
                              Entao LÃ©xico(token)
                              Senao ERRO
                              Fim
                          Senao Se (token.lexema = verdadeiro) ou (token.lexema = falso)
                                Entao LÃ©xico(token)
                                Senao ERRO
      Fim*/
        
        
        
        if(tk.getSimbolo()==Simbolos.Identificador)
        {
            
            if(semantico.isIdentificadorDeclarado(tk.getLexema()))
            {
                Simbolo s = semantico.buscaSimbolo(tk.getLexema());
                
                if(s.getTipo()==Tipos.FuncaoInteiro || s.getTipo()==Tipos.FuncaoBooleano)
                    analisaChamadaFuncao(s);
                expressao.add(tk);
                tk = lexico.token();  
            }
            else
                semantico.erro(lexico.getN_line(),"identificador '" + tk.getLexema() + "' nao declarado.");
                
        }
        else if (tk.getSimbolo()==Simbolos.Numero)
        {expressao.add(tk);
            tk = lexico.token();
        
        }else if (tk.getSimbolo()==Simbolos.Nao)
        {expressao.add(tk);
            tk = lexico.token();
            analisaFator();
        }
        else if (tk.getSimbolo()==Simbolos.AbreParenteses)
        {   expressao.add(tk);
            tk = lexico.token();
            
            analisaExpressao();
            if(tk.getSimbolo()==Simbolos.FechaParenteses)
            {expressao.add(tk);
                tk = lexico.token();
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "token ')' esperado.");
        }
        else if (tk.getSimbolo()==Simbolos.Verdadeiro || tk.getSimbolo()==Simbolos.Falso)
        {expressao.add(tk);
            tk = lexico.token();
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"fator '" + tk.getLexema() + "' invalido na expressao.");
        
    }

    private void geraExpressao() throws Exception{
     for(Token termo : expressao)
        {
            if(termo.getSimbolo()==Simbolos.Identificador)
            {   if(semantico.isIdentificadorDeclarado(termo.getLexema()))
                {
                    Simbolo s = semantico.buscaSimbolo(termo.getLexema());
                    if(s.getTipo()==Tipos.Inteiro || s.getTipo()==Tipos.Booleano)
                        codigo.gera(Comandos.LoadValue, s.getEndereco());
                    
                }
                else
                    semantico.erro(lexico.getN_line(), "identificador '" + termo.getLexema() + "' nao declarado.");
            }
            else if(termo.getSimbolo()==Simbolos.Numero)
                codigo.gera(Comandos.LoadConst, termo.getLexema());
            else if(termo.getSimbolo()==Simbolos.Verdadeiro)
                codigo.gera(Comandos.LoadConst, 1);
            else if(termo.getSimbolo()==Simbolos.Falso)
                codigo.gera(Comandos.LoadConst, 0);
            else if(termo.getSimbolo()==Simbolos.Ou)
                codigo.gera(Comandos.Or);
            else if(termo.getSimbolo()==Simbolos.E)
                codigo.gera(Comandos.And);
            else if(termo.getSimbolo()==Simbolos.Nao)
                codigo.gera(Comandos.Negate);
            else if(termo.getSimbolo()==Simbolos.Menor)
                codigo.gera(Comandos.CmpLower);
            else if(termo.getSimbolo()==Simbolos.MenorIgual)
                codigo.gera(Comandos.CmpLowerEqual);
            else if(termo.getSimbolo()==Simbolos.Maior)
                codigo.gera(Comandos.CmpHigher);
            else if(termo.getSimbolo()==Simbolos.MaiorIgual)
                codigo.gera(Comandos.CmpHigherEqual);
            else if(termo.getSimbolo()==Simbolos.Igual)
                codigo.gera(Comandos.CmpEqual);
            else if(termo.getSimbolo()==Simbolos.Diferente)
                codigo.gera(Comandos.CmpDifferent);
            else if (termo.getSimbolo()==Simbolos.Negativo)
                codigo.gera(Comandos.Invert);
            else if(termo.getSimbolo()==Simbolos.Mais)
                codigo.gera(Comandos.Add);
            else if(termo.getSimbolo()==Simbolos.Menos)
                codigo.gera(Comandos.Subtract);
            else if(termo.getSimbolo()==Simbolos.Multiplicacao)
                codigo.gera(Comandos.Multiply);
            else if(termo.getSimbolo()==Simbolos.Divisao)
                codigo.gera(Comandos.Divide);
        }
    }

}