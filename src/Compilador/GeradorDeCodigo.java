package Compilador;

import java.io.File;
import java.util.Formatter;

import Compilador.Constants.Comandos;

public class GeradorDeCodigo {
	
	private File arquivo;
	private Formatter out;
	
	public GeradorDeCodigo(File source) throws Exception {
		arquivo = new File(source.getPath() + ".obj");
		
		if(arquivo.exists())
			arquivo.delete();
		arquivo.createNewFile();
		
		out = new Formatter(arquivo);
	}
	
	public void geraLabel(int n) {
		out.format(Comandos.Label + n + "%tNULL%n");
	}
	
	public void gera(String op) {
		out.format("%t" + op + "%n");
	}

	public void gera(String op, String arg1) {
		out.format("%t" + op + "%t" + arg1 + "%n");
	}
	
        public void gera(String op, int arg1) {
		out.format("%t" + op + "%t" + Comandos.Label + arg1 + "%n");
	}
        
	public void gera(String op, String arg1, String arg2) {
		out.format("%t" + op + "%t" + arg1 + "%t" + arg2 + "%n");
	}
        
        public void gera(String op, int arg1, int arg2) {
		out.format("%t" + op + "%t" + arg1 + "%t" + arg2 + "%n");
	}
	
	public void close() {
		out.close();
	}

}
