package Compilador.Models;

import Compilador.Constants.Comandos;

public class Simbolo {

        private int tipo;
        private String lexema;
        private boolean novoEscopo;
        private String endereco;

        public Simbolo() {
        }

        public Simbolo(int tipo, String lexema, boolean novoEscopo, String end_memoria) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.novoEscopo = novoEscopo;
            this.endereco = end_memoria;
        }

        public void setEndereco(String endereco) {
            this.endereco = endereco;
        }

        public void setEndereco(int endereco) {
            this.endereco = Comandos.Label + endereco;
        }
        
        public void setLexema(String lexema) {
            this.lexema = lexema;
        }

        public void setNovoEscopo(boolean novoEscopo) {
            this.novoEscopo = novoEscopo;
        }

        public void setTipo(int tipo) {
            this.tipo = tipo;
        }

        public String getEndereco() {
            return endereco;
        }

        public String getLexema() {
            return lexema;
        }

        public boolean isNovoEscopo() {
            return novoEscopo;
        }

        public int getTipo() {
            return tipo;
        }
}
