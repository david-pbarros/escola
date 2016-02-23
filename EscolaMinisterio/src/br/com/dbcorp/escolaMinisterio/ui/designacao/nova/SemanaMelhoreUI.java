package br.com.dbcorp.escolaMinisterio.ui.designacao.nova;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Sizes;

import br.com.dbcorp.escolaMinisterio.AvaliacaoDOM;
import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.Pessoa;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaMelhoreUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.EscolhaEstudanteDialog;
import br.com.dbcorp.escolaMinisterio.ui.dialog.EstudosDialog;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoDecorator;

@SuppressWarnings("rawtypes")
public class SemanaMelhoreUI extends ASemanaMelhoreUI {
	private static final long serialVersionUID = -7064940912653423525L;
	private static final String SELECIONE = "Selecione...";
	private static final String NR_LIMPO = "--";
	private static final String DATA_LIMPA = "--/--/----";
	private static final String BT_ESTUDO_DEF = "Nr.";
	
	private DesignacaoGerenciador gerenciador;
	private EstudanteGerenciador estudantesGerenciador;
	
	private List<Estudo> estudos;
	private List<String> ajudantes;
	private List<String> ajudantesHomens;
	private List<String> ajudMulheresPrime;
	
	private JTextField txData;
	
	private JButton btnProcurar1;
	private JButton btnProcurar2;
	private JButton btnProcurar3;
	private JButton btnProcurar4;
	private JLabel lblUldDes1;
	private JLabel lblUldDes2;
	private JLabel lblUldDes3;
	private JLabel lblUldDes4;
	private JLabel lblDtUldDes1;
	private JLabel lblDtUldDes2;
	private JLabel lblDtUldDes3;
	private JLabel lblDtUldDes4;
	private JComboBox cbAjudante1;
	private JComboBox cbAjudante2;
	private JComboBox cbAjudante3;
	private JComboBox cbAjudante4;
	private JPanel dataPanel;
	private JPanel EstudantePanel1;
	private JPanel EstudantePanel2;
	private JPanel EstudantePanel3;
	private JPanel EstudantePanel4;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JPanel panel;
	private JSeparator separator_3;
	
	private JButton btnEstudo1;
	private JButton btnEstudo2;
	private JButton btnEstudo3;
	private JButton btnEstudo4;
	
	public SemanaMelhoreUI(DesignacaoGerenciador gerenciador, EstudanteGerenciador estudanteGerenciador, SemanaDesignacao semanaDesignacao, String sala, List<Estudo> estudos, List<String> ajudantesHomens, List<String> ajudantesMulheres, boolean editDetalhes) {
		this.setMinimumSize(new Dimension(631, 232));
		this.setPreferredSize(new Dimension(941, 229));
		
		this.gerenciador = gerenciador;
		this.estudantesGerenciador = estudanteGerenciador;
		
		this.editDetalhes = editDetalhes;

		this.sala = sala;
		this.semanaDesignacao = semanaDesignacao;
		
		this.txData = new JTextField();
		this.txData.setText(semanaDesignacao.getData().format(Params.dateFormate()));
		
		this.estudos = estudos;

		this.ajudantesHomens = this.gerenciador.listaEstudantes(Genero.MASCULINO);
		this.ajudantesHomens.addAll(ajudantesHomens);
		
		this.ajudantesHomens = this.ajudantesHomens.stream()
				.filter(p->!SELECIONE.equals(p))
				.sorted(Comparator.naturalOrder())
				.collect(Collectors.toList());
		
		
		this.ajudantes = new ArrayList<String>(); 
		this.ajudantes.addAll(this.ajudantesHomens);
		this.ajudantes.addAll(ajudantesMulheres);
		
		this.ajudantes = this.ajudantes.stream()
							.filter(p->!SELECIONE.equals(p))
							.sorted(Comparator.naturalOrder())
							.collect(Collectors.toList());
		
		this.ajudMulheresPrime = ajudantesMulheres.stream()
									.filter(p->!SELECIONE.equals(p))
									.collect(Collectors.toList());
		this.ajudMulheresPrime.addAll(this.ajudantesHomens);
		
		this.ajudantes.add(0, SELECIONE);
		this.ajudantesHomens.add(0, SELECIONE);
		this.ajudMulheresPrime.add(0, SELECIONE);
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.bounded(Sizes.MINIMUM, Sizes.constant("150dlu", true), Sizes.constant("200dlu", true)), 0),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(33dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:12dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				new ColumnSpec(ColumnSpec.FILL, Sizes.bounded(Sizes.MINIMUM, Sizes.constant("150dlu", true), Sizes.constant("170dlu", true)), 1),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("10dlu"),},
			new RowSpec[] {
				RowSpec.decode("10dlu"),
				RowSpec.decode("fill:default"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("fill:default"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("10dlu"),}));
		
		this.setFields();
		
		this.setDesignacoes();
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		this.actionDetalhes(event, this.editDetalhes);
		
		if (event.getSource() == this.chAssCongr) {
			if (this.chAssCongr.isSelected()) {
				this.chVisSuper.setEnabled(false);
				this.chRecapitulao.setEnabled(false);
				this.chSemReuniao.setEnabled(false);
				this.chVideos.setEnabled(false);
				this.semanaEnabled(false);
				
			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chVisSuper) {
			if (this.chVisSuper.isSelected()) {
				this.chRecapitulao.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.chSemReuniao.setEnabled(false);
				this.chVideos.setEnabled(false);
				this.semanaEnabled(false);

			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chRecapitulao) {
			if (this.chRecapitulao.isSelected()) {
				this.chVisSuper.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.chSemReuniao.setEnabled(false);
				this.chVideos.setEnabled(false);
				this.semanaEnabled(false);

			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chSemReuniao) {
			if (this.chSemReuniao.isSelected()) {
				this.chRecapitulao.setEnabled(false);
				this.chVisSuper.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.chVideos.setEnabled(false);
				this.semanaEnabled(false);
			} else {
				enableChecks();
			}
		}  else if (event.getSource() == this.chVideos) {
			if (this.chVideos.isSelected()) {
				this.chSemReuniao.setEnabled(false);
				this.chRecapitulao.setEnabled(false);
				this.chVisSuper.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.semanaEnabled(false);
			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.btnProcurar1) {
			EscolhaEstudanteDialog d = new EscolhaEstudanteDialog(this.estudantesGerenciador, Genero.MASCULINO);
			this.estudanteSet(d.exibir(), this.lbEstudante1);
			
		} else if (event.getSource() == this.btnProcurar2 || event.getSource() == this.btnProcurar3 || event.getSource() == this.btnProcurar4) {
			EscolhaEstudanteDialog d = new EscolhaEstudanteDialog(this.estudantesGerenciador, null);
		
			if (event.getSource() == this.btnProcurar2) {
				this.estudanteSet(d.exibir(), this.lbEstudante2);
			
			} else if (event.getSource() == this.btnProcurar3) {
				this.estudanteSet(d.exibir(), this.lbEstudante3);
				
			} else if (event.getSource() == this.btnProcurar4) {
				this.estudanteSet(d.exibir(), this.lbEstudante4);
			} 
		} else if (event.getSource() == this.btnEstudo1) {
			this.selecionaEstudo(this.designacao1, this.btnEstudo1);

		} else if (event.getSource() == this.btnEstudo2) {
			this.selecionaEstudo(this.designacao2, this.btnEstudo2);
		
		} else if (event.getSource() == this.btnEstudo3) {
			this.selecionaEstudo(this.designacao3, this.btnEstudo3);

		} else if (event.getSource() == this.btnEstudo4) {
			this.selecionaEstudo(this.designacao4, this.btnEstudo4);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void estudanteSet(Estudante estudante, JLabel estudanteLB) {
		estudanteLB.setText(estudante.getNome());
		
		if (!estudanteLB.equals(SELECIONE)) {
			if (estudanteLB == this.lbEstudante1) {
				if (this.designacao1 == null) {
					this.designacao1 = new Designacao();
					this.designacao1.setNumero(1);
					this.semanaDesignacao.getDesignacoes().add(this.designacao1);
				}
				
				this.carregaDadosProcurar(this.designacao1, estudante, this.lblUldDes1, this.lblDtUldDes1, this.cbAjudante1);
			
			} else if (estudanteLB == this.lbEstudante2) {
				if (this.designacao2 == null) {
					this.designacao2 = new Designacao();
					this.designacao2.setNumero(2);
					this.semanaDesignacao.getDesignacoes().add(this.designacao2);
				}
				
				this.carregaDadosProcurar(this.designacao2, estudante, this.lblUldDes2, this.lblDtUldDes2, this.cbAjudante2);
			
			} else if (estudanteLB == this.lbEstudante3) {
				if (this.designacao3 == null) {
					this.designacao3 = new Designacao();
					this.designacao3.setNumero(3);
					this.semanaDesignacao.getDesignacoes().add(this.designacao3);
				}
				
				this.carregaDadosProcurar(this.designacao3, estudante, this.lblUldDes3, this.lblDtUldDes3, this.cbAjudante3);
			
			} else if (estudanteLB == this.lbEstudante4) {
				if (this.designacao4 == null) {
					this.designacao4 = new Designacao();
					this.designacao4.setNumero(4);
					this.semanaDesignacao.getDesignacoes().add(this.designacao4);
				}
				
				this.carregaDadosProcurar(this.designacao4, estudante, this.lblUldDes4, this.lblDtUldDes4, this.cbAjudante4);
			}
		}
	}
	
	private void carregaDadosProcurar(Designacao designacao, Estudante estudante, JLabel ultimaDesignacao, JLabel dtUltmaDesignacao, JComboBox<String> cbAjudantes) {
		designacao.setSala(this.sala);
		designacao.setStatus(AvaliacaoDOM.NAO_AVALIADO.getSigla());
		designacao.setSemana(this.semanaDesignacao);
		designacao.setEstudante(estudante);

		this.setUltimaDesignacao(designacao.getEstudante(), ultimaDesignacao, dtUltmaDesignacao);
		this.setAjudantes(designacao, cbAjudantes);
	}
	
	public void fecharSemana() {
		this.semanaDesignacao.setAssebleia(this.chAssCongr.isSelected());
		this.semanaDesignacao.setRecapitulacao(this.chRecapitulao.isSelected());
		this.semanaDesignacao.setVisita(this.chVisSuper.isSelected());
		this.semanaDesignacao.setSemReuniao(this.chSemReuniao.isSelected());
		this.semanaDesignacao.setVideos(this.chVideos.isSelected());
		
		if (this.chVideos.isSelected()) { 
			if ("A".equalsIgnoreCase(this.sala) && this.designacao1 != null) {
				this.designacao1.setAjudante(this.obtemPessoa(this.cbAjudante1));
				this.designacao1.setData(this.semanaDesignacao.getData());
			} 
		} else if ( !this.chAssCongr.isSelected() && !this.chRecapitulao.isSelected() && !this.chSemReuniao.isSelected() && !this.chVisSuper.isSelected() ) {
			if (this.designacao1 != null) {
				this.designacao1.setAjudante(this.obtemPessoa(this.cbAjudante1));
				this.designacao1.setData(this.semanaDesignacao.getData());
			} 
			
			if (this.designacao2 != null) {
				this.designacao2.setAjudante(this.obtemPessoa(this.cbAjudante2));
				this.designacao2.setData(this.semanaDesignacao.getData());
			}
			
			if (this.designacao3 != null) {
				this.designacao3.setAjudante(this.obtemPessoa(this.cbAjudante3));
				this.designacao3.setData(this.semanaDesignacao.getData());
			}
			
			if (this.designacao4 != null) {
				this.designacao4.setAjudante(this.obtemPessoa(this.cbAjudante4));
				this.designacao4.setData(this.semanaDesignacao.getData());
			}
		}
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
					boolean habilitado = designacao.getStatus() == '\u0000' || designacao.getStatus() == 'A';
					
					switch (designacao.getNumero()) {
					case 1:
						this.designacao1 = designacao;
						this.setCamposTela(designacao, this.lbEstudante1, this.btnEstudo1, this.cbAjudante1, this.lblDtUldDes1, this.lblUldDes1, habilitado, this.btnProcurar1);
						break;
					case 2:
						this.designacao2 = designacao;
						this.setCamposTela(designacao, this.lbEstudante2, this.btnEstudo2, this.cbAjudante2, this.lblDtUldDes2, this.lblUldDes2, habilitado, this.btnProcurar2);
						break;
					case 3:
						this.designacao3 = designacao;
						this.setCamposTela(designacao, this.lbEstudante3, this.btnEstudo3, this.cbAjudante3, this.lblDtUldDes3, this.lblUldDes3, habilitado, this.btnProcurar3);
						break;
					case 4:
						this.designacao4 = designacao;
						this.setCamposTela(designacao, this.lbEstudante4, this.btnEstudo4, this.cbAjudante4, this.lblDtUldDes4, this.lblUldDes4, habilitado, this.btnProcurar4);
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
		this.chVideos.setEnabled(true);
		
		this.semanaEnabled(true);
	}
	
	private void semanaEnabled(boolean valor) {
		this.btnProcurar1.setEnabled(this.isApresentacoes() || valor);
		this.btnProcurar2.setEnabled(valor);
		this.btnProcurar3.setEnabled(valor);
		this.btnProcurar4.setEnabled(valor);
		
		this.btnEstudo1.setEnabled(this.isApresentacoes() || valor);
		this.btnEstudo2.setEnabled(valor);
		this.btnEstudo3.setEnabled(valor);
		this.btnEstudo4.setEnabled(valor);
		
		this.cbAjudante1.setEnabled(this.isApresentacoes()  || valor);
		this.cbAjudante2.setEnabled(valor);
		this.cbAjudante3.setEnabled(valor);
		this.cbAjudante4.setEnabled(valor);
		
		this.btnDetalhes1.setEnabled(this.isApresentacoes()  || valor);
		this.btnDetalhes2.setEnabled(valor);
		this.btnDetalhes3.setEnabled(valor);
		this.btnDetalhes4.setEnabled(valor);
		
		if (!valor) {
			this.limparCampos();
		}
	}
	
	private void limparCampos() {
		if (!this.isApresentacoes()) {
			this.lbEstudante1.setText(SELECIONE);
			this.btnEstudo1.setText(BT_ESTUDO_DEF);
			this.cbAjudante1.setSelectedIndex(0);
			this.lblUldDes1.setText(NR_LIMPO);
			this.lblDtUldDes1.setText(DATA_LIMPA);
		}
		
		this.lbEstudante2.setText(SELECIONE);
		this.lbEstudante3.setText(SELECIONE);
		this.lbEstudante4.setText(SELECIONE);
		
		this.btnEstudo2.setText(BT_ESTUDO_DEF);
		this.btnEstudo3.setText(BT_ESTUDO_DEF);
		this.btnEstudo4.setText(BT_ESTUDO_DEF);
		
		this.cbAjudante2.setSelectedIndex(0);
		this.cbAjudante3.setSelectedIndex(0);
		this.cbAjudante4.setSelectedIndex(0);
		
		this.lblUldDes2.setText(NR_LIMPO);
		this.lblUldDes3.setText(NR_LIMPO);
		this.lblUldDes4.setText(NR_LIMPO);
		
		this.lblDtUldDes2.setText(DATA_LIMPA);
		this.lblDtUldDes3.setText(DATA_LIMPA);
		this.lblDtUldDes4.setText(DATA_LIMPA);
	}
	
	@SuppressWarnings("unchecked")
	private void setFields() {
		this.chRecapitulao = new JCheckBox("Recapitulação");
		this.chAssCongr = new JCheckBox("Ass. / Congr.");
		this.chVisSuper = new JCheckBox("Vis. Super.");
		this.chSemReuniao = new JCheckBox("Dia sem reuni\u00E3o");
		this.chVideos = new JCheckBox("Apresentações");
		
		this.chRecapitulao.addActionListener(this);
		this.chAssCongr.addActionListener(this);
		this.chVisSuper.addActionListener(this);
		this.chSemReuniao.addActionListener(this);
		this.chVideos.addActionListener(this);
		
		this.lbEstudante1 = new JLabel(SELECIONE);
		this.lbEstudante2 = new JLabel(SELECIONE);
		this.lbEstudante3 = new JLabel(SELECIONE);
		this.lbEstudante4 = new JLabel(SELECIONE);
		
		this.lblUldDes1 = new JLabel(NR_LIMPO);
		this.lblUldDes2 = new JLabel(NR_LIMPO);
		this.lblUldDes3 = new JLabel(NR_LIMPO);
		this.lblUldDes4 = new JLabel(NR_LIMPO);
		this.lblDtUldDes1 = new JLabel(DATA_LIMPA);
		this.lblDtUldDes2 = new JLabel(DATA_LIMPA);
		this.lblDtUldDes3 = new JLabel(DATA_LIMPA);
		this.lblDtUldDes4 = new JLabel(DATA_LIMPA);

		this.btnEstudo1 = new JButton(BT_ESTUDO_DEF);
		this.btnEstudo2 = new JButton(BT_ESTUDO_DEF);
		this.btnEstudo3 = new JButton(BT_ESTUDO_DEF);
		this.btnEstudo4 = new JButton(BT_ESTUDO_DEF);
		
		Object[] item = {"Selecione Estudante"};
		
		this.cbAjudante1 = new JComboBox(item);
		this.cbAjudante2 = new JComboBox(item);
		this.cbAjudante3 = new JComboBox(item);
		this.cbAjudante4 = new JComboBox(item);
		
		this.dataPanel = new JPanel();
		this.dataPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.txData.setColumns(7);
		this.txData.setEnabled(false);
		this.dataPanel.add(new JLabel("Dia:"), "1, 2");
		this.dataPanel.add(txData, "3, 2");
		
		separator_1 = new JSeparator();
		separator_1.setOrientation(SwingConstants.VERTICAL);
		add(separator_1, "1, 1, 1, 15, right, default");
		
		panel = new JPanel();
		add(panel, "2, 1, 17, 1, fill, fill");
		panel.setLayout(new BorderLayout(0, 0));
		
		separator_3 = new JSeparator();
		panel.add(separator_3, BorderLayout.NORTH);
		
		separator_2 = new JSeparator();
		separator_2.setOrientation(SwingConstants.VERTICAL);
		add(separator_2, "19, 1, 1, 15, left, default");
		
		this.btnProcurar1 = new JButton(Params.btLupaImg());
		this.btnProcurar2 = new JButton(Params.btLupaImg());
		this.btnProcurar3 = new JButton(Params.btLupaImg());
		this.btnProcurar4 = new JButton(Params.btLupaImg());
		
		this.btnProcurar1.setToolTipText("Selecionar estudante.");
		this.btnProcurar2.setToolTipText("Selecionar estudante.");
		this.btnProcurar3.setToolTipText("Selecionar estudante.");
		this.btnProcurar4.setToolTipText("Selecionar estudante.");
		
		this.btnProcurar1.setPreferredSize(new Dimension(25, 25));
		this.btnProcurar2.setPreferredSize(new Dimension(25, 25));
		this.btnProcurar3.setPreferredSize(new Dimension(25, 25));
		this.btnProcurar4.setPreferredSize(new Dimension(25, 25));
		
		this.btnProcurar1.addActionListener(this);
		this.btnProcurar2.addActionListener(this);
		this.btnProcurar3.addActionListener(this);
		this.btnProcurar4.addActionListener(this);
		
		this.btnEstudo1.addActionListener(this);
		this.btnEstudo2.addActionListener(this);
		this.btnEstudo3.addActionListener(this);
		this.btnEstudo4.addActionListener(this);
		
		this.EstudantePanel1 = new JPanel();
		this.EstudantePanel1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("17dlu"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel2 = new JPanel();
		this.EstudantePanel2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("17dlu"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel1.add(this.lbEstudante1, "1, 1");
		this.EstudantePanel1.add(this.btnProcurar1, "3, 1");
		
		this.EstudantePanel2.add(this.lbEstudante2, "1, 1");
		this.EstudantePanel2.add(this.btnProcurar2, "3, 1");
		
		JPanel checkPanel = new JPanel();
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
		
		this.EstudantePanel4 = new JPanel();
		this.EstudantePanel4.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("17dlu"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel4.add(this.lbEstudante4, "1, 1");
		this.EstudantePanel4.add(this.btnProcurar4, "3, 1");
		
		JPanel separatorPanel = new JPanel();
		separatorPanel.setLayout(new BorderLayout(0, 0));
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel.add(separator, BorderLayout.SOUTH);
		
		this.EstudantePanel3 = new JPanel();
		this.EstudantePanel3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("17dlu"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel3.add(this.lbEstudante3, "1, 1");
		this.EstudantePanel3.add(this.btnProcurar3, "3, 1");
		
		this.setDetalhesButtons();
		
		add(this.dataPanel, "5, 2, center, fill");
		add(checkPanel, "6, 2, 12, 1, fill, fill");
		add(new JLabel("Ultima Designa\u00E7\u00E3o"), "7, 4, 3, 1, center, default");
		add(new JLabel("Estudante:"), "5, 6, center, default");
		add(new JLabel("Estudo:"), "7, 6, center, default");
		add(new JLabel("Data:"), "9, 6, center, default");
		add(new JLabel("Estudo:"), "11, 6, center, default");
		add(new JLabel("Ajudante:"), "15, 6, 3, 1, center, default");
		add(new JLabel("Leitura:"), "3, 8, left, default");
		add(this.EstudantePanel1, "5, 8");
		add(this.lblUldDes1, "7, 8, center, default");
		add(this.lblDtUldDes1, "9, 8, right, default");
		add(this.btnEstudo1, "11, 8, fill, default");
		add(this.cbAjudante1, "15, 8, 3, 1, fill, default");
		add(new JLabel("Visita:"), "3, 10, left, default");
		add(this.EstudantePanel2, "5, 10, fill, default");
		add(this.lblUldDes2, "7, 10, center, default");
		add(this.lblDtUldDes2, "9, 10, right, default");
		add(this.btnEstudo2, "11, 10, fill, default");
		add(this.cbAjudante2, "15, 10, 3, 1, fill, default");
		add(new JLabel("Revisita:"), "3, 12, left, default");
		add(this.EstudantePanel3, "5, 12");
		add(this.lblUldDes3, "7, 12, center, default");
		add(this.lblDtUldDes3, "9, 12, right, default");
		add(this.btnEstudo3, "11, 12, fill, center");
		add(this.cbAjudante3, "15, 12, 3, 1");
		add(new JLabel("Estudo:"), "3, 14, left, default");
		add(this.lblUldDes4, "7, 14, center, default");
		add(this.lblDtUldDes4, "9, 14, right, default");
		add(this.btnEstudo4, "11, 14, fill, default");
		add(this.cbAjudante4, "15, 14, 3, 1, fill, default");
		add(separatorPanel, "2, 15, 17, 1, fill, bottom");
		add(this.btnDetalhes1, "13, 8");
		add(this.btnDetalhes2, "13, 10");
		add(this.btnDetalhes3, "13, 12");
		add(this.btnDetalhes4, "13, 14");
		add(this.EstudantePanel4, "5, 14, fill, fill");
	}
		
	private Pessoa obtemPessoa(JComboBox pessoa) {
		Pessoa ajudante = null;
		
		if ( !SELECIONE.equals(pessoa.getSelectedItem()) ) {
			ajudante = this.gerenciador.estudantePorNome((String) pessoa.getSelectedItem());
			
			if (ajudante == null) {
				ajudante = this.gerenciador.ajudantePorNome((String) pessoa.getSelectedItem());
			}
		}
		
		return ajudante;
	}
	
	private void setUltimaDesignacao(Estudante estudante, JLabel estudo, JLabel data) {
		estudo.setText(NR_LIMPO);
		data.setText(DATA_LIMPA);
		
		if (estudante.getUltimaDesignacao() != null) {
			data.setText(estudante.getUltimaDesignacao().format(Params.dateFormate()));
			
			for (Designacao designacao : estudante.getDesignacoes()) {
				if (designacao.getData().equals(estudante.getUltimaDesignacao())) {
					if (designacao.getEstudo() != null) {
						estudo.setText(Integer.toString(designacao.getEstudo().getNrEstudo()));
						
						estudo.setToolTipText(designacao.getObservacao());
						data.setToolTipText(designacao.getObservacao());
						
						Color c = Color.BLACK;
						
						if (designacao.getStatus() == 'F') {
							c = Color.RED;
							
						} else if (designacao.getStatus() == 'P') {
							c = Color.BLUE;
							
						} else if (designacao.getStatus() == 'A') {
							c = Color.GREEN;
						}
						
						data.setForeground(c);
						estudo.setForeground(c);
						break;
					}
				}
			}
		}
	}
	
	private List<EstudoDecorator> carregarEstudos(Estudante estudante) {
		List<EstudoDecorator> estudos = new ArrayList<>();
		
		for (Estudo estudo : this.estudos) {
			EstudoDecorator temp;
			
			Designacao designacao = estudante.getDesignacoes().stream().filter(d->d.getEstudo() != null && d.getEstudo().getNrEstudo() == estudo.getNrEstudo()).findAny().orElse(null);
			
			if (designacao == null) {
				temp = new EstudoDecorator(estudo);
				
			} else {
				temp = new EstudoDecorator(estudo, designacao);
			}
			
			estudos.add(temp);
		}
		
		return estudos;
	}
	
	@SuppressWarnings("unchecked")
	private void setCamposTela(Designacao designacao, JLabel lbEstudante, JButton btnEstudo, JComboBox cbAjudante, JLabel lbData, JLabel lbNr, boolean habilitado, JButton btnProcurar) {
		btnEstudo.setEnabled(habilitado);
		cbAjudante.setEnabled(habilitado);
		btnProcurar.setEnabled(habilitado);
		
		if (designacao.getEstudante() != null) {
			lbEstudante.setText(designacao.getEstudante().getNome());
			
			this.setUltimaDesignacao(designacao.getEstudante(), lbNr, lbData);
			
			this.setAjudantes(designacao, cbAjudante);
		}
		
		if (designacao.getEstudo() != null) {
			btnEstudo.setText("" + designacao.getEstudo().getNrEstudo());
		}
		
		if (designacao.getAjudante() != null) {
			for (int i = 1; i < cbAjudante.getItemCount(); i++) {
				if (cbAjudante.getItemAt(i).equals(designacao.getAjudante().getNome())) {
					cbAjudante.setSelectedIndex(i);
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void setAjudantes(Designacao designacao, JComboBox<String> cbAjudantes) {
		if (designacao.getEstudante() != null) {
			List<String> ajudantes = null;
			
			if (designacao.getNumero() == 1) {
				ajudantes = this.ajudantesHomens;
				
			} else {
				if (designacao.getEstudante().getGenero() == Genero.MASCULINO) {
					ajudantes = this.ajudantes;
					
				} else {
					ajudantes = this.ajudMulheresPrime;
				}
			}
		
			if (ajudantes != null && !ajudantes.isEmpty()) {
				if (designacao.getEstudante() != null) {
					System.out.println(designacao.getId());
					
					ajudantes = ajudantes.stream().filter(p->!designacao.getEstudante().getNome().equals(p)).collect(Collectors.toList());
				}
			
				cbAjudantes.setModel(new DefaultComboBoxModel(ajudantes.toArray()));
			}
		}
	}
	
	private boolean isApresentacoes() {
		return this.chVideos.isSelected() && "A".equals(this.sala);
	}
	
	private void selecionaEstudo(Designacao designacao, JButton btnEstudo) {
		if (designacao.getEstudante() == null) {
			JOptionPane.showMessageDialog(this, "Primeiro selecione um estudante!", "Informação", JOptionPane.INFORMATION_MESSAGE);
			
		} else {
			new EstudosDialog(this.carregarEstudos(designacao.getEstudante()), designacao).setVisible(true);
			
			if (designacao.getEstudo() != null) {
				btnEstudo.setText("" + designacao.getEstudo().getNrEstudo());
				
			} else {
				btnEstudo.setText(BT_ESTUDO_DEF);
			}
		}
	}
}
