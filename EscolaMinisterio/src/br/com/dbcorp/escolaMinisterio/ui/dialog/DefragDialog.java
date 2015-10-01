package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.Gerenciador;
import br.com.dbcorp.escolaMinisterio.ui.MainFrame;
import br.com.dbcorp.escolaMinisterio.ui.Params;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

public class DefragDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5266823832368284482L;
	
	private JButton btnConfirma;
	private JButton btnCancelar;
	private MainFrame mainframe;
	
	public DefragDialog(MainFrame mainframe) {
		setTitle("Aviso");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(340, 147));
		this.setMinimumSize(new Dimension(340, 147));
		
		this.mainframe = mainframe;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		this.btnConfirma = new JButton("OK");
		this.btnCancelar = new JButton("Cancelar");
		btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		
		this.btnConfirma.addActionListener(this);
		this.btnCancelar.addActionListener(this);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5dlu"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("144px:grow"),
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("5dlu"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));

		panel.add(this.btnConfirma, "5, 1, left, top");
		panel.add(this.btnCancelar, "3, 1, left, top");
		
		JPanel panel_1 = new JPanel();
		getContentPane().add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				RowSpec.decode("5dlu"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.LINE_GAP_ROWSPEC,}));
		
		JLabel lblNewLabel = new JLabel("Este processo pode demorar alguns minutos.");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblNewLabel, "2, 3");
		
		JLabel lblDavidPereiraB = new JLabel("Alguns dados fragmentados ser\u00E3o apagados da base");
		lblDavidPereiraB.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblDavidPereiraB, "2, 5, center, default");
		
		JLabel lblEmailDavidpbarroshotmailcom = new JLabel("Deseja Continuar?");
		panel_1.add(lblEmailDavidpbarroshotmailcom, "2, 7, center, default");
		
		this.centerScreen();
	}
	
	private void centerScreen() {
		int w = Params.WIDTH - this.getPreferredSize().width;
		int h = Params.HEIGHT - this.getPreferredSize().height;
		setLocation(w/2, h/2);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.btnCancelar) {
			dispose();
			
		} else if (event.getSource() == this.btnConfirma) {
			this.mainframe.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			try {
				Gerenciador gerenciador = new Gerenciador();

				gerenciador.limparDesignacoesNaoVinculadas();
				gerenciador.limparSemEstudo();
				gerenciador.limpaIndevidos();
				gerenciador.limparDuplicados();
			
				this.mainframe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "Processo de de Desfragmentação da Base Finalizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
				
			} catch( Exception e) {
				this.mainframe.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "Erro no Processo de Desfragmentação da Base. Verificar detalhes no Log", "Erro", JOptionPane.ERROR_MESSAGE);
				Log.getInstance().error("", e);
				
			} finally {
				dispose();
			}
		}
	}
}
