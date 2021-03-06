package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Event;

import javax.swing.Icon;

import br.com.dbcorp.escolaMinisterio.ui.ConfigUI;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;

public class ConfigAction extends Action {
	private static final long serialVersionUID = -206618326998913902L;

	public static final String NAME_VALUE = "Configura��es";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = NAME_VALUE;

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//�cone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descri��o do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public ConfigAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);

		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = ConfigUI.class;
	}
}