package br.com.dbcorp.escolaMinisterio;

public enum MesesDom {
	Janeiro(1),
	Fevereiro(2),
	Março(3),
	Abril(4),
	Maio(5),
	Junho(6),
	Julho(7),
	Agosto(8),
	Setembro(9),
	Outubro(10),
	Novembro(11),
	Dezembro(12);
	
	int numero;
	
	private MesesDom(int numero) {
		this.numero = numero;
	}
	
	public int getNumero() {
		return numero;
	}
}