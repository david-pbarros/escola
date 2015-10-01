package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import br.com.dbcorp.escolaMinisterio.sincronismo.Sincronizador;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class SincDialog extends JDialog {
	private static final long serialVersionUID = -5266823832368284482L;
	
	private JButton btnFechar;
	
	public static final int LOGIN = 1;
	public static final int GERAL = 2;
	public static final int PRIMEIRO = 3;
	
	protected int tipo;
	
	public SincDialog(int tipo) {
		setTitle("Sincronizador");
		setResizable(false);
		setModal(true);
		
		this.tipo = tipo;
		
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(618, 361));
		this.setMinimumSize(new Dimension(618, 361));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		this.btnFechar = new JButton("Fechar");
		this.btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (tipo == LOGIN || tipo == PRIMEIRO) {
					System.exit(0);
				} else {
					dispose();
				}
			}
		});
		this.btnFechar.setEnabled(false);
		
		panel.add(this.btnFechar);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("5dlu"),
				FormFactory.LINE_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JScrollPane scrollPane = new JScrollPane();
		panel_1.add(scrollPane, "2, 3, fill, fill");
		
		JTextArea textArea = new JTextArea();
		
		DefaultCaret caret = (DefaultCaret)textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		scrollPane.setViewportView(textArea);
		
		this.centerScreen();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				Sincronizador sic = new Sincronizador(textArea);
				
				if (tipo == 1) {
					sic.sincronizarSeguranca();
				} else {
					sic.sincronizar();
				}
				
				btnFechar.setEnabled(true);
				setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
			}
		}).start();
	}
	
	private void centerScreen() {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		
		int w = dimension.width - this.getPreferredSize().width;
		int h = dimension.height - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}
