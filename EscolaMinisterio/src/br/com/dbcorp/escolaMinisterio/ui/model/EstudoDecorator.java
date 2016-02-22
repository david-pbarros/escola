package br.com.dbcorp.escolaMinisterio.ui.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import br.com.dbcorp.escolaMinisterio.AvaliacaoDOM;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;

public class EstudoDecorator {
	
	private Estudo estudo;
	private Designacao designacao;
	
	public EstudoDecorator(Estudo estudo) {
		this.estudo = estudo;
	}
	
	public EstudoDecorator(Estudo estudo, Designacao designacao) {
		this.estudo = estudo;
		this.designacao = designacao;
	}
	
	public Estudo getEstudo() {
		return this.estudo;
	}
	
	public int getNrEstudo() {
		return this.estudo.getNrEstudo();
	}
	
	public LocalDateTime getDtUltimaAtualiza() {
		return this.estudo.getDtUltimaAtualiza();
	}
	
	public String getDescricao() {
		return this.estudo.getDescricao();
	}
	
	public boolean isLeitura() {
		return this.estudo.isLeitura();
	}
	
	public boolean isDemonstracao() {
		return this.estudo.isDemonstracao();
	}
	
	public boolean isDiscurso() {
		return this.estudo.isDiscurso();
	}
	
	public boolean isExcluido() {
		return this.estudo.isExcluido();
	}

	public AvaliacaoDOM getAvaliacao() {
		return this.designacao != null ? AvaliacaoDOM.getByInitials(this.designacao.getStatus()) : null;
	}
	
	public LocalDate getData() {
		return this.designacao != null ? this.designacao.getData() : null;
	}
}
