package br.com.dbcorp.escolaMinisterio.dataBase;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MyLocalDateConverter2 implements AttributeConverter<LocalDate, Date> {

	@Override
    public Date convertToDatabaseColumn(LocalDate date) {
		 return java.sql.Date.valueOf(date);
    }

    @Override
    public LocalDate convertToEntityAttribute(Date date) {
		return date.toLocalDate();
    }
}
