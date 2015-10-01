package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;


import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;


import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.DataBaseHelper;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;
import br.com.dbcorp.escolaMinisterio.ui.dialog.LogonDialog;

public class Splash extends JWindow {
	private static final long serialVersionUID = -7952839849461887170L;

	private Log log = Log.getInstance();
	
	private int width;
	private int height;
	final int widthScreen;
	final int heightScreen;
	
	public Splash(int width, int height) {
		final Splash splash = this;
		this.widthScreen = width;
		this.heightScreen = height;
		
		Image img = Params.splashImage();
		
		this.width = img.getWidth(null) - 139;
		this.height = img.getHeight(null);
		
		Dimension size = new Dimension(this.width, this.height);
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    
	    this.centerScreen();
	    try {
	    	getContentPane().add(new JPanelWithBackground(img));
	    	
		} catch (IOException e) {
			this.log.error("", e);
		}
	    
	    new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					DataBaseHelper.find(Estudo.class, 1);//soh pra carregar o banco
					
					Thread.sleep(500);

					new LogonDialog(splash).setVisible(true);
						
				} catch (Exception e) {
					log.error("", e);
					
					JOptionPane.showMessageDialog(splash, "Erro ao iniciar o aplicativo.", "", JOptionPane.ERROR_MESSAGE);
					
					splash.dispose();
				}
				
			}
		}).start();
	}
	
	public void continuar(boolean continua) {
		if (continua) {
			try {
				new MainFrame(this.widthScreen, this.heightScreen, this).setVisible(true);
				
				new Thread(()->{new Sincronizador().verificaVersao();}).start();
				
			} catch (Exception e) {
				Log.getInstance().error("Erro desconhecido.", e);
			}
		
		} else {
			dispose();
		}
	}
		
	private void centerScreen() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = dimension.width - this.width;
		int h = dimension.height - this.height;
		setLocation(w/2, h/2);
	}
}

class JPanelWithBackground extends JPanel {
	private static final long serialVersionUID = -3737552790292607704L;
	
	private Image backgroundImage;
	
	public JPanelWithBackground(Image img) throws IOException {
		this.backgroundImage = img;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.backgroundImage, -69, 0, this);
	}
}