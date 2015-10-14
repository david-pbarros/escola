package br.com.dbcorp.escolaMinisterio.ui.designacao;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.dialog.DetalhesDialog;

public abstract class ASemanaUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = -4557864402358095884L;
	
	protected SemanaDesignacao semanaDesignacao;
	protected Designacao designacao1;
	protected Designacao designacao2;
	protected Designacao designacao3;
	
	protected String sala;
	protected boolean editDetalhes;
	
	protected JCheckBox chRecapitulao;
	protected JCheckBox chAssCongr;
	protected JCheckBox chVisSuper;
	protected JCheckBox chSemReuniao;
	protected JLabel lbEstudante1;
	protected JLabel lbEstudante2;
	protected JLabel lbEstudante3;
	protected JLabel lbEstudante4;
	protected JButton btnDetalhes1;
	protected JButton btnDetalhes2;
	protected JButton btnDetalhes3;
	protected JButton btnDetalhes4;
	
	protected void setDetalhesButtons() {
		this.btnDetalhes1 = new JButton("...");
		this.btnDetalhes2 = new JButton("...");
		this.btnDetalhes3 = new JButton("...");
		this.btnDetalhes4 = new JButton("...");

		this.btnDetalhes1.setToolTipText("Detalhes");
		this.btnDetalhes2.setToolTipText("Detalhes");
		this.btnDetalhes3.setToolTipText("Detalhes");
		this.btnDetalhes4.setToolTipText("Detalhes");
		
		this.btnDetalhes1.addActionListener(this);
		this.btnDetalhes2.addActionListener(this);
		this.btnDetalhes3.addActionListener(this);
		this.btnDetalhes4.addActionListener(this);
	}
	
	protected void actionDetalhes(ActionEvent event, boolean editable) {
		if (event.getSource() == this.btnDetalhes1 ) {
			new DetalhesDialog(this.designacao1, this.sala, editable).setVisible(true);
			
		} else if (event.getSource() == this.btnDetalhes2 ) {
			new DetalhesDialog(this.designacao2, this.sala, editable).setVisible(true);
			
		} else if (event.getSource() == this.btnDetalhes3 ) {
			new DetalhesDialog(this.designacao3, this.sala, editable).setVisible(true);
		}
	}
	
	public abstract void reset();
	public abstract void fecharSemana();
}
