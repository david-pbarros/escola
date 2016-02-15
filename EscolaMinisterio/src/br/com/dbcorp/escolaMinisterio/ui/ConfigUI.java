package br.com.dbcorp.escolaMinisterio.ui;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jfree.util.Log;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.IniTools;
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
	
	private String dbPath;
	private String logPath;
	
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
		this.dbPath = Params.propriedades().getProperty("javax.persistence.jdbc.url").replace("jdbc:sqlite:", "");
		this.logPath = Params.propriedades().getProperty("logPath");
		
		this.txCong.setText(Params.propriedades().getProperty("congregacao"));
		this.txServidor.setText(Params.propriedades().getProperty("server"));
		this.txHash.setText(Params.propriedades().getProperty("hash"));
		this.txLog.setText(this.logPath);
		this.txBanco.setText(this.dbPath);
	}

	//ActionListener
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnDesfragmentar) {
			new DefragDialog(this).setVisible(true);
			
		} else if (event.getSource() == this.btnApagarBaseLocal) {
			new DelBaseDialog(this).setVisible(true);
			
		} else if (event.getSource() == this.btnLog) {
			this.logFolderChoose();
			
		} else if (event.getSource() == this.btnBase) {
			this.dataBaseFolderChoose();
		}
	}

	private boolean exibe(List<ItemProfile> itens, ItensSeg item) {
		ItemProfile itemP = new ItemProfile(item);
		
		return itens.contains(itemP);
	}
	
	private void logFolderChoose() {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Caminho do Log");
		fileChooser.setCurrentDirectory(new File(this.logPath));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				String path = fileChooser.getSelectedFile().getAbsolutePath();
				path = path.replace("\\", "/") + "/";
				
				IniTools.modificarValor("logPath", path);
				
				IniTools.incluiLinha("oldLog=" + this.logPath);
				
				this.txLog.setText(path);
				
				JOptionPane.showMessageDialog(this, "Alteração terá efeito após reiniciar o sistema.", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
			
			} catch (IOException ex) {
				String msg = "Erro modificando caminho do log";

				Log.error(msg, ex);
				JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void dataBaseFolderChoose() {
		String dbDirectory = this.dbPath.substring(0, this.dbPath.lastIndexOf("/"));
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Caminho do Banco de Dados");
		fileChooser.setCurrentDirectory(new File(dbDirectory));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			try {
				String path = fileChooser.getSelectedFile().getAbsolutePath();
				path = path.replace("\\", "/") + "/escola.db";
				
				IniTools.modificarValor("javax.persistence.jdbc.url", "jdbc:sqlite:" + path);
				
				IniTools.incluiLinha("oldBD=" + dbDirectory);
				
				this.txBanco.setText(path);
				
				JOptionPane.showMessageDialog(this, "Alteração terá efeito após reiniciar o sistema.", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
			
			} catch (IOException ex) {
				String msg = "Erro modificando caminho da base de dados";

				Log.error(msg, ex);
				JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
