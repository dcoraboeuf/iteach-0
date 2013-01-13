package net.iteach.api.admin;

import java.util.List;

public interface AdminService {

	List<AccountSummary> getAccounts();

	AccountSummary getAccount(int id);

	void deleteAccount(int id);

}
