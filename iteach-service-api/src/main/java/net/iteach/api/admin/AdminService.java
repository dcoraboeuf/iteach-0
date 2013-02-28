package net.iteach.api.admin;

import net.iteach.api.model.copy.Copy;

import java.util.List;

public interface AdminService {

	List<AccountSummary> getAccounts();

	AccountSummary getAccount(int id);

	void deleteAccount(int id);

	Settings getSettings();

	void setSettings(SettingsUpdate update);

    Copy export(int id);
}
