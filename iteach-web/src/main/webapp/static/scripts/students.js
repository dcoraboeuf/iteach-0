var Students = function () {
	
	// Private members
	
	function createStudent () {
		application.dialog({
			id: 'student-dialog',
			title: loc('student.new'),
			width: 500,
			data: {
				studentName: '',
				studentSchool: '',
				studentSubject: ''
			},
			open: function () {
				Coordinates.clear();
			},
			submit: {
				name: loc('general.create'),
				action: submitCreateStudent
			}
		});
	}
	
	function editStudent (id) {
		var name = $('#student-name-{0}'.format(id)).val();
		var school = $('#student-school-{0}'.format(id)).val();
		var subject = $('#student-subject-{0}'.format(id)).val();
		application.dialog({
			id: 'student-dialog',
			title: loc('student.edit', name),
			width: 500,
			data: {
				studentName: name,
				studentSchool: school,
				studentSubject: subject
			},
			submit: {
				name: loc('general.update'),
				action: function () {
					return submitEditStudent(id);
				}
			}
		});
	}
	
	function submitCreateStudent () {
		$.ajax({
			type: 'POST',
			url: 'ui/teacher/student',
			contentType: 'application/json',
			data: JSON.stringify({
				name: $('#studentName').val(),
				school: $('#studentSchool').val(),
				subject: $('#studentSubject').val(),
				coordinates: Coordinates.getValues()
			}),
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError(loc('student.new.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#student-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#student-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('student.new.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function submitEditStudent (id) {
		$.ajax({
			type: 'PUT',
			url: 'ui/teacher/student/{0}'.format(id),
			contentType: 'application/json',
			data: JSON.stringify({
				name: $('#studentName').val(),
				school: $('#studentSchool').val(),
				subject: $('#studentSubject').val(),
				coordinates: Coordinates.getValues()
			}),
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError(loc('student.edit.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#student-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#student-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('student.edit.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function deleteStudent (id) {
		var name = $('#student-name-{0}'.format(id)).val();
		application.confirmAndCall(
			loc('student.delete.prompt', name),
			function () {
				$.ajax({
					type: 'DELETE',
					url: 'ui/teacher/student/{0}'.format(id),
					contentType: 'application/json',
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							location.reload();
						} else {
							application.displayError(loc('student.delete.error'));
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
					  	application.displayAjaxError (loc('student.delete.error'), jqXHR, textStatus, errorThrown);
					}
				});
			}
		);
	}
	
	// Public members
	return {
		createStudent: createStudent,
		deleteStudent: deleteStudent,
		editStudent: editStudent
	};
	
} ();