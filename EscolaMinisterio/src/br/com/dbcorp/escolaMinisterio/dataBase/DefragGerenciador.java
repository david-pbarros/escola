package br.com.dbcorp.escolaMinisterio.dataBase;


public class DefragGerenciador {

	//apaga designações em estado de historico e designadas que não tem estudo definido
	public void limparSemEstudo() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE EXISTS (SELECT d1 FROM MesDesignacao m INNER JOIN m.semanas s INNER JOIN s.designacoes d1 "
				+ "WHERE m.status IN ('D', 'F') AND d1 = d AND d1.estudo IS NULL)");
	}
	
	//apaga designações que não tem vinculo com uma semana de reunião.
	public void limparDesignacoesNaoVinculadas() {
		DataBaseHelper.executeDeleteQuery("DELETE FROM Designacao d WHERE NOT EXISTS (SELECT s FROM SemanaDesignacao s WHERE s.designacoes = d)");
	}
}
