package net.iteach.service.template

import net.iteach.api.TemplateService
import net.iteach.api.model.TemplateModel
import net.iteach.test.AbstractIntegrationTest

import org.junit.Assert
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class TemplateServiceImplTest extends AbstractIntegrationTest {
	
	@Autowired
	TemplateService service
	
	@Test
	void registration_en () {
		TemplateModel model = createModel()
		String content = service.generate("registration.txt", Locale.ENGLISH, model)
		Assert.assertEquals("""Dear Damien Coraboeuf,

A new account has been registered for you using the following email:

user@test.com

Please follow this link in order to validate your account:

http://my/link

Regards,
the iTeach team.""", content)
	}
	
	@Test
	void registration_de () {
		TemplateModel model = createModel()
		String content = service.generate("registration.txt", Locale.GERMAN, model)
		Assert.assertEquals("""Dear Damien Coraboeuf,

A new account has been registered for you using the following email:

user@test.com

Please follow this link in order to validate your account:

http://my/link

Regards,
the iTeach team.""", content)
	}
	
	@Test
	void registration_fr () {
		TemplateModel model = createModel()
		String content = service.generate("registration.txt", Locale.FRENCH, model)
		Assert.assertEquals("""Cher/chère Damien Coraboeuf,

Un nouveau compte a été enrégistré pour le courrier suivant :

user@test.com

Veuillez suivre le lien suivant afin de poursuivre la validation de votre compte :

http://my/link

Cordialement,
l'équipe iTeach.""", content)
	}
	
	@Test
	void registration_unknown () {
		TemplateModel model = createModel()
		String content = service.generate("registration.txt", Locale.ITALIAN, model)
		Assert.assertEquals("""Dear Damien Coraboeuf,

A new account has been registered for you using the following email:

user@test.com

Please follow this link in order to validate your account:

http://my/link

Regards,
the iTeach team.""", content)
	}

	private TemplateModel createModel() {
		TemplateModel model = new TemplateModel()
		model.add("userFirstName", "Damien").add("userLastName", "Coraboeuf").add("userEmail", "user@test.com")
		model.add("link", "http://my/link")
		return model
	}

}
