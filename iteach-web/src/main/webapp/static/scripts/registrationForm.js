var registration = function () {
	
	return {
		init: function () {
			var mode = document.getElementById('mode').value;
			if (mode == 'openid') {
				$('#password').removeAttr('required');
				$('#password-line').hide();
				$('#confirmPassword').removeAttr('required');
				$('#password-confirm-line').hide();
			}
		},
		control: function () {
			var basicsOK = 
				   application.validateTextAsName('#firstName')
				&& application.validateTextAsName('#lastName')
				&& application.validateTextAsName('#email');
			if (basicsOK) {
				var mode = document.getElementById('mode').value;
				if (mode == 'openid') {
					return true;
				} else {
					if (application.validateConfirmation('#password', '#confirmPassword')) {
						return true;
					} else {
						$('#password-confirm-line').addClass('error');
						return false;
					}
				}
			} else {
				return false;
			}
		}
	};
	
} ();

$(document).ready(function() {
	registration.init();
});