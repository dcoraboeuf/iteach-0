package net.iteach.web.admin;

import net.iteach.api.admin.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
	 * Main page
	 */
	@RequestMapping(value = "/accounts", method = RequestMethod.GET)
	public String accounts(Model model) {
		// List of account summaries
		model.addAttribute("accounts", adminService.getAccounts());
		// OK
		return "admin/accounts";
	}

}
