package net.iteach.service.security;

import com.google.common.base.Function;
import net.iteach.api.MessageService;
import net.iteach.api.SecurityService;
import net.iteach.api.TemplateService;
import net.iteach.api.UIService;
import net.iteach.api.model.MessageChannel;
import net.iteach.api.model.MessageDestination;
import net.iteach.api.model.TemplateModel;
import net.iteach.core.model.*;
import net.iteach.service.dao.UserDao;
import net.iteach.service.dao.model.TUser;
import net.iteach.service.token.TokenKey;
import net.iteach.service.token.TokenService;
import net.sf.jstring.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final MessageService messageService;
    private final TokenService tokenService;
    private final UIService uiService;
    private final TemplateService templateService;
    private final Strings strings;

    private final Function<TUser, UserSummary> userSummaryFunction = new Function<TUser, UserSummary>() {
        @Override
        public UserSummary apply(TUser t) {
            return new UserSummary(
                    t.getId(),
                    t.getFirstName(),
                    t.getLastName(),
                    t.getEmail()
            );
        }
    };

    @Autowired
    public SecurityServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, MessageService messageService, TokenService tokenService, UIService uiService, TemplateService templateService, Strings strings) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.messageService = messageService;
        this.tokenService = tokenService;
        this.uiService = uiService;
        this.templateService = templateService;
        this.strings = strings;
    }

    protected String digest(String password, String email) {
        return passwordEncoder.encodePassword(password, email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isAdminInitialized() {
        return userDao.isAdminInitialized();
    }

    @Override
    @Transactional
    public Ack register(Locale locale, AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
        // Admin?
        boolean administrator = !isAdminInitialized();
        // Creates the account
        userDao.createUser(
                mode,
                mode == AuthenticationMode.password ? email : identifier,
                email,
                firstName,
                lastName,
                administrator,
                administrator,
                mode == AuthenticationMode.password ? digest(password, email) : ""
        );

        // In case of not administrator
        if (!administrator) {
            // Its initial state is not verified and a notification must be sent
            // to the email
            Message message = createNewUserMessage(locale, firstName, lastName, email);
            // Sends the message
            messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
        }

        // OK
        return Ack.validate(administrator);
    }

    @Override
    @Transactional
    public Ack completeRegistration(String token) {
        // User email
        TokenKey key = tokenService.checkToken(token, TokenType.REGISTRATION);
        String email = key.getKey();
        // Gets the user basic data for display
        UserSummary user = getUserSummaryByEmail(email);
        // Consumes the token
        tokenService.consumesToken(token, TokenType.REGISTRATION, user.getEmail());
        // Updates the verified flag
        return userDao.userVerified(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public void checkTokenForUserId(TokenType tokenType, String token, int userId) {
        // Gets the user
        UserSummary user = getUserSummaryByID(userId);
        // Token
        TokenKey key = tokenService.checkToken(token, tokenType);
        String email = key.getKey();
        // Check
        if (!StringUtils.equals(email, user.getEmail())) {
            throw new TokenMatchException(tokenType, token, user.getEmail());
        }
    }

    @Override
    @Transactional
    public void passwordRequest(Locale locale, int userId) {
        // Gets the user
        UserSummary user = getUserSummaryByID(userId);
        // Creates the message
        Message message = createPasswordRequestMessage(locale, user.getFirstName(), user.getLastName(), user.getEmail());
        // Sends the message
        messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, user.getEmail()));
    }

    @Override
    @Transactional
    public Ack passwordChange(int userId, String token, String oldPassword, String newPassword) {
        // Gets the user details
        UserSummary user = getUserSummaryByID(userId);
        // Consumes the token
        tokenService.consumesToken(token, TokenType.PASSWORD_REQUEST, user.getEmail());
        // Changes the verified flag
        return userDao.changePassword(user.getId(),
                passwordEncoder.encodePassword(newPassword, user.getEmail()),
                passwordEncoder.encodePassword(oldPassword, user.getEmail())
        );
    }

    protected UserSummary getUserSummaryByEmail(String email) {
        return userSummaryFunction.apply(
                userDao.findUserByEmail(email)
        );
    }

    protected UserSummary getUserSummaryByID(int id) {
        return userSummaryFunction.apply(
                userDao.getUserById(id)
        );
    }

    private Message createPasswordRequestMessage(Locale locale, String firstName, String lastName, String email) {
        return createUserMessage(locale, firstName, lastName, email, TokenType.PASSWORD_REQUEST, strings.get(locale, "message.passwordRequest"));
    }

    private Message createNewUserMessage(Locale locale, String firstName, String lastName, String email) {
        return createUserMessage(locale, firstName, lastName, email, TokenType.REGISTRATION, strings.get(locale, "message.registration"));
    }

    private Message createUserMessage(Locale locale, String firstName, String lastName, String email,
                                      TokenType tokenType,
                                      String title) {
        // Generates a token for the response
        String token = tokenService.generateToken(tokenType, email);
        // Gets the return link
        String link = uiService.getLink(tokenType, token);
        // Message template model
        TemplateModel model = new TemplateModel();
        model.add("userFirstName", firstName);
        model.add("userLastName", lastName);
        model.add("userEmail", email);
        model.add("link", link);
        // Template ID
        String templateId = tokenType.name().toLowerCase() + ".txt";
        // Message content
        String content = templateService.generate(templateId, locale, model);
        // Creates the message
        return new Message(title, new MessageContent(content, link, token));
    }

}
