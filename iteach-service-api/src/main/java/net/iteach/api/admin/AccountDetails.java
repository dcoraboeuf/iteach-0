package net.iteach.api.admin;

import lombok.Data;

@Data
public class AccountDetails {
	
	private final AccountSummary summary;
	private final int schoolCount;
	private final int studentCount;
	private final int lessonCount;

}
