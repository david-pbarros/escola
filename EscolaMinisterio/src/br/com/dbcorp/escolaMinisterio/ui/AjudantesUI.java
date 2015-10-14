package br.com.dbcorp.escolaMinisterio.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumnModel;

import br.com.dbcorp.escolaMinisterio.dataBase.AjudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.ui.dialog.AjudanteDialog;
import br.com.dbcorp.escolaMinisterio.ui.model.AjudanteTableModel;

public class AjudantesUI extends InternalUI implements TableModelListener, ActionListener, ListSelectionListener {
	private static final long serialVersionUID = -1753463790399252181L;

	private JTable table;
	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnRemove;
	private JButton btnSair;
	
	private AjudanteGerenciador gerenciador;
	private Ajudante ajudanteSelecionado;
	private List<Ajudante> ajudantesSelecionados;
	
	/**
	 * Create the frame.
	 */
	public AjudantesUI() {
		super();
		
		this.gerenciador = new AjudanteGerenciador(); 
		this.ajudantesSelecionados = new ArrayList<Ajudante>();
		
		this.setButtons();
		
		TitledBorder title = BorderFactory.createTitledBorder("Ajudantes da Escola");

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
		containerPanel.add(this.setTable(), BorderLayout.CENTER);
		
		getContentPane().add(containerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		this.reset();
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
		}
	}
	
	//ListSelectionListener
	public void valueChanged(ListSelectionEvent event) {
		List<Ajudante> ajudantes = ((AjudanteTableModel) this.table.getModel()).getAjudantes();
		
		if (this.table.getSelectedRow() > -1 && ajudantes.size() > this.table.getSelectedRow()) {
			this.ajudanteSelecionado = ajudantes.get(this.table.getSelectedRow());
			
			this.btnRemove.setVisible(true);
		}
	}

	//TableModelListener
	public void tableChanged(TableModelEvent event) {
	    if (event.getType() == TableModelEvent.UPDATE) {
	    	AjudanteTableModel model = (AjudanteTableModel)event.getSource();
	    	
	    	if (!model.getAjudantes().isEmpty()) {
	    		this.ajudantesSelecionados.add(model.getAjudantes().get(event.getFirstRow()));
	    		this.btnSalvar.setVisible(true);
	    	}
	    }
	}
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.getSelectionModel().addListSelectionListener(this);
		
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
		
		this.btnNovo.addActionListener(this);
		this.btnSalvar.addActionListener(this);
		this.btnRemove.addActionListener(this);
		this.btnSair.addActionListener(this);
		
		this.btnNovo.setToolTipText("Novo");
		this.btnSalvar.setToolTipText("Salvar");
		this.btnRemove.setToolTipText("Remover");
		this.btnSair.setToolTipText("Sair");
	}
	
	public void reset() {
		if (this.table.getModel() instanceof AjudanteTableModel) {
			AjudanteTableModel model = (AjudanteTableModel) this.table.getModel();
			model.setItens(this.gerenciador.listarAjudantes());
			model.fireTableDataChanged();
		
		} else {
			AjudanteTableModel model = new AjudanteTableModel(this.gerenciador.listarAjudantes());
			model.addTableModelListener(this);
			this.table.setModel(model);
		} 
		
		this.btnSalvar.setVisible(false);
		this.btnRemove.setVisible(false);
		
		this.setColumnSize();
	}
	
	private void setColumnSize() {
		TableColumnModel model = this.table.getColumnModel();
		
		int larguraTela = Params.INTERNAL_WIDTH - 220;
		
		model.getColumn(0).setMinWidth(larguraTela);
		model.getColumn(0).setPreferredWidth(larguraTela);
		model.getColumn(0).setMaxWidth(larguraTela);
		
		model.getColumn(1).setMinWidth(220);
		model.getColumn(1).setPreferredWidth(220);
		model.getColumn(1).setMaxWidth(220);
	}
	
	private void openDialogNovo() {
		AjudanteDialog d = new AjudanteDialog(this.gerenciador);
		d.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				reset();
			}
		});
		
		d.setVisible(true);
	}
	
	private void atualizarBanco() {
		for (Ajudante ajudante : this.ajudantesSelecionados) {
			this.gerenciador.atualizar(ajudante);
		}
		
		this.btnSalvar.setVisible(false);
	}
	
	private void removerBanco() {
		this.gerenciador.remover(this.ajudanteSelecionado);
		
		this.reset();
	}
}
