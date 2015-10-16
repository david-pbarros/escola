package br.com.dbcorp.escolaMinisterio;

public enum AvaliacaoDOM {
	PASSOU('P', "Passou"), NAO_PASSOU('F', "N�o Passou"), SUBSTITUIDO('S', "Substituido"), NAO_AVALIADO('A', "N�o Avaliado");
	
	char sigla;
	String label;
	
	private AvaliacaoDOM(char sigla, String label) {
		this.sigla = sigla;
		this.label = label;
	}
	
	public static AvaliacaoDOM getByDescription(String label) {
		switch (label) {
		case "Passou":
			return PASSOU;
		case "N�o Passou":
			return NAO_PASSOU;
		case "Substituido":
			return SUBSTITUIDO;
		case "N�o Avaliado":
		case "Selecionar...":
			return NAO_AVALIADO;
		default:
			throw new RuntimeException("Op��o inv�lida");
		}
	}
	
	public static AvaliacaoDOM getByInitials(char sigla) {
		switch (sigla) {
		case 'P':
			return PASSOU;
		case 'F':
			return NAO_PASSOU;
		case 'S':
			return SUBSTITUIDO;
		case 'A':
			return NAO_AVALIADO;
		default:
			throw new RuntimeException("Op��o inv�lida");
		}
	}
	
	public String getLabel() {
		return label;
	}
	
	public char getSigla() {
		return sigla;
	}
	
	@Override
	public String toString() {
		return this.label;
	}
}
