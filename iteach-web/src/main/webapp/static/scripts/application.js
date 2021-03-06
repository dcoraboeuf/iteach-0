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

function id (value) {
	return '#' + value;
}

// Application singleton

var application = function () {
	
	function formatTimePart (n) {
		return n < 10 ? '0' + n : '' + n;
	}

	function formatTimeHM (hours, minutes) {
		var hours = formatTimePart(hours);
		var minutes = formatTimePart(minutes);
		return hours + ':' + minutes;
	}

	function formatTime (d) {
		return formatTimeHM(d.getHours(), d.getMinutes());
	}
	
	function formatDate (d) {
		return '{0}-{1}-{2}'.format(d.getFullYear(), formatTimePart(d.getMonth() + 1), formatTimePart(d.getDate()));
	}
	
	function formatDateTime (d) {
		return '{0}T{1}'.format(formatDate(d), formatTime(d));
	}
	
	function getCurrentDate () {
		return new Date($('#current_date').val());
	}
	
	function getMonth (d) {
		return i18n.monthNames[d.getMonth()];
	}
	
	function dialog (config) {
		// Values
		if (config.data) {
			for (var name in config.data) {
				$(id(name)).val(config.data[name]);
			}
		}
		if (config.submit) {
			$(id(config.id + '-submit')).text(config.submit.name);
			$(id(config.id + '-form')).unbind('submit');
			$(id(config.id + '-form')).submit(function () {
				return config.submit.action();
			});
		}
		// Open event
		var onOpen;
		if (config.open) {
			onOpen = config.open;
		} else {
			onOpen = function () {};
		}
		// Closing event
		var onClose;
		if (config.close) {
			onClose = function () {
				config.close();
			};
		} else {
			onClose = function () {};
		}
		// Error section
		$(id(config.id + '-error')).hide();
		// Shows the dialog
		$(id(config.id)).dialog({
		 	modal: true,
			title: config.title,
			width: config.width,
			close: onClose,
			open: onOpen
		});
	}
	
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
		$('<div>{0}</div>'.format(text.htmlWithLines())).dialog({
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
	
	function getAjaxError (message, jqXHR, textStatus, errorThrown) {
		return '{0}\n[{1}] {2}'.format(message, jqXHR.status, jqXHR.statusText);
	}
	
	function displayAjaxError (message, jqXHR, textStatus, errorThrown) {
		var text = getAjaxError(message, jqXHR, textStatus, errorThrown);
		displayError(text);
	}
	
	function changeLanguage (lang) {
		if (location.search.indexOf("language") > -1) {
	    	location.search = location.search.replace(/language=[a-z][a-z]/, "language=" + lang);
		} else if (location.search == "") {
			var url = location.href;
			if (url.substr(url.length - 1) == '?') {
				location.href += "language=" + lang;
			} else {
				location.href += "?language=" + lang;
			}
		} else {
			location.href += "&language=" + lang;
		}
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
	
	function loading (selector, loading) {
		$(selector).empty();
		if (loading) {
			$(selector).append('<div class="loading">{0}</div>'.format(loc('general.loading')));
		}
	}
	
	return {
		confirmAndCall: confirmAndCall,
		confirmIDAndCall: confirmIDAndCall,
		displayError: displayError,
		displayAjaxError: displayAjaxError,
		getAjaxError: getAjaxError,
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
		},
		validate: validate,
		dialog: dialog,
		loading: loading,
		getCurrentDate: getCurrentDate,
		getMonth: getMonth,
		formatTimePart: formatTimePart,
		formatTimeHM: formatTimeHM,
		formatTime: formatTime,
		formatDate: formatDate,
		formatDateTime: formatDateTime
	};
	
} ();