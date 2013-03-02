package net.iteach.service.dao;

import net.iteach.service.dao.model.TUser;

import java.util.List;

public interface UserDao {

    TUser getUserById(int id);

    List<TUser> findAll();

    void deleteUser(int id);
}
