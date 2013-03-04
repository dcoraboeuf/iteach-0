package net.iteach.service.security;

import net.iteach.core.model.AuthenticationMode;
import net.iteach.service.dao.UserDao;
import net.iteach.service.dao.model.TUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("basicUserService")
public class BasicUserService extends AbstractUserService {

    private final UserDao userDao;

    @Autowired
    public BasicUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    protected UserAccount loadUserAccount(String username) {
        TUser t = userDao.findUserByUsernameForPasswordMode(username);
        if (t != null) {
            String password = userDao.getPassword(t.getId());
            return new UserAccount(
                    t.getId(),
                    AuthenticationMode.password,
                    username,
                    password,
                    t.getEmail(),
                    t.getFirstName(),
                    t.getLastName(),
                    t.isAdministrator()
            );
        } else {
            return null;
        }
    }
}
