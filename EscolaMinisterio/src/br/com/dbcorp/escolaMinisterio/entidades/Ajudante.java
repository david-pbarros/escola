package br.com.dbcorp.escolaMinisterio.entidades;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;

@Entity
@EntityListeners({RemoveListener.class, PersistListener.class})
public class Ajudante extends Pessoa {
}