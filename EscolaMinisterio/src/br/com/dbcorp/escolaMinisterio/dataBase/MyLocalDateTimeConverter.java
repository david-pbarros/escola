package br.com.dbcorp.escolaMinisterio.dataBase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class MyLocalDateTimeConverter implements AttributeConverter<LocalDate, Long> {

	@Override
    public Long convertToDatabaseColumn(LocalDate date) {
		if (date != null) {
			return date.atStartOfDay().atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli();
		}
		
		return null;
    }

    @Override
    public LocalDate convertToEntityAttribute(Long date) {
    	if (date != null) {
    		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("America/Sao_Paulo")).toLocalDate();
    	}

    	return null;
    }
}
