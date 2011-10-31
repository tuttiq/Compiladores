package Compilador;

import Compilador.Structures.Token;
import Compilador.Structures.TabelaDeSimbolos;
import Compilador.Exceptions.AnaliseSintaticaException;
import Compilador.Exceptions.CompiladoComSucesso;
import java.io.File;

public class Sintatico {

    private TabelaDeSimbolos tabela = new TabelaDeSimbolos();
    private Lexico lexico;
    private Semantico semantico;
    private Token tk;

    public Sintatico(File source) {
        lexico = new Lexico(source);
        semantico = new Semantico();
        
    }

    public void execute() throws Exception {

    /*início
    Léxico(token)
    se token.simbolo = sprograma
    então início
        Léxico(token)
        se token.simbolo = sidentificador
        então início
            insere_tabela(token.lexema,”nomedeprograma”,””,””)
            Léxico(token)
            se token.simbolo = spontovirgula
            então início
                analisa_bloco
                se token.simbolo = sponto
                então se acabou arquivo ou é comentário
                      então sucesso
                      senão ERRO
                senão ERRO
                fim
            senão ERRO
            fim
        senão ERRO
        fim
    senão ERRO
    fim.
    */
        tk = lexico.token();
        if(tk!=null && tk.getSimbolo().equals("sPrograma"))
        {
            tk = lexico.token();
            if(tk!=null && tk.getSimbolo().equals("sIdentificador"))
            {
                tabela.insere("nomedoprograma", tk.getLexema(), true, null);
                tk = lexico.token();
                if(tk!=null && tk.getSimbolo().equals("sPontoVirgula"))
                {
                    analisaBloco();
                    
                    if(tk!=null && tk.getSimbolo().equals("sPonto"))
                    {
                        tk = lexico.token();
                        if(tk==null)
                           throw new CompiladoComSucesso();
                        else
                            throw new AnaliseSintaticaException(lexico.getN_line(),"codigo apos final do programa.");
                    }
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(),"final do programa, '.' esperado.");
                }
                else
                    throw new AnaliseSintaticaException(lexico.getN_line(),"';' esperado.");
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(),"nome do programa, identificador esperado.");
        }
        else
            throw new AnaliseSintaticaException(lexico.getN_line(),"programa deve iniciar com a palavra 'programa'.");


    }
    
    public void analisaBloco() throws Exception
    {
        /*Algoritmo Analisa_Bloco <bloco>
            início
            Léxico(token)
            Analisa_et_variáveis
            Analisa_subrotinas
            Analisa_comandos
        fim*/
        
        tk = lexico.token();
        analisaEtapaVariaveis();
        analisaSubRotinas();
        analisaComandos();

    }
    
    public void analisaEtapaVariaveis() throws Exception{
        /*início
            se token.simbolo = svar
            então início
                Léxico(token)
                se token.símbolo = sidentificador
                então enquanto(token.símbolo = sidentificador)
                      faça início
                           Analisa_Variáveis
                           se token.símbolo = spontvirg
                           então Léxico (token)
                           senão ERRO
                      fim
                senão ERRO
         fim*/
        
        if(tk.getSimbolo().equals("sVar"))
        {
            tk = lexico.token();
            if(tk.getSimbolo().equals("sIdentificador"))
            {
                while(tk.getSimbolo().equals("sIdentificador"))
                {
                    analisaVariaveis();
                    if(tk.getSimbolo().equals("sPontoVirgula"))
                        tk = lexico.token();
                    else
                        throw new AnaliseSintaticaException(lexico.getN_line(), "';' esperado.");
                }
            }
            else
                throw new AnaliseSintaticaException(lexico.getN_line(), "nome de variavel, identificador esperado.");
        }
    }
    
    public void analisaVariaveis() throws Exception {
    /*início
        repita
            se token.símbolo = sidentificador
            então
                início
                Pesquisa_duplicvar_ tabela(token.lexema)
                se não encontrou duplicidade
                então
                    início
                    insere_tabela(token.lexema, “variável”)
                    Léxico(token)
                    se (token.símbolo = Svírgula) ou (token.símbolo = Sdoispontos)
                    então
                        início
                        se token.símbolo = Svírgula
                        então
                            início
                            Léxico(token)
                            se token.simbolo = Sdoispontos
                            então ERRO
                            fim
                        fim
                    senão ERRO
                    fim
                senão ERRO
                fim
            senão ERRO
        até que (token.símbolo = sdoispontos)
        Léxico(token)
        Analisa_Tipo
      fim*/
        
        do
        {
            if(tk.getSimbolo().equals("sIdentificador"))
                if(!semantico.isVariavelDuplicada(tk))
                {
                   //semantico.insereTabela()...
                }
        }
        while(tk.getSimbolo().equals("sDoisPontos"));
        
        
    }

    private void analisaSubRotinas() throws Exception {
       throw new AnaliseSintaticaException(lexico.getN_line(), "Ainda nao implementada AnalisaSubRotinas");
       
    }

    private void analisaComandos() throws Exception {
       throw new AnaliseSintaticaException(lexico.getN_line(), "Ainda nao implementada AnalisaComandos");
        
    }

}
