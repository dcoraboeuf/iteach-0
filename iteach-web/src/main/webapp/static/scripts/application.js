// Prototypes

String.prototype.format = function() {
	var args = arguments;
	return this.replace(/\{\{|\}\}|\{(\d+)\}/g, function(m, n) {
		if (m == "{{") {
			return "{";
		}
		if (m == "}}") {
			return "}";
		}
		return args[n];
	});
};

String.prototype.html = function() {
	return $('<i></i>').text(this).html();
};

String.prototype.htmlWithLines = function() {
	var text = this.html();
	return text.replace(/\n/g, '<br/>');
}

// Global functions

function loc () {
	var code = arguments[0];
	var text = l[code];
	if (text != null) {
		var params = []	;
		for (var i = 1 ; i < arguments.length ; i++) {
			params.push(arguments[i]);
		}
		return text.format (params);
	} else {
		return "##" + code + "##";
	}
}

// Application singleton

var application = function () {
	
	function confirmAndCall (text, callback) {
		$('<div>{0}</div>'.format(text)).dialog({
			title: loc('general.confirm.title'),
			dialogClass: 'confirm-dialog',
			modal: true,
			buttons: {
				Ok: function () {
					$( this ).dialog( "close" );
					callback();
				},
				Cancel: function () {
					$( this ).dialog( "close" );
				}
			}
		});
	}
	
	function confirmIDAndCall (id, callback) {
		var text = document.getElementById(id).value;
		confirmAndCall(text, callback);
	}
	
	function onConfirmedAction (form, confirmID) {
		// Asks for confirmation
		myconfig.confirmIDAndCall (confirmID, function () {
			form.submit();
		});
		// Does not submit the form
		return false;
	}
	
	function displayError (text) {
		$('<div>{0}</div>'.format(text)).dialog({
			title: loc('general.error.title'),
			dialogClass: 'error-dialog',
			modal: true,
            buttons: {
                Ok: function() {
                    $( this ).dialog( "close" );
                }
            }
		});
	}
	
	function displayAjaxError (message, jqXHR, textStatus, errorThrown) {
		var text = '{0}\n[{1}] {2}'.format(message, jqXHR.status, jqXHR.statusText);
		displayError(text);
	}
	
	function changeLanguage (input) {
		var language = input.getValue();
		var url = "language/" + language;
		$.post (url, function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError (loc('message.cannotChangeLanguage'));
				}
			})
			.error(function (jqXHR, textStatus, errorThrown) {
				myconfig.displayAjaxError (loc('message.cannotChangeLanguage'), jqXHR, textStatus, errorThrown);
			});
	}

	function validate (selector, test) {
		if (test) {
			$(selector).removeClass("invalid");
			return true;
		} else {
			$(selector).addClass("invalid");
			return false;
		}
	}
	
	return {
		confirmAndCall: confirmAndCall,
		confirmIDAndCall: confirmIDAndCall,
		displayError: displayError,
		displayAjaxError: displayAjaxError,
		onConfirmedAction: onConfirmedAction,
		changeLanguage: changeLanguage,
		validateTextAsName: function (selector) {
			var value = $(selector).val();
			var trimmedValue = value.trim();
			return validate (selector, trimmedValue == value && trimmedValue != "");
		},
		validateTextAsTrimmed: function (selector) {
			var value = $(selector).val();
			var trimmedValue = value.trim();
			return validate (selector, trimmedValue == value);
		},
		validateConfirmation: function (source, confirmation) {
			var value = $(source).val();
			var confirmValue = $(confirmation).val();
			return validate (confirmation, confirmValue == value);
		}
	};
	
} ();