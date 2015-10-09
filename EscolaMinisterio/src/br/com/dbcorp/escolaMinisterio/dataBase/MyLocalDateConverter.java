package br.com.dbcorp.escolaMinisterio.dataBase;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = false)
public class MyLocalDateConverter implements AttributeConverter<LocalDate, Long> {

	@Override
    public Long convertToDatabaseColumn(LocalDate date) {
		 return date.atStartOfDay().atZone(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli();
    }

    @Override
    public LocalDate convertToEntityAttribute(Long date) {
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(date), ZoneId.of("America/Sao_Paulo")).toLocalDate();
    }
}
