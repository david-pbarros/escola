package br.com.dbcorp.escolaMinisterio.ui.designacao;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import br.com.dbcorp.escolaMinisterio.Log;
import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.InternalUI;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.dialog.PrintDialog;

@SuppressWarnings("rawtypes")
public abstract class ADesignacoesUI extends InternalUI implements ActionListener, ItemListener {
	private static final long serialVersionUID = -5374222922400466549L;
	
	protected Log log = Log.getInstance();
	
	protected List<ASemanaUI> semanas;
	protected List<MesDesignacao> mesesDesignacoes;
	
	protected DesignacaoGerenciador gerenciador;
	
	protected MesDesignacao mesDesignacao;
	
	protected JPanel containerPanel;
	protected JPanel buttonPanel;
	protected JComboBox cbMeses;
	protected JComboBox cbSala;
	protected JButton btnSalvar;
	protected JButton btnSair;
	protected JPanel mesPanel;
	protected JScrollPane scroll;
	
	protected static int ANO_ATUAL;
	protected static int MES_ATUAL;
	
	static {
		Calendar c = Calendar.getInstance();
		
		ANO_ATUAL = c.get(Calendar.YEAR);
		MES_ATUAL = c.get(Calendar.MONTH) + 1;
	}
	
	protected boolean editDetalhes;
	
	@SuppressWarnings("unchecked")
	public ADesignacoesUI() {
		super();
		
		this.setButtons();
		
		this.gerenciador = new DesignacaoGerenciador();
		
		this.cbSala = new JComboBox(new String[]{"A", "B"});
		this.cbSala.setPreferredSize(new Dimension(50, 25));
		this.cbSala.addItemListener(this);
		
		this.containerPanel = new JPanel();
		this.containerPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel mesPanel = new JPanel(new BorderLayout(0, 0));
		mesPanel.add(new JLabel("Mês: "), BorderLayout.WEST);
		mesPanel.add(this.setMeses(), BorderLayout.CENTER);
		
		JPanel salaPanel = new JPanel(new BorderLayout(0, 0));
		salaPanel.add(new JLabel(" Sala: "), BorderLayout.WEST);
		salaPanel.add(this.cbSala, BorderLayout.CENTER);
		
		this.buttonPanel = new JPanel();
		this.buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		this.buttonPanel.add(mesPanel);
		this.buttonPanel.add(salaPanel);
		
		JPanel sairPanel = new JPanel();
		sairPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

		sairPanel.add(this.btnSair);
		
		JPanel commandPanel = new JPanel(new BorderLayout(0, 0));
		
		commandPanel.add(this.buttonPanel, BorderLayout.WEST);
		commandPanel.add(sairPanel, BorderLayout.EAST);
		
		this.containerPanel.add(commandPanel, BorderLayout.NORTH);
		
		getContentPane().add(this.containerPanel, BorderLayout.CENTER);
	}
	
	@Override
	public void reset() {
		this.btnSalvar.setVisible(false);

		if (this.mesPanel != null) {
			for (ASemanaUI semana : this.semanas) {
				semana.reset();
			}
			
			this.mesPanel.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH - 50, this.mesPanel.getHeight() + 3));
		}
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		if (event.getSource().equals(this.btnSalvar)) {
			this.salvar();
			
		} else if (event.getSource().equals(this.btnSair)) {
			dispose();
		}
	}
	
	protected void setButtons() {
		this.btnSalvar = new JButton(Params.btSalvarImg());
		this.btnSair = new JButton(Params.btFecharImg());
		
		this.btnSalvar.addActionListener(this);
		this.btnSair.addActionListener(this);
		
		this.btnSalvar.setToolTipText("Salvar");
		this.btnSair.setToolTipText("Sair");
	}
	
	protected JComboBox setMeses() {
		this.cbMeses = new JComboBox();
		this.cbMeses.addItemListener(this);
		this.cbMeses.setPreferredSize(new Dimension(120, 25));
		
		this.getMeses();
		
		this.comboMeses();
		
		return this.cbMeses;
	}
	
	@SuppressWarnings("unchecked")
	protected void comboMeses() {
		if (this.cbMeses.getItemCount() > 0) {
			this.cbMeses.removeAllItems();
		}
		
		this.cbMeses.addItem("Selecione...");
		
		for (MesDesignacao mes : this.mesesDesignacoes) {
			String temp = mes.getMes().toString();
			
			if (mes.getAno() != Calendar.getInstance().get(Calendar.YEAR)) {
				temp += "/" + mes.getAno();
			}
			
			this.cbMeses.addItem(temp);
		}
	}
	
	protected void carregarMes() {
		if (this.mesDesignacao != null) {
			this.montaMes();
		}
	}
	
	protected void montaMes() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		this.semanas = new ArrayList<ASemanaUI>();
		
		this.btnSalvar.setVisible(true);
		
		if ( this.scroll == null ) {
			this.mesPanel = new JPanel();
			
			this.scroll = new JScrollPane();
			this.scroll.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, Params.INTERNAL_HEIGHT));
			this.scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			this.scroll.setViewportView(this.mesPanel);
			
			this.containerPanel.add(scroll, BorderLayout.CENTER);
		
		} else {
			this.mesPanel.removeAll();
		}
		
		this.onMontaMes();

		this.mesPanel.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, 181 * this.mesDesignacao.getSemanas().size()));
		
		this.afterMountMonth();
		
		this.revalidate();
		this.repaint();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	protected void salvar() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		for (ASemanaUI semana : this.semanas) {
			semana.fecharSemana();
		}
		
		this.gerenciador.salvaMesDesignacao(this.mesDesignacao);

		if (this.editDetalhes) {
			this.detalhesClone();
		}
		
		this.afterSave();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	protected void comboChanges(ItemEvent event) {
		if (event.getSource() == this.cbMeses) {
			if ( !"Selecione...".equals(this.cbMeses.getSelectedItem()) && this.cbMeses.getSelectedItem() != null ) {
				this.mesDesignacao = this.mesesDesignacoes.get(this.cbMeses.getSelectedIndex() -1);
				this.carregarMes();
				
			} else {
				if (this.scroll != null) {
					this.containerPanel.remove(this.scroll);
					this.scroll = null;
					
					this.btnSalvar.setVisible(false);
					
					this.afterComboChanges();
					
					this.revalidate();
					this.repaint();
				}
			}
		} else if (event.getSource() == this.cbSala) {
			this.carregarMes();
		}
	}
	
	private void detalhesClone() {
		String salaSelecionada = (String)this.cbSala.getSelectedItem();
		
		for (SemanaDesignacao semana : this.mesDesignacao.getSemanas()) {
			if (semana.getDesignacoes() != null) {
				Map<Integer, Designacao> originais = new HashMap<Integer, Designacao>();
				Map<Integer, Designacao> destinos = new HashMap<Integer, Designacao>();
				
				for (Designacao designacao : semana.getDesignacoes()) {
					if (salaSelecionada.equalsIgnoreCase(designacao.getSala())) {
						originais.put(designacao.getNumero(), designacao);
					
					} else {
						destinos.put(designacao.getNumero(), designacao);
					}
				}
				
				for (Integer key : destinos.keySet()) {
					Designacao origem = originais.get(key);
					
					if (origem != null) {
						Designacao destino = destinos.get(key);
						
						destino.setTema(origem.getTema());
						destino.setFonte(origem.getFonte());
						
						if (origem.isCopiar()) {
							destino.setObsFolha(origem.getObsFolha());
						}
						
					}
				}
			}
		}
		
		this.gerenciador.salvaMesDesignacao(this.mesDesignacao);
	}
	
	protected void imprimir(boolean salvar) {
		if (salvar) {
			this.salvar();
		}
		
		new PrintDialog(this.mesDesignacao).setVisible(true);
	}
	
	protected abstract void getMeses();
	protected abstract void afterMountMonth();
	protected abstract void afterSave();
	protected abstract void afterComboChanges();
	protected abstract void onMontaMes();
}
