package br.com.dbcorp.escolaMinisterio.ui.designacao.avaliacao;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import br.com.dbcorp.escolaMinisterio.dataBase.DesignacaoGerenciador;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.ItemProfile.ItensSeg;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;
import br.com.dbcorp.escolaMinisterio.ui.Params;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ADesignacoesUI;
import br.com.dbcorp.escolaMinisterio.ui.designacao.ASemanaUI;

public class DesignacoesUI extends ADesignacoesUI {
	private static final long serialVersionUID = 8557021439788640363L;

	private JButton btnRegresso;
	private JButton btnPrint;
	private JButton btnExport;
	private JButton btnFechar;
	
	public DesignacoesUI() {
		super();
		
		this.editDetalhes = true;
		
		TitledBorder title = BorderFactory.createTitledBorder("Avaliar Designações");
		this.containerPanel.setBorder(title);
		
		if (this.gerenciador.isEnable(ItensSeg.AVALIA_SALVA)) {
			this.buttonPanel.add(this.btnSalvar);
		}
		
		if (this.gerenciador.isEnable(ItensSeg.AVALIA_REABRE)) {
			this.buttonPanel.add(this.btnRegresso);
		}
		
		if (this.gerenciador.isEnable(ItensSeg.AVALIA_APROVA)) {
			this.buttonPanel.add(this.btnFechar);
		}
		
		if (this.gerenciador.isEnable(ItensSeg.AVALIA_PRINT)) {
			this.buttonPanel.add(this.btnPrint);
		}

		if (this.gerenciador.isEnable(ItensSeg.AVALIA_EXPORT)) {
			this.buttonPanel.add(this.btnExport);
		}
		
		setVisible(true);
		
		this.validaMeses();
		
		this.reset();
	}
	
	@Override
	public void reset() {
		super.reset();
		
		this.btnRegresso.setVisible(false);
		this.btnPrint.setVisible(false);
		this.btnExport.setVisible(false);
		this.btnFechar.setVisible(false);
	}
	
	//ActionListener
	public void actionPerformed(ActionEvent event) {
		super.actionPerformed(event);
		
		if (event.getSource().equals(this.btnFechar)) {
			int response = JOptionPane.showConfirmDialog(null, "Fechar as designações?", "Fechar?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.salvar();
		    	this.fecharDesignacao();
		    }
		} else if (event.getSource().equals(this.btnRegresso)) {
			int response = JOptionPane.showConfirmDialog(null, "Reabrir as designações?", "Reabrir?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		    if (response == JOptionPane.YES_OPTION) {
		    	this.salvar();
		    	this.reabrirDesignacao();
		    }
		} else if (event.getSource().equals(this.btnPrint)) {
			this.imprimir(this.gerenciador.isEnable(ItensSeg.AVALIA_SALVA));
			
		} else if (event.getSource().equals(this.btnExport)) {
			this.exportFile();
		}
	}
	
	//ItemListener
	public void itemStateChanged(ItemEvent event) {
		this.comboChanges(event);
	}
	
	@Override
	protected void afterComboChanges() {
		this.btnRegresso.setVisible(false);
		this.btnFechar.setVisible(false);
		this.btnPrint.setVisible(false);
		this.btnExport.setVisible(false);
	}
	
	@Override
	protected void setButtons() {
		super.setButtons();
		
		this.btnRegresso = new JButton(Params.btRegressoImg());
		this.btnPrint = new JButton(Params.btImprimirImg());
		this.btnExport = new JButton(Params.btExportImg());
		this.btnFechar = new JButton(Params.btAprovarImg());
		
		this.btnRegresso.addActionListener(this);
		this.btnPrint.addActionListener(this);
		this.btnExport.addActionListener(this);
		this.btnFechar.addActionListener(this);
		
		this.btnRegresso.setToolTipText("Reabrir Designações");
		this.btnPrint.setToolTipText("Imprimir");
		this.btnExport.setToolTipText("Exportar Informações");
		this.btnFechar.setToolTipText("Aprovar Designações");
	}
	
	@Override
	protected void getMeses() {
		this.mesesDesignacoes = this.gerenciador.meses(DesignacaoGerenciador.DESIGNADO);
	}
	
	@Override
	protected void afterMountMonth() {
		this.btnRegresso.setVisible(true);
		this.btnPrint.setVisible(true);
		this.btnExport.setVisible(true);
	}
	
	@Override
	protected void afterSave() {
		this.btnFechar.setVisible(true);
	}
	
	private void fecharDesignacao() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		String erro = "";
		
		for (SemanaDesignacao semana : this.mesDesignacao.getSemanas()) {
			for (Designacao designacao : semana.getDesignacoes()) {
				if (designacao.getStatus() == 'A') {
					erro += "\nDia: " + semana.getData().format(Params.dateFormate()) + " - Nr.: " + designacao.getNumero();
				}
			}
		}
		
		if (!"".equals(erro)) {
			JOptionPane.showMessageDialog(this, "Não é possivel fechar, existe(m) problema(s):" + erro, "Informação", JOptionPane.WARNING_MESSAGE);
		
		} else {
			this.btnRegresso.setVisible(false);
			this.btnFechar.setVisible(false);
			
			this.mesesDesignacoes.remove(this.mesDesignacao);
			
			this.cbMeses.removeAllItems();
			
			this.comboMeses();
			
			this.cbMeses.setSelectedIndex(0);
			
			this.mesDesignacao.setStatus(DesignacaoGerenciador.FECHADO);
			
			this.gerenciador.salvaMesDesignacao(mesDesignacao);
		}
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void reabrirDesignacao() {
		this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
		
		this.btnRegresso.setVisible(false);
		this.btnFechar.setVisible(false);
		
		this.mesesDesignacoes.remove(this.mesDesignacao);
		
		this.cbMeses.removeAllItems();
		
		this.comboMeses();
		
		this.cbMeses.setSelectedIndex(0);
		
		this.mesDesignacao.setStatus(DesignacaoGerenciador.NOVO);
		
		this.gerenciador.salvaMesDesignacao(mesDesignacao);
		
		this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}
	
	private void exportFile() {
		String fileName = this.mesDesignacao.getMes().toString() + ".csv";
		
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File(fileName));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV", "csv");
		fileChooser.setFileFilter(filter);
		
		if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			
			this.salvar();
			
			PrintWriter pw = null;
			
			try {
				pw = new PrintWriter(fileChooser.getSelectedFile());
				
				StringBuffer sb = new StringBuffer("dia;tipo;sala;numero;estudante;genero;ajudante;genero;tema");
				pw.println(sb);
				
				for (SemanaDesignacao semana : this.mesDesignacao.getSemanas()) {
					if (!semana.isSemReuniao()) {
						sb = new StringBuffer(semana.getData().format(Params.dateFormate())).append(";");
						
						if (semana.isAssebleia()) {
							sb.append("A");
							pw.println(sb);
							
						} else if (semana.isRecapitulacao()) {
							sb.append("R");
							pw.println(sb);
							
						} else if (semana.isVisita()) {
							sb.append("V");
							pw.println(sb);
							
						} else {
							sb.append("N").append(";");
							
							for (Designacao designacao : semana.getDesignacoes()) {
								StringBuffer sbDes = new StringBuffer(sb);
								
								sbDes.append(designacao.getSala()).append(";")
								     .append(designacao.getNumero()).append(";")
								     .append(designacao.getEstudante().getNome()).append(";");
								
								if (designacao.getEstudante().getGenero() == Genero.MASCULINO) {
									sbDes.append("M");
								} else {
									sbDes.append("F");
								}
								
								sbDes.append(";");
								
								if (designacao.getAjudante() != null) {
									sbDes.append(designacao.getAjudante().getNome()).append(";");
									
									if (designacao.getAjudante().getGenero() == Genero.MASCULINO) {
										sbDes.append("M");
									} else {
										sbDes.append("F");
									}
								} else {
									sbDes.append(";");
								}
								
								sbDes.append(";");
								
								if (designacao.getNumero() == 1) {
									sbDes.append(designacao.getFonte() == null ? "" : designacao.getFonte());
								
								} else {
									sbDes.append(designacao.getTema() == null ? "" : designacao.getTema());
								}
								
								pw.println(sbDes);
							}
						}
					}
				}
				
				pw.flush();
				
			} catch (IOException e) {
				this.log.error("", e);
				
				JOptionPane.showMessageDialog(this, "Erro no Processo de Exportação.", "Erro", JOptionPane.ERROR_MESSAGE);

			} finally {
				if (pw != null) {
					pw.close();
				}
				
				this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				
				JOptionPane.showMessageDialog(this, "Processo de Exportação Finalizado.", "Informação", JOptionPane.INFORMATION_MESSAGE);
			}
			
		}
	}

	@Override
	protected void onMontaMes() {
		for (SemanaDesignacao semanaD : this.mesDesignacao.getSemanas()) {
			ASemanaUI semana = new SemanaUI(semanaD, (String)this.cbSala.getSelectedItem(), this.editDetalhes);
			
			this.semanas.add(semana);
			this.mesPanel.add(semana);
		}
	}
	
	private void validaMeses() {
		for (MesDesignacao mes : this.mesesDesignacoes) {
			if (mes.getAno() < ANO_ATUAL || (mes.getAno() == ANO_ATUAL && mes.getMes().getNumero() < MES_ATUAL )) {
				JOptionPane.showMessageDialog(this, "Existe(m) mes(es) que já deveria(m) ter sido fechado(s)", "Informação", JOptionPane.WARNING_MESSAGE);
				break;
			}
		}
	}
}