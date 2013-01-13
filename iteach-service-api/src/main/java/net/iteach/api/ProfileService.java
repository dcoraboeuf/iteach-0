package net.iteach.api;

import net.iteach.core.model.AccountProfile;

public interface ProfileService {

	AccountProfile getProfile();

	AccountProfile getProfile(int userId);

}
