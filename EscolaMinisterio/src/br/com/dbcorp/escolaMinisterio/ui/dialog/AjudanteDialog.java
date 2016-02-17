package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.dbcorp.escolaMinisterio.dataBase.AjudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class AjudanteDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private AjudanteGerenciador gerenciador;
	private Ajudante ajudante;
	private JTextField nomeFl;
	private JButton btnOK;
	private JButton btnCancela;
	
	public AjudanteDialog(AjudanteGerenciador gerenciador) {
		setTitle("Novo ajudante");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.ajudante = new Ajudante();
		this.nomeFl = new JTextField();
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		
		this.nomeFl.setColumns(70);
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("Nome: "));
		infoPanel.add(this.nomeFl);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(infoPanel, BorderLayout.CENTER); 
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
		this.ajudante.setNome(this.nomeFl.getText());
		this.ajudante.setGenero(Genero.MASCULINO);
		
		try {
			if (this.gerenciador.existeEstudante(ajudante)) {
				JOptionPane.showMessageDialog(this, "Pessoa já cadastrada como estudante", "Erro!", JOptionPane.WARNING_MESSAGE);
				
			} else {
				this.gerenciador.inserir(ajudante);
				dispose();
			}
		} catch (DuplicateKeyException e) {
			JOptionPane.showMessageDialog(this, "Ajudante já existente", "Erro!", JOptionPane.WARNING_MESSAGE);
		}
	}
}