package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoDecorator;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoModalCellRender;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoTableModalModel;

public class EstudosDialog extends JDialog implements ListSelectionListener {
	private static final long serialVersionUID = -1788757880039179872L;
	
	private JTextField textField;
	private JButton btnFiltar;
	private JButton btnOK;
	private JCheckBox chLeitura;
	private JCheckBox chDemostracao;
	private JCheckBox chDiscurso;
	private JTable table;
	
	private List<EstudoDecorator> estudos;
	private EstudoDecorator estudoSelecionado;
	
	public EstudosDialog(List<EstudoDecorator> estudos) {
		int height = new Double(Params.INTERNAL_HEIGHT / 1.1).intValue();
		int width = new Double(Params.INTERNAL_WIDTH / 1.3).intValue();
		
		Dimension d = new Dimension(width, height);
		
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		
		setTitle("Selecione o ponto de Estudo");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.estudos = estudos;
		
		JPanel procuraPanel = new JPanel();
		procuraPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("max(86dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,}));

		JPanel btnPanel = new JPanel();

		this.textField = new JTextField();
		this.chLeitura = new JCheckBox("Leitura");
		this.chDemostracao = new JCheckBox("Demostracao");
		this.chDiscurso = new JCheckBox("Discurso");
		this.btnFiltar = new JButton("Procurar");
		this.btnOK = new JButton("Confirmar");
		
		procuraPanel.add(new JLabel("Descri\u00E7\u00E3o:"), "2, 2, right, default");
		procuraPanel.add(this.textField, "4, 2, 5, 1, fill, default");
		procuraPanel.add(this.btnFiltar, "10, 2, 1, 3");
		procuraPanel.add(new JLabel("Tipo:"), "2, 4, right, default");
		procuraPanel.add(this.chLeitura, "4, 4");
		procuraPanel.add(this.chDemostracao, "6, 4");
		procuraPanel.add(this.chDiscurso, "8, 4");
		
		btnPanel.add(this.btnOK);
		
		getContentPane().add(procuraPanel, BorderLayout.NORTH);
		getContentPane().add(this.setTable(), BorderLayout.CENTER);
		getContentPane().add(btnPanel, BorderLayout.SOUTH);
		
		this.reset();
		
		pack();
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
	
	//ListSelectionListener
	@Override
	public void valueChanged(ListSelectionEvent e) {
		List<EstudoDecorator> estudos = ((EstudoTableModalModel) this.table.getModel()).getEstudos();
		
		if (this.table.getSelectedRow() > -1 && estudos.size() > this.table.getSelectedRow()) {
			this.estudoSelecionado = estudos.get(this.table.getSelectedRow());
		}
	}
	
	public void reset() {
		if (this.table.getModel() instanceof EstudoTableModalModel) {
			EstudoTableModalModel model = (EstudoTableModalModel) this.table.getModel();
			model.setItens(this.estudos);
			model.fireTableDataChanged();
		
		} else {
			EstudoTableModalModel model = new EstudoTableModalModel(this.estudos);
			this.table.setModel(model);
		} 
		
		this.setColumnSize();
	}
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.setDefaultRenderer(Integer.class, new EstudoModalCellRender());
		this.table.setDefaultRenderer(Boolean.class, new EstudoModalCellRender());
		this.table.getSelectionModel().addListSelectionListener(this);
		
		DScrollPane scrollPane = new DScrollPane(this.table);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, 100));
		
		return scrollPane;
	}
	
	private void setColumnSize() {
		TableColumnModel model = this.table.getColumnModel();
		
		int colunmSize1 = 30;
		int colunmSize2 = 70;
		int colunmSize3 = 110;
		int colunmSize4 = 210;
		
		model.getColumn(0).setMinWidth(colunmSize1);
		model.getColumn(0).setPreferredWidth(colunmSize1);
		model.getColumn(0).setMaxWidth(colunmSize1);
		
		model.getColumn(1).setMinWidth(colunmSize2);
		model.getColumn(1).setPreferredWidth(colunmSize2);
		model.getColumn(1).setMaxWidth(colunmSize2);
		
		model.getColumn(2).setMinWidth(colunmSize3);
		model.getColumn(2).setPreferredWidth(colunmSize3);
		model.getColumn(2).setMaxWidth(colunmSize3);
		
		model.getColumn(3).setMinWidth(colunmSize2);
		model.getColumn(3).setPreferredWidth(colunmSize2);
		model.getColumn(3).setMaxWidth(colunmSize2);
		
		model.getColumn(5).setMinWidth(colunmSize4);
		model.getColumn(5).setPreferredWidth(colunmSize4);
		model.getColumn(5).setMaxWidth(colunmSize4);
		
		int larguraTela = this.getPreferredSize().width - (colunmSize1 + colunmSize2 + colunmSize3 + colunmSize4 + 60);
		
		model.getColumn(4).setMinWidth(larguraTela);
		model.getColumn(4).setPreferredWidth(larguraTela);
		model.getColumn(4).setMaxWidth(larguraTela);
	}
}
