package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Event;

import javax.swing.Icon;

import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.seguranca.ProfilesUI;

public class ProfileAction extends Action {
	private static final long serialVersionUID = -206618326998913902L;

	public static final String NAME_VALUE = "Perfis";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = "Perfis";

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//Ícone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descrição do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public ProfileAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = ProfilesUI.class;
	}
}