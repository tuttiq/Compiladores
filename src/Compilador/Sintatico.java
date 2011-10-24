package Compilador;


import java.io.File;

public class Sintatico {

    private TabelaDeSimbolos tabela = new TabelaDeSimbolos();
    private Lexico lexico;
    private Token tk;
   // private String mensagem = null;



    public Sintatico(File source) {
        lexico = new Lexico(source);
        
    }

    public String execute() {

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
        //CHAMA ANÁLISE LÉXICA
        String mensagem = lexico.fileToTokenList();
        if(mensagem!=null)
            return mensagem;
        
        tk = lexico.token();
        if(tk.getSimbolo().equals("sPrograma"))
        {
            tk = lexico.token();
            if(tk.getSimbolo().equals("sIdentificador"))
            {
                tabela.insere("nomedoprograma", tk.getLexema(), true, null);
                tk = lexico.token();
                if(tk.getSimbolo().equals("sPontoVirgula"))
                {
                    //analisa_bloco()
                    if(tk.getSimbolo().equals("sPonto"))
                    {
                        tk = lexico.token();
                        if(tk==null)
                            return "Compilado com sucesso!";
                        else
                            mensagem = "codigo apos final do programa.";
                    }
                    else
                        mensagem = "final do programa, '.' esperado.";
                }
                else
                    mensagem = "';' esperado.";
            }
            else
                mensagem = "nome do programa, identificador esperado.";
        }
        else
            mensagem = "programa deve iniciar com a palavra 'programa'.";


        return "Analisador sintatico: " + mensagem;
    }
    
    public String analisaBloco()
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

        return null;
    }
    
    public String analisaEtapaVariaveis() {
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
        
        return null;
    }
    
    public String analisaVariaveis() {
        return null;
    }

    private String analisaSubRotinas() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String analisaComandos() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
