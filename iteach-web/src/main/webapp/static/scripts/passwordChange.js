var PasswordChange = function () {
	
	return {
		control: function () {
			if (application.validateConfirmation('#newPassword', '#confirmPassword')) {
				return true;
			} else {
				$('#confirmPassword-group').addClass('error');
				return false;
			}
		}
	};
	
} ();