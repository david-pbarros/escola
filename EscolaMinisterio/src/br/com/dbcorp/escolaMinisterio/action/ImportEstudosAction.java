package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Cursor;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.FileReader;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.DataBaseHelper;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.ui.EstudosUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ImportEstudosAction extends Action {
	private static final long serialVersionUID = -6984594985658043194L;
	
	private Log log = Log.getInstance();
	
	public static final String NAME_VALUE = "Estudos";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = "Pontos de estudo";

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//Ícone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descrição do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public ImportEstudosAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = EstudosUI.class;
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
					
					if ( !campos[0].equalsIgnoreCase("nrEstudo") || !campos[1].equalsIgnoreCase("descricao") || !campos[2].equalsIgnoreCase("leitura") 
							|| !campos[3].equalsIgnoreCase("demonstracao") || !campos[4].equalsIgnoreCase("discurso") ) {
						
						JOptionPane.showMessageDialog(this.mainFrame, "Arquivo inválido.", "Erro", JOptionPane.WARNING_MESSAGE);
						
					} else {
						while ((line = bf.readLine()) != null) {
							campos = line.split(";");
							
							int nrEstudo = Integer.parseInt(campos[0]);
							
							if (DataBaseHelper.find(Estudo.class, nrEstudo) == null) {
								Estudo estudo = new Estudo();
								estudo.setNrEstudo(nrEstudo);
								estudo.setDescricao(campos[1]);
								estudo.setLeitura("S".equalsIgnoreCase(campos[2]) ? true : false);
								estudo.setDemonstracao("S".equalsIgnoreCase(campos[3]) ? true : false);
								estudo.setDiscurso("S".equalsIgnoreCase(campos[4]) ? true : false);
								
								DataBaseHelper.persist(estudo);
							}
						}
						
						if ( !this.isNaoAberta() ) {
							EstudosUI ui = (EstudosUI) this.internalFrame;
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
