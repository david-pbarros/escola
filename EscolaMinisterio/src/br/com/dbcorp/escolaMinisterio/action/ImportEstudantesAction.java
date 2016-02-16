package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Cursor;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;

import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.DataBaseHelper;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.ui.EstudantesUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ImportEstudantesAction extends Action {
	private static final long serialVersionUID = -6984594985658043194L;
	
	private Log log = Log.getInstance();
	
	public static final String NAME_VALUE = "Estudantes";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = NAME_VALUE;

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//Ícone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descrição do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public ImportEstudantesAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = EstudantesUI.class;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		if (Params.isOnLineMode()) {
			JFileChooser fileChooser = new JFileChooser();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
			fileChooser.setFileFilter(filter);
			
			if (fileChooser.showOpenDialog(this.mainFrame) == JFileChooser.APPROVE_OPTION) {
				this.mainFrame.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				FileReader fr = null;
				BufferedReader bf = null;
				
				try {
					fr = new FileReader(fileChooser.getSelectedFile());
					bf = new BufferedReader(fr);
				
					String line = bf.readLine();
					String[] campos = line.split(";");
					
					if ( !campos[0].equalsIgnoreCase("nome") || !campos[1].equalsIgnoreCase("genero") 
							|| !campos[2].equalsIgnoreCase("sala") || !campos[3].equalsIgnoreCase("data ultima designacao") ) {
						
						JOptionPane.showMessageDialog(this.mainFrame, "Arquivo inválido.", "Erro", JOptionPane.WARNING_MESSAGE);
						
					} else {
						while ((line = bf.readLine()) != null) {
							campos = line.split(";");
							
							try {
								Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.nome = :nome");
								query.setParameter("nome", campos[0]);
								query.getSingleResult();
								
							} catch (NoResultException exception) {
								Estudante estudante = new Estudante();
								estudante.setNome(campos[0]);
								estudante.setGenero(Genero.valueOf(campos[1].toUpperCase()));
								
								if ( campos.length >= 3 && !"".equals(campos[2]) ) {
									estudante.setSalaUltimaDesignacao(campos[2].charAt(0));
								}
								
								if (campos.length == 4) {
									estudante.setUltimaDesignacao(LocalDate.parse(campos[3], Params.dateFormate()));
								}
								
								DataBaseHelper.persist(estudante);
							}
						}
	
						if ( !this.isNaoAberta() ) {
							EstudantesUI ui = (EstudantesUI) this.internalFrame;
							ui.reset();
						}
						
						JOptionPane.showMessageDialog(this.mainFrame, "Processo de importação finalizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (Exception ex) {
					this.log.error("", ex);
					
					JOptionPane.showMessageDialog(this.mainFrame, "Erro no processo de importação.", "Erro", JOptionPane.ERROR_MESSAGE);
					
				} finally {
					try {
						if (bf != null) {
							bf.close();
						}
						
						if (fr != null) {
							fr.close();
						}
					} catch (Exception ex) {
						this.log.error("", ex);
					}
					
					this.mainFrame.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}
		} else {
			JOptionPane.showMessageDialog(this.mainFrame, "Processo desabilitado por não estar online.", "Informação", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
