package br.com.dbcorp.escolaMinisterio.ui.seguranca;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.dataBase.ProfileGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.dialog.ProfileDialog;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoCellRender;
import br.com.dbcorp.escolaMinisterio.ui.model.ProfileTableModel;

public class ProfilesUI extends InternalUI implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = -8357592201532513235L;

	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnRemove;
	private JButton btnSair;
	private JTable table;
	private JCheckBox chkMenuProg;
	private JCheckBox chkItmNova;
	private JCheckBox chkItmAvaliar;
	private JCheckBox chkItmHistorico;
	private JCheckBox chkMenuCad;
	private JCheckBox chkItmEstudante;
	private JCheckBox chkItmAjudante;
	private JCheckBox chkItmEstudo;
	private JCheckBox chkItmSeguranca;
	private JCheckBox chkItmImportar;
	private JCheckBox chkItmDefrag;
	private JCheckBox chkNovaAdiciona;
	private JCheckBox chkNovaSalvar;
	private JCheckBox chkNovaAprovar;
	private JCheckBox chkAvaSalvar;
	private JCheckBox chkAvaPrint;
	private JCheckBox chkAvaReabre;
	private JCheckBox chkAvaAprova;
	private JCheckBox chkAvaExport;
	private JCheckBox chkHstPrint;
	
	private Profile profileSelecionado;
	private ProfileGerenciador gerenciador;
	
	private Map<JCheckBox, ItensSeg> checkProfile;
	private Map<ItensSeg, JCheckBox> profileCheck;
	
	public ProfilesUI() {
		super();
		
		this.gerenciador = new ProfileGerenciador();
		
		this.setButtons();
		
		TitledBorder title = BorderFactory.createTitledBorder("Perfis");

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout(0, 0));
		containerPanel.setBorder(title);
		
		JPanel commandPanel = new JPanel(new BorderLayout(0, 0));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		buttonPanel.add(this.btnNovo);
		buttonPanel.add(this.btnSalvar);
		buttonPanel.add(this.btnRemove);
		
		JPanel sairPanel = new JPanel();
		sairPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		sairPanel.add(this.btnSair);
		
		commandPanel.add(buttonPanel, BorderLayout.WEST);
		commandPanel.add(sairPanel, BorderLayout.EAST);

		JPanel itensPanel = new JPanel();
		itensPanel.setBorder(BorderFactory.createTitledBorder("Itens de acesso:"));
		itensPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				RowSpec.decode("default:grow"),
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_ROWSPEC,}));
		
		containerPanel.add(commandPanel, BorderLayout.NORTH);
		containerPanel.add(this.setTable(), BorderLayout.CENTER);
		containerPanel.add(itensPanel, BorderLayout.SOUTH);
		
		this.chkMenuProg = new JCheckBox("Menu Programa");
		this.chkMenuCad = new JCheckBox("Menu Cadastro");
		this.chkItmNova = new JCheckBox("Nova Designa\u00E7\u00E3o:");
		this.chkItmEstudante = new JCheckBox("Estudante");
		this.chkItmAjudante = new JCheckBox("Ajudante");
		this.chkItmEstudo = new JCheckBox("Estudo");
		this.chkItmSeguranca = new JCheckBox("Seguran\u00E7a");
		this.chkItmDefrag = new JCheckBox("Desfragmentar Base");
		this.chkItmImportar = new JCheckBox("Importar");
		this.chkItmHistorico = new JCheckBox("Historico Designa\u00E7\u00E3o:");
		this.chkItmAvaliar = new JCheckBox("Avaliar Designa\u00E7\u00E3o:");
		this.chkAvaPrint = new JCheckBox("Imprimir");
		this.chkAvaSalvar = new JCheckBox("Salvar");
		this.chkNovaSalvar = new JCheckBox("Salvar");
		this.chkNovaAdiciona = new JCheckBox("Adicionar M\u00EAs");
		this.chkNovaAprovar = new JCheckBox("Aprovar");
		this.chkAvaReabre = new JCheckBox("Reabrir");
		this.chkAvaAprova = new JCheckBox("Aprovar");
		this.chkAvaExport = new JCheckBox("Exportar");
		this.chkHstPrint = new JCheckBox("Imprimir");
		
		this.chkMenuProg.addActionListener(this);
		this.chkMenuCad.addActionListener(this);
		this.chkItmNova.addActionListener(this);
		this.chkItmEstudante.addActionListener(this);
		this.chkItmAjudante.addActionListener(this);
		this.chkItmEstudo.addActionListener(this);
		this.chkItmSeguranca.addActionListener(this);
		this.chkItmImportar.addActionListener(this);
		this.chkItmAvaliar.addActionListener(this);
		this.chkItmHistorico.addActionListener(this);
		this.chkNovaSalvar.addActionListener(this);
		this.chkAvaSalvar.addActionListener(this);
		
		this.setItens();
		
		JPanel programaPanel1 = new JPanel();
		programaPanel1.setLayout(new FormLayout(new ColumnSpec[] {
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
				RowSpec.decode("default:grow"),}));
		
		JPanel programaPanel2 = new JPanel();
		programaPanel2.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
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
				RowSpec.decode("default:grow"),}));
		
		JPanel programaPanel3 = new JPanel();
		programaPanel3.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("default:grow"),}));
		
		JPanel cadastroPanel = new JPanel();
		cadastroPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
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
		
		JPanel opcaoPanel = new JPanel();
		opcaoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		programaPanel1.add(this.chkItmNova, "3, 1");
		programaPanel1.add(this.chkNovaSalvar, "5, 1");
		programaPanel1.add(this.chkNovaAdiciona, "7, 1");
		programaPanel1.add(this.chkNovaAprovar, "9, 1");
		
		programaPanel2.add(this.chkItmAvaliar, "3, 1");
		programaPanel2.add(this.chkAvaPrint, "5, 1");
		programaPanel2.add(this.chkAvaSalvar, "7, 1");
		programaPanel2.add(this.chkAvaReabre, "9, 1");
		programaPanel2.add(this.chkAvaAprova, "11, 1");
		programaPanel2.add(this.chkAvaExport, "13, 1");
		
		programaPanel3.add(this.chkItmHistorico, "3, 1");
		programaPanel3.add(this.chkHstPrint, "5, 1");
		
		cadastroPanel.add(this.chkItmEstudante, "3, 1");
		cadastroPanel.add(this.chkItmAjudante, "5, 1");
		cadastroPanel.add(this.chkItmEstudo, "7, 1");
		cadastroPanel.add(this.chkItmSeguranca, "9, 1");
		cadastroPanel.add(this.chkItmImportar, "11, 1");
		
		opcaoPanel.add(this.chkItmDefrag, "3, 1");
		
		itensPanel.add(this.chkMenuProg, "2, 1");
		itensPanel.add(programaPanel1, "2, 2, fill, fill");
		itensPanel.add(programaPanel2, "2, 3, fill, fill");
		itensPanel.add(programaPanel3, "2, 4, fill, fill");
		itensPanel.add(this.chkMenuCad, "2, 5");
		itensPanel.add(cadastroPanel, "2, 6, fill, fill");
		itensPanel.add(new JLabel("Menu Op\u00E7\u00F5es"), "2, 7");
		itensPanel.add(opcaoPanel, "2, 8, fill, fill");

		getContentPane().add(containerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		this.reset();
	}

	@Override
	public void reset() {
		if (this.table.getModel() instanceof ProfileTableModel) {
			ProfileTableModel model = (ProfileTableModel) this.table.getModel();
			model.setItens(this.gerenciador.listarProfiles());
			model.fireTableDataChanged();
		
		} else {
			ProfileTableModel model = new ProfileTableModel(this.gerenciador.listarProfiles());
			this.table.setModel(model);
		} 
		
		this.btnSalvar.setVisible(false);
		this.btnRemove.setVisible(false);
		
		for (ItensSeg item : this.profileCheck.keySet()) {
			this.profileCheck.get(item).setSelected(false);
			this.profileCheck.get(item).setEnabled(false);
		}
	}
	
	//ActionListener
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnNovo) {
			this.novo();
			
		} else if (event.getSource() == this.btnRemove) {
			int response = JOptionPane.showConfirmDialog(null, "Confirma Remoção?", "Remover?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.removerBanco();
		    } 
		} else if (event.getSource() == this.btnSalvar) {
			this.salvar();
		
		} else if (event.getSource() == this.btnSair) {
			this.dispose();
		
		} else if (event.getSource() == this.chkItmNova || event.getSource() == this.chkItmAvaliar || event.getSource() == this.chkItmHistorico) {
			this.setProgramaSub();
			this.subNova();
			this.subAvalia();
			this.subHist();
			
		} else if (event.getSource() == this.chkItmEstudante || event.getSource() == this.chkItmAjudante || event.getSource() == this.chkItmEstudo ||
				event.getSource() == this.chkItmSeguranca || event.getSource() == this.chkItmImportar) {
			this.setCadastroSub();
			
		} else if (event.getSource() == this.chkMenuCad) {
			this.setCadastro();
			
		} else if (event.getSource() == this.chkMenuProg) {
			this.setPrograma();
		
		} else if (event.getSource() == this.chkNovaSalvar) {
			this.subNovaSave();
		
		} else if (event.getSource() == this.chkAvaSalvar) {
			this.subAvaSave();
		}
	}
	
	//ListSelectionListener
	@Override
	public void valueChanged(ListSelectionEvent event) {
		List<Profile> profiles = ((ProfileTableModel) this.table.getModel()).getProfile();
		
		if (this.table.getSelectedRow() > -1 && profiles.size() > this.table.getSelectedRow()) {
			this.profileSelecionado = profiles.get(this.table.getSelectedRow());
			
			this.setFields();
			
			this.setCadastro();
			this.setPrograma();
			this.setProgramaSub();
			this.setCadastroSub();
			
			this.btnRemove.setVisible(true && Params.isOnLineMode());
			this.btnSalvar.setVisible(true && Params.isOnLineMode());
		}
	}
	
	private void setButtons() {
		this.btnNovo = new JButton(Params.btNovoImg());
		this.btnSalvar = new JButton(Params.btSalvarImg());
		this.btnRemove = new JButton(Params.btRemoverImg());
		this.btnSair = new JButton(Params.btFecharImg());
		
		this.btnNovo.addActionListener(this);
		this.btnSalvar.addActionListener(this);
		this.btnRemove.addActionListener(this);
		this.btnSair.addActionListener(this);
		
		this.btnNovo.setToolTipText("Novo");
		this.btnSalvar.setToolTipText("Salvar");
		this.btnRemove.setToolTipText("Remover");
		this.btnSair.setToolTipText("Sair");
		
		this.btnNovo.setVisible(Params.isOnLineMode());
	}
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.setDefaultRenderer(Integer.class, new EstudoCellRender());
		this.table.setDefaultRenderer(Boolean.class, new EstudoCellRender());
		this.table.getSelectionModel().addListSelectionListener(this);
		
		DScrollPane scrollPane = new DScrollPane(this.table);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH / 3, 100));
		
		return scrollPane;
	}
	
	private void novo() {
		ProfileDialog d = new ProfileDialog(this.gerenciador);
		d.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				reset();
			}
		});
		
		d.setVisible(true);
	}
	
	private void removerBanco() {
		this.gerenciador.remover(this.profileSelecionado);
		
		this.reset();
	}
	
	private void setItens() {
		this.checkProfile = new HashMap<JCheckBox, ItensSeg>();
		this.profileCheck = new HashMap<ItensSeg, JCheckBox>();
		
		this.checkProfile.put(this.chkMenuProg, ItensSeg.MENU_PROGRAMA);
		this.checkProfile.put(this.chkItmNova, ItensSeg.ITM_NOVA);
		this.checkProfile.put(this.chkItmAvaliar, ItensSeg.ITM_AVALIA);
		this.checkProfile.put(this.chkItmHistorico, ItensSeg.ITM_HISTORICO);
		this.checkProfile.put(this.chkMenuCad, ItensSeg.MENU_CADASTRO);
		this.checkProfile.put(this.chkItmEstudante, ItensSeg.ITM_ESTUDANTES);
		this.checkProfile.put(this.chkItmAjudante, ItensSeg.ITM_AJUDANTES);
		this.checkProfile.put(this.chkItmEstudo, ItensSeg.ITM_ESTUDO);
		this.checkProfile.put(this.chkItmSeguranca, ItensSeg.ITM_SEGURANCA);
		this.checkProfile.put(this.chkItmImportar, ItensSeg.ITM_IMPORTAR);
		this.checkProfile.put(this.chkItmDefrag, ItensSeg.ITM_DEFRAG);
		this.checkProfile.put(this.chkNovaSalvar, ItensSeg.NOVA_SALVA);
		this.checkProfile.put(this.chkNovaAdiciona, ItensSeg.NOVA_ADICIONA);
		this.checkProfile.put(this.chkNovaAprovar, ItensSeg.NOVA_APROVA);
		this.checkProfile.put(this.chkAvaSalvar, ItensSeg.AVALIA_SALVA);
		this.checkProfile.put(this.chkAvaPrint, ItensSeg.AVALIA_PRINT);
		this.checkProfile.put(this.chkAvaReabre, ItensSeg.AVALIA_REABRE);
		this.checkProfile.put(this.chkAvaAprova, ItensSeg.AVALIA_APROVA);
		this.checkProfile.put(this.chkAvaExport, ItensSeg.AVALIA_EXPORT);
		this.checkProfile.put(this.chkHstPrint, ItensSeg.HIST_PRINT);
		
		this.profileCheck.put(ItensSeg.MENU_PROGRAMA, this.chkMenuProg);
		this.profileCheck.put(ItensSeg.ITM_NOVA, this.chkItmNova);
		this.profileCheck.put(ItensSeg.ITM_AVALIA, this.chkItmAvaliar);
		this.profileCheck.put(ItensSeg.ITM_HISTORICO, this.chkItmHistorico);
		this.profileCheck.put(ItensSeg.MENU_CADASTRO, this.chkMenuCad);
		this.profileCheck.put(ItensSeg.ITM_ESTUDANTES, this.chkItmEstudante);
		this.profileCheck.put(ItensSeg.ITM_AJUDANTES, this.chkItmAjudante);
		this.profileCheck.put(ItensSeg.ITM_ESTUDO, this.chkItmEstudo);
		this.profileCheck.put(ItensSeg.ITM_SEGURANCA, this.chkItmSeguranca);
		this.profileCheck.put(ItensSeg.ITM_IMPORTAR, this.chkItmImportar);
		this.profileCheck.put(ItensSeg.ITM_DEFRAG, this.chkItmDefrag);
		this.profileCheck.put(ItensSeg.NOVA_SALVA, this.chkNovaSalvar);
		this.profileCheck.put(ItensSeg.NOVA_ADICIONA,this.chkNovaAdiciona);
		this.profileCheck.put(ItensSeg.NOVA_APROVA, this.chkNovaAprovar);
		this.profileCheck.put(ItensSeg.AVALIA_SALVA, this.chkAvaSalvar);
		this.profileCheck.put(ItensSeg.AVALIA_PRINT, this.chkAvaPrint);
		this.profileCheck.put(ItensSeg.AVALIA_REABRE, this.chkAvaReabre);
		this.profileCheck.put(ItensSeg.AVALIA_APROVA, this.chkAvaAprova);
		this.profileCheck.put(ItensSeg.AVALIA_EXPORT, this.chkAvaExport);
		this.profileCheck.put(ItensSeg.HIST_PRINT, this.chkHstPrint);
	}
	
	private void setFields() {
		for (ItensSeg item :  this.profileCheck.keySet()) {
			this.profileCheck.get(item).setEnabled(true);
			this.profileCheck.get(item).setSelected(false);
		}
		
		for (ItemProfile item : this.profileSelecionado.getItens()) {
			this.profileCheck.get(item.getItem()).setSelected(true);
		}
	}
	
	private void salvar() {
		List<ItemProfile> itensProfiles = this.profileSelecionado.getItens();
		List<ItemProfile> removidos = new ArrayList<ItemProfile>();
		
		
		for (JCheckBox item : this.checkProfile.keySet()) {
			ItemProfile itmPro = new ItemProfile();
			itmPro.setItem(this.checkProfile.get(item));
			
			if (itensProfiles.contains(itmPro)) {
				itmPro = itensProfiles.get(itensProfiles.indexOf(itmPro));
			};
			
			if (item.isSelected()) {
				if (itmPro.getId() == 0) {
					itensProfiles.add(itmPro);
					itmPro.setProfile(this.profileSelecionado);
				}
			} else {
				if (itmPro.getId() != 0) {
					itensProfiles.remove(itmPro);
					removidos.add(itmPro);
				}
			}
		}
		
		this.profileSelecionado.setItens(itensProfiles);
		
		this.gerenciador.salvar(this.profileSelecionado);
		
		for (ItemProfile item : removidos) {
			this.gerenciador.removerItem(item);
		}
		
		this.reset();
	}
	
	private void setEnable(boolean isEnable, JCheckBox...boxes ) {
		for (JCheckBox box : boxes) {
			box.setEnabled(isEnable);
		}
	}
	
	private void setSelected(boolean isSelected, JCheckBox...boxes ) {
		for (JCheckBox box : boxes) {
			box.setSelected(isSelected);
		}
	}
	
	private void setPrograma() {
		boolean enable = false;
		
		if (this.chkMenuProg.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkItmNova, this.chkItmAvaliar, this.chkItmHistorico);
		
		if (!enable) {
			this.setSelected(enable, this.chkItmNova, this.chkItmAvaliar, this.chkItmHistorico);
		}
		
		this.subNova();
		this.subAvalia();
		this.subHist();
	}
	
	private void setCadastro() {
		boolean enable = false;
		
		if (this.chkMenuCad.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkItmEstudante, this.chkItmAjudante, this.chkItmEstudo, this.chkItmSeguranca, this.chkItmImportar);
		
		if (!enable) {
			this.setSelected(enable, this.chkItmEstudante, this.chkItmAjudante, this.chkItmEstudo, this.chkItmSeguranca, this.chkItmImportar);
		}
	}
	
	private void setProgramaSub() {
		if (!this.chkItmNova.isSelected() && !this.chkItmAvaliar.isSelected() & !this.chkItmHistorico.isSelected()) {
			this.chkMenuProg.setSelected(false);
			
			this.setEnable(false, this.chkItmNova, this.chkItmAvaliar, this.chkItmHistorico, this.chkNovaAdiciona, this.chkNovaSalvar, this.chkNovaAprovar, 
					this.chkAvaSalvar, this.chkAvaPrint, this.chkAvaReabre, this.chkAvaAprova, this.chkAvaExport, this.chkHstPrint);
		}
	}
	
	private void setCadastroSub() {
		if (!this.chkItmEstudante.isSelected() && !this.chkItmAjudante.isSelected() && !this.chkItmEstudo.isSelected() &&
				!this.chkItmSeguranca.isSelected() && !this.chkItmImportar.isSelected()) {
			
			this.chkMenuCad.setSelected(false);
			
			this.setEnable(false, this.chkItmEstudante, this.chkItmAjudante, this.chkItmEstudo, this.chkItmSeguranca, this.chkItmImportar);
		}
	}
	
	private void subNova() {
		boolean enable = false;
		
		if (this.chkItmNova.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkNovaSalvar);
		
		if (!enable) {
			this.setSelected(enable, this.chkNovaSalvar);
		}
		
		this.subNovaSave();
	}
	
	private void subAvalia() {
		boolean enable = false;
		
		if (this.chkItmAvaliar.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkAvaSalvar, this.chkAvaPrint);
		
		if (!enable) {
			this.setSelected(enable, this.chkAvaSalvar, this.chkAvaPrint);
		}
		
		this.subAvaSave();
	}
	
	private void subNovaSave() {
		boolean enable = false;
		
		if (this.chkNovaSalvar.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkNovaAdiciona, this.chkNovaAprovar);
		
		if (!enable) {
			this.setSelected(enable, this.chkNovaAdiciona, this.chkNovaAprovar);
		}
	}
	
	private void subAvaSave() {
		boolean enable = false;
		
		if (this.chkAvaSalvar.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable,  this.chkAvaReabre, this.chkAvaAprova, this.chkAvaExport);
		
		if (!enable) {
			this.setSelected(enable, this.chkAvaReabre, this.chkAvaAprova, this.chkAvaExport);
		}
	}
	
	private void subHist() {
		boolean enable = false;
		
		if (this.chkItmHistorico.isSelected()) {
			enable = true;
		}
		
		this.setEnable(enable, this.chkHstPrint);
		
		if (!enable) {
			this.setSelected(enable, this.chkHstPrint);
		}
	}
}
