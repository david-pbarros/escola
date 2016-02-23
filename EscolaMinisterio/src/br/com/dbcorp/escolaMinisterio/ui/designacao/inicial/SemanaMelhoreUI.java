package br.com.dbcorp.escolaMinisterio.ui.designacao.inicial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

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

import br.com.dbcorp.escolaMinisterio.AvaliacaoDOM;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaMelhoreUI;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.ObservacaoDialog;

@SuppressWarnings({"rawtypes", "unused"})
public class SemanaMelhoreUI extends ASemanaMelhoreUI {
	private static final long serialVersionUID = -7064940912653423525L;
	
	private JLabel lbData;
	private JLabel lbEstudo1;
	private JLabel lbEstudo2;
	private JLabel lbEstudo3;
	private JLabel lbAjudante1;
	private JLabel lbAjudante2;
	private JLabel lbAjudante3;
	private JLabel lbAjudante4;
	private JLabel lbEstudo4;
	private JPanel dataPanel;
	private JLabel lbAvaliacao1;
	private JLabel lbAvaliacao2;
	private JLabel lbAvaliacao3;
	private JLabel lbAvaliacao4;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JPanel separatorPanel1;
	private JSeparator separator_3;
	private JLabel lblEstudo;
	
	public SemanaMelhoreUI(SemanaDesignacao semanaDesignacao, String sala, boolean editDetalhes) {
		setBackground(Color.WHITE);
		this.setMinimumSize(new Dimension(743, 175));
		this.setPreferredSize(new Dimension(965, 208));
		
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
				ColumnSpec.decode("max(20dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:12dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(190dlu;min):grow"),
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
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("10dlu"),}));
		
		this.setFields();
		
		this.setDesignacoes();
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		this.actionDetalhes(event, false);
	}
	
	public void reset() {
		this.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 220));
	}

	private void setDesignacoes() {
		if (this.semanaDesignacao.isAssebleia() || this.semanaDesignacao.isRecapitulacao() || this.semanaDesignacao.isVisita() || this.semanaDesignacao.isSemReuniao() || this.semanaDesignacao.isVideos()) {
			this.chAssCongr.setSelected(this.semanaDesignacao.isAssebleia());
			this.chRecapitulao.setSelected(this.semanaDesignacao.isRecapitulacao());
			this.chVisSuper.setSelected(this.semanaDesignacao.isVisita());
			this.chSemReuniao.setSelected(this.semanaDesignacao.isSemReuniao());
			this.chVideos.setSelected(this.semanaDesignacao.isVideos());
			
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
						this.btnDetalhes1.setEnabled(true);
						this.btnDetalhes1.setEnabled(true);
						break;
					case 2:
						this.designacao2 = designacao;
						this.setCamposTela(designacao, this.lbEstudante2, this.lbEstudo2, this.lbAjudante2, this.lbAvaliacao2);
						this.btnDetalhes2.setEnabled(true);
						break;
					case 3:
						this.designacao3 = designacao;
						this.setCamposTela(designacao, this.lbEstudante3, this.lbEstudo3, this.lbAjudante3, this.lbAvaliacao3);
						this.btnDetalhes3.setEnabled(true);
						break;
					case 4:
						this.designacao4 = designacao;
						this.setCamposTela(designacao, this.lbEstudante4, this.lbEstudo4, this.lbAjudante4, this.lbAvaliacao4);
						this.btnDetalhes4.setEnabled(true);
						break;
					}
				}
			}
		}
	}
	
	private void semanaEnabled(boolean valor) {
		this.lbAvaliacao1.setEnabled(valor);
		this.lbAvaliacao2.setEnabled(valor);
		this.lbAvaliacao3.setEnabled(valor);
		this.lbAvaliacao4.setEnabled(valor);
		
		this.btnDetalhes1.setEnabled(valor);
		this.btnDetalhes2.setEnabled(valor);
		this.btnDetalhes3.setEnabled(valor);
		this.btnDetalhes4.setEnabled(valor);
		
		this.limparCampos();
	}
	
	private void limparCampos() {
		this.lbEstudante1.setText("");
		this.lbEstudante2.setText("");
		this.lbEstudante3.setText("");
		this.lbEstudante4.setText("");
		
		this.lbEstudo1.setText("");
		this.lbEstudo2.setText("");
		this.lbEstudo3.setText("");
		this.lbEstudo4.setText("");
		
		this.lbAjudante1.setText("");
		this.lbAjudante2.setText("");
		this.lbAjudante3.setText("");
		this.lbAjudante4.setText("");
		
		this.lbAvaliacao1.setText("");
		this.lbAvaliacao2.setText("");
		this.lbAvaliacao3.setText("");
		this.lbAvaliacao4.setText("");
	}
	
	private void setFields() {
		this.chRecapitulao = new JCheckBox("Recapitulação");
		this.chRecapitulao.setBackground(Color.WHITE);
		this.chRecapitulao.setEnabled(false);
		this.chRecapitulao.addActionListener(this);
		
		this.chAssCongr = new JCheckBox("Ass. / Congr.");
		this.chAssCongr.setBackground(Color.WHITE);
		this.chAssCongr.setEnabled(false);
		this.chAssCongr.addActionListener(this);
		
		this.chVisSuper = new JCheckBox("Vis. Super.");
		this.chVisSuper.setBackground(Color.WHITE);
		this.chVisSuper.setEnabled(false);
		this.chVisSuper.addActionListener(this);
		
		this.chSemReuniao = new JCheckBox("Dia sem reuni\u00E3o");
		this.chSemReuniao.setBackground(Color.WHITE);
		this.chSemReuniao.setEnabled(false);
		
		this.chVideos = new JCheckBox("Apresentações");
		this.chVideos.setBackground(Color.WHITE);
		this.chVideos.setEnabled(false);
		
		this.lbEstudante1 = new JLabel();
		this.lbEstudante2 = new JLabel();
		this.lbEstudante3 = new JLabel();
		this.lbEstudante4 = new JLabel();
		
		this.lbAvaliacao1 = new JLabel();
		this.lbAvaliacao2 = new JLabel();
		this.lbAvaliacao3 = new JLabel();
		this.lbAvaliacao4 = new JLabel();
		
		this.dataPanel = new JPanel();
		dataPanel.setBackground(Color.WHITE);
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
		separatorPanel.setBackground(Color.WHITE);
		separatorPanel.setLayout(new BorderLayout(0, 0));
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel.add(separator, BorderLayout.SOUTH);
		
		this.setDetalhesButtons();
		
		separatorPanel1 = new JPanel();
		separatorPanel1.setBackground(Color.WHITE);
		separatorPanel1.setLayout(new BorderLayout(0, 0));
		add(separatorPanel1, "2, 1, 15, 1, fill, top");
		
		separator_3 = new JSeparator();
		separator_3.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel1.add(separator_3, BorderLayout.NORTH);
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "1, 1, 1, 14, right, default");
		
		add(this.dataPanel, "5, 2, center, fill");

		JPanel checkPanel = new JPanel();
		checkPanel.setBackground(Color.WHITE);
		add(checkPanel, "7, 2, 9, 1, fill, fill");
		checkPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
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
		checkPanel.add(this.chVideos, "9, 1");
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		
		this.lbEstudo1 = new JLabel();
		this.lbEstudo2 = new JLabel();
		this.lbEstudo3 = new JLabel();
		this.lbEstudo4 = new JLabel();

		this.lbAjudante1 = new JLabel();
		this.lbAjudante2 = new JLabel();
		this.lbAjudante3 = new JLabel();
		this.lbAjudante4 = new JLabel();

		this.btnDetalhes1.setEnabled(false);
		this.btnDetalhes2.setEnabled(false);
		this.btnDetalhes3.setEnabled(false);
		this.btnDetalhes4.setEnabled(false);

		add(separator_2, "17, 1, 1, 14, left, default");
		add(new JLabel("Estudante:"), "5, 4, center, default");
		add(new JLabel("Estudo:"), "7, 4, center, default");
		add(new JLabel("Avalia\u00E7\u00E3o:"), "9, 4, center, default");
		add(new JLabel("Ajudante:"), "11, 4, 5, 1, center, default");
		add(new JLabel("Leitura:"), "3, 6, center, default");
		add(this.lbEstudante1, "5, 6, center, default");
		add(this.lbEstudo1, "7, 6, center, default");
		add(this.lbAvaliacao1, "9, 6, center, default");
		add(this.lbAjudante1, "13, 6, 3, 1, center, default");
		add(new JLabel("Visita:"), "3, 8, center, default");
		add(this.lbEstudante2, "5, 8, center, default");
		add(this.lbEstudo2, "7, 8, center, default");
		add(this.lbAvaliacao2, "9, 8, center, default");
		add(this.lbAjudante2, "13, 8, 3, 1, center, default");
		add(new JLabel("Revisita:"), "3, 10, center, default");
		add(this.lbEstudante3, "5, 10, center, default");
		add(this.lbEstudo3, "7, 10, center, center");
		add(this.lbAvaliacao3, "9, 10, center, default");
		add(this.lbAjudante3, "13, 10, 3, 1, center, center");
		add(new JLabel("Estudo:"), "3, 12");
		add(this.lbEstudante4, "5, 12, center, default");
		add(this.lbEstudo4, "7, 12, center, center");
		add(this.lbAvaliacao4, "9, 12, center, default");
		add(this.lbAjudante4, "13, 12, 3, 1, center, center");
		add(separatorPanel, "2, 14, 15, 1, fill, bottom");
		add(this.btnDetalhes1, "11, 6");
		add(this.btnDetalhes2, "11, 8");
		add(this.btnDetalhes3, "11, 10");
		add(this.btnDetalhes4, "11, 12");
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
		
		lbAvaliacao.setText(AvaliacaoDOM.getByInitials(designacao.getStatus()).getLabel());
	}
	
	private void setAvaliacao(Designacao designacao, JComboBox cbAvaliacao) {
		designacao.setStatus(AvaliacaoDOM.getByDescription((String) cbAvaliacao.getSelectedItem()).getSigla());
	}
	
	private void abrirObservacao(Designacao designacao) {
		ObservacaoDialog dialog = new ObservacaoDialog(designacao.getObservacao(), designacao.getTempo(), this.editDetalhes);
		
		String[] dados = dialog.exibir();
		
		designacao.setObservacao(dados[0]);
		designacao.setTempo(dados[1]);
	}

	@Override
	public void fecharSemana() {}
}
