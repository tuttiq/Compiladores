package Compilador;

import Compilador.Structures.Token;
import Compilador.Exceptions.AnaliseLexicaException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JOptionPane;

public class Lexico {

    private File sourceFile;
    private int n_line = 0; //contador para guardar o numero da linha
    private BufferedReader in;
    private char caracter;
    
  
    public Lexico(File source) {
       sourceFile = source;
       try{
           in = new BufferedReader(new FileReader(sourceFile));
           caracter = (char) in.read();
       } catch(Exception ex)
       {
           JOptionPane.showMessageDialog(null, "I/O: Erro ao ler o arquivo.", "Erro!", JOptionPane.ERROR_MESSAGE);
       }
       
            
    }

    
    public Token token() throws Exception
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

        if(caracter=='\n')
        {   n_line++;
            caracter = (char) in.read();
        }
        if( (int)caracter != -1) { 

            while( (int)caracter!=-1 && (caracter=='{' || Character.isSpaceChar(caracter)) )
                {
                    if(caracter=='{')
                    {
                        while ( (int)caracter!=-1 && caracter!='}') 
                        {   
                            caracter = (char) in.read();
                        }
                        caracter = (char) in.read();
                    }
                    while( (int)caracter!=-1 && (Character.isSpaceChar(caracter) || caracter=='\n')) 
                        caracter = (char) in.read();
               }
               if( (int)caracter != -1) 
               {
                   Token tk = pegaToken();
                   return tk;
               }
        }    
            
           return null;
           

    }

    private Token pegaToken() throws Exception {
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


        if(Character.isDigit(caracter))
            return trataDigito();
        if(Character.isLetter(caracter))
            return trataIdentPalavraReservada();
        if(caracter==':')
            return trataAtribuicao();
        if(caracter=='+' || caracter=='-' || caracter=='*')
            return trataOpAritmetico();   
        if(caracter=='<' || caracter=='>' || caracter=='=')
            return trataOpRelacional();
        if(caracter==';' || caracter==',' || caracter=='(' || caracter==')' || caracter=='.')
            return trataPontuacao();
        
        throw new AnaliseLexicaException(n_line, "Caracter invalido '" + caracter + "'.");

    }

    private Token trataDigito() throws Exception{
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
        
        while(Character.isDigit(caracter))
        {
            num+=caracter;
            caracter = (char) in.read();
        }
        
        return new Token(num, "sNumero");

    }

    private Token trataIdentPalavraReservada() throws Exception {
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
     
        while(Character.isLetterOrDigit(caracter) || caracter=='_')
        {
            id+=caracter;
            caracter = (char) in.read();
        }
        

        if(id.equals("programa"))
            return new Token(id,"sPrograma");
        else if (id.equals("se"))
            return new Token(id,"sSe");
        else if (id.equals("entao"))
            return new Token(id,"sEntao");
        else if (id.equals("senao"))
            return new Token(id,"sSenao");
        else if (id.equals("enquanto"))
            return new Token(id,"sEnquanto");
        else if (id.equals("faca"))
            return new Token(id,"sFaca");
        else if (id.equals("inicio"))
            return new Token(id,"sInicio");
        else if (id.equals("fim"))
            return new Token(id,"sFim");
        else if (id.equals("escreva"))
            return new Token(id,"sEscreva");
        else if (id.equals("leia"))
            return new Token(id,"sLeia");
        else if (id.equals("var"))
            return new Token(id,"sVar");
        else if (id.equals("inteiro"))
            return new Token(id,"sInteiro");
        else if (id.equals("booleano"))
            return new Token(id,"sBooleano");
        else if (id.equals("verdadeiro"))
            return new Token(id,"sVerdadeiro");
        else if (id.equals("falso"))
            return new Token(id,"sFalso");
        else if (id.equals("procedimento"))
            return new Token(id,"sProcedimento");
        else if (id.equals("funcao"))
            return new Token(id,"sFuncao");
        else if (id.equals("div"))
            return new Token(id,"sDiv");
        else if (id.equals("e"))
            return new Token(id,"sE");
        else if (id.equals("ou"))
            return new Token(id,"sOu");
        else if (id.equals("nao"))
            return new Token(id,"sNao");
        else
            return new Token(id,"sIdentificador");
        
    }

    private Token trataAtribuicao() throws Exception {

        //conferir no caderno
        //mas eh soh
        //pegar proximo token: i++
        //se for '=', entao lexema=":="; simbolo="sAtribuicao"
        //senao, erro na atribuicao

        String atrib = "";
        atrib+=caracter;
        caracter = (char) in.read();
        
        if(caracter!='=')
           return new Token(atrib, "sDoisPontos");
        
        atrib+=caracter;
        caracter = (char) in.read();
        return new Token(atrib,"sAtribuicao");
        
    }

    private Token trataOpAritmetico() throws Exception {
        //if else vendo qual operador é
        //tk.lexema = "+" ou "-" ou "*"
        //tk.simbolo = "sMais" ou "sMenos" ou "sMult"

        String operador="";
        operador+=caracter;
        caracter = (char) in.read();
        
        if(operador.equals("+"))
            return new Token(operador, "sMais");
        else if(operador.equals("-"))
            return new Token(operador, "sMenos");
        else
            return new Token(operador, "sMult");

    }

    private Token trataOpRelacional() throws Exception{

        //mesma coisa q o aritmetico
        String opRelacional="";
        opRelacional += caracter;            
        caracter = (char) in.read();
        
        if (opRelacional.equals("!")) {         // diferente 

            if (caracter == '=')
            {
                opRelacional+=caracter;
                caracter = (char) in.read();
                return new Token(opRelacional, "sDif");
            }               
            else
                throw new AnaliseLexicaException(n_line, "caracter invalido '!'.");
            
        } else if (opRelacional.equals("="))  //igual
            return new Token(opRelacional, "sIgual");
        
        else if (opRelacional.equals("<")) {        // <= ou <
            
            if (caracter == '=')
            {   
                opRelacional+=caracter;
                caracter = (char) in.read();
                return new Token(opRelacional, "sMenorIgual");
            }
            else
                return new Token(opRelacional, "sMenor");
            
        } else {    // >= ou >
            
            if (caracter == '=') 
            {
                opRelacional+=caracter;
                caracter = (char) in.read();
                return new Token(opRelacional, "sMaiorIgual");
            }
            
            else 
                return new Token(opRelacional, "sMaior");
        }
        
    }

    private Token trataPontuacao() throws Exception {

        //mesma coisa que os operadores, soh que com os caracteres de pontuacao
        String pontuacao="";
        pontuacao+=caracter;
        caracter = (char) in.read();
        
        if(pontuacao.equals(";"))
            return new Token(pontuacao, "sPontoVirgula");
        else if(pontuacao.equals(","))
            return new Token(pontuacao, "sVirgula");
        else if (pontuacao.equals("("))
            return new Token(pontuacao, "sAbreParenteses");
        else if(pontuacao.equals(")"))
            return new Token(pontuacao, "sFechaParenteses");
        else
            return new Token(pontuacao, "sPonto");
        
    }
    
    
    public int getN_line() {
        return n_line;
    }

}
