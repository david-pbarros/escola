package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ObservacaoDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2942269817954443289L;
	
	private String observacao;
	private String minutos;
	private String segundos;
	
	private JButton btnConfirmar;
	private JButton btnCancelar;
	private JTextArea txObservacao;
	private JFormattedTextField txMinutos;
	private JFormattedTextField txSegundos;

	public ObservacaoDialog(String observacao, boolean editable) {
		this(observacao, null, editable);
	}
	
	public ObservacaoDialog(String observacao, String tempo, boolean editable) {
		setTitle("Observação");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		Dimension dimension = new Dimension(450, 237);
		setMinimumSize(dimension);
		setPreferredSize(dimension);
		
		MaskFormatter mf1 = null;
		
		try {
			mf1 = new MaskFormatter("##");
			mf1.setPlaceholderCharacter('0');
		} catch (ParseException e) {
			Log.getInstance().debug("", e);
		}
		
		this.observacao = observacao;
		
		if (tempo != null && tempo.contains(":")) {
			String[] temp = tempo.split(":");
			
			this.minutos = temp[0];
			this.segundos = temp[1];
		
		} else {
			this.minutos = "0";
			this.segundos = "0";
		}
		
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
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.UNRELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				new RowSpec(RowSpec.CENTER, Sizes.bounded(Sizes.DEFAULT, Sizes.constant("10dlu", false), Sizes.constant("10dlu", false)), 0),}));
		
		this.txObservacao = new JTextArea(this.observacao);
		this.txMinutos = new JFormattedTextField(mf1);
		this.txSegundos = new JFormattedTextField(mf1);
		
		this.txMinutos.setText(this.leftZero(this.minutos));
		this.txSegundos.setText(this.leftZero(this.segundos));
		
		DScrollPane scrollPane = new DScrollPane(this.txObservacao);
		
		if (editable) {
			this.btnConfirmar = new JButton("Confirmar");
			this.btnCancelar = new JButton("Cancelar");
			
			this.btnConfirmar.addActionListener(this);
			
			getContentPane().add(this.btnConfirmar, "6, 6");
			
		} else {
			this.btnCancelar = new JButton("Fechar");
			this.txObservacao.setEditable(false);
			this.txMinutos.setEditable(false);
			this.txSegundos.setEditable(false);
		}
		
		this.btnCancelar.addActionListener(this);
		
		
		JPanel tempoPanel = new JPanel();
		tempoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		tempoPanel.add(new JLabel("Minutos:"), "1, 1, right, default");
		tempoPanel.add(this.txMinutos, "3, 1, fill, default");
		tempoPanel.add(new JLabel("Segundos:"), "5, 1, right, default");
		tempoPanel.add(this.txSegundos, "7, 1, fill, default");
		
		getContentPane().add(new JLabel("Observa\u00E7\u00E3o:"), "2, 2, right, default");
		getContentPane().add(scrollPane, "4, 2, 3, 1, fill, fill");
		getContentPane().add(this.btnCancelar, "2, 6");
		getContentPane().add(new JLabel("Tempo:"), "2, 4, right, default");
		getContentPane().add(tempoPanel, "4, 4, fill, fill");
		
		pack();
		
		this.centerScreen();
	}

	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnConfirmar) {
			this.observacao = this.txObservacao.getText();
			this.minutos = this.txMinutos.getText();
			this.segundos = this.txSegundos.getText();
		}
		
		this.dispose();
	}
	
	public String[] exibir() {
		this.setVisible(true);
		
		String[] temp = {this.observacao, this.minutos + ":" + this.segundos};
		
		return temp;
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
	
	private String leftZero(String number) {
		if (number.length() < 2) {
			return "0" + number;
		
		} else {
			return number;
		}
	}
}