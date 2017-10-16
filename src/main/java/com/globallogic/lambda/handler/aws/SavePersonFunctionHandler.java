package com.globallogic.lambda.handler.aws;

import org.springframework.cloud.function.adapter.aws.SpringBootRequestHandler;

import com.globallogic.lambda.domain.PersonRequest;
import com.globallogic.lambda.domain.PersonResponse;

public class SavePersonFunctionHandler extends SpringBootRequestHandler<PersonRequest, PersonResponse> {
}
