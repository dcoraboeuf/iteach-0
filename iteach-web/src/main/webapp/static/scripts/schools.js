var Schools = function () {
	
	// Private members
	
	function createSchool () {
		application.dialog({
			id: 'school-dialog',
			title: loc('school.new'),
			width: 500,
			data: {
				schoolName: '',
				schoolColor: '#000000',
				schoolHourlyRate: '0.0'
			},
			open: function () {
				Coordinates.clear('school');
			},
			submit: {
				name: loc('general.create'),
				action: submitCreateSchool
			}
		});
	}
	
	function editSchool (id) {
		var name = $('#school-name-{0}'.format(id)).val();
		var color = $('#school-color-{0}'.format(id)).val();
		var hourlyRate = $('#school-hourlyRate-{0}'.format(id)).val();
		application.dialog({
			id: 'school-dialog',
			title: loc('school.edit', name),
			width: 500,
			data: {
				schoolName: name,
				schoolColor: color,
				schoolHourlyRate: hourlyRate
			},
			open: function () {
				Coordinates.setValues('school', 'ui/teacher/school/{0}/coordinates'.format(id));
			},
			submit: {
				name: loc('general.update'),
				action: function () {
					return submitEditSchool(id);
				}
			}
		});
	}
	
	function validateHourlyRate () {
		var value = $('#schoolHourlyRate').val();
		var n = Number(value);
		if (n == NaN) {
			return false;
		} else {
			return n >= 0;
		}
	}
	
	function validateSchool () {
		if (!validateHourlyRate()) {
			$('#school-dialog-error').html(loc('school.hourlyRate.wrong'));
			$('#school-dialog-error').show();
			return false;
		} else {
			return true;
		}
	}
	
	function submitCreateSchool () {
		if (validateSchool()) {
			$.ajax({
				type: 'POST',
				url: 'ui/teacher/school',
				contentType: 'application/json',
				data: JSON.stringify({
					name: $('#schoolName').val(),
					color: $('#schoolColor').val(),
					coordinates: Coordinates.getValues('school')
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
		}
		return false;
	}
	
	function submitEditSchool (id) {
		if (validateSchool()) {
			$.ajax({
				type: 'PUT',
				url: 'ui/teacher/school/{0}'.format(id),
				contentType: 'application/json',
				data: JSON.stringify({
					name: $('#schoolName').val(),
					color: $('#schoolColor').val(),
					coordinates: Coordinates.getValues('school')
				}),
				dataType: 'json',
				success: function (data) {
					if (data.success) {
						location.reload();
					} else {
						application.displayError(loc('school.edit.error'));
					}
				},
				error: function (jqXHR, textStatus, errorThrown) {
				  	if (jqXHR.responseText && jqXHR.responseText != '') {
				  		$('#school-dialog-error').html(jqXHR.responseText.htmlWithLines());
				  		$('#school-dialog-error').show();
				  	} else {
				  		application.displayAjaxError (loc('school.edit.error'), jqXHR, textStatus, errorThrown);
				  	}
				}
			});
		}
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
		deleteSchool: deleteSchool,
		editSchool: editSchool
	};
	
} ();