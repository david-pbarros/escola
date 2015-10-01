package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;
import br.com.dbcorp.escolaMinisterio.ui.Params;

@SuppressWarnings("rawtypes")
public class EstudanteDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private EstudanteGerenciador gerenciador;
	private Estudante estudante;
	private JTextField nomeFl;
	private JButton btnOK;
	private JButton btnCancela;
	private JComboBox cbGenero;
	
	@SuppressWarnings("unchecked")
	public EstudanteDialog(EstudanteGerenciador gerenciador) {
		setTitle("Novo estudante");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.estudante = new Estudante();
		this.nomeFl = new JTextField();
		this.cbGenero = new JComboBox(new Genero[]{Genero.MASCULINO, Genero.FEMININO});
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		
		this.nomeFl.setColumns(50);
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("Nome: "));
		infoPanel.add(this.nomeFl);
		infoPanel.add(new JLabel("Genero: "));
		infoPanel.add(this.cbGenero);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(infoPanel, BorderLayout.CENTER); 
		
		infoPanel.add(cbGenero);
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
		this.estudante.setNome(this.nomeFl.getText());
		this.estudante.setGenero((Genero) this.cbGenero.getSelectedItem());
		
		try {
			this.gerenciador.inserir(estudante);
			
			dispose();
		} catch (DuplicateKeyException e) {
			JOptionPane.showMessageDialog(this, "Ajudante já existente", "Erro!", JOptionPane.WARNING_MESSAGE);
		}
	}
}