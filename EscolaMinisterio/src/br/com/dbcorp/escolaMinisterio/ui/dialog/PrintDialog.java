package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.report.ReportCommon;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class PrintDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5266823832368284482L;
	
	private JButton btnOK;
	private JRadioButton rbDirigente;
	private JRadioButton rbAuxiliar;
	private JRadioButton rbDesignacao;
	
	private MesDesignacao mes;
	private JLabel lbSala;
	private JComboBox<String> cbSala;
	private JComboBox<String> cbDe;
	private JComboBox<String> cbAte;
	
	private Dimension defaultDimension;
	
	public PrintDialog(MesDesignacao mes) {
		setTitle("Escolha de impress\u00E3o...");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mes = mes;
		
		this.defaultDimension = new Dimension(160, 155);
		
		this.setPreferredSize(this.defaultDimension);
		this.setMinimumSize(this.defaultDimension);
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		this.btnOK = new JButton("Continuar");
		this.btnOK.addActionListener(this);
		
		panel.add(this.btnOK);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.rbDirigente = new JRadioButton("Controle Dirigente");
		this.rbAuxiliar = new JRadioButton("Controle Auxiliar");
		this.rbDesignacao = new JRadioButton("Designa\u00E7\u00F5es");
		
		this.rbDirigente.addActionListener(this);
		this.rbAuxiliar.addActionListener(this);
		this.rbDesignacao.addActionListener(this);
		
		this.rbDirigente.setSelected(true);
		
		String[] salas = {"Todas", "A", "B"};
		this.cbSala = new JComboBox<>(salas);
		this.lbSala = new JLabel("Sala:");

		this.lbSala.setVisible(false);
		this.cbSala.setVisible(false);
		
		this.combosData(mes);

		JPanel salaPanel = new JPanel();
		salaPanel.add(this.rbDesignacao);
		salaPanel.add(this.lbSala);
		salaPanel.add(this.cbSala);
		
		JPanel dataPanel = new JPanel();
		dataPanel.add(new JLabel("De:"));
		dataPanel.add(cbDe);
		dataPanel.add(new JLabel("At\u00E9:"));
		dataPanel.add(cbAte);

		panel_1.add(this.rbDirigente, "2, 2");
		panel_1.add(this.rbAuxiliar, "2, 4");
		panel_1.add(salaPanel, "1, 5, 2, 1, left, fill");
		panel_1.add(dataPanel, "1, 6, 2, 1, left, fill");
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		Dimension d = this.defaultDimension;
		this.lbSala.setVisible(false);
		this.cbSala.setVisible(false);
		
		if (event.getSource() == this.btnOK) {
			this.imprimir();
		
		} else if (event.getSource() == this.rbDirigente) {
			this.setRadio(this.rbDirigente);
			
		} else if (event.getSource() == this.rbAuxiliar) {
			this.setRadio(this.rbAuxiliar);
			
		} else if (event.getSource() == this.rbDesignacao) {
			this.setRadio(this.rbDesignacao);
			
			this.lbSala.setVisible(true);
			this.cbSala.setVisible(true);
			d = new Dimension(250, 190);
		}
		
		this.setPreferredSize(d);
		this.setMinimumSize(d);
		this.setMaximumSize(d);
		
		this.centerScreen();
		
		this.pack();
	}
	
	private void setRadio(JRadioButton selecionado) {
		this.rbDirigente.setSelected(false);
		this.rbAuxiliar.setSelected(false);
		this.rbDesignacao.setSelected(false);
		
		selecionado.setSelected(true);
	}
	
	private void imprimir() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		try {
			ReportCommon rpc = new ReportCommon();
		
			if (this.rbDirigente.isSelected()) {
				rpc.gerarTipoDirigente(this.mes);
				
			} else if (this.rbAuxiliar.isSelected()) {
				rpc.gerarTipoAuxiliar(this.mes);
			
			} else if (this.rbDesignacao.isSelected()) {
				rpc.gerarTipoDesignacoes(this.mes, (String)this.cbSala.getSelectedItem(), (String)this.cbDe.getSelectedItem(), (String)this.cbAte.getSelectedItem());
			}
			
			this.dispose();
			
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
	
	private void combosData(MesDesignacao mes) {
		String[] datas = new String[mes.getSemanas().size()];
		
		for (int i = 0; i < mes.getSemanas().size(); i++) {
			datas[i] = mes.getSemanas().get(i).getData().format(Params.dateFormate());
		}
		
		this.cbDe = new JComboBox<>(datas);
		this.cbAte = new JComboBox<>(datas);
		
		this.cbAte.setSelectedIndex(datas.length-1);
	}
}
