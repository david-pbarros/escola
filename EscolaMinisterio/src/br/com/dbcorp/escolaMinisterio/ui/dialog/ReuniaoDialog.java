package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import br.com.dbcorp.escolaMinisterio.ui.Params;

public class ReuniaoDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	@SuppressWarnings("rawtypes")
	private JComboBox dias;
	private JButton btnOK;
	private JButton btnCancela;
	private int diaDaSemana;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ReuniaoDialog() {
		setTitle("Escolha o Dia");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.btnOK = new JButton("Continuar");
		this.btnCancela = new JButton("Cancelar");
		this.dias = new JComboBox(new String[]{"Segunda", "Terca", "Quarta", "Quinta", "Sexta"});
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel infoPanel = new JPanel();
		infoPanel.add(new JLabel("Dia da Reunião:"));
		infoPanel.add(this.dias);
		
		panel.add(infoPanel, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(panel, BorderLayout.CENTER); 
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		
		pack();
		
		this.centerScreen();
	}
	
	public int exibir() {
		this.setVisible(true);
		
		return this.diaDaSemana;
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnOK)) {
			this.marcar();
		} 

		dispose();
	}
	
	private void marcar() {
		switch (this.dias.getSelectedIndex()) {
		case 0:
			this.diaDaSemana = Calendar.MONDAY;
			break;
		case 1:
			this.diaDaSemana = Calendar.TUESDAY;
			break;
		case 2:
			this.diaDaSemana = Calendar.WEDNESDAY;
			break;
		case 3:
			this.diaDaSemana = Calendar.THURSDAY;
			break;
		case 4:
			this.diaDaSemana = Calendar.FRIDAY;
			break;
		}
	}
}