var Students = function () {
	
	// Private members
	
	function createStudent () {
		application.dialog({
			id: 'student-dialog',
			title: loc('student.new'),
			width: 500,
			data: {
				studentFirstName: '',
				studentLastName: '',
				studentSubject: ''
			},
			submit: {
				name: loc('general.create'),
				action: submitCreateStudent
			}
		});
	}
	
	function editStudent (id) {
		var name = $('#Student-name-{0}'.format(id)).val();
		var color = $('#Student-color-{0}'.format(id)).val();
		application.dialog({
			id: 'Student-dialog',
			title: loc('Student.edit', name),
			width: 500,
			data: {
				StudentName: name,
				StudentColor: color
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
				firstName: $('#studentFirstName').val(),
				lastName: $('#studentLastName').val(),
				subject: $('#studentSubject').val()
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
			url: 'ui/teacher/Student/{0}'.format(id),
			contentType: 'application/json',
			data: JSON.stringify({
				name: $('#StudentName').val(),
				color: $('#StudentColor').val()
			}),
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError(loc('Student.edit.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#Student-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#Student-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('Student.edit.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function deleteStudent (id) {
		var name = $('#Student-name-{0}'.format(id)).val();
		application.confirmAndCall(
			loc('Student.delete.prompt', name),
			function () {
				$.ajax({
					type: 'DELETE',
					url: 'ui/teacher/Student/{0}'.format(id),
					contentType: 'application/json',
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							location.reload();
						} else {
							application.displayError(loc('Student.delete.error'));
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
					  	application.displayAjaxError (loc('Student.delete.error'), jqXHR, textStatus, errorThrown);
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