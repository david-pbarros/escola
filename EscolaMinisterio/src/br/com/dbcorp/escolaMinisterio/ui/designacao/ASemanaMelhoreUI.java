package br.com.dbcorp.escolaMinisterio.ui.designacao;

import java.awt.event.ActionEvent;

import br.com.dbcorp.escolaMinisterio.ui.dialog.DetalhesDialogMelhore;

public abstract class ASemanaMelhoreUI extends ASemanaUI {
	private static final long serialVersionUID = 2073066523868910958L;
	
	protected void actionDetalhes(ActionEvent event, boolean editable) {
		if (event.getSource() == this.btnDetalhes1 ) {
			new DetalhesDialogMelhore(this.designacao1, this.sala, editable).setVisible(true);
			
		} else if (event.getSource() == this.btnDetalhes2 ) {
			new DetalhesDialogMelhore(this.designacao2, this.sala, editable).setVisible(true);
			
		} else if (event.getSource() == this.btnDetalhes3 ) {
			new DetalhesDialogMelhore(this.designacao3, this.sala, editable).setVisible(true);
		
		} else if (event.getSource() == this.btnDetalhes4 ) {
			new DetalhesDialogMelhore(this.designacao4, this.sala, editable).setVisible(true);
		}
	}
	
	public abstract void reset();
	public abstract void fecharSemana();
}
