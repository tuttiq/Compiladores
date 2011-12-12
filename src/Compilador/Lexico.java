package Compilador;

import Compilador.Constants.Simbolos;
import Compilador.Models.Token;
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
       n_line = 0;
       
            
    }

    
    public Token token() throws Exception
    {
    /* Inicio
            Abre arquivo fonte
            Ler(caracter)
            Enquanto nao acabou o arquivo fonte
                Faca
                {
                    Enquanto ((caracter = '{')ou (caracter = espaco)) e (nao acabou o arquivo fonte)
                        Faca
                        {
                            Se caracter = '{'
                                Entao
                                {
                                    Enquanto (caracter != '}' ) e (nao acabou o arquivo fonte)
                                        Faca Ler(caracter)

                                    Ler(caracter)
                                }
                            Enquanto (caracter = espaco) e (nao acabou o arquivo fonte)
                                Faca Ler(caracter)
                        }
                        Se caracter != fim de arquivo
                            Entao
                            {
                                Pega Token
                                Insere Lista
                            }
                }
            Fecha arquivo fonte
            Fim. */

        while(caracter==10 || caracter==13)
        {   
            caracter = (char) in.read();
                n_line++;
            
        }
        if( (int)caracter != -1 && caracter!='\uffff') { 

            while( (int)caracter!=-1 && (caracter=='{' || caracter==8 || caracter==9 || caracter==32 ) )
            {
                if(caracter=='{')
                {
                    while ( (int)caracter!=-1 && caracter!='\uffff' && caracter!='}') 
                    {   if(caracter==10 || caracter==13)
                            n_line++;
                        caracter = (char) in.read();
                    }
                    caracter = (char) in.read();
                }
                while( (int)caracter!=-1 && caracter!='\uffff' && (caracter==8 || caracter==9 || caracter==32 || caracter==10 || caracter ==13))
                {   if(caracter== 10 || caracter ==13)
                       n_line++;
                    
                    caracter = (char) in.read();
                }
            }
           if( (int)caracter != -1 && caracter!='\uffff') 
           {
               Token tk = pegaToken();
               return tk;
           }
        }    
            
           return null;
           

    }

    private Token pegaToken() throws Exception {
    /*Inicio
        Se caracter eh digito
        Entao Trata Digito
        Senao Se caracter eh letra
              Entao Trata Identificador e Palavra Reservada
              Senao Se caracter = ':'
                    Entao Trata Atribuicao
                    Senao Se caracter = {+,-,*}
                          Entao Trata Operador Aritmehtico
                          Senao Se caracter = {<,>,=, !}
                                Entao TrataOperadorRelacional
                                Senao Se caracter = {; , ( ) .}
                                      Entao Trata Pontuacao
                                      Senao ERRO
    Fim.*/


        if(Character.isDigit(caracter))
            return trataDigito();
        if(Character.isLetter(caracter))
            return trataIdentPalavraReservada();
        if(caracter==':')
            return trataAtribuicao();
        if(caracter=='+' || caracter=='-' || caracter=='*')
            return trataOpAritmetico();   
        if(caracter=='<' || caracter=='>' || caracter=='=' || caracter=='!')
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
                Enquanto caracter eh digito
                Faca
                {
                    num += caracter
                    Ler(caracter)
                }
                token.simbolo = snumero
                token.lexema = num
            Fim.*/

        String num = "";
        
        while(Character.isDigit(caracter))
        {
            num+=caracter;
            caracter = (char) in.read();
        }
        
        return new Token(num, Simbolos.Numero);

    }

    private Token trataIdentPalavraReservada() throws Exception {
    /*Def id: Palavra
      Inicio
        id = caracter
        Ler(caracter)
        Enquanto caracter eh letra ou digito ou '_'
        Faca
            { id = id + caracter
              Ler(caracter)  }
        token.lexema = id
        caso
            id = 'programa' : token.simbolo = sprograma
            id = 'se' : token.simbolo = sse
            id = 'entao' : token.simbolo = sentao
            id = 'senao' : token.simbolo = ssenao
            id = 'enquanto' : token.simbolo = senquanto
            id = 'faca' : token.simbolo = sfaca
            id = 'inicio' : token.simbolo = sinicio
            id = 'fim' : token.simbolo = sfim
            id = 'escreva' : token.simbolo = sescreva
            id = 'leia' :token.simbolo = sleia
            id = 'var' : token.simbolo = svar
            id = 'inteiro' : token.simbolo = sinteiro
            id = 'booleano' : token.simbolo = sbooleano
            id = 'verdadeiro' : token.simbolo = sverdadeiro
            id = 'falso' : token.simbolo = sfalso
            id = 'procedimento' : token.simbolo = sprocedimento
            id = 'funcao' : token.simbolo = sfuncao
            id = 'div' : token.simbolo = sdiv
            id = 'e' : token.simbolo = se
            id = 'ou' : token.simbolo = sou
            id = 'nao' : token.simbolo = snao
        senao : token.simbolo = sidentificador
        Fim.*/

        String id = "";
     
        while(Character.isLetterOrDigit(caracter) || caracter=='_')
        {
            id+=caracter;
            caracter = (char) in.read();
        }
        

        if(id.equals("programa"))
            return new Token(id,Simbolos.Programa);
        else if (id.equals("se"))
            return new Token(id,Simbolos.Se);
        else if (id.equals("entao"))
            return new Token(id,Simbolos.Entao);
        else if (id.equals("senao"))
            return new Token(id,Simbolos.Senao);
        else if (id.equals("enquanto"))
            return new Token(id,Simbolos.Enquanto);
        else if (id.equals("faca"))
            return new Token(id,Simbolos.Faca);
        else if (id.equals("inicio"))
            return new Token(id,Simbolos.Inicio);
        else if (id.equals("fim"))
            return new Token(id,Simbolos.Fim);
        else if (id.equals("escreva"))
            return new Token(id,Simbolos.Escreva);
        else if (id.equals("leia"))
            return new Token(id,Simbolos.Leia);
        else if (id.equals("var"))
            return new Token(id,Simbolos.Var);
        else if (id.equals("inteiro"))
            return new Token(id,Simbolos.Inteiro);
        else if (id.equals("booleano"))
            return new Token(id,Simbolos.Booleano);
        else if (id.equals("verdadeiro"))
            return new Token(id,Simbolos.Verdadeiro);
        else if (id.equals("falso"))
            return new Token(id,Simbolos.Falso);
        else if (id.equals("procedimento"))
            return new Token(id,Simbolos.Procedimento);
        else if (id.equals("funcao"))
            return new Token(id,Simbolos.Funcao);
        else if (id.equals("div"))
            return new Token(id,Simbolos.Divisao);
        else if (id.equals("e"))
            return new Token(id,Simbolos.E);
        else if (id.equals("ou"))
            return new Token(id,Simbolos.Ou);
        else if (id.equals("nao"))
            return new Token(id,Simbolos.Nao);
        else
            return new Token(id,Simbolos.Identificador);
        
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
           return new Token(atrib, Simbolos.DoisPontos);
        
        atrib+=caracter;
        caracter = (char) in.read();
        return new Token(atrib,Simbolos.Atribuicao);
        
    }

    private Token trataOpAritmetico() throws Exception {
        //if else vendo qual operador eh
        //tk.lexema = "+" ou "-" ou "*"
        //tk.simbolo = "sMais" ou "sMenos" ou "sMult"

        String operador="";
        operador+=caracter;
        caracter = (char) in.read();
        
        if(operador.equals("+"))
            return new Token(operador, Simbolos.Mais);
        else if(operador.equals("-"))
            return new Token(operador, Simbolos.Menos);
        else
            return new Token(operador, Simbolos.Multiplicacao);

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
                return new Token(opRelacional, Simbolos.Diferente);
            }               
            else
                throw new AnaliseLexicaException(n_line, "caracter invalido '!'.");
            
        } else if (opRelacional.equals("="))  //igual
            return new Token(opRelacional, Simbolos.Igual);
        
        else if (opRelacional.equals("<")) {        // <= ou <
            
            if (caracter == '=')
            {   
                opRelacional+=caracter;
                caracter = (char) in.read();
                return new Token(opRelacional, Simbolos.MenorIgual);
            }
            else
                return new Token(opRelacional,Simbolos.Menor);
            
        } else {    // >= ou >
            
            if (caracter == '=') 
            {
                opRelacional+=caracter;
                caracter = (char) in.read();
                return new Token(opRelacional, Simbolos.MaiorIgual);
            }
            
            else 
                return new Token(opRelacional, Simbolos.Maior);
        }
        
    }

    private Token trataPontuacao() throws Exception {

        //mesma coisa que os operadores, soh que com os caracteres de pontuacao
        String pontuacao="";
        pontuacao+=caracter;
        caracter = (char) in.read();
        
        if(pontuacao.equals(";"))
            return new Token(pontuacao, Simbolos.PontoVirgula);
        else if(pontuacao.equals(","))
            return new Token(pontuacao, Simbolos.Virgula);
        else if (pontuacao.equals("("))
            return new Token(pontuacao, Simbolos.AbreParenteses);
        else if(pontuacao.equals(")"))
            return new Token(pontuacao, Simbolos.FechaParenteses);
        else
            return new Token(pontuacao, Simbolos.Ponto);
        
    }
    
    
    public int getN_line() {
        return n_line;
    }

}
