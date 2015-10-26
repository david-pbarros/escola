package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.model.EscolhaEstudanteCellRender;
import br.com.dbcorp.escolaMinisterio.ui.model.EscolhaEstudanteTableModel;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudanteTableModel;
import javax.swing.JCheckBox;

public class EscolhaEstudanteDialog extends JDialog implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private EstudanteGerenciador gerenciador;
	private Estudante estudante;
	private Genero genero;
	private JTable table;
	private JTextField nomeFl;
	private JButton btnOK;
	private JButton btnFiltro;
	private JCheckBox chkHomem;
	private JCheckBox chkMulher;
	
	public EscolhaEstudanteDialog(EstudanteGerenciador gerenciador, Genero genero) {
		setTitle("Escolher Estudante");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.estudante = new Estudante();
		this.nomeFl = new JTextField();
		this.btnOK = new JButton("Confirmar");
		this.btnFiltro = new JButton("Procurar...");
		this.chkHomem = new JCheckBox("Homem");
		this.chkMulher = new JCheckBox("Mulher");
		
		this.nomeFl.setColumns(50);
		
		this.btnOK.addActionListener(this);
		this.btnFiltro.addActionListener(this);
		this.chkHomem.addActionListener(this);
		this.chkMulher.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("Nome: "));
		infoPanel.add(this.nomeFl);
		infoPanel.add(this.chkHomem);
		infoPanel.add(this.chkMulher);
		infoPanel.add(this.btnFiltro);
		
		if (genero != null) {
			this.chkHomem.setEnabled(false);
			this.chkMulher.setEnabled(false);
			
		} else {
			genero = Genero.MASCULINO;
		}
		
		this.genero = genero;
		
		JPanel listPanel = new JPanel(new BorderLayout(0, 0));
		listPanel.add(this.setTable(), BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		
		getContentPane().add(infoPanel, BorderLayout.NORTH); 
		getContentPane().add(listPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		
		this.setPreferredSize(new Dimension(925, (Params.INTERNAL_HEIGHT - 100)));
		this.setPreferredSize(new Dimension(925, 300));
		
		if (genero == Genero.MASCULINO) {
			this.chkHomem.setSelected(true);
		
		} else {
			this.chkMulher.setSelected(true);
		}
		
		pack();
		
		this.setColumnSize();
		
		this.centerScreen();
	}
	
	public Estudante exibir() {
		this.setVisible(true);
		
		return this.estudante;
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnOK)) {
			dispose();
			
		} else if (event.getSource().equals(this.btnFiltro)) {
			this.setContent(true);
		
		} else if (event.getSource().equals(this.chkHomem) || event.getSource().equals(this.chkMulher)) {
			this.chkMulher.setSelected(event.getSource().equals(this.chkMulher));
			this.chkHomem.setSelected(event.getSource().equals(this.chkHomem));
			
			this.genero = event.getSource().equals(this.chkHomem) ? Genero.MASCULINO : Genero.FEMININO;
			
			this.setContent(!"".equals(this.nomeFl.getText().trim()));
		}
	}
	
	//ListSelectionListener
	public void valueChanged(ListSelectionEvent event) {
		List<Estudante> estudantes = ((EscolhaEstudanteTableModel) this.table.getModel()).getEstudantes();
		
		if (this.table.getSelectedRow() > -1 && estudantes.size() > this.table.getSelectedRow()) {
			this.estudante = estudantes.get(this.table.getSelectedRow());
			this.nomeFl.setText(this.estudante.getNome());
		}
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.setDefaultRenderer(Integer.class, new EscolhaEstudanteCellRender());
		this.table.setDefaultRenderer(String.class, new EscolhaEstudanteCellRender());
		this.table.getSelectionModel().addListSelectionListener(this);
		
		DScrollPane scrollPane = new DScrollPane(this.table);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension(500 , Params.INTERNAL_HEIGHT - 250));
		
		this.setContent(false);
		
		this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		return scrollPane;
	}
	
	private void setContent(boolean filtro) {
		List<Estudante> estudantes = null;
		
		if (filtro) {
			estudantes = this.gerenciador.listarEstudantes(this.genero, this.nomeFl.getText());
			
		} else {
			estudantes = this.gerenciador.listarEstudantes(this.genero);
		}
		
		if (this.table.getModel() instanceof EstudanteTableModel) {
			EscolhaEstudanteTableModel model = (EscolhaEstudanteTableModel) this.table.getModel();
			model.setItens(estudantes);
			model.fireTableDataChanged();
		
		} else {
			EscolhaEstudanteTableModel model = new EscolhaEstudanteTableModel(estudantes);
			this.table.setModel(model);
		} 
	}
	
	private void setColumnSize() {
		TableColumnModel model = this.table.getColumnModel();
		
		model.getColumn(0).setMinWidth(570);
		model.getColumn(0).setPreferredWidth(570);
		model.getColumn(0).setMaxWidth(570);
		
		model.getColumn(1).setMinWidth(120);
		model.getColumn(1).setPreferredWidth(120);
		model.getColumn(1).setMaxWidth(120);
		
		model.getColumn(2).setMinWidth(130);
		model.getColumn(2).setPreferredWidth(130);
		model.getColumn(2).setMaxWidth(130);
		
		model.getColumn(3).setMinWidth(90);
		model.getColumn(3).setPreferredWidth(90);
		model.getColumn(3).setMaxWidth(90);
	}
}