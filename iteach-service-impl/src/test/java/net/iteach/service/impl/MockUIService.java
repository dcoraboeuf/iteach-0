package net.iteach.service.impl;

import net.iteach.api.UIService;
import net.iteach.core.model.TokenType;

import org.springframework.stereotype.Component;

@Component
public class MockUIService implements UIService {

	@Override
	public String getLink(TokenType type, String token) {
		return String.format("http://mock/%s/%s", type, token);
	}

}
