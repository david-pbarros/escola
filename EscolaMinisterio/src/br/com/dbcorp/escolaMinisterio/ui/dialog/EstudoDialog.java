package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudoGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;
import br.com.dbcorp.escolaMinisterio.ui.NumberVerify;
import br.com.dbcorp.escolaMinisterio.ui.NumberVerify.Tipo;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class EstudoDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private EstudoGerenciador gerenciador;
	private Estudo estudo;
	private JTextField nrFl;
	private JTextField descricaoFl;
	private JCheckBox leituraChk;
	private JCheckBox demonstracaoChk;
	private JCheckBox discusorChk;
	private JButton btnOK;
	private JButton btnCancela;
	
	public EstudoDialog(EstudoGerenciador gerenciador) {
		setTitle("Novo ponto de Estudo");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.estudo = new Estudo();
		this.leituraChk = new JCheckBox("Leitura");
		this.demonstracaoChk = new JCheckBox("Demonstação");
		this.discusorChk = new JCheckBox("Discurso");
		this.nrFl = new JTextField();
		this.descricaoFl = new JTextField();
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		
		this.nrFl.setColumns(4);
		this.nrFl.setInputVerifier(new NumberVerify(Tipo.INTEGER));
		this.descricaoFl.setColumns(70);
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("Nº:"));
		infoPanel.add(this.nrFl);
		infoPanel.add(new JLabel("Descrição"));
		infoPanel.add(this.descricaoFl);
		
		JPanel checkPanel = new JPanel();
		checkPanel.setBorder(BorderFactory.createTitledBorder("Tipo de Estudo"));
		checkPanel.add(this.leituraChk);
		checkPanel.add(this.demonstracaoChk);
		checkPanel.add(this.discusorChk);
		
		panel.add(infoPanel, BorderLayout.CENTER);
		panel.add(checkPanel, BorderLayout.SOUTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(panel, BorderLayout.CENTER); 
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		
		pack();
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnOK)) {
			this.inserir();
		} else {
			dispose();
		}
	}
	
	private void inserir() {
		int nrEstudo = Integer.parseInt(this.nrFl.getText());
		this.estudo.setNrEstudo(nrEstudo);
		this.estudo.setDescricao(this.descricaoFl.getText());
		this.estudo.setDemonstracao(this.demonstracaoChk.isSelected());
		this.estudo.setLeitura(this.leituraChk.isSelected());
		this.estudo.setDiscurso(this.discusorChk.isSelected());
		
		try {
			this.gerenciador.inserir(estudo);
			
			dispose();
		} catch (DuplicateKeyException e) {
			JOptionPane.showMessageDialog(this, "Estudo já existente", "Erro!", JOptionPane.WARNING_MESSAGE);
		}
	}
}