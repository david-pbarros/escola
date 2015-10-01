package br.com.dbcorp.escolaMinisterio.dataBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.MesesDom;
import br.com.dbcorp.escolaMinisterio.entidades.Ajudante;
import br.com.dbcorp.escolaMinisterio.entidades.Designacao;
import br.com.dbcorp.escolaMinisterio.entidades.Estudante;
import br.com.dbcorp.escolaMinisterio.entidades.Estudo;
import br.com.dbcorp.escolaMinisterio.entidades.Genero;
import br.com.dbcorp.escolaMinisterio.entidades.MesDesignacao;
import br.com.dbcorp.escolaMinisterio.entidades.SemanaDesignacao;

public class DesignacaoGerenciador extends Gerenciador {
	
	public final static char NOVO = 'N';
	public final static char DESIGNADO = 'D';
	public final static char FECHADO = 'F';
	
	@SuppressWarnings("unchecked")
	public List<MesDesignacao> meses(char tipo) {
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao d WHERE d.status = :status ORDER BY d.ano, d.mes")
				.setParameter("status", tipo);
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public MesDesignacao abrirMes(int diaEscolhido) {
		MesDesignacao mesDesignacao = new MesDesignacao();
		
		Calendar cd = Calendar.getInstance();
		
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao d ORDER BY d.ano DESC, d.mes DESC");
		
		List<MesDesignacao> meses = query.getResultList();
		
		if (!meses.isEmpty()) {
			MesDesignacao mesD = meses.get(0);
			cd.set(Calendar.MONTH, mesD.getMes().ordinal() + 1);
			cd.set(Calendar.YEAR, mesD.getAno());
		}
		
		mesDesignacao.setAno(cd.get(Calendar.YEAR));
		mesDesignacao.setMes(MesesDom.values()[cd.get(Calendar.MONTH)]);
		
		//Seta o mes no primeiro dia
		cd.set(Calendar.DAY_OF_MONTH, 1);
		
		int mes = cd.get(Calendar.MONTH);
		
		//pega o primeiro dia da semana escolhido no mes
		int weekday = cd.get(Calendar.DAY_OF_WEEK);
		int dayDiff = 7 - (Calendar.SATURDAY - diaEscolhido);
		int days = (Calendar.SATURDAY - weekday + dayDiff) % 7;
		cd.add(Calendar.DAY_OF_YEAR, days);
		
		List<SemanaDesignacao> semanas = new ArrayList<SemanaDesignacao>();
		
		while (mes == cd.get(Calendar.MONTH)) {
			Calendar ct = Calendar.getInstance();
			ct.setTime(cd.getTime());
			
			SemanaDesignacao semana = new SemanaDesignacao();
			semana.setData(ct.getTime());
			semana.setMes(mesDesignacao);

			semanas.add(semana);
			
			cd.add(Calendar.DAY_OF_YEAR, 7);
		}
		
		mesDesignacao.setSemanas(semanas);
		
		return mesDesignacao;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listaEstudantes(Genero genero) {
		List<String> nomes = new ArrayList<String>();
		
		Query query = DataBaseHelper.createQuery("SELECT e.nome FROM Estudante e WHERE e.genero = :genero ORDER BY e.ultimaDesignacao")
				.setParameter("genero", genero);
		
		nomes.add("Selecione...");
		
		for (String nome : (List<String>) query.getResultList()){
			nomes.add(nome);
		}
		
		return nomes;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listaAjudantes(Genero genero) {
		List<String> nomes = new ArrayList<String>();
		
		Query query = null;
		
		if (genero == Genero.MASCULINO) {
			query = DataBaseHelper.createQuery("SELECT a.nome FROM Ajudante a");
			
		} else {
			query = DataBaseHelper.createQuery("SELECT e.nome FROM Estudante e WHERE (e.naoAjudante = false OR e.naoAjudante IS NULL) AND e.genero = :genero")
				.setParameter("genero", genero);
		}
		
		nomes.add("Selecione...");
		
		for (String nome : (List<String>) query.getResultList()){
			nomes.add(nome);
		}
		
		return nomes;
	}
	
	public Ajudante ajudantePorNome(String nome) {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE a.nome = :nome")
				.setParameter("nome", nome);
		
		return (Ajudante)query.getSingleResult();
	}
	
	public Estudante estudantePorNome(String nome) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE e.nome = :nome")
				.setParameter("nome", nome);
		
		return (Estudante) query.getSingleResult();
	}
	
	public Estudo estudoPorNumero(int numero) {
		return DataBaseHelper.find(Estudo.class, numero);
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudo> listarEstudos() {
		Query query = DataBaseHelper.createQuery("FROM Estudo e");
		
		return query.getResultList();
	}
	
	public void salvaMesDesignacao(MesDesignacao mesDesignacao) {
		for (SemanaDesignacao semana : mesDesignacao.getSemanas()) {
			if (semana.getDesignacoesRemovidas() != null && (semana.isAssebleia() || semana.isRecapitulacao() || semana.isSemReuniao() || semana.isVisita())) {
				for (Designacao designacao : semana.getDesignacoesRemovidas()) {
					if (designacao.getId() != 0) {
						DataBaseHelper.remove(designacao);
					}
				}
			}
		}
		
		if (mesDesignacao.getId() == 0) {
			DataBaseHelper.persist(mesDesignacao);
			
		} else {
			DataBaseHelper.merge(mesDesignacao);
		}
	}
	
	public MesDesignacao mesAtua() {
		Calendar cd = Calendar.getInstance();

		Query query = DataBaseHelper.createQuery("FROM MesDesignacao m JOIN FETCH m.semanas WHERE m.ano = :ano AND m.mes = :mes")
				.setParameter("ano", cd.get(Calendar.YEAR))
				.setParameter("mes", MesesDom.values()[cd.get(Calendar.MONTH)]);
		
		try {
			return (MesDesignacao) query.getSingleResult();
		} catch(Exception ex) {
			return null;
		}
	}
}
