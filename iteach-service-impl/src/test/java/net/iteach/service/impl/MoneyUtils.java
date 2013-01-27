package net.iteach.service.impl;

import java.math.BigDecimal;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

public class MoneyUtils {

	public static Money money() {
		return money(BigDecimal.ZERO);
	}

	public static Money money(BigDecimal amount) {
		return money(CurrencyUnit.EUR, amount);
	}

	private static Money money(CurrencyUnit currency, BigDecimal amount) {
		return Money.of(currency, amount);
	}

}
