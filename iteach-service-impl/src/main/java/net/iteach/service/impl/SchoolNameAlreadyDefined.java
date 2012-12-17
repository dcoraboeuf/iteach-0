package net.iteach.service.impl;

import net.iteach.utils.InputException;

public class SchoolNameAlreadyDefined extends InputException {

	public SchoolNameAlreadyDefined(String name) {
		super(name);
	}

}
