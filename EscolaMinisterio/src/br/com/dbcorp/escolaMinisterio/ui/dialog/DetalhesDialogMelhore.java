package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import javax.swing.JTextArea;

public class DetalhesDialogMelhore extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2942269817954443289L;
	
	private Designacao designacao;
	
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JTextArea txtFonte;
	private JTextField txtObs;
	private JCheckBox chkCopia;

	public DetalhesDialogMelhore(Designacao designacao, String sala, boolean editable) {
		setTitle("Detalhes da Designação");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setMinimumSize(new Dimension(450, 218));
		setPreferredSize(new Dimension(450, 218));
		
		this.designacao = designacao;
		this.txtObs = new JTextField();
		this.txtFonte = new JTextArea();
		txtFonte.setRows(5);

		DScrollPane scrollPane = new DScrollPane(this.txtFonte);
		
		this.chkCopia = new JCheckBox("Copia");
		this.chkCopia.setToolTipText("Copiar observação para mesma designação na sala " + ("A".equalsIgnoreCase(sala) ? "B" : "A"));
		
		this.btnConfirmar = new JButton("Confirmar");

		this.btnCancelar = new JButton("Cancelar");
		this.btnCancelar.addActionListener(this);
		
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
				RowSpec.decode("fill:default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("10dlu", false), Sizes.constant("10dlu", false)), 0),}));
		
		if (editable && this.designacao != null) {
			this.btnConfirmar.addActionListener(this);
			
		} else {
			this.btnConfirmar.setVisible(false);
			this.btnCancelar.setText("Fechar");
			this.txtFonte.setEditable(false);
			this.txtObs.setEditable(false);
			this.chkCopia.setEnabled(false);
		}
		
		if (this.designacao != null) {
			this.txtFonte.setText(designacao.getFonte());
			this.txtObs.setText(designacao.getObsFolha());
		}
		
		JPanel fontePanel = new JPanel();
		fontePanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("fill:default"),}));
		
		JPanel obsPanel = new JPanel();
		obsPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("right:default"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		fontePanel.add(new JLabel("Fonte:"), "1, 1, right, default");
		fontePanel.add(scrollPane, "3, 1, fill, default");
		
		obsPanel.add(this.chkCopia, "1, 1");
		obsPanel.add(new JLabel("  Obs.:"), "2, 1, right, default");
		obsPanel.add(this.txtObs, "4, 1, fill, default");
		getContentPane().add(fontePanel, "2, 2, 5, 1, fill, fill");
		getContentPane().add(obsPanel, "2, 4, 5, 1, fill, fill");
		getContentPane().add(this.btnCancelar, "2, 6");
		getContentPane().add(this.btnConfirmar, "6, 6");
		
		pack();
		
		this.centerScreen();
	}

	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnConfirmar) {
			this.designacao.setFonte(this.txtFonte.getText());
			this.designacao.setObsFolha(this.txtObs.getText());
			this.designacao.setCopiar(this.chkCopia.isSelected());
		}
		
		this.dispose();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}