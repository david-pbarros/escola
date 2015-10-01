package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.report.ReportCommon;
import br.com.dbcorp.escolaMinisterio.ui.Params;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class PrintDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5266823832368284482L;
	
	private JButton btnOK;
	private JRadioButton rbDirigente;
	private JRadioButton rbAuxiliar;
	private JRadioButton rbDesignacao;
	
	private MesDesignacao mes;
	
	public PrintDialog(MesDesignacao mes) {
		setTitle("Escolha de impress\u00E3o...");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.mes = mes;
		
		this.setPreferredSize(new Dimension(240, 150));
		this.setMinimumSize(new Dimension(240, 150));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		this.btnOK = new JButton("Continuar");
		this.btnOK.addActionListener(this);
		
		panel.add(this.btnOK);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		this.rbDirigente = new JRadioButton("Controle Dirigente");
		this.rbAuxiliar = new JRadioButton("Controle Auxiliar");
		this.rbDesignacao = new JRadioButton("Designa\u00E7\u00F5es");
		
		this.rbDirigente.addActionListener(this);
		this.rbAuxiliar.addActionListener(this);
		this.rbDesignacao.addActionListener(this);
		
		this.rbDirigente.setSelected(true);

		panel_1.add(this.rbDirigente, "2, 2");
		panel_1.add(this.rbAuxiliar, "2, 4");
		panel_1.add(this.rbDesignacao, "2, 6");
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnOK) {
			this.imprimir();
		
		} else if (event.getSource() == this.rbDirigente) {
			this.setRadio(this.rbDirigente);
			
		} else if (event.getSource() == this.rbAuxiliar) {
			this.setRadio(this.rbAuxiliar);
			
		} else if (event.getSource() == this.rbDesignacao) {
			this.setRadio(this.rbDesignacao);
		}
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
				rpc.gerarTipoDesignacoes(this.mes);
			}
			
			this.dispose();
			
		} finally {
			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}
	}
}
