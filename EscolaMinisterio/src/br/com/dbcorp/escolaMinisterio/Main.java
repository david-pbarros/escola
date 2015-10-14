package br.com.dbcorp.escolaMinisterio;

import java.awt.EventQueue;

import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;
import br.com.dbcorp.escolaMinisterio.ui.Splash;

public class Main {

	public static int width = 1024;
	public static int height = 768;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Splash splash = new Splash(Main.width, Main.height);
				splash.setVisible(true);
			}
		});
		
		new Thread(()->{new Sincronizador().verificaSinc();}).start();
	}
}