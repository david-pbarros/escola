package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ObservacaoDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2942269817954443289L;
	
	private String observacao;
	
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JTextArea txObservacao;

	public ObservacaoDialog(String observacao, boolean editable) {
		setTitle("Observação");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(450, 211));
		setPreferredSize(new Dimension(450, 211));
		
		this.observacao = observacao;
		
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("10dlu"),},
			new RowSpec[] {
				new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("10dlu", false), Sizes.constant("10dlu", false)), 0),
				new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("75dlu", false), Sizes.constant("75dlu", false)), 1),
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("10dlu", false), Sizes.constant("10dlu", false)), 0),}));
		
		this.txObservacao = new JTextArea(this.observacao);
		JScrollPane scrollPane = new JScrollPane(this.txObservacao);
		
		if (editable) {
			this.btnConfirmar = new JButton("Confirmar");
			this.btnCancelar = new JButton("Cancelar");
			
			this.btnConfirmar.addActionListener(this);
			
			getContentPane().add(this.btnConfirmar, "6, 4");
			
		} else {
			this.btnCancelar = new JButton("Fechar");
			this.txObservacao.setEditable(false);
		}
		
		this.btnCancelar.addActionListener(this);
		
		getContentPane().add(new JLabel("Observa\u00E7\u00E3o:"), "2, 2, right, default");
		getContentPane().add(scrollPane, "4, 2, 3, 1, fill, fill");
		
		getContentPane().add(this.btnCancelar, "2, 4");
		
		pack();
		
		this.centerScreen();
	}

	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnConfirmar) {
			this.observacao = this.txObservacao.getText();
		}
		
		this.dispose();
	}
	
	public String exibir() {
		this.setVisible(true);
		
		return this.observacao;
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}