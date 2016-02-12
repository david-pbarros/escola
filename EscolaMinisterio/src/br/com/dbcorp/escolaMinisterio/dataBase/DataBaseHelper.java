package br.com.dbcorp.escolaMinisterio.dataBase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.dbcorp.escolaMinisterio.Params;

public class DataBaseHelper {

	private static EntityManagerFactory fac;
	private static EntityManager em;
	private static EntityTransaction tx;
	
	private DataBaseHelper() {
	}
	
	private static void inicialize(boolean withTx) {
		if (DataBaseHelper.fac == null) {
			DataBaseHelper.fac = Persistence.createEntityManagerFactory("escola", Params.propriedades());
		}
		
		if (DataBaseHelper.em == null) {
			DataBaseHelper.em = DataBaseHelper.fac.createEntityManager();
		}
		
		if (withTx && DataBaseHelper.tx == null) {
			DataBaseHelper.tx = em.getTransaction();
		}
	}
	
	public static void resetDB(String valor) throws IOException {
		Files.delete(Paths.get(valor.replace("jdbc:sqlite:", "")));
	}
	
	public static void beginTX() {
		DataBaseHelper.inicialize(true);
		DataBaseHelper.tx.begin();
	}
	
	public static void commitTX() {
		DataBaseHelper.tx.commit();
	}
	
	public static <T> void persist(T entity) {
		DataBaseHelper.inicialize(true);
		
		DataBaseHelper.tx.begin();
		DataBaseHelper.em.persist(entity);
		DataBaseHelper.tx.commit();
	}
	
	public static <T> void persistWTX(T entity) {
		DataBaseHelper.em.persist(entity);
	}
	
	public static <T> T merge(T entity) {
		DataBaseHelper.inicialize(true);
		
		DataBaseHelper.tx.begin();
		entity = DataBaseHelper.em.merge(entity);
		DataBaseHelper.tx.commit();
		
		return entity;
	}
	
	public static <T> T mergeWTX(T entity) {
		entity = DataBaseHelper.em.merge(entity);
		
		return entity;
	}
	
	public static <T> void remove(T entity) {
		DataBaseHelper.inicialize(true);
		
		DataBaseHelper.tx.begin();
		DataBaseHelper.em.remove(DataBaseHelper.em.merge(entity));
		DataBaseHelper.tx.commit();
	}
	
	public static <T> void refresh(T entity) {
		DataBaseHelper.inicialize(true);
		
		DataBaseHelper.tx.begin();
		DataBaseHelper.em.refresh(entity);
		DataBaseHelper.tx.commit();
	}
	
	public static <T> T find(Class<T> entityClass, Object id) {
		DataBaseHelper.inicialize(false);
		
		return DataBaseHelper.em.find(entityClass, id);
	}
	
	public static Query createQuery(String sql) {
		DataBaseHelper.inicialize(false);
		
		return DataBaseHelper.em.createQuery(sql);
	}
	
	public static void executeDeleteQuery(String sql) {
		DataBaseHelper.executeDeleteQuery(sql, null);
	}
	
	public static void executeDeleteQuery(String sql, Properties prop) {
		DataBaseHelper.inicialize(true);
		
		DataBaseHelper.tx.begin();
		
		Query query = DataBaseHelper.em.createQuery(sql);
		
		if (prop != null) {
			for (Object key : prop.keySet()) {
				String chave = (String) key;
				
				query.setParameter(chave, prop.getProperty(chave));
			}
		}
		
		query.executeUpdate();
		
		DataBaseHelper.tx.commit();
	}
}
