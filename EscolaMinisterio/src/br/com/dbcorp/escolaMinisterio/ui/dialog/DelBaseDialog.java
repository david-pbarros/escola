package br.com.dbcorp.escolaMinisterio.ui.dialog;

import static br.com.dbcorp.escolaMinisterio.Params.getAppPath;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpecs;
import com.jgoodies.forms.layout.RowSpec;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.Params;

public class DelBaseDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = -5266823832368284482L;
	
	private JButton btnConfirma;
	private JButton btnCancelar;
	private InternalUI internalUI;
	
	public DelBaseDialog(InternalUI mainframe) {
		setTitle("Aviso");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.setPreferredSize(new Dimension(340, 147));
		this.setMinimumSize(new Dimension(340, 147));
		
		this.internalUI = mainframe;
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.SOUTH);

		this.btnConfirma = new JButton("OK");
		this.btnCancelar = new JButton("Cancelar");
		btnCancelar.setHorizontalAlignment(SwingConstants.RIGHT);
		
		this.btnConfirma.addActionListener(this);
		this.btnCancelar.addActionListener(this);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("5dlu"),
				FormSpecs.RELATED_GAP_COLSPEC,
				FormSpecs.DEFAULT_COLSPEC,
				ColumnSpec.decode("144px:grow"),
				FormSpecs.DEFAULT_COLSPEC,
				FormSpecs.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("5dlu"),},
			new RowSpec[] {
				FormSpecs.DEFAULT_ROWSPEC,
				FormSpecs.RELATED_GAP_ROWSPEC,}));

		panel.add(this.btnConfirma, "5, 1, left, top");
		panel.add(this.btnCancelar, "3, 1, left, top");
		
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
		
		JLabel lblLine1 = new JLabel("A base de dados local ser\u00E1 apagada.");
		lblLine1.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblLine1, "2, 3");
		
		JLabel lblLine2 = new JLabel("Ser\u00E1 necess\u00E1rio o sincronismo inicial.");
		lblLine2.setHorizontalAlignment(SwingConstants.CENTER);
		panel_1.add(lblLine2, "2, 5, center, default");
		
		JLabel lblLine3 = new JLabel("Processo Irreverss\u00EDvel, deseja continuar?");
		panel_1.add(lblLine3, "2, 7, center, default");
		
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
			this.internalUI.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			try {
				Path iniPath = Paths.get(getAppPath() + File.separator + "escola.ini");
				BufferedWriter bw = Files.newBufferedWriter(iniPath, StandardOpenOption.APPEND);
				bw.newLine();
				bw.write("resetBase=true");
				bw.flush();
			
				this.internalUI.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "O sistema deve ser reiniciado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
				
			} catch( Exception e) {
				this.internalUI.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				JOptionPane.showMessageDialog(this, "Erro preparando a deleção da base local", "Erro", JOptionPane.ERROR_MESSAGE);
				Log.getInstance().error("", e);
				
			} finally {
				dispose();
			}
		}
	}
}
