package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ContatoDialog extends JDialog {
	private static final long serialVersionUID = -5266823832368284482L;
	
	public ContatoDialog() {
		setTitle("Escola do Ministério Teocrátio - v" + br.com.dbcorp.escolaMinisterio.Params.propriedades().getProperty("versionName"));
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(340, 147));
		this.setMinimumSize(new Dimension(340, 147));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		JButton btnFechar = new JButton("Fechar");
		btnFechar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		
		panel.add(btnFechar);
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormSpecs.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("5dlu"),
				FormSpecs.RELATED_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.LINE_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Contato para Informa\u00E7\u00F5es e Suporte:");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel, "2, 3");
		
		JLabel lblDavidPereiraB = new JLabel("David Barros - Congrega\u00E7\u00E3o Jd. Monte Carlos");
		lblDavidPereiraB.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblDavidPereiraB, "2, 5, center, default");
		
		JLabel lblEmailDavidpbarroshotmailcom = new JLabel("E-mail: david_pbarros@hotmail.com");
		panel_1.add(lblEmailDavidpbarroshotmailcom, "2, 7, center, default");
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}
}
