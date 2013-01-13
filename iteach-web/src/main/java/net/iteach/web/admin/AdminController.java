package net.iteach.web.admin;

import net.iteach.api.admin.AccountSummary;
import net.iteach.api.admin.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin")
public class AdminController {

	private final AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		super();
		this.adminService = adminService;
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
	 * Deletes the user
	 */
	@RequestMapping(value = "/account/{id}/delete", method = RequestMethod.POST)
	public String accountDelete(Model model, @PathVariable int id) {
		// Deletes the user
		adminService.deleteAccount(id);
		// OK
		return "redirect:/admin/accounts";
	}

}
