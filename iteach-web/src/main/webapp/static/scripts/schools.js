var Schools = function () {
	
	// Private members
	
	function create () {
		application.dialog('school', {
			title: loc('school.new'),
			width: 500
		});
	}
	
	function doCreate (formId) {
		$.ajax({
			type: 'POST',
			url: 'ui/teacher/school',
			contentType: 'application/json',
			data: JSON.stringify({
				name: $('#schoolName').val(),
				color: $('#schoolColor').val()
			}),
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError(loc('school.new.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#school-new-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#school-new-error').show();
			  	} else {
			  		application.displayAjaxError (loc('school.new.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	// Public members
	return {
		create: create,
		doCreate: doCreate
	};
	
} ();