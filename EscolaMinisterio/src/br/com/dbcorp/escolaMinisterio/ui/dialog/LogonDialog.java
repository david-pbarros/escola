package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador.StatusLogon;
import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;
import br.com.dbcorp.escolaMinisterio.ui.Splash;

public class LogonDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2046432224214280872L;
	
	private JTextField txtNome;
	private JPasswordField txtSenha;
	private JButton btnCancelar;
	private JButton btnConfirmar;
	
	private Gerenciador gerenciador;
	private Splash parent;
	
	private boolean continuar;
	
	public LogonDialog(Splash parent) {
		getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormSpecs.PARAGRAPH_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		this.gerenciador = new Gerenciador();
		this.parent = parent;
		
		this.txtNome = new JTextField();
		this.txtSenha = new JPasswordField();
		this.btnCancelar = new JButton("Cancelar");
		this.btnConfirmar = new JButton("Confirmar");
		
		this.getRootPane().setDefaultButton(this.btnConfirmar);
		
		this.btnConfirmar.addActionListener(this);
		this.btnCancelar.addActionListener(this);
		
		JPanel nomePanel = new JPanel();
		nomePanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(30dlu;min)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		nomePanel.add(new JLabel("Nome:"), "1, 1, right, default");
		nomePanel.add(this.txtNome, "3, 1, fill, default");
		
		JPanel senhaPanel = new JPanel();
		senhaPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(30dlu;min)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		senhaPanel.add(new JLabel("Senha:"), "1, 1, right, default");
		senhaPanel.add(this.txtSenha, "3, 1, fill, default");
		
		JPanel cancelarPnel = new JPanel();
		cancelarPnel.add(this.btnCancelar);
		
		JPanel okPanel = new JPanel();
		okPanel.add(this.btnConfirmar);

		getContentPane().add(nomePanel, "2, 2, 3, 1, fill, fill");
		getContentPane().add(senhaPanel, "2, 4, 3, 1, fill, fill");
		getContentPane().add(cancelarPnel, "2, 6, fill, fill");
		getContentPane().add(okPanel, "4, 6, fill, fill");
		
		Dimension d = new Dimension(369, 150);
		
		this.setMinimumSize(d);
		this.setPreferredSize(d);
		
		this.centerScreen();
	}
	
	//ActionListener
	@Override
	public void actionPerformed(ActionEvent event) {
		this.continuar = false;
		
		if (event.getSource() == this.btnConfirmar) {
			try {
				this.parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
				
				String senha = new String(this.txtSenha.getPassword());
				
				this.verificarLogin(this.gerenciador.login(this.txtNome.getText(), senha), this.gerenciador.criptoSenha(senha));
				
			} catch (NoSuchAlgorithmException ex) {
				Log.getInstance().equals(ex);
				
				JOptionPane.showMessageDialog(this, "Erro no processo, verificar o Log", "Não é possível efetuar o logon.", JOptionPane.ERROR_MESSAGE);
			
			} finally {
				this.parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			}
		} else {
			this.dispose();
			this.parent.dispose();
		}
	}
	
	public void verificarLogin(StatusLogon retorno, String senha) throws NoSuchAlgorithmException {
		if (retorno != StatusLogon.VALIDO) {
			if (retorno == StatusLogon.NAO_LOCALIZADO) {
				this.loginOnline(senha);
			
			} else 	if (retorno == StatusLogon.REINICIAR) {
				new SenhaDialog(this).setVisible(true);
				
			} else {
				JOptionPane.showMessageDialog(this, retorno.msg, "Não é possível efetuar o logon.", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			this.continuar = true;
			dispose();
		}
	}
	
	
	public void loginOnline(String senha) throws NoSuchAlgorithmException {
		JOptionPane.showMessageDialog(this, "Usuário e senha não localizado localmente. O sistema tentará procurar na WEB.", "Não é possível efetuar o logon.", JOptionPane.INFORMATION_MESSAGE);
		
		Sincronizador sinc = new Sincronizador();
			
		try {
			if (sinc.login()) {
				new SincDialog().setVisible(true);
				
				this.gerenciador.localizaUsuario(this.gerenciador.getUser().getNome(), senha);
				
				if (this.gerenciador.getUser() != null) {
					StatusLogon retorno = StatusLogon.VALIDO;
					
					if (this.gerenciador.getUser().isReiniciaSenha()) {
						retorno = StatusLogon.REINICIAR;
					}
					
					this.verificarLogin(retorno, senha);
					
				} else {
					this.verificarLogin(StatusLogon.NAO_LOCALIZADO, senha);
				}
				
				
			} else {
				verificarLogin(StatusLogon.NAO_LOCALIZADO_WEB, senha);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Problemas tentando obter login online. Verificar Log", "Logon.", JOptionPane.ERROR_MESSAGE);
			Log.getInstance().error("logon online", e);
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		this.parent.continuar(this.continuar);
	}
	
	public void continuar(boolean continua) {
		if (continua) {
			this.continuar = true;
			this.dispose();
		}
	}
	
	private void centerScreen() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = dimension.width - this.getPreferredSize().width;
		int h = dimension.height - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}
