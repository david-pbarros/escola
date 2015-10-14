package br.com.dbcorp.escolaMinisterio.ui.seguranca;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.ProfileGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.UsuarioGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Profile;
import br.com.dbcorp.escolaMinisterio.entidades.Usuario;
import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.dialog.UsuarioDialog;
import br.com.dbcorp.escolaMinisterio.ui.model.EstudoCellRender;
import br.com.dbcorp.escolaMinisterio.ui.model.UsuarioTableModel;

public class UsuariosUI extends InternalUI implements ActionListener, ListSelectionListener {
	private static final long serialVersionUID = -8357592201532513235L;

	private JButton btnNovo;
	private JButton btnSalvar;
	private JButton btnRemove;
	private JButton btnSair;
	private JComboBox<String> cbProfile;
	private JTable table;
	private JCheckBox chkBloquear;
	private JCheckBox chkSenha;
	
	private Usuario usuarioSelecionado;
	private JTextField txtNome;
	private JPasswordField txtSenha;
	
	private UsuarioGerenciador gerenciador;
	private ProfileGerenciador profGerenciador;
	
	List<Profile> profiles = new ArrayList<Profile>();
	private JLabel lblAvisoSenha;
	
	public UsuariosUI() {
		super();
		
		this.gerenciador = new UsuarioGerenciador();
		this.profGerenciador = new ProfileGerenciador();
		
		this.profiles = this.profGerenciador.listarProfiles();
		
		this.setButtons();
		
		TitledBorder title = BorderFactory.createTitledBorder("Usuários");

		JPanel containerPanel = new JPanel();
		containerPanel.setLayout(new BorderLayout(0, 0));
		containerPanel.setBorder(title);
		
		this.chkBloquear = new JCheckBox("Bloquear Usu\u00E1rio");
		this.chkSenha = new JCheckBox("Mudar senha");
		
		this.chkBloquear.addActionListener(this);
		this.chkSenha.addActionListener(this);
		
		this.lblAvisoSenha = new JLabel("*A senha ser\u00E1 modificada no pr\u00F3ximo logon");
		this.lblAvisoSenha.setForeground(Color.RED);
		this.lblAvisoSenha.setFont(new Font("Tahoma", Font.PLAIN, 9));
		
		this.txtNome = new JTextField();
		this.txtSenha = new JPasswordField();
		this.cbProfile = new JComboBox<String>();
		
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

		JPanel itensPanel = new JPanel();
		itensPanel.setBorder(BorderFactory.createTitledBorder("Dados do usuário:"));
		itensPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		containerPanel.add(commandPanel, BorderLayout.NORTH);
		containerPanel.add(this.setTable(), BorderLayout.WEST);
		containerPanel.add(itensPanel, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		
		panel.add(this.chkBloquear);
		panel.add(this.chkSenha);
		panel.add(this.lblAvisoSenha);

		itensPanel.add(panel, "1, 1, 3, 1, fill, fill");
		itensPanel.add(new JLabel("Nome:"), "1, 2, right, default");
		itensPanel.add(txtNome, "3, 2, fill, default");
		itensPanel.add(new JLabel("Senha:"), "1, 4, right, default");
		itensPanel.add(this.txtSenha, "3, 4, fill, default");
		itensPanel.add(new JLabel("Perfil:"), "1, 6, right, default");
		itensPanel.add(this.cbProfile, "3, 6, fill, default");
		
		getContentPane().add(containerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		this.setCombos();
		
		this.reset();
	}

	@Override
	public void reset() {
		if (this.table.getModel() instanceof UsuarioTableModel) {
			UsuarioTableModel model = (UsuarioTableModel) this.table.getModel();
			model.setItens(this.gerenciador.listarUsuarios());
			model.fireTableDataChanged();
		
		} else {
			UsuarioTableModel model = new UsuarioTableModel(this.gerenciador.listarUsuarios());
			this.table.setModel(model);
		} 
		
		this.lblAvisoSenha.setVisible(false);
		this.chkBloquear.setSelected(false);
		this.chkSenha.setSelected(false);
		this.txtNome.setText("");
		this.txtSenha.setText("");
		this.cbProfile.setSelectedIndex(0);
		
		this.chkBloquear.setEnabled(false);
		this.chkSenha.setEnabled(false);
		this.txtNome.setEnabled(false);
		this.txtSenha.setEnabled(false);
		this.cbProfile.setEnabled(false);
		
		this.btnSalvar.setVisible(false);
		this.btnRemove.setVisible(false);
	}
	
	//ActionListener
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnNovo) {
			this.novo();
			
		} else if (event.getSource() == this.btnRemove) {
			int response = JOptionPane.showConfirmDialog(null, "Confirma Remoção?", "Remover?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.removerBanco();
		    } 
		} else if (event.getSource() == this.btnSalvar) {
			this.salvar();
		
		} else if (event.getSource() == this.chkSenha) {
			if (this.chkSenha.isSelected()) {
				this.lblAvisoSenha.setVisible(true);
				this.txtSenha.setText("");
				this.txtSenha.setEnabled(true);
				this.txtSenha.setEditable(true);
				
			} else {
				this.lblAvisoSenha.setVisible(false);
				this.txtSenha.setEnabled(false);
				this.txtSenha.setEditable(false);
				
			}
		} else if (event.getSource() == this.chkBloquear) {
			if (this.chkBloquear.isSelected()) {
				this.chkSenha.setEnabled(false);
				this.txtNome.setEnabled(false);
				this.txtSenha.setEnabled(false);
				this.cbProfile.setEnabled(false);
				this.lblAvisoSenha.setVisible(false);
				this.chkSenha.setSelected(false);
			
			} else {
				this.chkSenha.setEnabled(true);
				this.txtNome.setEnabled(true);
				this.cbProfile.setEnabled(true);
				this.txtSenha.setEnabled(true);
				this.txtSenha.setEditable(false);
			}
		} else if (event.getSource() == this.btnSair) {
			this.dispose();
		}
	}
	
	//ListSelectionListener
	@Override
	public void valueChanged(ListSelectionEvent event) {
		List<Usuario> Usuarios = ((UsuarioTableModel) this.table.getModel()).getUsuario();
		
		if (this.table.getSelectedRow() > -1 && Usuarios.size() > this.table.getSelectedRow()) {
			this.usuarioSelecionado = Usuarios.get(this.table.getSelectedRow());
			
			this.btnRemove.setVisible(true);
			this.btnSalvar.setVisible(true);
			
			this.setFields();
		}
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
	
	private DScrollPane setTable() {
		this.table = new JTable();
		this.table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
		this.table.setDefaultRenderer(Integer.class, new EstudoCellRender());
		this.table.setDefaultRenderer(Boolean.class, new EstudoCellRender());
		this.table.getSelectionModel().addListSelectionListener(this);
		
		DScrollPane scrollPane = new DScrollPane(this.table);
		scrollPane.setViewportBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		scrollPane.setPreferredSize(new Dimension((Params.INTERNAL_WIDTH / 4), 100));
		
		return scrollPane;
	}
	
	private void novo() {
		UsuarioDialog d = new UsuarioDialog(this.gerenciador, this.profiles);
		d.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				super.windowClosed(arg0);
				reset();
			}
		});
		
		d.setVisible(true);
	}
	
	private void removerBanco() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		this.gerenciador.remover(this.usuarioSelecionado);
		
		this.reset();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void salvar() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		if (this.chkSenha.isSelected()) {
			if (this.txtSenha.getPassword().length == 0) {
				JOptionPane.showMessageDialog(this, "Nenhuma senha informada.", "Use outra...", JOptionPane.WARNING_MESSAGE);
				return;
				
			} else {
				String senha = new String(this.txtSenha.getPassword());
				
				try {
					senha = this.gerenciador.criptoSenha(senha);
					
					if (new Sincronizador().trocaSenha(senha, true, this.usuarioSelecionado)) {
						this.usuarioSelecionado.setSenha(senha);
						this.usuarioSelecionado.setReiniciaSenha(true);
					
					} else {
						JOptionPane.showMessageDialog(this, "Problemas ao trocar a senha online. Tente novamente.", "Erro na troca de senha.", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NoSuchAlgorithmException e) {
					JOptionPane.showMessageDialog(this, "Problemas ao trocar a senha, verifique o log.", "Erro na troca de senha.", JOptionPane.ERROR_MESSAGE);
					Log.getInstance().equals(e);
				}
			}
		}
		
		this.usuarioSelecionado.setNome(this.txtNome.getText());
		this.usuarioSelecionado.setBloqueado(this.chkBloquear.isSelected());
		this.usuarioSelecionado.setProfile(this.profiles.get(this.cbProfile.getSelectedIndex() - 1));
		
		this.gerenciador.salvar(this.usuarioSelecionado);
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		
		this.reset();
	}
	
	private void setFields() {
		this.chkBloquear.setEnabled(true);
		this.chkBloquear.setSelected(this.usuarioSelecionado.isBloqueado());
		
		if (!this.usuarioSelecionado.isBloqueado()) {
			this.chkSenha.setEnabled(true);
			this.txtNome.setEnabled(true);
			this.cbProfile.setEnabled(true);
		
			this.txtSenha.setEnabled(true);
			this.txtSenha.setEditable(false);
		
		} else {
			this.chkSenha.setEnabled(false);
			this.txtNome.setEnabled(false);
			this.cbProfile.setEnabled(false);
			this.txtSenha.setEnabled(false);
			this.txtSenha.setEditable(false);
		}
		
		this.txtNome.setText(this.usuarioSelecionado.getNome());
		this.txtSenha.setText(this.usuarioSelecionado.getSenha());
		
		if (this.profiles.contains(this.usuarioSelecionado.getProfile())) {
			this.cbProfile.setSelectedIndex(this.profiles.indexOf(this.usuarioSelecionado.getProfile()) + 1);
		}
	}
	
	private void setCombos() {
		this.cbProfile.addItem("Selecione...");
		
		for (Profile profile : this.profiles) {
			this.cbProfile.addItem(profile.getNome());
		}
	}
}
