package br.com.dbcorp.escolaMinisterio.dataBase;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MyLocalDateConverter implements AttributeConverter<LocalDateTime, Long> {

	@Override
    public Long convertToDatabaseColumn(LocalDateTime date) {
		if (date != null) {
			return date.atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli();
		}
		
		return null;
    }

    @Override
    public LocalDateTime convertToEntityAttribute(Long date) {
    	if (date != null) {
    		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("America/Sao_Paulo"));
    	}
    	
    	return null;
    }
}
