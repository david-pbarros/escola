package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.Image;
import java.net.URL;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;


public class Params {
	
	public static int WIDTH;
	public static int HEIGHT;
	public static int INTERNAL_WIDTH;
	public static int INTERNAL_HEIGHT;
	
	private static ImageIcon btNovo;
	private static ImageIcon btFechar;
	private static ImageIcon btRemover;
	private static ImageIcon btSalvar;
	private static ImageIcon btAprovar;
	private static ImageIcon btImprimir;
	private static ImageIcon btExport;
	private static ImageIcon btRegresso;
	private static ImageIcon btLupa;
	
	private static DateTimeFormatter dateFormatter;
	private static DateTimeFormatter dateTimeFormatter;
	
	public static void setDimensions(MainFrame frame) {
		WIDTH = frame.getWidth();
		HEIGHT = frame.getHeight();
		
		INTERNAL_WIDTH = (int) (WIDTH - (WIDTH * 0.02));
		INTERNAL_HEIGHT = (int) (HEIGHT - (HEIGHT * 0.15));
	}
	
	public static Image iconeAplicacao() {
		return obterImagem("/benificie-se.jpg").getImage();
	}
	
	public static ImageIcon btNovoImg() {
		if (btNovo == null) {
			btNovo = obterImagem("/newButton.png");
		}
		
		return btNovo;
	}
	
	public static ImageIcon btFecharImg() {
		if (btFechar == null) {
			btFechar = obterImagem("/closeButton.png");
		}
		
		return btFechar;
	}
	
	public static ImageIcon btRemoverImg() {
		if (btRemover == null) {
			btRemover = obterImagem("/deleteButton.png");
		}
		
		return btRemover;
	}
	
	public static ImageIcon btSalvarImg() {
		if (btSalvar == null) {
			btSalvar = obterImagem("/saveButton.png");
		}
		
		return btSalvar;
	}
	
	public static ImageIcon btAprovarImg() {
		if (btAprovar == null) {
			btAprovar = obterImagem("/check.png");
		}
		
		return btAprovar;
	}
	
	public static ImageIcon btImprimirImg() {
		if (btImprimir == null) {
			btImprimir = obterImagem("/printer.png");
		}
		
		return btImprimir;
	}
	
	public static ImageIcon btExportImg() {
		if (btExport == null) {
			btExport = obterImagem("/export.png");
		}
		
		return btExport;
	}
	
	public static ImageIcon btRegressoImg() {
		if (btRegresso == null) {
			btRegresso = obterImagem("/regresar.png");
		}
		
		return btRegresso;
	}
	
	public static ImageIcon btLupaImg() {
		if (btLupa == null) {
			btLupa = obterImagem("/lupa.png");
		}
		
		return btLupa;
	}
	
	public static Image splashImage() {
		return obterImagem("/Bebeficiese.png").getImage();
	}
	
	private static ImageIcon obterImagem(String caminho) {
		URL url = Params.class.getResource(caminho);

		if (url == null) {
			url = Params.class.getResource("/resources" + caminho);
		}
		
		return new ImageIcon(url);
	}
	
	public static DateTimeFormatter dateFormate() {
		if (dateFormatter == null) {
			dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		}
		
		return dateFormatter;
	}
	
	public static DateTimeFormatter dateTimeFormate() {
		if (dateTimeFormatter == null) {
			dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		}
		
		return dateTimeFormatter;
	}
}