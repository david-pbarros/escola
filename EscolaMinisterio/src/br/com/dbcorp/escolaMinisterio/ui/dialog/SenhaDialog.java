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

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;
import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;

public class SenhaDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 2046432224214280872L;
	
	private JPasswordField txtNovaSenha;
	private JPasswordField txtReentra;
	private JButton btnCancelar;
	private JButton btnConfirmar;
	
	private Gerenciador gerenciador;
	
	private LogonDialog parent;
	
	private boolean trocada;
	
	public SenhaDialog(LogonDialog parent) {
		setTitle("Reiniciar Senha");
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
		
		this.txtNovaSenha = new JPasswordField();
		this.txtReentra = new JPasswordField();
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
		
		nomePanel.add(new JLabel("Nova Senha:"), "1, 1, right, default");
		nomePanel.add(this.txtNovaSenha, "3, 1, fill, default");
		
		JPanel senhaPanel = new JPanel();
		senhaPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("max(30dlu;min)"),
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,}));
		
		senhaPanel.add(new JLabel("Conrima Senha:"), "1, 1, right, default");
		senhaPanel.add(this.txtReentra, "3, 1, fill, default");
		
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
		this.trocada = false;
		
		if (event.getSource() == this.btnConfirmar) {
			String senha1 = new String(this.txtNovaSenha.getPassword());
			String senha2 = new String(this.txtReentra.getPassword());
			
			if (senha1.length() == 0) {
				JOptionPane.showMessageDialog(this, "Nenhuma senha informada.", "Use outra...", JOptionPane.WARNING_MESSAGE);
				
			} else if (senha1.equals(senha2)) {
				try {
					this.parent.setCursor(new Cursor(Cursor.WAIT_CURSOR));
					
					String senha = this.gerenciador.criptoSenha(new String(this.txtNovaSenha.getPassword()));
					
					if(new Sincronizador().trocaSenha(senha, false, null)) {
						this.gerenciador.trocaSenha(senha);
						
						this.trocada = true;
						dispose();
					
					} else {
						JOptionPane.showMessageDialog(this, "Problemas ao trocar a senha online. Tente novamente.", "Erro na troca de senha.", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NoSuchAlgorithmException e) {
					JOptionPane.showMessageDialog(this, "Problemas ao trocar a senha, verifique o log.", "Erro na troca de senha.", JOptionPane.ERROR_MESSAGE);
					Log.getInstance().error("trocar senha", e);
				
				} finally {
					this.parent.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
 			} else {
				JOptionPane.showMessageDialog(this, "Senhas informadas não conferem.", "Erro na troca de senha.", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			this.dispose();
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		this.parent.continuar(this.trocada);
	}
	
	private void centerScreen() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = dimension.width - this.getPreferredSize().width;
		int h = dimension.height - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}
