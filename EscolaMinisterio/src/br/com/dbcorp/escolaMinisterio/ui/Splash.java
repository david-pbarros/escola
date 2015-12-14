package br.com.dbcorp.escolaMinisterio.ui;

import static br.com.dbcorp.escolaMinisterio.Params.getAppPath;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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

	private static Path logPath = Paths.get(getAppPath() + File.separator + "Log");
	
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
			System.out.println(e);
		}
	    
	    new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					verificarInicializacao();
					
					DataBaseHelper.find(Estudo.class, 1);//soh pra carregar o banco
					
					Thread.sleep(500);

					new LogonDialog(splash).setVisible(true);
						
				} catch (Exception e) {
					System.out.println(e);
					
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
	
	public static void verificarInicializacao() throws IOException {
		Path iniPath = Paths.get(getAppPath() + File.separator + "escola.ini");
		
		try {
			Files.createFile(iniPath);
			preencheIni(Files.newBufferedWriter(iniPath, StandardOpenOption.APPEND));
			
			JOptionPane.showMessageDialog(null, "Reinicie o aplicativo.", "", JOptionPane.INFORMATION_MESSAGE);
			
			System.exit(0);
			
		} catch (FileAlreadyExistsException faex) {
			System.out.println("Arquivo ini j� existente.");
		}
		
		try {
			Files.createDirectory(logPath);

		} catch (FileAlreadyExistsException faex) {
			System.out.println("Diretorio de Log j� existente.");
		}
	}
	
	private static void preencheIni(BufferedWriter bw) throws IOException {
		bw.write("[DATABASE]");
		bw.newLine();
		bw.write("javax.persistence.jdbc.url=jdbc:sqlite:");
		
		String db = JOptionPane.showInputDialog("Caminho do banco de dados:", getAppPath() + File.separator + "escola.db");
		
		bw.write(db.replace("\\", "/"));
		bw.newLine();
		bw.write("eclipselink.ddl-generation=create-or-extend-tables");
		bw.newLine();
		bw.write("eclipselink.ddl-generation.output-mode=database");
		bw.newLine();
		bw.write("javax.persistence.jdbc.driver=org.sqlite.JDBC");
		bw.newLine();
		bw.write("javax.persistence.jdbc.user=sa");
		bw.newLine();
		bw.write("javax.persistence.jdbc.password=");
		bw.newLine();
		bw.write("[LOG]");
		bw.newLine();
		bw.write("logType=DEBUG");
		bw.newLine();
		bw.write("logPath=");
		
		String log = JOptionPane.showInputDialog("Caminho do diretorio de logs:", logPath.toString());
		
		bw.write(log.replace("\\", "/"));
		
		bw.newLine();
		bw.write("[SINCRONISMO]");
		bw.newLine();
		bw.write("server=");
		bw.write(JOptionPane.showInputDialog("URL do servidor de sincronismo:", "escolaministerio.jwdbcorp.dx.am"));
		bw.newLine();
		bw.write("congregacao=");
		bw.write(JOptionPane.showInputDialog("N�mero da congrega��o:", "11111"));
		bw.newLine();
		bw.write("hash=");
		bw.write(JOptionPane.showInputDialog("Chave p�blica:", "LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0NCk1JR2ZNQTBHQ1NxR1NJYjNEUUVCQVFVQUE0R05BRENCaVFLQmdRQzNwbVVNVVEvNDRvN3h2TDJIUmhWUC8ycVYNCkEvTkRCRGZGdENrbFJldU1iTGNRa1k1UlVqU05JaFBZdlFpN3V3dG52NUdWZ1RaK1BreU55UmdPdnUvTGlhKysNCm4yeFJLMDhma05xdkxNR2trZFg0VWo5Q0V5U2hsNEFGRXZCeVpDTjFiOU52cGVWVzJ5dmY5eUl1eXVtUjV2SjgNCmxMbXVPSXZQZmpHTkkvUkJQd0lEQVFBQg0KLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t"));
		bw.flush();
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