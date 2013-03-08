package net.iteach.web.admin;

import net.iteach.api.admin.AccountSummary;
import net.iteach.api.admin.AdminService;
import net.iteach.api.admin.SettingsUpdate;
import net.iteach.api.model.ConfigurationKey;
import net.iteach.api.model.copy.ExportedTeacher;
import net.iteach.core.model.Ack;
import net.iteach.core.model.UserMessage;
import net.iteach.utils.InputException;
import net.iteach.web.support.AbstractGUIController;
import net.iteach.web.support.ErrorHandler;
import net.sf.jstring.Strings;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Locale;

@Controller
@RequestMapping("/admin")
public class AdminController extends AbstractGUIController {

    private final AdminService adminService;
    private final Strings strings;
    private final ObjectMapper mapper;

    @Autowired
    public AdminController(ErrorHandler errorHandler, AdminService adminService, Strings strings, ObjectMapper mapper) {
        super(errorHandler);
        this.adminService = adminService;
        this.strings = strings;
        this.mapper = mapper;
    }

    /**
     * Settings
     */
    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings(Model model) {
        // Loads the settings
        model.addAttribute("settings", adminService.getSettings());
        // OK
        return "admin/settings";
    }

    /**
     * Uploads the settings
     */
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public String setSettings(Model model, WebRequest request, Locale locale) {
        // Gets all keys
        SettingsUpdate update = new SettingsUpdate();
        for (ConfigurationKey key : ConfigurationKey.values()) {
            String[] values = request.getParameterValues(key.name());
            if (values != null && values.length == 1) {
                update = update.withValue(key, values[0]);
            }
        }
        // Loads the settings
        model.addAttribute("settings", adminService.getSettings());
        try {
            // Sends the update
            adminService.setSettings(update);
            // Message
            model.addAttribute("message", UserMessage.success(strings.get(locale, "admin.settings.ok")));
        } catch (InputException ex) {
            // Message
            model.addAttribute("message", UserMessage.error(errorHandler.displayableError(ex, locale)));
        }
        // OK
        return "admin/settings";
    }

    /**
     * List of accounts
     */
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public String accounts(Model model) {
        // List of account summaries
        model.addAttribute("accounts", adminService.getAccounts());
        // OK
        return "admin/accounts";
    }

    /**
     * Page to confirm the deletion
     */
    @RequestMapping(value = "/account/{id}/delete", method = RequestMethod.GET)
    public String accountDeleteForm(Model model, @PathVariable int id) {
        // Loads user details
        AccountSummary details = adminService.getAccount(id);
        model.addAttribute("details", details);
        // OK
        return "admin/accountDelete";
    }

    /**
     * Disables a user
     */
    @RequestMapping(value = "/account/{id}/disable", method = RequestMethod.PUT)
    public @ResponseBody
    Ack userDisable (@PathVariable int id) {
        return adminService.userDisable(id);
    }

    /**
     * Enables a user
     */
    @RequestMapping(value = "/account/{id}/enable", method = RequestMethod.PUT)
    public @ResponseBody
    Ack userEnable (@PathVariable int id) {
        return adminService.userEnable(id);
    }

    /**
     * Deletes the user
     */
    @RequestMapping(value = "/account/{id}/delete", method = RequestMethod.POST)
    public String accountDelete(Model model, @PathVariable int id) {
        // Deletes the user
        adminService.deleteAccount(id);
        // OK
        return "redirect:/admin/accounts";
    }

    /**
     * Exports the data for the user
     */
    @RequestMapping(value = "/account/{id}/export", method = RequestMethod.GET)
    public void accountExport(@PathVariable int id, HttpServletResponse response) throws IOException {
        // Gets a copy of the account
        ExportedTeacher copy = adminService.export(id);
        // Headers
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.addHeader("Content-Disposition", "attachment; filename=account.json");
        // Serializes as JSON
        mapper.writeValue(
                new OutputStreamWriter(
                        response.getOutputStream(),
                        "UTF-8"),
                copy);
    }

    /**
     * Import the data for a user, selection page
     */
    @RequestMapping(value = "/account/{id}/import", method = RequestMethod.GET)
    public String accountImportForm(@PathVariable int id) {
        // Gets to the form
        return "admin/import";
    }

    /**
     * Import the data for a user
     */
    @RequestMapping(value = "/account/{id}/import", method = RequestMethod.POST)
    public String accountImport(Locale locale, @PathVariable int id, @RequestParam MultipartFile file, RedirectAttributes redirectAttributes) {
        // Performs the import
        AccountSummary account = adminService.importData(id, file);
        // Message
        redirectAttributes.addFlashAttribute("message", UserMessage.success(strings.get(locale, "admin.accounts.import.success", account.getEmail())));
        // Gets to the form
        return "redirect:/admin/accounts";
    }

}
