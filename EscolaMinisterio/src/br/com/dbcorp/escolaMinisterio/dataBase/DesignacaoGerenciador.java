package br.com.dbcorp.escolaMinisterio.dataBase;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;
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
		
		LocalDate date;
		Month mesAtual;
		
		Query query = DataBaseHelper.createQuery("FROM MesDesignacao d ORDER BY d.ano DESC, d.mes DESC");
		
		List<MesDesignacao> meses = query.getResultList();
		
		if (!meses.isEmpty()) {
			MesDesignacao mesD = meses.get(0);
			
			Month mesAnterior = Month.values()[mesD.getMes().ordinal()];
			int ano = mesAnterior == Month.DECEMBER ? mesD.getAno() + 1 : mesD.getAno();
			
			date = LocalDate.of(ano, mesAnterior.plus(1), 1);
			
		} else {
			date = LocalDate.now().withDayOfMonth(1);
		}
		
		mesAtual = date.getMonth();
		
		mesDesignacao.setAno(date.getYear());
		mesDesignacao.setMes(MesesDom.values()[date.getMonth().ordinal()]);
		mesDesignacao.setMelhoreMinisterio(date.getYear() > 2015);
		
		//pega o primeiro dia da semana escolhido no mes
		int weekday = date.getDayOfWeek().getValue();
		int dayDiff = 6 - (DayOfWeek.SATURDAY.getValue() - diaEscolhido);
		int days = (DayOfWeek.SATURDAY.getValue() - weekday + dayDiff) % 7;
		date = date.plusDays(days);
		
		List<SemanaDesignacao> semanas = new ArrayList<SemanaDesignacao>();
		
		while (mesAtual == date.getMonth()) {
			SemanaDesignacao semana = new SemanaDesignacao();
			//semana.setData(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			semana.setData(date);
			
			semana.setMes(mesDesignacao);
			
			if (semanas.isEmpty()) {
				semana.setVideos(true);
			}

			semanas.add(semana);
			
			date = date.plusDays(7);
		}
		
		mesDesignacao.setSemanas(semanas);
		mesDesignacao.setStatus(DesignacaoGerenciador.NOVO);
		
		return mesDesignacao;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> listaEstudantes(Genero genero) {
		List<String> nomes = new ArrayList<String>();
		
		Query query = DataBaseHelper.createQuery("SELECT e.nome FROM Estudante e WHERE (e.excluido = false OR e.excluido IS NULL) AND e.genero = :genero ORDER BY e.ultimaDesignacao")
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
			query = DataBaseHelper.createQuery("SELECT a.nome FROM Ajudante a WHERE a.excluido = false OR a.excluido IS NULL");
			
		} else {
			query = DataBaseHelper.createQuery("SELECT e.nome FROM Estudante e WHERE (e.excluido = false OR e.excluido IS NULL) AND "
			+ "(e.naoAjudante = false OR e.naoAjudante IS NULL) AND e.genero = :genero")
				.setParameter("genero", genero);
		}
		
		nomes.add("Selecione...");
		
		for (String nome : (List<String>) query.getResultList()){
			nomes.add(nome);
		}
		
		return nomes;
	}
	
	public Ajudante ajudantePorNome(String nome) {
		Query query = DataBaseHelper.createQuery("FROM Ajudante a WHERE (a.excluido = false OR a.excluido IS NULL ) AND a.nome = :nome")
				.setParameter("nome", nome);
		
		return (Ajudante)query.getSingleResult();
	}
	
	public Estudante estudantePorNome(String nome) {
		Query query = DataBaseHelper.createQuery("FROM Estudante e WHERE (e.excluido = false OR e.excluido IS NULL) AND e.nome = :nome")
				.setParameter("nome", nome);
		
		try {
			return (Estudante) query.getSingleResult();
			
		} catch (NoResultException ex) {
			return null;
		}
	}
	
	public Estudo estudoPorNumero(int numero) {
		return DataBaseHelper.find(Estudo.class, numero);
	}
	
	@SuppressWarnings("unchecked")
	public List<Estudo> listarEstudos() {
		Query query = DataBaseHelper.createQuery("FROM Estudo e WHERE e.excluido = false OR e.excluido IS NULL ORDER BY e.nrEstudo");
		
		return query.getResultList();
	}
	
	public void salvaMesDesignacao(MesDesignacao mesDesignacao) {
		if (mesDesignacao.getId() == 0) {
			DataBaseHelper.persist(mesDesignacao);
			
		} else {
			DataBaseHelper.merge(mesDesignacao);
		}

		this.ajustaDesignacoes(mesDesignacao);
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
	
	@SuppressWarnings("unchecked")
	private void ajustaDesignacoes(MesDesignacao mesDesignacao) {
		//pré 2016
		if (!mesDesignacao.isMelhoreMinisterio()) {
			for (SemanaDesignacao semana : mesDesignacao.getSemanas()) {
				if (semana.getDesignacoesRemovidas() != null && (semana.isAssebleia() || semana.isRecapitulacao() || semana.isSemReuniao() || semana.isVisita() || semana.isVideos())) {
					for (Designacao designacao : semana.getDesignacoesRemovidas()) {
						if (designacao.getId() != 0) {
							DataBaseHelper.remove(designacao);
						}
					}
				}
			}
		} else {
			//pós 2015
			List<Designacao> designacoesExistentes = new ArrayList<>();
			
			if (mesDesignacao.getId() == 0) {
				designacoesExistentes.addAll(mesDesignacao.getSemanas().stream().flatMap(s->s.getDesignacoes().stream()).collect(Collectors.toList()));
			
			} else {
				Query query = DataBaseHelper.createQuery("FROM Designacao d JOIN FETCH d.semana s WHERE s.mes.id = :id")
						.setParameter("id", mesDesignacao.getId());
				
				designacoesExistentes.addAll((List<Designacao>) query.getResultList());
			}
			
			for (Designacao designacao : designacoesExistentes) {
				SemanaDesignacao semana = designacao.getSemana();
				
				if (semana.isAssebleia() || semana.isRecapitulacao() || semana.isSemReuniao() || semana.isVisita()) {
					DataBaseHelper.remove(designacao);
					
				} else if (semana.isVideos() && ("B".equalsIgnoreCase(designacao.getSala()) || ("A".equalsIgnoreCase(designacao.getSala()) && designacao.getNumero() != 1))) {
					DataBaseHelper.remove(designacao);

				}
			}
		}
	}
}
