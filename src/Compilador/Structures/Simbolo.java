package Compilador.Structures;

public class Simbolo {

        private int tipo;
        private String lexema;
        private boolean novo_escopo;
        private String end_memoria;

        public Simbolo() {
        }

        public Simbolo(int tipo, String lexema, boolean novo_escopo, String end_memoria) {
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

        public void setTipo(int tipo) {
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

        public int getTipo() {
            return tipo;
        }
}
