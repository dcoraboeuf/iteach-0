package net.iteach.service.security;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import javax.sql.DataSource;
import javax.validation.Validator;

import net.iteach.api.MessageService;
import net.iteach.api.SecurityService;
import net.iteach.api.TemplateService;
import net.iteach.api.UIService;
import net.iteach.api.model.AuthenticationMode;
import net.iteach.api.model.MessageChannel;
import net.iteach.api.model.MessageDestination;
import net.iteach.api.model.TemplateModel;
import net.iteach.core.model.Message;
import net.iteach.core.model.MessageContent;
import net.iteach.core.model.RegistrationCompletionForm;
import net.iteach.core.model.TokenType;
import net.iteach.core.model.UserSummary;
import net.iteach.service.db.SQL;
import net.iteach.service.token.TokenKey;
import net.iteach.service.token.TokenService;
import net.sf.jstring.Strings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecurityServiceImpl extends AbstractSecurityService implements SecurityService {
	
	private final PasswordEncoder passwordEncoder;
	private final MessageService messageService;
	private final TokenService tokenService;
	private final UIService uiService;
	private final TemplateService templateService;
	private final Strings strings;

	@Autowired
	public SecurityServiceImpl(DataSource dataSource, Validator validator, PasswordEncoder passwordEncoder, MessageService messageService, TokenService tokenService, UIService uiService, TemplateService templateService, Strings strings) {
		super(dataSource, validator);
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
		return getJdbcTemplate().queryForInt(SQL.USER_ADMINISTRATOR_COUNT) > 0;
	}

	@Override
	@Transactional
	public void register(Locale locale, AuthenticationMode mode, String identifier, String firstName, String lastName, String email, String password) {
		// Checks for unicity of identifier
		Integer existingUserId = getFirstItem(SQL.USER_BY_IDENTIFIER, params("identifier", identifier), Integer.class);
		if (existingUserId != null) {
			throw new UserIdentifierAlreadyExistsException(identifier);
		}
		// Checks for unicity of email
		existingUserId = getFirstItem(SQL.USER_ID_BY_EMAIL, params("email", email), Integer.class);
		if (existingUserId != null) {
			throw new UserEmailAlreadyExistsException(email);
		}
		// Parameters
		MapSqlParameterSource params = params("email", email);
		params.addValue("firstName", firstName);
		params.addValue("lastName", lastName);
		// Administrator accounts are not to be verified
		boolean administrator = !isAdminInitialized();
		params.addValue("administrator", administrator);
		params.addValue("verified", administrator);
		// Mode
		params.addValue("mode", mode.name());
		if (mode == AuthenticationMode.openid) {
			params.addValue("identifier", identifier);
			params.addValue("password", "");
		} else if (mode == AuthenticationMode.password) {
			params.addValue("identifier", email);
			params.addValue("password", digest(password, email));
		} else {
			throw new UnknownAuthenticationModeException(mode);
		}
		// Insert the user
		getNamedParameterJdbcTemplate().update(SQL.USER_CREATE, params);
		
		// In case of not administrator
		if (!administrator) {
			// Its initial state is not verified and a notification must be sent
			// to the email
			Message message = createNewUserMessage(locale, firstName, lastName, email);
			// Sends the message
			messageService.sendMessage(message, new MessageDestination(MessageChannel.EMAIL, email));
		}
	}
	
	@Override
	@Transactional(readOnly = true)
	public RegistrationCompletionForm getRegistrationCompletionForm(String token) {
		// User email
		TokenKey key = tokenService.checkToken(token, TokenType.REGISTRATION);
		String email = key.getKey();
		// Gets the user basic data for display
		UserSummary user = getUserSummaryByEmail(email);
		// Returns the form
		return new RegistrationCompletionForm(token, user);
	}

	protected UserSummary getUserSummaryByEmail(final String email) {
		return getNamedParameterJdbcTemplate().queryForObject(
			SQL.USER_SUMMARY_BY_EMAIL,
			params("email", email),
			new RowMapper<UserSummary>() {
				@Override
				public UserSummary mapRow(ResultSet rs, int index) throws SQLException {
					return new UserSummary(
						rs.getInt("id"),
						rs.getString("firstName"),
						rs.getString("lastName"),
						email);
				}
			});
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
