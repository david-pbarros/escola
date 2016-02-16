package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.ui.dialog.EstudanteDialog;
import br.com.dbcorp.escolaMinisterio.ui.dialog.HistoricoDesignacaoDialog;
import br.com.dbcorp.escolaMinisterio.ui.model.DesignacaoCellRender;
import br.com.dbcorp.escolaMinisterio.ui.model.DesignacaoTableModel;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudanteTableModel;

@SuppressWarnings("rawtypes")
public class EstudantesUI extends InternalUI implements ActionListener, ListSelectionListener, ItemListener, TableModelListener {
	private static final long serialVersionUID = -3266306781029202761L;

	private Log log = Log.getInstance();
	
	private JTable table;
	private JTable tableEstudo;
	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnRemove;
	private JButton btnSair;
	private JButton btnHistorico;
	private JComboBox cbGenero;
	private JTextField txNome;
	private JTextField txDataDesignacao;
	private JTextArea txObservacao;
	private JTextField txSalaDesignacao;
	
	private EstudanteGerenciador gerenciador;
	private Estudante estudanteSelecionado;
	private Genero genero;
	private List<Estudante> estudantesSelecionados;
	
	public EstudantesUI() {
		super();
		
		this.estudantesSelecionados = new ArrayList<Estudante>();
		this.gerenciador = new EstudanteGerenciador();
		this.genero = Genero.MASCULINO;
		
		this.setButtons();
		
		this.setComboGenero();
		
		this.setFields();
		
		TitledBorder title = BorderFactory.createTitledBorder("Estudantes da Escola");

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
		
		containerPanel.add(commandPanel, BorderLayout.NORTH);
		
		JPanel listPanel = new JPanel(new BorderLayout(0, 0));
		listPanel.add(this.cbGenero, BorderLayout.NORTH);
		listPanel.add(this.setTable(), BorderLayout.CENTER);
		
		JPanel fieldsPanel = new JPanel(new FormLayout("default:grow", "200dlu, 3dlu, 10dlu:grow"));
		
		fieldsPanel.add(listPanel, "1, 1, fill, fill");
		fieldsPanel.add(this.setFieldsPanel(), "1, 3, fill, fill");
		
		containerPanel.add(fieldsPanel, BorderLayout.CENTER);
		
		getContentPane().add(containerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		this.reset();
	}

	@Override
	public void reset() {
		if (this.table.getModel() instanceof EstudanteTableModel) {
			EstudanteTableModel model = (EstudanteTableModel) this.table.getModel();
			model.setItens(this.gerenciador.listarEstudantesTodos(this.genero));
		
		} else {
			EstudanteTableModel model = new EstudanteTableModel(this.gerenciador.listarEstudantesTodos(this.genero));
			model.addTableModelListener(this);
			this.table.setModel(model);
		}
		
		DesignacaoTableModel model = null;
		
		if (this.tableEstudo.getModel() instanceof DesignacaoTableModel) {
			model = (DesignacaoTableModel) this.tableEstudo.getModel();
			
		} else {
			model = new DesignacaoTableModel(this.gerenciador.listarEstudos());
			this.tableEstudo.setModel(model);
		}
		
		if (this.estudanteSelecionado == null) {
			model.setDesignacaoes(new HashMap<Integer, Designacao>());
			
		} else {
			model.setDesignacaoes(this.estudanteSelecionado.getEstudos());
		}
		
		
		this.tableEstudo.setModel(model);
		
		model.fireTableDataChanged();
		
		if (this.estudanteSelecionado == null) {
			this.txNome.setEditable(false);
			this.txDataDesignacao.setEditable(false);
			this.txSalaDesignacao.setEditable(false);
			this.txObservacao.setEditable(false);
			
			this.btnSalvar.setVisible(false);
			this.btnRemove.setVisible(false);
			this.btnHistorico.setVisible(false);
			
		} else {
			this.txNome.setEditable(true);
			this.txNome.setEnabled(false);
			this.txNome.setText(this.estudanteSelecionado.getNome());
			
			this.txDataDesignacao.setEditable(true);
			this.txDataDesignacao.setEnabled(false);
			this.txDataDesignacao.setText(this.estudanteSelecionado.getUltimaDesignacao() == null ? 
											"" : this.estudanteSelecionado.getUltimaDesignacao().format(Params.dateFormate()));
			
			this.txSalaDesignacao.setEditable(true);
			this.txSalaDesignacao.setEnabled(false);
			this.txSalaDesignacao.setText( "" + this.estudanteSelecionado.getSalaUltimaDesignacao());
			
			this.txObservacao.setEditable(true);
			this.txObservacao.setText(this.estudanteSelecionado.getObservacao());
		}
		
		this.setColumnSize();
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnNovo)) {
			this.openDialogNovo();
			
		} else if (event.getSource().equals(this.btnSalvar)) {
			this.atualizarBanco();
		
		} else if (event.getSource().equals(this.btnRemove)) {
			int response = JOptionPane.showConfirmDialog(null, "Confirma Remoção?", "Remover?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.removerBanco();
		    } 
		} else if (event.getSource().equals(this.btnSair)) {
			dispose();
		
		} else if (event.getSource().equals(this.btnHistorico)) {
			this.historicoDesignacoes();
		}
	}
	
	//ListSelectionListener
	public void valueChanged(ListSelectionEvent event) {
		List<Estudante> estudantes = ((EstudanteTableModel) this.table.getModel()).getEstudantes();
		
		if (this.table.getSelectedRow() > -1 && estudantes.size() > this.table.getSelectedRow()) {
			this.estudanteSelecionado = estudantes.get(this.table.getSelectedRow());
			
			this.estudanteSelecionado.reLoad();
			
			this.btnRemove.setVisible(true && Params.canEdit());
			this.btnHistorico.setVisible(true);
		}
		
		this.reset();
	}
	
	//ItemListener
	public void itemStateChanged(ItemEvent event) {
		this.genero = (Genero) event.getItem();
		
		this.reset();
	}
	
	//TableModelListener
	@Override
	public void tableChanged(TableModelEvent event) {
		if (event.getType() == TableModelEvent.UPDATE) {
	    	EstudanteTableModel model = (EstudanteTableModel)event.getSource();
	    	this.estudantesSelecionados.add(model.getEstudantes().get(event.getFirstRow()));
	    	
	    	this.btnSalvar.setVisible(true && Params.canEdit());
	    }
	}
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.getSelectionModel().addListSelectionListener(this);
		
		DefaultCellEditor dce = (DefaultCellEditor)table.getDefaultEditor(Object.class);
		((JTextField)dce.getComponent()).addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				btnSalvar.setVisible(true && Params.canEdit());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {}
		});
		
		DScrollPane scrollPane = new DScrollPane(this.table);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, 100));
		
		return scrollPane;
	}
	
	private void setButtons() {
		this.btnNovo = new JButton(Params.btNovoImg());
		this.btnSalvar = new JButton(Params.btSalvarImg());
		this.btnRemove = new JButton(Params.btRemoverImg());
		this.btnSair = new JButton(Params.btFecharImg());
		this.btnHistorico = new JButton("Inserir Histórico");
		
		this.btnNovo.addActionListener(this);
		this.btnSalvar.addActionListener(this);
		this.btnRemove.addActionListener(this);
		this.btnSair.addActionListener(this);
		this.btnHistorico.addActionListener(this);
		
		this.btnNovo.setToolTipText("Novo");
		this.btnSalvar.setToolTipText("Salvar");
		this.btnRemove.setToolTipText("Remover");
		this.btnSair.setToolTipText("Sair");
		
		this.btnNovo.setVisible(Params.canEdit());
	}
	
	@SuppressWarnings("unchecked")
	private void setComboGenero() {
		this.cbGenero = new JComboBox(new Genero[]{Genero.MASCULINO, Genero.FEMININO});
		this.cbGenero.addItemListener(this);
	}
	
	private JPanel setFieldsPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("right:pref"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("45dlu"),
				FormSpecs.UNRELATED_GAP_COLSPEC,
				ColumnSpec.decode("right:pref"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("15dlu"),
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.PREF_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.PREF_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.PREF_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.PREF_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.PREF_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,}));
		
		panel.add(new JLabel("Nome:"), "2, 1");
		panel.add(this.txNome, "4, 1, 5, 1, fill, fill");
		
		panel.add(new JLabel("Ult. Desig.:"), "2, 3");
		panel.add(this.txDataDesignacao, "4, 3, fill, fill");
		
		panel.add(new JLabel("Obs.:"), "6, 3");
		panel.add(this.txObservacao, "8, 3, 1, 6, fill, fill");
		
		panel.add(new JLabel("Sl. Ult. Desig.:"), "2, 5, right, default");
		
		panel.add(txSalaDesignacao, "4, 5, fill, fill");
		
		panel.add(new JLabel("Designações"), "2, 9");
		panel.add(new JSeparator(), "3, 9, 6, 1, fill, center");
		
		panel.add(this.setPontos(), "2, 11, 7, 1, fill, fill");
		
		return panel;
	}
	
	private void setFields() {
		this.txNome = new JTextField();
		this.txDataDesignacao = new JTextField();
		this.txSalaDesignacao = new JTextField();
		
		this.txObservacao = new JTextArea();
		this.txObservacao.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {
				btnSalvar.setVisible(true && Params.canEdit());
			}
			
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {}
		});
	}
	
	private JPanel setPontos() {
		JPanel panel = new JPanel(new BorderLayout(0, 5));
		
		this.tableEstudo = new JTable();
		this.tableEstudo.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.tableEstudo.setDefaultRenderer(Character.class, new DesignacaoCellRender());
		
		DScrollPane scrollPane = new DScrollPane(this.tableEstudo);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, (this.tableEstudo.getRowHeight() + 29)));
		
		JPanel btnPanel = new JPanel();
		((FlowLayout) btnPanel.getLayout()).setAlignment(FlowLayout.RIGHT);
		btnPanel.add(this.btnHistorico);
		
		panel.add(scrollPane, BorderLayout.CENTER);
		panel.add(btnPanel, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void atualizarBanco() {
		TableCellEditor tce = this.table.getCellEditor();
        if(tce != null) {
            tce.stopCellEditing();
        }
        
		this.estudanteSelecionado.setObservacao(this.txObservacao.getText());
		
		this.gerenciador.atualizar(this.estudanteSelecionado);
		
		if (!this.estudantesSelecionados.isEmpty()) {
			for (Estudante estudante : this.estudantesSelecionados) {
				this.gerenciador.atualizar(estudante);
			}
		}
		
		this.btnSalvar.setVisible(false);
	}
	
	private void removerBanco() {
		this.gerenciador.remover(this.estudanteSelecionado);
		
		this.estudanteSelecionado = null;
		
		int row = this.table.getSelectedRow();
		((EstudanteTableModel) this.table.getModel()).fireTableRowsDeleted(row, row);

		this.reset();
	}
	
	private void openDialogNovo() {
		EstudanteDialog d = new EstudanteDialog(this.gerenciador);
		d.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				reset();
			}
		});
		
		d.setVisible(true);
	}
	
	private void historicoDesignacoes() {
		HistoricoDesignacaoDialog d;
		try {
			d = new HistoricoDesignacaoDialog(this.gerenciador, this.estudanteSelecionado);
		
			d.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent arg0) {
					super.windowClosed(arg0);
					reset();
				}
			});
			
			d.setVisible(true);
			
		} catch (ParseException e) {
			this.log.error("", e);
			
			JOptionPane.showMessageDialog(this, "Não foi posível criar pop-up.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void setColumnSize() {
		TableColumnModel model = this.table.getColumnModel();

		int larguraHab = 150;
		int larguraAju = 0;
		
		if (this.genero == Genero.FEMININO) {
			larguraAju = 150;
		}

		int larguraTela = Params.INTERNAL_WIDTH - larguraHab - larguraAju;
		
		model.getColumn(0).setMinWidth(larguraTela);
		model.getColumn(0).setPreferredWidth(larguraTela);
		model.getColumn(0).setMaxWidth(larguraTela);
		
		model.getColumn(1).setMinWidth(larguraHab);
		model.getColumn(1).setPreferredWidth(larguraHab);
		model.getColumn(1).setMaxWidth(larguraHab);
		
		model.getColumn(2).setMinWidth(larguraAju);
		model.getColumn(2).setPreferredWidth(larguraAju);
		model.getColumn(2).setMaxWidth(larguraAju);
	}
}