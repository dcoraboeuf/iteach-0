var registration = function () {
	
	return {
		init: function () {
			var mode = document.getElementById('mode').value;
			if (mode == 'openid') {
				$('#password-line').hide();
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
					return application.validateConfirmation('#password', '#confirmPassword');
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