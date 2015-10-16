package br.com.dbcorp.escolaMinisterio.ui.designacao.nova;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.AvaliacaoDOM;
import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.EscolhaEstudanteDialog;

@SuppressWarnings("rawtypes")
public class SemanaMelhoreUI extends ASemanaUI {
	private static final long serialVersionUID = -7064940912653423525L;
	
	private DesignacaoGerenciador gerenciador;
	private EstudanteGerenciador estudantesGerenciador;
	
	private boolean load = false;
	
	private List<Estudo> estudos;
	private List<String> ajudantesHomens;
	private List<String> ajudantesHomensComplemento;
	private List<String> ajudantesMulheres;
	
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
	private JComboBox cbEstudo1;
	private JComboBox cbEstudo2;
	private JComboBox cbEstudo3;
	private JComboBox cbEstudo4;
	private JComboBox cbAjudante1;
	private JComboBox cbAjudante2;
	private JComboBox cbAjudante3;
	private JCheckBox chHomem;
	private JCheckBox chMulher;
	private JPanel dataPanel;
	private JPanel EstudantePanel1;
	private JPanel EstudantePanel2;
	private JPanel EstudantePanel3;
	private JPanel EstudantePanel4;
	private JSeparator separator_1;
	private JSeparator separator_2;
	private JPanel panel;
	private JSeparator separator_3;
	private JCheckBox chDiscurso;
	private JCheckBox chDemostracao;
	
	public SemanaMelhoreUI(DesignacaoGerenciador gerenciador, EstudanteGerenciador estudanteGerenciador, SemanaDesignacao semanaDesignacao, String sala, List<Estudo> estudos, List<String> ajudantesHomens, List<String> ajudantesMulheres, boolean editDetalhes) {
		this.setMinimumSize(new Dimension(631, 232));
		this.setPreferredSize(new Dimension(931, 260));
		
		this.gerenciador = gerenciador;
		this.estudantesGerenciador = estudanteGerenciador;
		
		this.editDetalhes = editDetalhes;

		this.sala = sala;
		this.semanaDesignacao = semanaDesignacao;
		
		this.txData = new JTextField();
		this.txData.setText(semanaDesignacao.getData().format(Params.dateFormate()));
		
		this.estudos = estudos;
		this.ajudantesHomens = ajudantesHomens;
		this.ajudantesMulheres = ajudantesMulheres;
		this.ajudantesHomensComplemento = this.gerenciador.listaEstudantes(Genero.MASCULINO);
		
		setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("10dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(100dlu;min):grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(33dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("center:12dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(100dlu;min):grow"),
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
				FormSpecs.LINE_GAP_ROWSPEC,
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
				this.semanaEnabled(false);
				
			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chVisSuper) {
			if (this.chVisSuper.isSelected()) {
				this.chRecapitulao.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.chSemReuniao.setEnabled(false);
				this.semanaEnabled(false);

			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chRecapitulao) {
			if (this.chRecapitulao.isSelected()) {
				this.chVisSuper.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.chSemReuniao.setEnabled(false);
				this.semanaEnabled(false);

			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chSemReuniao) {
			if (this.chSemReuniao.isSelected()) {
				this.chRecapitulao.setEnabled(false);
				this.chVisSuper.setEnabled(false);
				this.chAssCongr.setEnabled(false);
				this.semanaEnabled(false);
			} else {
				enableChecks();
			}
		} else if (event.getSource() == this.chHomem) {
			this.chMulher.setSelected(false);
			this.chDiscurso.setSelected(true);
			this.chDemostracao.setSelected(false);
			this.setNumero3();

		} else if (event.getSource() == this.chMulher) {
			this.chHomem.setSelected(false);
			this.setNumero3();
		
		} else if (event.getSource() == this.chDiscurso) {
			this.chDemostracao.setSelected(false);
			
			this.ajudantesDiscurso();
			
		} else if (event.getSource() == this.chDemostracao) {
			this.chDiscurso.setSelected(false);
			
			this.ajudantesDiscurso();
			this.ajudantesComplemento();
			this.ajudantesDemostracao(false);
			
		} else if (event.getSource() == this.btnProcurar1) {
			EscolhaEstudanteDialog d = new EscolhaEstudanteDialog(this.estudantesGerenciador, Genero.MASCULINO);
			this.estudanteSet(d.exibir(), this.lbEstudante1);
			
		} else if (event.getSource() == this.btnProcurar2) {
			EscolhaEstudanteDialog d = new EscolhaEstudanteDialog(this.estudantesGerenciador, Genero.FEMININO);
			
			this.estudanteSet(d.exibir(), this.lbEstudante2);
		
		} else if (event.getSource() == this.btnProcurar3) {
			Genero genero = this.chHomem.isSelected() ? Genero.MASCULINO : Genero.FEMININO;
			
			EscolhaEstudanteDialog d = new EscolhaEstudanteDialog(this.estudantesGerenciador, genero);
			
			this.estudanteSet(d.exibir(), this.lbEstudante3);
		}
	}
	
	private void estudanteSet(Estudante estudante, JLabel estudanteLB) {
		estudanteLB.setText(estudante.getNome());
		
		if (estudanteLB == this.lbEstudante1 && !estudanteLB.equals("Selecione...")) {
			if (this.designacao1 == null) {
				this.designacao1 = new Designacao();
				this.semanaDesignacao.getDesignacoes().add(this.designacao1);
				this.designacao1.setNumero(1);
				this.designacao1.setSala(this.sala);
				this.designacao1.setStatus(AvaliacaoDOM.NAO_AVALIADO.getSigla());
				this.designacao1.setSemana(this.semanaDesignacao);
			}

			this.designacao1.setEstudante(estudante);
			this.setUltimaDesignacao(this.designacao1.getEstudante(), this.lblUldDes1, this.lblDtUldDes1);
			this.carregarEstudos(this.designacao1.getEstudante(), cbEstudo1);
			
		} else if (estudanteLB == this.lbEstudante2 && !estudanteLB.equals("Selecione...")) {
			if (this.designacao2 == null) {
				this.designacao2 = new Designacao();
				this.semanaDesignacao.getDesignacoes().add(this.designacao2);
				this.designacao2.setNumero(2);
				this.designacao2.setSala(this.sala);
				this.designacao2.setStatus(AvaliacaoDOM.NAO_AVALIADO.getSigla());
				this.designacao2.setSemana(this.semanaDesignacao);
			}

			this.designacao2.setEstudante(estudante);
			this.setUltimaDesignacao(this.designacao2.getEstudante(), this.lblUldDes2, this.lblDtUldDes2);
			this.carregarEstudos(this.designacao2.getEstudante(), cbEstudo2);
			
		} else if (estudanteLB == this.lbEstudante3 && !estudanteLB.equals("Selecione...")) {
			if (this.designacao3 == null) {
				this.designacao3 = new Designacao();
				this.semanaDesignacao.getDesignacoes().add(this.designacao3);
				this.designacao3.setNumero(3);
				this.designacao3.setSala(this.sala);
				this.designacao3.setStatus(AvaliacaoDOM.NAO_AVALIADO.getSigla());
				this.designacao3.setSemana(this.semanaDesignacao);
			}

			this.designacao3.setEstudante(estudante);
			this.setUltimaDesignacao(this.designacao3.getEstudante(), this.lblUldDes3, this.lblDtUldDes3);
			this.carregarEstudos(this.designacao3.getEstudante(), cbEstudo3);
		}
	}
	
	public void fecharSemana() {
		this.semanaDesignacao.setAssebleia(this.chAssCongr.isSelected());
		this.semanaDesignacao.setRecapitulacao(this.chRecapitulao.isSelected());
		this.semanaDesignacao.setVisita(this.chVisSuper.isSelected());
		this.semanaDesignacao.setSemReuniao(this.chSemReuniao.isSelected());
		
		if (this.chAssCongr.isSelected() || this.chRecapitulao.isSelected() || this.chVisSuper.isSelected() || this.chSemReuniao.isSelected()) {
			this.semanaDesignacao.setDesignacoesRemovidas(this.semanaDesignacao.getDesignacoes());
			this.semanaDesignacao.setDesignacoes(null);

		} else {
			if (this.designacao1 != null) {
				this.designacao1.setEstudo(this.obtemEstudo(this.cbEstudo1));
				this.designacao1.setAjudante(this.obtemAjudante(this.cbAjudante1));
				this.designacao1.setData(this.semanaDesignacao.getData());
				
			} 
			
			if (this.designacao2 != null) {
				this.designacao2.setEstudo(this.obtemEstudo(this.cbEstudo2));
				this.designacao2.setAjudante(this.obtemPessoa(this.cbAjudante2));
				this.designacao2.setData(this.semanaDesignacao.getData());
				
			}
			
			if (this.designacao3 != null) {
				this.designacao3.setEstudo(this.obtemEstudo(this.cbEstudo3));
				this.designacao3.setData(this.semanaDesignacao.getData());
				
				if (this.chHomem.isSelected()) {
					if (this.ajudantesHomens.contains(this.cbAjudante3.getSelectedItem())) {
						this.designacao3.setAjudante(this.obtemAjudante(this.cbAjudante3));
						
					} else {
						this.designacao3.setAjudante(this.obtemPessoa(this.cbAjudante3));
					}
				} else if (this.chMulher.isSelected()) {
					this.designacao3.setAjudante(this.obtemPessoa(this.cbAjudante3));
				}
			}
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
					this.load = true;
					
					boolean habilitado = designacao.getStatus() == '\u0000' || designacao.getStatus() == 'A';
					
					switch (designacao.getNumero()) {
					case 1:
						this.designacao1 = designacao;
						this.carregarEstudos(designacao1.getEstudante(), this.cbEstudo1);
						this.setCamposTela(designacao, this.lbEstudante1, this.cbEstudo1, this.cbAjudante1, this.lblDtUldDes1, this.lblUldDes1, habilitado, this.btnProcurar1);
						break;
					case 2:
						this.designacao2 = designacao;
						this.carregarEstudos(designacao2.getEstudante(), this.cbEstudo2);
						this.setCamposTela(designacao, this.lbEstudante2, this.cbEstudo2, this.cbAjudante2, this.lblDtUldDes2, this.lblUldDes2, habilitado, this.btnProcurar2);
						break;
					case 3:
						this.designacao3 = designacao;
						if (designacao.getEstudante() != null) {
							switch (designacao.getEstudante().getGenero()) {
							case MASCULINO:
								this.chHomem.setSelected(true);
								
								this.chDiscurso.setSelected(true);
								this.chDemostracao.setSelected(false);
								
								if (designacao.getAjudante() != null && !this.ajudantesHomens.contains(designacao.getAjudante().getNome())) {
									this.chDemostracao.setSelected(true);
									this.chDiscurso.setSelected(false);
								}
								
								break;
							case FEMININO:
								this.chMulher.setSelected(true);
								this.chDemostracao.setSelected(true);
								break;
							}
							
							this.chHomem.setEnabled(habilitado);
							this.chMulher.setEnabled(habilitado);
						}
						this.setNumero3();
						this.carregarEstudos(designacao3.getEstudante(), this.cbEstudo3);
						this.setCamposTela(designacao, this.lbEstudante3, this.cbEstudo3, this.cbAjudante3, this.lblDtUldDes3, this.lblUldDes3, habilitado, this.btnProcurar3);
						break;
					}
					
					this.load = false;
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
		this.chHomem.setEnabled(valor);
		this.chMulher.setEnabled(valor);
		
		this.btnProcurar1.setEnabled(valor);
		this.btnProcurar2.setEnabled(valor);
		this.btnProcurar3.setEnabled(valor);
		this.btnProcurar4.setEnabled(valor);
		
		this.cbEstudo1.setEnabled(valor);
		this.cbEstudo2.setEnabled(valor);
		this.cbEstudo3.setEnabled(valor);
		this.cbEstudo4.setEnabled(valor);
		
		this.cbAjudante1.setEnabled(valor);
		this.cbAjudante2.setEnabled(valor);
		this.cbAjudante3.setEnabled(valor);
		
		this.btnDetalhes1.setEnabled(valor);
		this.btnDetalhes2.setEnabled(valor);
		this.btnDetalhes3.setEnabled(valor);
		this.btnDetalhes4.setEnabled(valor);
		
		if (!valor) {
			this.limparCampos();
		}
	}
	
	private void limparCampos() {
		this.lbEstudante1.setText("Selecione...");
		this.lbEstudante2.setText("Selecione...");
		this.lbEstudante3.setText("Selecione...");
		this.lbEstudante4.setText("Selecione...");
		
		this.cbEstudo1.removeAllItems();
		this.cbEstudo2.removeAllItems();
		this.cbEstudo3.removeAllItems();
		this.cbEstudo4.removeAllItems();
		
		this.cbAjudante1.setSelectedIndex(0);
		this.cbAjudante2.setSelectedIndex(0);
		this.cbAjudante3.setSelectedIndex(0);
		
		this.chHomem.setSelected(false);
		this.chMulher.setSelected(false);
		
		this.lblUldDes1.setText("--");
		this.lblUldDes2.setText("--");
		this.lblUldDes3.setText("--");
		this.lblUldDes4.setText("--");
		
		this.lblDtUldDes1.setText("--/--/----");
		this.lblDtUldDes2.setText("--/--/----");
		this.lblDtUldDes3.setText("--/--/----");
		this.lblDtUldDes4.setText("--/--/----");
	}
	
	@SuppressWarnings("unchecked")
	private void setFields() {
		this.chRecapitulao = new JCheckBox("Recapitulação");
		this.chAssCongr = new JCheckBox("Ass. / Congr.");
		this.chVisSuper = new JCheckBox("Vis. Super.");
		this.chSemReuniao = new JCheckBox("Dia sem reuni\u00E3o");
		
		this.chRecapitulao.addActionListener(this);
		this.chAssCongr.addActionListener(this);
		this.chVisSuper.addActionListener(this);
		this.chSemReuniao.addActionListener(this);
		
		this.lbEstudante1 = new JLabel("Selecione...");
		this.lbEstudante2 = new JLabel("Selecione...");
		this.lbEstudante3 = new JLabel("Selecione...");
		this.lbEstudante4 = new JLabel("Selecione...");
		
		this.lblUldDes1 = new JLabel("--");
		this.lblUldDes2 = new JLabel("--");
		this.lblUldDes3 = new JLabel("--");
		this.lblUldDes4 = new JLabel("--");
		this.lblDtUldDes1 = new JLabel("--/--/----");
		this.lblDtUldDes2 = new JLabel("--/--/----");
		this.lblDtUldDes3 = new JLabel("--/--/----");
		this.lblDtUldDes4 = new JLabel("--/--/----");

		this.cbEstudo1 = new JComboBox();
		this.cbEstudo2 = new JComboBox();
		this.cbEstudo3 = new JComboBox();
		this.cbEstudo4 = new JComboBox();
		this.cbAjudante1 = new JComboBox(this.ajudantesHomens.toArray());
		this.cbAjudante2 = new JComboBox(this.ajudantesMulheres.toArray());
		
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
		
		this.chHomem = new JCheckBox("Homem");
		this.chMulher = new JCheckBox("Mulher");
		
		this.chHomem.addActionListener(this);
		this.chMulher.addActionListener(this);
		
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
		
		this.EstudantePanel1 = new JPanel();
		this.EstudantePanel1.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel2 = new JPanel();
		this.EstudantePanel2.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel3 = new JPanel();
		this.EstudantePanel3.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
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
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		checkPanel.add(this.chRecapitulao, "1, 1");
		checkPanel.add(this.chAssCongr, "3, 1");
		checkPanel.add(this.chVisSuper, "5, 1");
		checkPanel.add(this.chSemReuniao, "7, 1");
		
		this.EstudantePanel3.add(this.lbEstudante3, "1, 1");
		this.EstudantePanel3.add(this.btnProcurar3, "3, 1");
		
		JPanel n3Panel = new JPanel();
		n3Panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		add(n3Panel, "5, 12, fill, fill");
		n3Panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				RowSpec.decode("fill:default"),
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		n3Panel.add(this.chHomem, "1, 1");
		n3Panel.add(this.chMulher, "3, 1");
		n3Panel.add(this.EstudantePanel3, "1, 3, 3, 1, fill, default");
		
		this.cbAjudante3 = new JComboBox(new String[]{"Escolha o Genero"});
		
		JPanel n3Panel2 = new JPanel();
		n3Panel2.setBorder(new LineBorder(new Color(0, 0, 0)));
		n3Panel2.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.chDiscurso = new JCheckBox("Discurso");
		this.chDemostracao = new JCheckBox("Demostra\u00E7\u00E3o");
		
		this.chDiscurso.setEnabled(false);
		this.chDemostracao.setEnabled(false);
		
		this.chDiscurso.addActionListener(this);
		this.chDemostracao.addActionListener(this);
		
		n3Panel2.add(chDiscurso, "1, 1");
		n3Panel2.add(chDemostracao, "3, 1");
		n3Panel2.add(cbAjudante3, "1, 3, 3, 1");
		
		this.EstudantePanel4 = new JPanel();
		this.EstudantePanel4.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.EstudantePanel4.add(this.lbEstudante1, "1, 1");
		this.EstudantePanel4.add(this.btnProcurar4, "3, 1");
		
		JPanel separatorPanel = new JPanel();
		separatorPanel.setLayout(new BorderLayout(0, 0));
		JSeparator separator = new JSeparator();
		separator.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, 5));
		separatorPanel.add(separator, BorderLayout.SOUTH);
		
		this.setDetalhesButtons();
		
		add(this.dataPanel, "5, 2, center, fill");
		add(checkPanel, "7, 2, 11, 1, fill, fill");
		add(new JLabel("Ultima Designa\u00E7\u00E3o"), "7, 4, 3, 1, center, default");
		add(new JLabel("Estudante:"), "5, 6, center, default");
		add(new JLabel("Estudo:"), "7, 6, center, default");
		add(new JLabel("Data:"), "9, 6, center, default");
		add(new JLabel("Estudo:"), "11, 6, center, default");
		add(new JLabel("Ajudante:"), "15, 6, 3, 1, center, default");
		add(new JLabel("Leitura"), "3, 8, left, default");
		add(this.EstudantePanel1, "5, 8");
		add(this.lblUldDes1, "7, 8, center, default");
		add(this.lblDtUldDes1, "9, 8, right, default");
		add(this.cbEstudo1, "11, 8, fill, default");
		add(this.cbAjudante1, "15, 8, 3, 1, fill, default");
		add(new JLabel("Visita"), "3, 10, left, default");
		add(this.EstudantePanel2, "5, 10, fill, default");
		add(this.lblUldDes2, "7, 10, center, default");
		add(this.lblDtUldDes2, "9, 10, right, default");
		add(this.cbEstudo2, "11, 10, fill, default");
		add(this.cbAjudante2, "15, 10, 3, 1, fill, default");
		add(new JLabel("Revisita"), "3, 12, left, default");
		add(this.lblUldDes3, "7, 12, center, default");
		add(this.lblDtUldDes3, "9, 12, right, default");
		add(this.cbEstudo3, "11, 12, fill, center");
		add(new JLabel("Estudo"), "3, 14, left, default");
		add(this.lblUldDes4, "7, 14, center, default");
		add(this.lblDtUldDes4, "9, 14, right, default");
		add(this.cbEstudo4, "11, 14, fill, default");
		add(separatorPanel, "2, 15, 17, 1, fill, bottom");
		add(this.btnDetalhes1, "13, 8");
		add(this.btnDetalhes2, "13, 10");
		add(this.btnDetalhes3, "13, 12");
		add(this.btnDetalhes4, "13, 14");
		add(this.EstudantePanel4, "5, 14, fill, fill");
		add(n3Panel2, "15, 12, 3, 1, fill, fill");
	}
		
	private Ajudante obtemAjudante(JComboBox ajudante) {
		if ( !"Selecione...".equals(ajudante.getSelectedItem()) ) {
			return this.gerenciador.ajudantePorNome((String) ajudante.getSelectedItem());
		}
		
		return null;
	}
	
	private Estudante obtemPessoa(JComboBox pessoa) {
		if ( !"Selecione...".equals(pessoa.getSelectedItem()) ) {
			return this.gerenciador.estudantePorNome((String) pessoa.getSelectedItem());
		}
		
		return null;
	}
	
	private Estudo obtemEstudo(JComboBox estudo) {
		if ( !"Selecione...".equals(estudo.getSelectedItem()) ) {
			return this.gerenciador.estudoPorNumero((Integer) estudo.getSelectedItem());
		}
		
		return null;
	}
	
	private void setNumero3() {
		this.lblDtUldDes3.setText("--/--/----");
		this.lblUldDes3.setText("--");
		this.lblDtUldDes3.setForeground(Color.BLACK);
		this.lblUldDes3.setForeground(Color.BLACK);
		this.lbEstudante3.setText("Selecione...");
		this.cbEstudo3.removeAllItems();
		this.cbAjudante3.removeAllItems();
		this.chDiscurso.setEnabled(false);
		this.chDemostracao.setEnabled(false);
		
		if (this.chHomem.isSelected()) {
			this.chDiscurso.setEnabled(true);
			this.chDemostracao.setEnabled(true);
			
			if (this.chDiscurso.isSelected()) {
				this.ajudantesDiscurso();
				
			} else {
				this.ajudantesDiscurso();
				this.ajudantesComplemento();
				this.ajudantesDemostracao(false);
			}
		} else if (this.chMulher.isSelected()) {
			this.chDemostracao.setSelected(true);
			this.chDiscurso.setSelected(false);
			this.ajudantesDemostracao(true);
		}
	}
	
	private void setUltimaDesignacao(Estudante estudante, JLabel estudo, JLabel data) {
		estudo.setText("--");
		data.setText("--/--/----");
		
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
	
	@SuppressWarnings("unchecked")
	private void carregarEstudos(Estudante estudante, JComboBox estudosCB) {
		estudosCB.removeAllItems();
		
		List<Integer> feitos = new ArrayList<Integer>();
		
		if (!this.load && estudante != null && estudante.getDesignacoes() != null) {
			for (Designacao designacao : estudante.getDesignacoes()) {
				if (designacao.getEstudo() != null && designacao.getStatus() != 'A') {
					feitos.add(designacao.getEstudo().getNrEstudo());
				}
			}
		}
		
		estudosCB.addItem("Selecione...");

		for (Estudo estudo : this.estudos) {
			if (!feitos.contains(estudo.getNrEstudo())) {
				estudosCB.addItem(estudo.getNrEstudo());
			}
		}
	}
	
	private void setCamposTela(Designacao designacao, JLabel lbEstudante, JComboBox cbEstudo, JComboBox cbAjudante, JLabel lbData, JLabel lbNr, boolean habilitado, JButton btnProcurar) {
		cbEstudo.setEnabled(habilitado);
		cbAjudante.setEnabled(habilitado);
		btnProcurar.setEnabled(habilitado);
		
		if (designacao.getEstudante() != null) {
			lbEstudante.setText(designacao.getEstudante().getNome());
			
			this.setUltimaDesignacao(designacao.getEstudante(), lbNr, lbData);
		}
		
		if (designacao.getEstudo() != null) {
			for (int i = 1; i < cbEstudo.getItemCount(); i++) {
				if (cbEstudo.getItemAt(i).equals(designacao.getEstudo().getNrEstudo())) {
					cbEstudo.setSelectedIndex(i);
				}
			}
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
	private void ajudantesDiscurso() {
		this.limpaAjudantesNr3(true);
		
		for (String nome : this.ajudantesHomens) {
			this.cbAjudante3.addItem(nome);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void ajudantesDemostracao(boolean limpar) {
		this.limpaAjudantesNr3(limpar);
		
		for (String nome : this.ajudantesMulheres) {
			if (limpar || !"Selecione...".equalsIgnoreCase(nome)) {
				this.cbAjudante3.addItem(nome);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void ajudantesComplemento() {
		for (String nome : this.ajudantesHomensComplemento) {
			if (!"Selecione...".equalsIgnoreCase(nome)) {
				this.cbAjudante3.addItem(nome);
			}
		}
	}
	
	private void limpaAjudantesNr3(boolean limpar) {
		if (limpar) {
			this.cbAjudante3.removeAllItems();
		}
	}

}
