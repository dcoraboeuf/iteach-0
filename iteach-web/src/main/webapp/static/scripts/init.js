var Admin = function () {
	
	return {
		control: function () {
			var basicsOK = 
				   application.validateTextAsName('#firstName')
				&& application.validateTextAsName('#lastName')
				&& application.validateTextAsName('#email');
			if (basicsOK) {
				if (application.validateConfirmation('#password', '#confirmPassword')) {
					return true;
				} else {
					$('#password-confirm-line').addClass('error');
					return false;
				}
			} else {
				return false;
			}
		}
	};
	
} ();