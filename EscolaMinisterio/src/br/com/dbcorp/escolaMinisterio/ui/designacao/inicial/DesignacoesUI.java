package br.com.dbcorp.escolaMinisterio.ui.designacao.inicial;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.PrintDialog;
import br.com.dbcorp.escolaMinisterio.ui.dialog.SincDialog;

@SuppressWarnings("rawtypes")
public class DesignacoesUI extends InternalUI implements ActionListener, ItemListener {
	private static final long serialVersionUID = 8557021439788640363L;

	private DesignacaoGerenciador gerenciador;
	
	private List<ASemanaUI> semanas;
	
	private MesDesignacao mesDesignacao;
	
	private TitledBorder title;
	
	private JPanel containerPanel;
	private JComboBox cbSala;
	private JButton btnImprimir;
	private JPanel mesPanel;
	private DScrollPane scroll;
	
	private boolean editDetalhes;
	
	@SuppressWarnings("unchecked")
	public DesignacoesUI() {
		super();
		
		this.setButtons();
		
		this.editDetalhes = false;
		
		this.cbSala = new JComboBox(new String[]{"A", "B"});
		this.cbSala.setPreferredSize(new Dimension(50, 25));
		this.cbSala.addItemListener(this);
		
		this.gerenciador = new DesignacaoGerenciador();
		
		this.title = BorderFactory.createTitledBorder("Sem Designações para o Mês Atual");

		this.containerPanel = new JPanel();
		containerPanel.setBackground(Color.WHITE);
		this.containerPanel.setLayout(new BorderLayout(0, 0));
		this.containerPanel.setBorder(title);
		
		JPanel commandPanel = new JPanel(new BorderLayout(0, 0));
		commandPanel.setBackground(Color.WHITE);
		
		JPanel salaPanel = new JPanel(new BorderLayout(0, 0));
		salaPanel.setBackground(Color.WHITE);
		JLabel label = new JLabel(" Sala: ");
		label.setBackground(Color.WHITE);
		salaPanel.add(label, BorderLayout.WEST);
		salaPanel.add(this.cbSala, BorderLayout.CENTER);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);
		buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		buttonPanel.add(salaPanel);
		
		JPanel sairPanel = new JPanel();
		sairPanel.setBackground(Color.WHITE);
		sairPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		sairPanel.add(this.btnImprimir);
		
		commandPanel.add(buttonPanel, BorderLayout.WEST);
		commandPanel.add(sairPanel, BorderLayout.EAST);
		
		this.containerPanel.add(commandPanel, BorderLayout.NORTH);
		
		getContentPane().add(this.containerPanel, BorderLayout.CENTER);
		
		setVisible(true);
		
		this.reset();
		
		Params.setSincronizado(true);
		
		Boolean sinc = (Boolean)br.com.dbcorp.escolaMinisterio.Params.propriedades().get("doSinc");
		
		if (sinc == null) {
			br.com.dbcorp.escolaMinisterio.Params.setOffLineMode();
			
			JOptionPane.showMessageDialog(null, "Não foi possível conectar ao servidor de dados. Não será possivel modificar os dados!", "Erro", JOptionPane.WARNING_MESSAGE);
			
		} else if (sinc) {
			int response = JOptionPane.showConfirmDialog(null, "Recomendamos um sincronismo com a WEB. Deseja Realizar?", "Sincronizar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    
			if (response == JOptionPane.YES_OPTION) {
		    	new SincDialog(SincDialog.GERAL).setVisible(true);
		    
			} else {
		    	Params.setSincronizado(false);
		    	JOptionPane.showMessageDialog(null, "Só será possivel modificar dados após um sincronismo!", "Aviso", JOptionPane.WARNING_MESSAGE);
				
		    }
		}
	}
	
	@Override
	public void reset() {
		if (this.mesPanel != null) {
			for (ASemanaUI semana : this.semanas) {
				semana.reset();
			}
			
			this.mesPanel.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, this.mesPanel.getHeight()));
		}
		
		this.mesDesignacao = this.gerenciador.mesAtua();
		
		if (this.mesDesignacao != null) {
			this.title.setTitle("Designações do Mês de " + this.mesDesignacao.getMes().toString());
			
			this.montaMes();
		}
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnImprimir)) {
			if (this.mesDesignacao != null) {
				new PrintDialog(this.mesDesignacao).setVisible(true);
			}
		} 
	}
	
	//ItemListener
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == this.cbSala) {
			this.montaMes();
		}
	}
	
	private void setButtons() {
		this.btnImprimir = new JButton(Params.btImprimirImg());
		this.btnImprimir.addActionListener(this);
		this.btnImprimir.setToolTipText("Sair");
	}
	
	private void montaMes() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		this.semanas = new ArrayList<ASemanaUI>();
		
		if ( this.scroll == null ) {
			this.mesPanel = new JPanel();
			this.mesPanel.setBackground(Color.WHITE);
			
			this.scroll = new DScrollPane();
			this.scroll.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, Params.INTERNAL_HEIGHT));
			this.scroll.setHorizontalScrollBarPolicy(DScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.scroll.setViewportView(this.mesPanel);
			
			this.containerPanel.add(scroll, BorderLayout.CENTER);
		
		} else {
			this.mesPanel.removeAll();
		}
		
		for (SemanaDesignacao semanaD : this.mesDesignacao.getSemanas()) {
			ASemanaUI semana;
			
			if (this.mesDesignacao.getAno() > 2015) {
				semana = new SemanaMelhoreUI(semanaD, (String)this.cbSala.getSelectedItem(), this.editDetalhes);
				
			} else {
				semana = new SemanaUI(semanaD, (String)this.cbSala.getSelectedItem(), this.editDetalhes);
			}
			
			this.semanas.add(semana);
			this.mesPanel.add(semana);
		}

		this.mesPanel.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, (this.mesDesignacao.getAno() > 2015 ? 214 : 181) * this.mesDesignacao.getSemanas().size()));
		
		this.revalidate();
		this.repaint();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
}