package br.com.dbcorp.escolaMinisterio.ui.designacao.historico;

import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.border.TitledBorder;

import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ADesignacoesUI;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;

public class DesignacoesUI extends ADesignacoesUI {
	private static final long serialVersionUID = 8557021439788640363L;
	
	private JButton btnPrint;
	
	public DesignacoesUI() {
		super();
		
		this.editDetalhes = false;
		
		TitledBorder title = BorderFactory.createTitledBorder("Histórico de Designações");
		this.containerPanel.setBorder(title);
		
		if (this.gerenciador.isEnable(ItensSeg.HIST_PRINT)) {
			buttonPanel.add(this.btnPrint);
		}
		
		setVisible(true);
		
		this.reset();
	}
	
	@Override
	public void reset() {
		this.btnPrint.setVisible(false);
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		
		if (event.getSource().equals(this.btnPrint)) {
			this.imprimir(false);
		}
	}
	
	//ItemListener
	public void itemStateChanged(ItemEvent event) {
		this.comboChanges(event);
	}
	
	protected void setButtons() {
		super.setButtons();
		
		this.btnPrint = new JButton(Params.btImprimirImg());
		
		this.btnPrint.addActionListener(this);
		
		this.btnPrint.setToolTipText("Imprimir");
	}
	
	@Override
	protected void getMeses() {
		this.mesesDesignacoes = this.gerenciador.meses(DesignacaoGerenciador.FECHADO);
	}

	@Override
	protected void afterMountMonth() {
		this.btnPrint.setVisible(true);
	}

	@Override
	protected void afterComboChanges() {
		this.btnPrint.setVisible(false);
	}
	
	@Override
	protected void onMontaMes() {
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
	}
	
	@Override
	protected void afterSave() {}
}