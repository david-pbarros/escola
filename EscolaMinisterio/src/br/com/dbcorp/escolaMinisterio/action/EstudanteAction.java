package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Event;

import javax.swing.Icon;

import br.com.dbcorp.escolaMinisterio.ui.EstudantesUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;

public class EstudanteAction extends Action {
	private static final long serialVersionUID = -1900363790893943261L;

	public static final String NAME_VALUE = "Estudantes";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = "Estudantes da Escola";

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//Ícone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descrição do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public EstudanteAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = EstudantesUI.class;
	}
}