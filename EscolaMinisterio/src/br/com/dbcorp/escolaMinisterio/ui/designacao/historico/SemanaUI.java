package br.com.dbcorp.escolaMinisterio.ui.designacao.historico;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.ObservacaoDialog;

@SuppressWarnings({"rawtypes", "unused"})
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
	private JLabel lbAvaliacao1;
	private JLabel lbAvaliacao2;
	private JLabel lbAvaliacao3;
	private JButton btnObs1;
	private JButton btnObs2;
	private JButton btnObs3;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JPanel separatorPanel1;
	private JSeparator separator_3;
	
	public SemanaUI(SemanaDesignacao semanaDesignacao, String sala, boolean editDetalhes) {
		this.setMinimumSize(new Dimension(743, 175));
		this.setPreferredSize(new Dimension(925, 175));
		
		this.sala = sala;
		this.semanaDesignacao = semanaDesignacao;
		this.editDetalhes = editDetalhes;
		
		this.lbData = new JLabel(semanaDesignacao.getData().format(Params.dateFormate()));
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(120dlu;min):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:12dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("120dlu:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("10dlu"),},
			new RowSpec[] {
				RowSpec.decode("10dlu"),
				RowSpec.decode("fill:default"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.RELATED_GAP_ROWSPEC,
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
						this.setCamposTela(designacao, this.lbEstudante1, this.lbEstudo1, this.lbAjudante1, this.lbAvaliacao1);
						this.btnObs1.setEnabled(true);
						this.btnDetalhes1.setEnabled(true);
						break;
					case 2:
						this.designacao2 = designacao;
						this.setCamposTela(designacao, this.lbEstudante2, this.lbEstudo2, this.lbAjudante2, this.lbAvaliacao2);
						this.btnObs2.setEnabled(true);
						this.btnDetalhes2.setEnabled(true);
						break;
					case 3:
						this.designacao3 = designacao;
						this.setCamposTela(designacao, this.lbEstudante3, this.lbEstudo3, this.lbAjudante3, this.lbAvaliacao3);
						this.btnObs3.setEnabled(true);
						this.btnDetalhes3.setEnabled(true);
						break;
					}
				}
			}
		}
	}
	
	private void enableChecks() {
		this.chAssCongr.setEnabled(true);
		this.chVisSuper.setEnabled(true);
		this.chRecapitulao.setEnabled(true);
		this.chSemReuniao.setEnabled(true);
		
		this.semanaEnabled(true);
	}
	
	private void semanaEnabled(boolean valor) {
		this.lbAvaliacao1.setEnabled(valor);
		this.lbAvaliacao2.setEnabled(valor);
		this.lbAvaliacao3.setEnabled(valor);
		
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
		
		this.lbAvaliacao1.setText("");
		this.lbAvaliacao2.setText("");
		this.lbAvaliacao3.setText("");
	}
	
	private void setFields() {
		this.chRecapitulao = new JCheckBox("Recapitulação");
		this.chVisSuper = new JCheckBox("Vis. Super.");
		this.chAssCongr = new JCheckBox("Ass. / Congr.");
		this.chSemReuniao = new JCheckBox("Dia sem reuni\u00E3o");
		
		this.chRecapitulao.setEnabled(false);
		this.chSemReuniao.setEnabled(false);
		this.chAssCongr.setEnabled(false);
		this.chVisSuper.setEnabled(false);

		this.chRecapitulao.addActionListener(this);
		this.chAssCongr.addActionListener(this);
		this.chVisSuper.addActionListener(this);
		this.chSemReuniao.addActionListener(this);

		this.lbEstudante1 = new JLabel();
		this.lbEstudante2 = new JLabel();
		this.lbEstudante3 = new JLabel();
		
		this.lbAvaliacao1 = new JLabel();
		this.lbAvaliacao2 = new JLabel();
		this.lbAvaliacao3 = new JLabel();
		
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.dataPanel.add(new JLabel("Dia:"), "1, 2");
		this.dataPanel.add(lbData, "3, 2");
		
		JPanel separatorPanel = new JPanel();
		separatorPanel.setLayout(new BorderLayout(0, 0));
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel.add(separator, BorderLayout.SOUTH);
		
		this.setDetalhesButtons();
		
		separatorPanel1 = new JPanel();
		separatorPanel1.setLayout(new BorderLayout(0, 0));
		add(separatorPanel1, "2, 1, 15, 1, fill, top");
		
		separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel1.add(separator_3, BorderLayout.NORTH);
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "1, 1, 1, 12, right, default");
		
		
		add(this.dataPanel, "5, 2, center, fill");

		JPanel checkPanel = new JPanel();
		checkPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		checkPanel.add(this.chRecapitulao, "1, 1");
		checkPanel.add(this.chAssCongr, "3, 1");
		checkPanel.add(this.chVisSuper, "5, 1");
		checkPanel.add(this.chSemReuniao, "7, 1");
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		add(separator_2, "17, 1, 1, 12, left, default");
		
		add(checkPanel, "7, 2, 9, 1, fill, fill");
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
		
		add(this.lbAvaliacao1, "11, 6, center, default");
		this.btnObs1 = new JButton("Obs.");
		this.btnObs1.setEnabled(false);
		this.btnObs1.addActionListener(this);
		
		add(this.btnObs1, "13, 6");
		this.lbAjudante1 = new JLabel();
		add(this.lbAjudante1, "15, 6, center, default");
		add(new JLabel("2"), "3, 8, center, default");
		add(this.lbEstudante2, "5, 8, center, default");
		this.lbEstudo2 = new JLabel();
		add(this.lbEstudo2, "7, 8, center, default");
		
		add(this.lbAvaliacao2, "11, 8, center, default");
		this.btnObs2 = new JButton("Obs.");
		this.btnObs2.setEnabled(false);
		this.btnObs2.addActionListener(this);
		
		add(this.btnObs2, "13, 8");
		this.lbAjudante2 = new JLabel();
		add(this.lbAjudante2, "15, 8, center, default");
		add(new JLabel("3"), "3, 10, center, default");
		add(lbEstudante3, "5, 10, center, default");
		this.lbEstudo3 = new JLabel();
		add(this.lbEstudo3, "7, 10, center, center");
		
		add(this.lbAvaliacao3, "11, 10, center, default");
		this.btnObs3 = new JButton("Obs.");
		this.btnObs3.setEnabled(false);
		this.btnObs3.addActionListener(this);
		
		add(this.btnObs3, "13, 10");
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
		
	private void setCamposTela(Designacao designacao, JLabel lbEstudante, JLabel lbEstudo, JLabel lbAjudante, JLabel lbAvaliacao) {
		if (designacao.getEstudante() != null) {
			lbEstudante.setText(designacao.getEstudante().getNome());
		}
		
		if (designacao.getEstudo() != null) {
			lbEstudo.setText(Integer.toString(designacao.getEstudo().getNrEstudo()));
		}
		
		if (designacao.getAjudante() != null) {
			lbAjudante.setText(designacao.getAjudante().getNome());
		}
		
		switch (designacao.getStatus()) {
		case 'P':
			lbAvaliacao.setText("Passou");
			break;
		case 'F':
			lbAvaliacao.setText("Não Passou");
			break;
		case 'A':
			lbAvaliacao.setText("Não Avaliado");
			break;
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
		ObservacaoDialog dialog = new ObservacaoDialog(designacao.getObservacao(), false);
		designacao.setObservacao(dialog.exibir());
	}

	@Override
	public void fecharSemana() {}
}
