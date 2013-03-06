package net.iteach.api.admin;

import net.iteach.api.model.copy.ExportedTeacher;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdminService {

	List<AccountSummary> getAccounts();

	AccountSummary getAccount(int id);

	void deleteAccount(int id);

	Settings getSettings();

	void setSettings(SettingsUpdate update);

    ExportedTeacher export(int id);

    AccountSummary importData(int id, MultipartFile file);
}
