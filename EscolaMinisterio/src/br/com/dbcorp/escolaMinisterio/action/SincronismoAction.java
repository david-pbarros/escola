package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.JOptionPane;

import br.com.dbcorp.escolaMinisterio.ui.AjudantesUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.dialog.SincDialog;

public class SincronismoAction extends Action {
	private static final long serialVersionUID = -1900363790893943261L;

	public static final String NAME_VALUE = "Sincronizar";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = "Sincronizar com Web";

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//Ícone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descrição do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public SincronismoAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = AjudantesUI.class;
	}
	
	public void actionPerformed(ActionEvent arg0) {
		int response = JOptionPane.showConfirmDialog(null, "Após o sincronismo o sistema terá de ser reiniciado. Continuar?", "Sincronizar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (response == JOptionPane.YES_OPTION) {
			new SincDialog(SincDialog.GERAL).setVisible(true);
		}
	}
}