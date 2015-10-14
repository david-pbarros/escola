package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.dataBase.ProfileGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ProfileDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private ProfileGerenciador gerenciador;
	private Profile profile;
	private JTextField nomeFl;
	private JButton btnOK;
	private JButton btnCancela;
	
	public ProfileDialog(ProfileGerenciador gerenciador) {
		setTitle("Novo profile");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.profile = new Profile();
		this.nomeFl = new JTextField();
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("406px:grow"),},
			new RowSpec[] {
				FormSpecs.LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),}));
		
		infoPanel.add(new JLabel("Nome: "), "2, 2, left, center");
		infoPanel.add(this.nomeFl, "4, 2, fill, default");
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		
		pack();
		
		this.setPreferredSize(new Dimension(456, 90));
		this.setMinimumSize(new Dimension(456, 90));
		
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
		this.profile.setNome(this.nomeFl.getText());
		
		try {
			this.gerenciador.inserir(profile);
			
			dispose();
		} catch (DuplicateKeyException e) {
			JOptionPane.showMessageDialog(this, "Usuário já existente", "Erro!", JOptionPane.WARNING_MESSAGE);
		}
	}
}