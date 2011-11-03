package Compilador.Structures;

public class Simbolo {
	
		public static final int Programa = 0;
		public static final int Se = 1;
		public static final int Entao = 2;
		public static final int Senao = 3;
		public static final int Enquanto = 4;
		public static final int Faca = 5;
		public static final int Inicio = 6;
		public static final int Fim = 7;
		public static final int Escreva = 8;
		public static final int Leia = 9;
		public static final int Var = 10;
		public static final int Inteiro = 11;
		public static final int Booleano = 12;
		public static final int Verdadeiro = 13;
		public static final int Falso = 14;
		public static final int Procedimento = 15;
		public static final int Funcao = 16;
		public static final int Divisao = 17;
		public static final int E = 18;
		public static final int Ou = 19;
		public static final int Nao = 20;
		public static final int Identificador = 21;
		public static final int Numero = 22;
		public static final int DoisPontos = 23;
		public static final int Atribuicao = 24;
		public static final int Mais = 25;
		public static final int Menos = 26;
		public static final int Multiplicacao = 27;
		public static final int Diferente = 28;
		public static final int Igual = 29;
		public static final int MenorIgual = 30;
		public static final int Menor = 31;
		public static final int MaiorIgual = 32;
		public static final int Maior = 33;
		public static final int PontoVirgula = 34;
		public static final int Virgula = 35;
		public static final int AbreParenteses = 36;
		public static final int FechaParenteses = 37;
		public static final int Ponto = 38;

        private String tipo;
        private String lexema;
        private boolean novo_escopo;
        private String end_memoria;

        public Simbolo() {
        }

        public Simbolo(String tipo, String lexema, boolean novo_escopo, String end_memoria) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.novo_escopo = novo_escopo;
            this.end_memoria = end_memoria;
        }

        public void setEnd_memoria(String end_memoria) {
            this.end_memoria = end_memoria;
        }

        public void setLexema(String lexema) {
            this.lexema = lexema;
        }

        public void setNovo_escopo(boolean novo_escopo) {
            this.novo_escopo = novo_escopo;
        }

        public void setTipo(String tipo) {
            this.tipo = tipo;
        }

        public String getEnd_memoria() {
            return end_memoria;
        }

        public String getLexema() {
            return lexema;
        }

        public boolean isNovo_escopo() {
            return novo_escopo;
        }

        public String getTipo() {
            return tipo;
        }
}
