var Schools = function () {
	
	// Private members
	
	function createSchool () {
		$('#schoolName').val('');
		$('#schoolColor').val('#FFFFFF');
		application.dialog({
			id: 'school-dialog',
			title: loc('school.new'),
			width: 500
		});
	}
	
	function submitCreateSchool () {
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
			  		$('#school-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#school-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('school.new.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function deleteSchool (id) {
		var name = $('#school-name-{0}'.format(id)).val();
		application.confirmAndCall(
			loc('school.delete.prompt', name),
			function () {
				$.ajax({
					type: 'DELETE',
					url: 'ui/teacher/school/{0}'.format(id),
					contentType: 'application/json',
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							location.reload();
						} else {
							application.displayError(loc('school.delete.error'));
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
					  	application.displayAjaxError (loc('school.delete.error'), jqXHR, textStatus, errorThrown);
					}
				});
			}
		);
	}
	
	// Public members
	return {
		createSchool: createSchool,
		submitCreateSchool: submitCreateSchool,
		deleteSchool: deleteSchool
	};
	
} ();