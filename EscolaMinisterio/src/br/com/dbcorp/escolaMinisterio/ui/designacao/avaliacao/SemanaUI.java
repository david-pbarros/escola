package br.com.dbcorp.escolaMinisterio.ui.designacao.avaliacao;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.ObservacaoDialog;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

@SuppressWarnings("rawtypes")
public class SemanaUI extends ASemanaUI {
	private static final long serialVersionUID = -7064940912653423525L;
	
	private JLabel lbData;
	
	private JLabel lbEstudo1;
	private JLabel lbEstudo2;
	private JLabel lbEstudo3;
	private JLabel lbAjudante1;
	private JLabel lbAjudante2;
	private JLabel lbAjudante3;
	private JPanel dataPanel;
	private JComboBox cbAvaliacao1;
	private JComboBox cbAvaliacao2;
	private JComboBox cbAvaliacao3;
	private JButton btnObs1;
	private JButton btnObs2;
	private JButton btnObs3;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JPanel panel;
	private JSeparator separator_3;
	
	public SemanaUI(SemanaDesignacao semanaDesignacao, String sala, boolean editDetalhes) {
		this.setMinimumSize(new Dimension(743, 175));
		this.setPreferredSize(new Dimension(925, 175));
		
		this.sala = sala;
		this.semanaDesignacao = semanaDesignacao;
		this.editDetalhes = editDetalhes;
		
		this.lbData = new JLabel(new SimpleDateFormat("dd/MM/yyyy").format(semanaDesignacao.getData()));
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(120dlu;min):grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:12dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("120dlu:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("10dlu"),},
			new RowSpec[] {
				RowSpec.decode("10dlu"),
				RowSpec.decode("fill:default"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("10dlu"),}));
		
		this.setFields();
		
		this.setDesignacoes();
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		this.actionDetalhes(event, this.editDetalhes);
		
		if (event.getSource() == this.btnObs1) {
			this.abrirObservacao(this.designacao1);
			
		} else if (event.getSource() == this.btnObs2) {
			this.abrirObservacao(this.designacao2);
			
		} else if (event.getSource() == this.btnObs3) {
			this.abrirObservacao(this.designacao3);
		}
	}
	
	public void fecharSemana() {
		if ( !"Selecione...".equals(this.cbAvaliacao1.getSelectedItem()) ) {
			this.setAvaliacao(this.designacao1, this.cbAvaliacao1);
		}
		
		if ( !"Selecione...".equals(this.cbAvaliacao2.getSelectedItem()) ) {
			this.setAvaliacao(this.designacao2, this.cbAvaliacao2);
		}

		if ( !"Selecione...".equals(this.cbAvaliacao3.getSelectedItem()) ) {
			this.setAvaliacao(this.designacao3, this.cbAvaliacao3);
		}
	}
	
	public void reset() {
		this.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 220));
	}

	private void setDesignacoes() {
		if (this.semanaDesignacao.isAssebleia() || this.semanaDesignacao.isRecapitulacao() || this.semanaDesignacao.isVisita() || this.semanaDesignacao.isSemReuniao()) {
			this.chAssCongr.setSelected(this.semanaDesignacao.isAssebleia());
			this.chRecapitulao.setSelected(this.semanaDesignacao.isRecapitulacao());
			this.chVisSuper.setSelected(this.semanaDesignacao.isVisita());
			this.chSemReuniao.setSelected(this.semanaDesignacao.isSemReuniao());
			
			this.semanaEnabled(false);
		}
		
		if (this.semanaDesignacao.getDesignacoes() == null) {
			this.semanaDesignacao.setDesignacoes(new ArrayList<Designacao>());
			
		} else {
			for (Designacao designacao : this.semanaDesignacao.getDesignacoes()) {
				if (designacao.getSala().equals(this.sala)) {
					switch (designacao.getNumero()) {
					case 1:
						this.designacao1 = designacao;
						this.setCamposTela(designacao, this.lbEstudante1, this.lbEstudo1, this.lbAjudante1, this.cbAvaliacao1);
						this.cbAvaliacao1.setEnabled(true);
						this.btnObs1.setEnabled(true);
						this.btnDetalhes1.setEnabled(true);
						break;
					case 2:
						this.designacao2 = designacao;
						this.setCamposTela(designacao, this.lbEstudante2, this.lbEstudo2, this.lbAjudante2, this.cbAvaliacao2);
						this.cbAvaliacao2.setEnabled(true);
						this.btnObs2.setEnabled(true);
						this.btnDetalhes2.setEnabled(true);
						break;
					case 3:
						this.designacao3 = designacao;
						this.setCamposTela(designacao, this.lbEstudante3, this.lbEstudo3, this.lbAjudante3, this.cbAvaliacao3);
						this.cbAvaliacao3.setEnabled(true);
						this.btnObs3.setEnabled(true);
						this.btnDetalhes3.setEnabled(true);
						break;
					}
				}
			}
		}
	}
	
	private void semanaEnabled(boolean valor) {
		this.cbAvaliacao1.setEnabled(valor);
		this.cbAvaliacao2.setEnabled(valor);
		this.cbAvaliacao3.setEnabled(valor);
		
		this.btnDetalhes1.setEnabled(valor);
		this.btnDetalhes2.setEnabled(valor);
		this.btnDetalhes3.setEnabled(valor);
		
		this.limparCampos();
	}
	
	private void limparCampos() {
		this.lbEstudante1.setText("");
		this.lbEstudante2.setText("");
		this.lbEstudante3.setText("");
		
		this.lbEstudo1.setText("");
		this.lbEstudo2.setText("");
		this.lbEstudo3.setText("");
		
		this.lbAjudante1.setText("");
		this.lbAjudante2.setText("");
		this.lbAjudante3.setText("");
		
		this.cbAvaliacao1.setSelectedIndex(0);
		this.cbAvaliacao2.setSelectedIndex(0);
		this.cbAvaliacao3.setSelectedIndex(0);
	}
	
	@SuppressWarnings("unchecked")
	private void setFields() {
		this.chRecapitulao = new JCheckBox("Recapitulação");
		this.chAssCongr = new JCheckBox("Ass. / Congr.");
		this.chVisSuper = new JCheckBox("Vis. Super.");
		this.chSemReuniao = new JCheckBox("Dia sem reuni\u00E3o");
		
		this.chRecapitulao.setEnabled(false);
		this.chAssCongr.setEnabled(false);
		this.chVisSuper.setEnabled(false);
		this.chSemReuniao.setEnabled(false);
		
		this.chRecapitulao.addActionListener(this);
		this.chAssCongr.addActionListener(this);
		this.chVisSuper.addActionListener(this);
		this.chSemReuniao.addActionListener(this);
		
		this.lbEstudante1 = new JLabel();
		this.lbEstudante2 = new JLabel();
		this.lbEstudante3 = new JLabel();
		
		String[] dominio = new String[]{"Selecionar...", "Passou", "Não Passou"};
		
		this.cbAvaliacao1 = new JComboBox(dominio);
		this.cbAvaliacao2 = new JComboBox(dominio);
		this.cbAvaliacao3 = new JComboBox(dominio);
		
		this.cbAvaliacao1.setEnabled(false);
		this.cbAvaliacao2.setEnabled(false);
		this.cbAvaliacao3.setEnabled(false);
		
		this.btnObs1 = new JButton("Obs.");
		this.btnObs2 = new JButton("Obs.");
		this.btnObs3 = new JButton("Obs.");
		
		this.btnObs1.setToolTipText("Observa\u00E7\u00F5es");
		this.btnObs2.setToolTipText("Observa\u00E7\u00F5es");
		this.btnObs3.setToolTipText("Observa\u00E7\u00F5es");
		
		this.btnObs1.addActionListener(this);
		this.btnObs2.addActionListener(this);
		this.btnObs3.addActionListener(this);
		
		this.btnObs1.setEnabled(false);
		this.btnObs2.setEnabled(false);
		this.btnObs3.setEnabled(false);
		
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		this.dataPanel.add(new JLabel("Dia:"), "1, 2");
		this.dataPanel.add(lbData, "3, 2");
		
		JPanel separatorPanel = new JPanel();
		separatorPanel.setLayout(new BorderLayout(0, 0));
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel.add(separator, BorderLayout.SOUTH);
		
		this.setDetalhesButtons();
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "1, 1, 1, 12, right, default");
		
		panel = new JPanel();
		add(panel, "2, 1, 15, 1, fill, top");
		panel.setLayout(new BorderLayout(0, 0));
		
		separator_3 = new JSeparator();
		panel.add(separator_3, BorderLayout.NORTH);
		
		add(this.dataPanel, "5, 2, center, fill");
		
		JPanel checkPanel = new JPanel();
		add(checkPanel, "7, 2, 9, 1, fill, fill");
		checkPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		checkPanel.add(this.chRecapitulao, "1, 1");
		checkPanel.add(this.chAssCongr, "3, 1");
		checkPanel.add(this.chVisSuper, "5, 1");
		checkPanel.add(this.chSemReuniao, "7, 1");
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		add(separator_2, "17, 1, 1, 12, left, default");
		
		add(new JLabel("N\u00BA:"), "3, 4, center, default");
		add(new JLabel("Estudante:"), "5, 4, center, default");
		JLabel label = new JLabel("Estudo:");
		add(label, "7, 4, center, default");
		add(new JLabel("Avalia\u00E7\u00E3o:"), "11, 4, 3, 1, center, default");
		JLabel label_1 = new JLabel("Ajudante:");
		add(label_1, "15, 4, center, default");
		add(new JLabel("1"), "3, 6, center, default");
		add(this.lbEstudante1, "5, 6, center, default");
		this.lbEstudo1 = new JLabel();
		add(this.lbEstudo1, "7, 6, center, default");
		
		add(this.cbAvaliacao1, "11, 6, fill, default");
		add(btnObs1, "13, 6");
		this.lbAjudante1 = new JLabel();
		add(this.lbAjudante1, "15, 6, center, default");
		add(new JLabel("2"), "3, 8, center, default");
		add(this.lbEstudante2, "5, 8, center, default");
		this.lbEstudo2 = new JLabel();
		add(this.lbEstudo2, "7, 8, center, default");
		
		add(this.cbAvaliacao2, "11, 8, fill, default");
		add(btnObs2, "13, 8");
		this.lbAjudante2 = new JLabel();
		add(this.lbAjudante2, "15, 8, center, default");
		add(new JLabel("3"), "3, 10, center, default");
		add(lbEstudante3, "5, 10, center, default");
		this.lbEstudo3 = new JLabel();
		add(this.lbEstudo3, "7, 10, center, center");
		
		add(this.cbAvaliacao3, "11, 10, fill, default");
		add(btnObs3, "13, 10");
		this.lbAjudante3 = new JLabel();
		add(this.lbAjudante3, "15, 10, center, center");
		add(separatorPanel, "2, 12, 15, 1, fill, bottom");
		
		add(this.btnDetalhes1, "9, 6");
		add(this.btnDetalhes2, "9, 8");
		add(this.btnDetalhes3, "9, 10");
		
		this.btnDetalhes1.setEnabled(false);
		this.btnDetalhes2.setEnabled(false);
		this.btnDetalhes3.setEnabled(false);
	}
		
	private void setCamposTela(Designacao designacao, JLabel lbEstudante, JLabel lbEstudo, JLabel lbAjudante, JComboBox cbAvaliacao) {
		if (designacao.getEstudante() != null) {
			lbEstudante.setText(designacao.getEstudante().getNome());
		}
		
		if (designacao.getEstudo() != null) {
			lbEstudo.setText(Integer.toString(designacao.getEstudo().getNrEstudo()));
		}
		
		if (designacao.getAjudante() != null) {
			lbAjudante.setText(designacao.getAjudante().getNome());
		}
		
		if ('P' == designacao.getStatus()) {
			cbAvaliacao.setSelectedIndex(1);
			
		} else if ('F' == designacao.getStatus()) {
			cbAvaliacao.setSelectedIndex(2);
			
		} else {
			cbAvaliacao.setSelectedIndex(0);
		}
		
	}
	
	private void setAvaliacao(Designacao designacao, JComboBox cbAvaliacao) {
		if ("Passou".equals(cbAvaliacao.getSelectedItem())) {
			designacao.setStatus('P');
			
		} else if ("Não Passou".equals(cbAvaliacao.getSelectedItem())) {
			designacao.setStatus('F');
		}
	}
	
	private void abrirObservacao(Designacao designacao) {
		ObservacaoDialog dialog = new ObservacaoDialog(designacao.getObservacao(), true);
		designacao.setObservacao(dialog.exibir());
	}
}
