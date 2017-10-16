package com.globallogic.lambda;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.globallogic.lambda.domain.PersonRequest;
import com.globallogic.lambda.domain.PersonResponse;
import com.globallogic.lambda.service.SavePersonService;

@Component("savePersonFunction")
public class SavePersonFunction implements Function<PersonRequest, PersonResponse> {

	private final SavePersonService savePersonService;

	public SavePersonFunction(final SavePersonService savePersonService) {
		this.savePersonService = savePersonService;
	}

	@Override
	public PersonResponse apply(final PersonRequest personRequest) {
		return savePersonService.savePerson(personRequest);
	}
}
