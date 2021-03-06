package br.com.dbcorp.escolaMinisterio.action;

import java.awt.Event;

import javax.swing.Icon;

import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.designacao.historico.DesignacoesUI;

public class HistoricoDesignacaoAction extends Action {
	private static final long serialVersionUID = 3808359432424184503L;
	
	public static final String NAME_VALUE = "Historico de Designa��es";

	//Tooltip
	public static final String LONG_DESCRIPTION_VALUE = "Historico de Designa��es";

	//Atalho do menu
	public static final int KEY_STROKE_MODIFIERS = Event.KEY_ACTION;

	//�cone do Menu
	public static Icon SMALL_ICON_VALUE = null;

	//Descri��o do menu
	public static final String SHORT_DESCRIPTION_VALUE = LONG_DESCRIPTION_VALUE;
	
	public HistoricoDesignacaoAction(MainFrame mainFrame) {
		super(NAME_VALUE, mainFrame);
		
		putValue(LONG_DESCRIPTION, LONG_DESCRIPTION_VALUE);
		putValue(SMALL_ICON, SMALL_ICON_VALUE);
		putValue(SHORT_DESCRIPTION, SHORT_DESCRIPTION_VALUE);
		
		this.internalFrameClass = DesignacoesUI.class;
	}
}