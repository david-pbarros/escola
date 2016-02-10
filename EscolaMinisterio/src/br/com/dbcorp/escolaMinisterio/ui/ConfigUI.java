package br.com.dbcorp.escolaMinisterio.ui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Params;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.ui.dialog.DefragDialog;
import br.com.dbcorp.escolaMinisterio.ui.dialog.DelBaseDialog;

public class ConfigUI extends InternalUI implements ActionListener {
	private static final long serialVersionUID = -6877743249664668740L;
	
	private Gerenciador gerenciador;
	
	private JButton btnDesfragmentar;
	private JButton btnApagarBaseLocal;
	private JTextField txCong;
	private JTextField txServidor;
	private JTextField txHash;
	private JTextField txLog;
	private JTextField txBanco;
	private JButton btnLog;
	private JButton btnBase;
	
	public ConfigUI() {
		this.gerenciador = new Gerenciador();
		
		JPanel dbPanel = new JPanel();
		dbPanel.setBorder(BorderFactory.createTitledBorder("Base de dados"));
		dbPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(150dlu;default)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		JPanel iniPanel = new JPanel();
		iniPanel.setBorder(BorderFactory.createTitledBorder("Dados de inicialização"));
		iniPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.btnDesfragmentar = new JButton("Desfragmentar");
		this.btnApagarBaseLocal = new JButton("Apagar Base Local");
		this.btnLog = new JButton("...");
		this.btnBase = new JButton("...");
		
		this.txCong = new JTextField();
		this.txServidor = new JTextField();
		this.txHash = new JTextField();
		this.txLog = new JTextField();
		this.txBanco = new JTextField();

		this.btnDesfragmentar.addActionListener(this);
		this.btnApagarBaseLocal.addActionListener(this);
		this.btnLog.addActionListener(this);
		this.btnBase.addActionListener(this);
		
		this.txCong.setEditable(false);
		this.txLog.setEditable(false);
		this.txBanco.setEditable(false);
		
		if (this.exibe(this.gerenciador.getUser().getProfile().getItens(), ItensSeg.ITM_DEFRAG)) {
			dbPanel.add(this.btnDesfragmentar, "1, 1");
			dbPanel.add(new JLabel("Desfragmenta a base local apagando registros com erro"), "3, 1");
		
			dbPanel.add(this.btnApagarBaseLocal, "1, 3");
			dbPanel.add(new JLabel("Apagar a base Local. Ser\u00E1 necess\u00E1rio o sincronismo inicial novamente."), "3, 3");
		}

		iniPanel.add(new JLabel("Nr. Congrega\u00E7\u00E3o:"), "1, 1, right, default");
		iniPanel.add(this.txCong, "3, 1, 3, 1, fill, default");
		iniPanel.add(new JLabel("Servidor Sincronismo:"), "1, 3, right, default");
		iniPanel.add(this.txServidor, "3, 3, 3, 1, fill, default");
		iniPanel.add(new JLabel("Chave P\u00FAblica:"), "1, 5, right, default");
		iniPanel.add(this.txHash, "3, 5, 3, 1, fill, default");
		iniPanel.add(new JLabel("Caminho do Log:"), "1, 7, right, default");
		iniPanel.add(this.txLog, "3, 7, fill, default");
		iniPanel.add(this.btnLog, "5, 7");
		iniPanel.add(new JLabel("Caminho do Banco de dados:"), "1, 9, right, default");
		iniPanel.add(this.txBanco, "3, 9, fill, default");
		iniPanel.add(this.btnBase, "5, 9");
		
		getContentPane().add(dbPanel, BorderLayout.NORTH);
		getContentPane().add(iniPanel, BorderLayout.CENTER);
	}	
	
	@Override
	public void reset() {
		this.txCong.setText(Params.propriedades().getProperty("congregacao"));
		this.txServidor.setText(Params.propriedades().getProperty("server"));
		this.txHash.setText(Params.propriedades().getProperty("hash"));
		this.txLog.setText(Params.propriedades().getProperty("logPath"));
		this.txBanco.setText(Params.propriedades().getProperty("javax.persistence.jdbc.url").replace("jdbc:sqlite:", ""));

	}

	//ActionListener
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnDesfragmentar) {
			new DefragDialog(this).setVisible(true);
			
		} else if (event.getSource() == this.btnApagarBaseLocal) {
			new DelBaseDialog(this).setVisible(true);
			
		} else if (event.getSource() == this.btnLog) {
			
		} else if (event.getSource() == this.btnBase) {
			
		}
	}

	private boolean exibe(List<ItemProfile> itens, ItensSeg item) {
		ItemProfile itemP = new ItemProfile(item);
		
		return itens.contains(itemP);
	}
}
