package net.iteach.service.impl;

import net.iteach.api.UIService;
import net.iteach.core.model.TokenType;

import org.springframework.stereotype.Component;

@Component
public class MockUIService implements UIService {

	@Override
	public String getLink(TokenType type, String... components) {
		StringBuilder s = new StringBuilder("http://mock/");
		s.append(type);
		for (String component : components) {
			s.append("/").append(component);
		}
		return s.toString();
	}

}
