package br.com.dbcorp.escolaMinisterio.dataBase;


public class DefragGerenciador {

	//apaga designa��es em estado de historico e designadas que n�o tem estudo definido
	public void limparSemEstudo() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM MesDesignacao m INNER JOIN m.semanas s INNER JOIN s.designacoes d1 "
				+ "WHERE m.status IN ('D', 'F') AND d1 = d AND d1.estudo IS NULL)");
	}
	
	//apaga designa��es que n�o tem vinculo com uma semana de reuni�o.
	public void limparDesignacoesNaoVinculadas() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE NOT EXISTS (SELECT s FROM SemanaDesignacao s WHERE s.designacoes = d)");
	}
}
