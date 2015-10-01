package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import br.com.dbcorp.escolaMinisterio.dataBase.UsuarioGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;
import br.com.dbcorp.escolaMinisterio.exceptions.DuplicateKeyException;
import br.com.dbcorp.escolaMinisterio.ui.Params;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class UsuarioDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private Usuario usuario;
	private JTextField nomeFl;
	private JButton btnOK;
	private JButton btnCancela;
	private JTextField senhaFl;
	private JComboBox<String> cbProfiles;
	
	private UsuarioGerenciador gerenciador;
	private List<Profile> profiles = new ArrayList<Profile>();
	
	public UsuarioDialog(UsuarioGerenciador gerenciador, List<Profile> profiles) {
		setTitle("Novo usuario");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.profiles = profiles;
		this.usuario = new Usuario();
		this.nomeFl = new JTextField();
		this.senhaFl = new JPasswordField();
		this.cbProfiles = new JComboBox<String>();
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.LABEL_COMPONENT_GAP_COLSPEC,
				ColumnSpec.decode("406px:grow"),},
			new RowSpec[] {
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("20px"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("max(9dlu;default)"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		JLabel lblNewLabel = new JLabel("*A senha dever\u00E1 ser trocada no pr\u00F3ximo login.");
		lblNewLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 8));
		lblNewLabel.setForeground(Color.RED);
		
		infoPanel.add(new JLabel("Nome: "), "2, 2, left, center");
		infoPanel.add(this.nomeFl, "4, 2, fill, default");
		infoPanel.add(new JLabel("Senha:"), "2, 4, right, default");
		infoPanel.add(this.senhaFl, "4, 4, fill, default");
		infoPanel.add(lblNewLabel, "3, 5, 2, 1");
		infoPanel.add(new JLabel("Profile:"), "2, 7, right, default");
		infoPanel.add(cbProfiles, "4, 7, fill, default");
		
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		getContentPane().add(infoPanel, BorderLayout.CENTER);
		
		this.setCombos();
		
		pack();
		
		this.setPreferredSize(new Dimension(456, 155));
		this.setMinimumSize(new Dimension(456, 155));
		
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
		if (this.cbProfiles.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Deve ser selecionado um profile.", "Erro!", JOptionPane.ERROR_MESSAGE);
			
		} else {
			this.usuario.setNome(this.nomeFl.getText());
			this.usuario.setReiniciaSenha(true);
			
			try {
				this.usuario.setProfile(this.profiles.get(this.cbProfiles.getSelectedIndex() - 1));
				
				this.usuario.setSenha(this.gerenciador.criptoSenha(this.senhaFl.getText()));
				this.gerenciador.inserir(usuario);
				
				dispose();
			} catch (DuplicateKeyException e) {
				JOptionPane.showMessageDialog(this, "Usuário já existente", "Erro!", JOptionPane.WARNING_MESSAGE);
			
			} catch (NoSuchAlgorithmException e1) {
				JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário", "Erro!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	private void setCombos() {
		this.cbProfiles.addItem("Selecione...");
		
		for (Profile profile : this.profiles) {
			this.cbProfiles.addItem(profile.getNome());
		}
	}
}