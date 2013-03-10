package net.iteach.service.template;

import net.iteach.api.TemplateService;
import net.iteach.api.model.TemplateModel;
import net.iteach.test.AbstractIntegrationTest;
import net.iteach.test.Helper;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Locale;

import static org.junit.Assert.assertEquals;

public class TemplateServiceImplTest extends AbstractIntegrationTest {

    @Autowired
    private TemplateService service;

    @Test
    public void registration_en() throws IOException {
        TemplateModel model = createModel();
        String content = service.generate("registration.txt", Locale.ENGLISH, model);
        assertEqualLines(Helper.getResourceAsString(this, "registration_en.txt"), content);
    }

    @Test
    public void registration_de() throws IOException {
        TemplateModel model = createModel();
        String content = service.generate("registration.txt", Locale.GERMAN, model);
        assertEqualLines(Helper.getResourceAsString(this, "registration_de.txt"), content);
    }

    @Test
    public void registration_fr() throws IOException {
        TemplateModel model = createModel();
        String content = service.generate("registration.txt", Locale.FRENCH, model);
        assertEqualLines(Helper.getResourceAsString(this, "registration_fr.txt"), content);
    }

    @Test
    public void registration_unknown() throws IOException {
        TemplateModel model = createModel();
        String content = service.generate("registration.txt", Locale.ITALIAN, model);
        assertEqualLines(Helper.getResourceAsString(this, "registration_en.txt"), content);
    }

    private TemplateModel createModel() {
        TemplateModel model = new TemplateModel();
        model.add("userFirstName", "Damien").add("userLastName", "Coraboeuf").add("userEmail", "user@test.com");
        model.add("link", "http://my/link");
        return model;
    }

    private void assertEqualLines(String a, String b) {
        String[] la = StringUtils.split(a, "\n\r");
        String[] lb = StringUtils.split(b, "\n\r");
        assertEquals(
                StringUtils.join(la, "\n"),
                StringUtils.join(lb, "\n")
        );
    }

}
