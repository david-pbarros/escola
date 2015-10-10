package br.com.dbcorp.escolaMinisterio.ui.dialog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;

import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.ui.Params;

@SuppressWarnings("rawtypes")
public class HistoricoDesignacaoDialog extends JDialog implements ActionListener {
	private static final long serialVersionUID = 7789542379134478478L;
	
	private EstudanteGerenciador gerenciador;
	private Estudante estudante;
	private List<Estudo> estudos;
	private Map<Integer, Estudo> numeroEstudos;
	
	private JComboBox cbNrEstudo;
	private JComboBox cbStatus;
	private JFormattedTextField txData;
	private JTextArea txObservacao;
	private JButton btnOK;
	private JButton btnCancela;
	
	@SuppressWarnings("unchecked")
	public HistoricoDesignacaoDialog(EstudanteGerenciador gerenciador, Estudante estudante) throws ParseException {
		setTitle("Historico Designação Passadas");
		setResizable(false);
		setModal(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.gerenciador = gerenciador;
		this.estudante = estudante;
		this.estudos = this.gerenciador.listarEstudos();
		this.numeroEstudos = new HashMap<Integer, Estudo>();
		this.btnOK = new JButton("Salvar");
		this.btnCancela = new JButton("Cancelar");
		this.txObservacao = new JTextArea();
		this.txData = new JFormattedTextField(new MaskFormatter("##/##/####"));
		this.cbNrEstudo = new JComboBox();
		this.cbStatus = new JComboBox(new String[]{"Passou", "Não passou"});
		
		for (Estudo estudo : this.estudos) {
			if (this.estudante.getEstudos().get(estudo.getNrEstudo()) == null) {
				this.cbNrEstudo.addItem(estudo.getNrEstudo());
				this.numeroEstudos.put(estudo.getNrEstudo(), estudo);
			}
		}
		
		this.btnOK.addActionListener(this);
		this.btnCancela.addActionListener(this);
		
		this.txObservacao.setColumns(40);
		this.txObservacao.setRows(4);
		
		this.txData.setColumns(6);
		
		JPanel linha1Panel = new JPanel();
		linha1Panel.add(new JLabel("Estudo: "));
		linha1Panel.add(this.cbNrEstudo);
		linha1Panel.add(new JLabel("Data Designação: "));
		linha1Panel.add(this.txData);
		linha1Panel.add(new JLabel("Situação: "));
		linha1Panel.add(this.cbStatus);
		
		JPanel linha2Panel = new JPanel();
		linha2Panel.add(new JLabel("Obs.: "));
		linha2Panel.add(this.txObservacao);
		
		JPanel infoPanel = new JPanel(new BorderLayout(0, 0));
		infoPanel.add(linha1Panel, BorderLayout.NORTH);
		infoPanel.add(linha2Panel, BorderLayout.SOUTH);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.add(this.btnOK);
		buttonPanel.add(this.btnCancela);
		
		getContentPane().add(infoPanel, BorderLayout.CENTER); 
		getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		
		pack();
		
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
		try {
			LocalDate data = LocalDate.parse(this.txData.getText(), Params.dateFormate());
			LocalDate c = LocalDate.now();
			
			if (!data.isBefore(c)) {
				JOptionPane.showMessageDialog(this, "A data tem que ser anterior a data atual.", "Data Inválida", JOptionPane.WARNING_MESSAGE);
			
			} else {
				Designacao designacao = new Designacao();
				designacao.setEstudante(this.estudante);
				designacao.setEstudo(this.numeroEstudos.get(this.cbNrEstudo.getSelectedItem()));
				designacao.setObservacao(this.txObservacao.getText().length() > 0 ? this.txObservacao.getText() : null);
				designacao.setData(data);
				
				switch (this.cbStatus.getSelectedIndex()) {
				case 0:
					designacao.setStatus('P');
					break;
				case 1:
					designacao.setStatus('F');
					break;
				}
				
				this.estudante.getDesignacoes().add(designacao);
				this.estudante.onLoad();
				
				this.gerenciador.inserirDesignacao(designacao);
				
				dispose();
			}
		} catch (DateTimeParseException e) {
			JOptionPane.showMessageDialog(this, "Data Inválida.", "Erro", JOptionPane.ERROR_MESSAGE);
		}
	}
}