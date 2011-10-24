package Compilador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Lexico {

    private File sourceFile;
    private ArrayList<Token> tokens = new ArrayList<Token>();
    private int n_token = -1; //contador para percorrer a lista de tokens

    public Lexico(File source) {
       sourceFile = source;
    }

    public Token token() {
        n_token++;
        
        if(n_token<tokens.size())
            return tokens.get(n_token);
        
        return null;
    }

    public String fileToTokenList()
    {
    /* Inicio
            Abre arquivo fonte
            Ler(caracter)
            Enquanto não acabou o arquivo fonte
                Faça
                {
                    Enquanto ((caracter = “{“)ou (caracter = espaço)) e (não acabou o arquivo fonte)
                        Faça
                        {
                            Se caracter = “{“
                                Então
                                {
                                    Enquanto (caracter != “}” ) e (não acabou o arquivo fonte)
                                        Faça Ler(caracter)

                                    Ler(caracter)
                                }
                            Enquanto (caracter = espaço) e (não acabou o arquivo fonte)
                                Faça Ler(caracter)
                        }
                        Se caracter != fim de arquivo
                            Então
                            {
                                Pega Token
                                Insere Lista
                            }
                }
            Fecha arquivo fonte
            Fim. */

        try{
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            char line[];

            for (int k = 0;in.ready();k++) {
               line = in.readLine().toCharArray();
               int i;
               for(i=0;i<line.length;i++) {
                    if((line[i]=='{' || Character.isSpaceChar(line[i])) && i<=line.length)
                    {
                        if(line[i]=='{')
                        {
                            while (line[i] != '}' && i<=line.length)
                                i++;
                            i++;
                        }
                        while(Character.isSpaceChar(line[i]) && i<=line.length)
                            i++;
                   }
               }
               if(i<=line.length)
               {
                   Token tk = null;
                   String msg = pegaToken(line,i,tk);
                   if(tk!=null)
                        tokens.add(tk);
                   else
                       return "Analisador lexico: Linha " + k + ": " + msg;
               }
                   
            }
            in.close();

        } catch(Exception ex)
        {
            return "I/O: Erro ao ler o arquivo";
        }

        return null; //sem mensagem de erro

    }

    public String pegaToken(char[] array,int i,Token tk) {
    /*Inicio
        Se caracter é digito
        Então Trata Digito
        Senão Se caracter é letra
              Então Trata Identificador e Palavra Reservada
              Senão Se caracter = “:”
                    Então Trata Atribuição
                    Senão Se caracter Î {+,-,*}
                          Então Trata Operador Aritmético
                          Senão Se caracter Î {<,>,=}
                                Então TrataOperadorRelacional
                                Senão Se caracter Î {; , ( ) .}
                                      Então Trata Pontuação
                                      Senão ERRO
    Fim.*/


        if(Character.isDigit(array[i]))
            return trataDigito(array,i,tk);
        if(Character.isLetter(array[i]))
            return trataIdentPalavraReservada(array,i,tk);
        if(array[i]==':')
            return trataAtribuicao(array,i,tk);
        if(array[i]=='+' || array[i]=='-' || array[i]=='*')
            return trataOpAritmetico(array,i,tk);
        if(array[i]=='<' || array[i]=='>' || array[i]=='=')
            return trataOpRelacional(array,i,tk);
        if(array[i]==';' || array[i]==',' || array[i]=='(' || array[i]==')' || array[i]=='.')
            return trataPontuacao(array,i,tk);
        
        return "Caracter invalido '" + array[i] + "'.";

    }

    public String trataDigito(char[] array, int i,Token tk) {
        /*Def num : Palavra
            Inicio
                num+=caracter
                Ler(caracter)
                Enquanto caracter é dígito
                Faça
                {
                    num += caracter
                    Ler(caracter)
                }
                token.símbolo = snúmero
                token.lexema = num
            Fim.*/

        String num = "";
        num+=array[i];
        i++;
        while(Character.isDigit(array[i]))
        {
            num+=array[i];
            i++;
        }
        tk = new Token(num, "snumero");

        return null; //nao tem mensagem de ERRO

    }

    private String trataIdentPalavraReservada(char[] array, int i, Token tk) {
    /*Def id: Palavra
      Inicio
        id = caracter
        Ler(caracter)
        Enquanto caracter é letra ou dígito ou “_”
        Faça
            { id = id + caracter
              Ler(caracter)  }
        token.lexema = id
        caso
            id = “programa” : token.símbolo ¬ sprograma
            id = “se” : token.símbolo ¬ sse
            id = “entao” : token.símbolo ¬ sentao
            id = “senao” : token.símbolo ¬ ssenao
            id = “enquanto” : token.símbolo ¬ senquanto
            id = “faca” : token.símbolo ¬ sfaca
            id = “início” : token.símbolo ¬ sinício
            id = “fim” : token.símbolo ¬ sfim
            id = “escreva” : token.símbolo ¬ sescreva
            id = “leia” :token.símbolo ¬ sleia
            id = “var” : token.símbolo ¬ svar
            id = “inteiro” : token.símbolo ¬ sinteiro
            id = “booleano” : token.símbolo ¬ sbooleano
            id = “verdadeiro” : token.símbolo ¬ sverdadeiro
            id = “falso” : token.símbolo ¬ sfalso
            id = “procedimento” : token.símbolo ¬ sprocedimento
            id = “funcao” : token.símbolo ¬ sfuncao
            id = “div” : token.símbolo ¬ sdiv
            id = “e” : token.símbolo ¬ se
            id = “ou” : token.símbolo ¬ sou
            id = “nao” : token.símbolo ¬ snao
        senão : token.símbolo ¬ sidentificador
        Fim.*/

        String id = "";
        id+=array[i];
        i++;
        while(Character.isLetterOrDigit(array[i]) || array[i]=='_')
        {
            id+=array[i];
            i++;
        }

        tk = new Token();
        tk.setLexema(id);

        if(id.equals("programa"))
            tk.setSimbolo("sPrograma");
        else if (id.equals("se"))
            tk.setSimbolo("sSe");
        //...


        else
            tk.setSimbolo("sIdentificador");

        return null; // não tem mensagem de ERRO
        
    }

    private String trataAtribuicao(char[] array, int i, Token tk) {

        //conferir no caderno
        //mas eh soh
        //pegar proximo token: i++
        //se for '=', entao lexema=":="; simbolo="sAtribuicao"
        //senao, erro na atribuicao

        String atrib = "";
        atrib+=array[i];
        i++;
        
        if(array[i]!='=')
            return "Erro de atribuicao, '=' era esperado.";

        atrib+=array[i];
        tk.setLexema(atrib);
        tk.setSimbolo("sAtribuicao");
        
        return null; //sem mensagem de erro;
    }

    private String trataOpAritmetico(char[] array, int i, Token tk) {
        //if else vendo qual operador é
        //tk.lexema = "+" ou "-" ou "*"
        //tk.simbolo = "sMais" ou "sMenos" ou "sMult"

        String operador="";
        operador+=array[i];

        if(array[i]=='+')
            tk = new Token(operador, "sMais");
        else if(array[i]=='-')
            tk = new Token(operador, "sMenos");
        else if (array[i]=='*')
            tk = new Token(operador, "sMult");

        return null; // não tem mensagem de ERRO
    }

    private String trataOpRelacional(char[] array, int i, Token tk) {

        //mesma coisa q o aritmetico
        return null;
    }

    private String trataPontuacao(char[] array, int i, Token tk) {

        //mesma coisa que os operadores, soh que com os caracteres de pontuacao
        return null;
    }

}
