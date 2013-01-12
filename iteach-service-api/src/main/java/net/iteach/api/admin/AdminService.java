package net.iteach.api.admin;

import java.util.List;

public interface AdminService {

	List<AccountSummary> getAccounts();

	AccountDetails getAccountDetails(int id);

}
