package ua.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import ua.model.entity.Ms;
import ua.repository.MsRepository;

@Component
public class MsConverter implements Converter<String, Ms> {

	private final MsRepository repository;

	public MsConverter(MsRepository repository) {
		this.repository = repository;
	}

	@Override
	public Ms convert(String name) {
		return repository.findByName(name);
	}
}
