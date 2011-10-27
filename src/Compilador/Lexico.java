package Compilador;

import Compilador.Structures.Token;
import Compilador.Exceptions.AnaliseLexicaException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Hashtable;
import javax.swing.JOptionPane;

public class Lexico {

    private File sourceFile;
    private ArrayList<Token> tokens;
    private int n_token = -1; //contador para percorrer a lista de tokens
    private BufferedReader in;
    private int n_line = -1; //contador de linhas

    public int getN_line() {
        return n_line;
    }
    private int i; //posicao na linha
  
    public Lexico(File source) {
       sourceFile = source;
       try{
           in = new BufferedReader(new FileReader(sourceFile));
       } catch(Exception ex)
       {
           JOptionPane.showMessageDialog(null, "I/O: Erro ao ler o arquivo.", "Erro!", JOptionPane.ERROR_MESSAGE);
       }
            
    }

    public Token token() throws Exception{
        n_token++;
        
        if(tokens!=null && n_token<tokens.size())
            return tokens.get(n_token);
        else
        {   n_token = 0; 
            fileToTokenList(); 
           return tokens.get(n_token);
                
        }
      //  return null;
    }

    public void fileToTokenList() throws Exception
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

       
            char line[];

            if(in.ready()) {
               tokens = new ArrayList<Token>();
              // tokenLines = new Hashtable<Token, Integer>();
               n_line++;
               line = in.readLine().toCharArray();
           //    int i;
               for(i=0;i<line.length;i++) {
                    if(i<line.length && (line[i]=='{' || Character.isSpaceChar(line[i])))
                    {
                        if(line[i]=='{')
                        {
                            while (i<line.length && line[i] != '}')
                                i++;
                            i++;
                        }
                        while(i<line.length && Character.isSpaceChar(line[i]))
                            i++;
                   }
               
                   if(i<line.length)
                   {
                       Token tk = pegaToken(line,i);                  
                       tokens.add(tk);
                      // tokenXlinhas.put(tk, n_line);
                   }
               }    
            }
            in.close();


        //return null; //sem mensagem de erro

    }

    public Token pegaToken(char[] array,int i) throws Exception {
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
            return trataDigito(array); //não tem msg de erro
        if(Character.isLetter(array[i]))
            return trataIdentPalavraReservada(array); //nao tem msg de erro
        if(array[i]==':')
            return trataAtribuicao(array);
        if(array[i]=='+' || array[i]=='-' || array[i]=='*')
            return trataOpAritmetico(array);   
        if(array[i]=='<' || array[i]=='>' || array[i]=='=')
            return trataOpRelacional(array);
        if(array[i]==';' || array[i]==',' || array[i]=='(' || array[i]==')' || array[i]=='.')
            return trataPontuacao(array);
        
        throw new AnaliseLexicaException(n_line, "Caracter invalido '" + array[i] + "'.");

    }

    public Token trataDigito(char[] array){
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
        
        while(Character.isDigit(array[i]))
        {
            num+=array[i];
            i++;
        }
        return new Token(num, "sNumero");

        //sem Throws pq nao tem mensagem de ERRO

    }

    private Token trataIdentPalavraReservada(char[] array) {
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
     
        while(Character.isLetterOrDigit(array[i]) || array[i]=='_')
        {
            id+=array[i];
            i++;
        }

        Token tk = new Token();
        tk.setLexema(id);

        if(id.equals("programa"))
            tk.setSimbolo("sPrograma");
        else if (id.equals("se"))
            tk.setSimbolo("sSe");
        else if (id.equals("entao"))
            tk.setSimbolo("sEntao");
        else if (id.equals("senao"))
            tk.setSimbolo("sSenao");
        else if (id.equals("enquanto"))
            tk.setSimbolo("sEnquanto");
        else if (id.equals("faca"))
            tk.setSimbolo("sFaca");
        else if (id.equals("inicio"))
            tk.setSimbolo("sInicio");
        else if (id.equals("fim"))
            tk.setSimbolo("sFim");
        else if (id.equals("escreva"))
            tk.setSimbolo("sEscreva");
        else if (id.equals("leia"))
            tk.setSimbolo("sLeia");
        else if (id.equals("var"))
            tk.setSimbolo("sVar");
        else if (id.equals("inteiro"))
            tk.setSimbolo("sInteiro");
        else if (id.equals("booleano"))
            tk.setSimbolo("sBooleano");
        else if (id.equals("verdadeiro"))
            tk.setSimbolo("sVerdadeiro");
        else if (id.equals("falso"))
            tk.setSimbolo("sFalso");
        else if (id.equals("procedimento"))
            tk.setSimbolo("sProcedimento");
        else if (id.equals("funcao"))
            tk.setSimbolo("sFuncao");
        else if (id.equals("div"))
            tk.setSimbolo("sDiv");
        else if (id.equals("e"))
            tk.setSimbolo("sE");
        else if (id.equals("ou"))
            tk.setSimbolo("sOu");
        else if (id.equals("nao"))
            tk.setSimbolo("sNao");
        else
            tk.setSimbolo("sIdentificador");
        
        return tk; // não tem mensagem de ERRO
        
    }

    private Token trataAtribuicao(char[] array) {

        //conferir no caderno
        //mas eh soh
        //pegar proximo token: i++
        //se for '=', entao lexema=":="; simbolo="sAtribuicao"
        //senao, erro na atribuicao

        String atrib = "";
        atrib+=array[i];
        i++;
        
        if(array[i]!='=')
        { 
            i--;
           Token tk = new Token(atrib, "sDoisPontos");
           return tk;
        }
        
        atrib+=array[i];
        Token tk = new Token();
        tk.setLexema(atrib);
        tk.setSimbolo("sAtribuicao");
        
        return tk; //sem mensagem de erro;
    }

    private Token trataOpAritmetico(char[] array) {
        //if else vendo qual operador é
        //tk.lexema = "+" ou "-" ou "*"
        //tk.simbolo = "sMais" ou "sMenos" ou "sMult"

        Token tk = null;
        String operador="";
        operador+=array[i];

        if(array[i]=='+')
            tk = new Token(operador, "sMais");
        else if(array[i]=='-')
            tk = new Token(operador, "sMenos");
        else if (array[i]=='*')
            tk = new Token(operador, "sMult");

        return tk; // não tem mensagem de ERRO
    }

    private Token trataOpRelacional(char[] array) throws Exception{

        //mesma coisa q o aritmetico
        String opRelacional="";
        Token tk = null;
        opRelacional += array[i];

        if (array[i] == '!') {         // diferente 
            i++;
            if (array[i] == '=')
                tk = new Token(opRelacional, "sDif");
            else
                throw new AnaliseLexicaException(n_line, "Caracter invalido '!'.");
            
        } else if (array[i] == '=')  //igual
            tk = new Token(opRelacional, "sIgual");
        
        else if (array[i] == '<') {        // <= ou <
            i++;
            if (array[i] == '=')
                tk = new Token(opRelacional, "sMenorIgual");
            else {
                i--;
                tk = new Token(opRelacional, "sMenor");
            }
        } else if (array[i] == '>') {    // >= ou >
            i++;
            if (array[i] == '=') 
                
                tk = new Token(opRelacional, "sMaiorIgual");
            
            else {
                i--;
                tk = new Token(opRelacional, "sMaior");
            }
        }

        return tk;


    }

    private Token trataPontuacao(char[] array) {

        //mesma coisa que os operadores, soh que com os caracteres de pontuacao
        String pontuacao="";
        pontuacao+=array[i];
        Token tk = null;
        if(array[i]==';')
            tk = new Token(pontuacao, "sPontoVirgula");
        else if(array[i]==',')
            tk = new Token(pontuacao, "sVirgula");
        else if (array[i]=='(')
            tk = new Token(pontuacao, "sAbreParenteses");
        else if(array[i]==')')
            tk = new Token(pontuacao, "sFechaParenteses");
        else if (array[i]=='.')
            tk = new Token(pontuacao, "sPonto");
        
        return tk;
    }

}
