package net.iteach.service.dao;

import net.iteach.core.model.Ack;
import net.iteach.core.model.AuthenticationMode;
import net.iteach.service.dao.model.TUser;

import java.util.List;

public interface UserDao {

    TUser getUserById(int id);

    List<TUser> findAll();

    void deleteUser(int id);

    TUser findUserByUsernameForPasswordMode(String username);

    String getPassword(int id);

    TUser findUserByUsernameForOpenIDMode(String identifier);

    boolean isAdminInitialized();

    void createUser(AuthenticationMode mode, String identifier, String email, String firstName, String lastName, boolean administrator, boolean verified, String password);

    Ack userVerified(int id);

    Ack changePassword(int id, String newPassword, String oldPassword);

    TUser findUserByEmail(String email);
}
