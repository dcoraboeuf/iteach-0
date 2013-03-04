package net.iteach.service.security;

import net.iteach.core.security.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

public abstract class AbstractUserService implements UserDetailsService {

    @Override
    @Transactional(readOnly = true)
    public User loadUserByUsername(final String identifier) throws UsernameNotFoundException {
        UserAccount userAccount = loadUserAccount(identifier);
        if (userAccount != null) {
            return new UserDefinition(userAccount);
        } else {
            throw new UsernameNotFoundException(identifier);
        }
    }

    protected abstract UserAccount loadUserAccount(String identifier);

}
