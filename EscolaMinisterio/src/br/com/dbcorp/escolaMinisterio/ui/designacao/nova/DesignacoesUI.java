package br.com.dbcorp.escolaMinisterio.ui.designacao.nova;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.dataBase.EstudanteGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.DScrollPane;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ADesignacoesUI;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;
import br.com.dbcorp.escolaMinisterio.ui.dialog.ReuniaoDialog;

public class DesignacoesUI extends ADesignacoesUI {
	private static final long serialVersionUID = 8557021439788640363L;

	private EstudanteGerenciador estudanteGerenciador;
	
	private JButton btnNovo;
	private JButton btnAprovar;
	
	public DesignacoesUI() {
		super();
		
		this.editDetalhes = true;
		
		this.estudanteGerenciador = new EstudanteGerenciador();
		
		TitledBorder title = BorderFactory.createTitledBorder("Novas Designações");
		this.containerPanel.setBorder(title);

		if (this.estudanteGerenciador.isEnable(ItensSeg.NOVA_ADICIONA) && Params.canEdit()) {
			buttonPanel.add(this.btnNovo);
		}
		 
		if (this.estudanteGerenciador.isEnable(ItensSeg.NOVA_SALVA) && Params.canEdit()) {
			buttonPanel.add(this.btnSalvar);
		}
		
		if (this.estudanteGerenciador.isEnable(ItensSeg.NOVA_APROVA) && Params.canEdit()) {
			buttonPanel.add(this.btnAprovar);
		}
		
		setVisible(true);
		
		this.validaMeses();
		
		this.reset();
	}
	
	@Override
	public void reset() {
		super.reset();
		
		this.btnAprovar.setVisible(false);
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);

		if (event.getSource().equals(this.btnNovo)) {
			this.diaReuniao();
			
		} else if (event.getSource().equals(this.btnAprovar)) {
			int response = JOptionPane.showConfirmDialog(null, "Aprovar as designações?", "Aprovar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.salvar();
		    	this.aprovarDesignacao();
		    }
		}
	}
	
	//ItemListener
	public void itemStateChanged(ItemEvent event) {
		this.comboChanges(event);
	}
	
	@Override
	protected void setButtons() {
		super.setButtons();
		
		this.btnNovo = new JButton(Params.btNovoImg());
		this.btnAprovar = new JButton(Params.btAprovarImg());
		
		this.btnNovo.addActionListener(this);
		this.btnAprovar.addActionListener(this);
		
		this.btnNovo.setToolTipText("Abrir Novo Mês");
		this.btnAprovar.setToolTipText("Aprovar Designações");
	}
	
	@Override
	protected void afterComboChanges() {
		this.btnAprovar.setVisible(false);
	}
	
	private void diaReuniao() {
		ReuniaoDialog d = new ReuniaoDialog();
		
		this.mesesDesignacoes.add(this.gerenciador.abrirMes(d.exibir()));

		this.comboMeses();
		
		this.cbMeses.setSelectedIndex(this.cbMeses.getItemCount() - 1);
		
		this.montaMes();
	}
	
	protected void montaMes() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		this.semanas = new ArrayList<ASemanaUI>();
		
		this.btnSalvar.setVisible(true);
		
		List<Estudo> estudos = this.gerenciador.listarEstudos();
		List<String> ajudanteM = this.gerenciador.listaAjudantes(Genero.FEMININO);
		List<String> ajudanteH = this.gerenciador.listaAjudantes(Genero.MASCULINO);
		
		if ( this.scroll == null ) {
			this.mesPanel = new JPanel();
			
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
			
			if (this.mesDesignacao.isMelhoreMinisterio()) {
				semana = new SemanaMelhoreUI(this.gerenciador, this.estudanteGerenciador, semanaD, (String)this.cbSala.getSelectedItem(), estudos, ajudanteH, ajudanteM, this.editDetalhes);
			} else {
				semana = new SemanaUI(this.gerenciador, this.estudanteGerenciador, semanaD, (String)this.cbSala.getSelectedItem(), estudos, ajudanteH, ajudanteM, this.editDetalhes);
			}
			
			this.semanas.add(semana);
			this.mesPanel.add(semana);
		}

		int heigth = this.mesDesignacao.isMelhoreMinisterio() ? 266 : 238;
		
		this.mesPanel.setPreferredSize(new Dimension(Params.INTERNAL_WIDTH, heigth * this.mesDesignacao.getSemanas().size()));
		
		this.revalidate();
		this.repaint();
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void aprovarDesignacao() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		String erro = "";
		
		for (SemanaDesignacao semana : this.mesDesignacao.getSemanas()) {
			for (Designacao designacao : semana.getDesignacoes()) {
				if (designacao.getEstudante() == null || designacao.getEstudo() == null) {
					erro += "\nDia: " + semana.getData().format(Params.dateFormate());

					if (designacao.getData().getYear() > 2015) {
						erro += " - Designação: ";
						
						switch (designacao.getNumero()) {
						case 1:
							erro += "Leitura";
							break;
						case 2:
							erro += "Visita";					
							break;
						case 3:
							erro += "Revisita";
							break;
						case 4:
							erro += "Estudo";
							break;
						}
					} else {
						erro += " - Nr.: " + designacao.getNumero();
					}
				}
			}
		}
		
		if (!"".equals(erro)) {
			JOptionPane.showMessageDialog(this, "Não é possivel aprovar, existe(m) problema(s):" + erro, "Informação", JOptionPane.WARNING_MESSAGE);
		
		} else {
			this.btnAprovar.setVisible(false);
			
			this.mesesDesignacoes.remove(this.mesDesignacao);
			
			this.cbMeses.removeAllItems();
			
			this.comboMeses();
			
			this.cbMeses.setSelectedIndex(0);
			
			this.mesDesignacao.setStatus(DesignacaoGerenciador.DESIGNADO);
			
			for (SemanaDesignacao semana : this.mesDesignacao.getSemanas()) {
				for (Designacao designacao : semana.getDesignacoes()) {
					if (designacao.getEstudante() != null) {
						designacao.getEstudante().setSalaUltimaDesignacao(designacao.getSala().charAt(0));
						designacao.getEstudante().setUltimaDesignacao(designacao.getData());
					}
				}
			}
			
			this.gerenciador.salvaMesDesignacao(mesDesignacao);
			
		}

		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	@Override
	protected void getMeses() {
		this.mesesDesignacoes = this.gerenciador.meses(DesignacaoGerenciador.NOVO);
	}

	@Override
	protected void afterSave() {
		this.btnAprovar.setVisible(true);
	}
	
	private void validaMeses() {
		for (MesDesignacao mes : this.mesesDesignacoes) {
			if (mes.getAno() < ANO_ATUAL || (mes.getAno() == ANO_ATUAL && mes.getMes().getNumero() < MES_ATUAL )) {
				JOptionPane.showMessageDialog(this, "Existe(m) mes(es) que já deveria(m) ter sido aprovado(s)", "Informação", JOptionPane.WARNING_MESSAGE);
				break;
			}
		}
	}
	
	@Override
	protected void afterMountMonth() {}

	@Override
	protected void onMontaMes() {}
}