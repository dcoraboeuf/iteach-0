var Student = function () {
	
	function loadLessons () {
		// Marks the lessons as being loading...
		application.loading('#lessons-list', true);
		// Student ID
		var id = $('#student-id').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: 'ui/teacher/student/{0}/lessons'.format(id),
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
				// FIXME Fills the data
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		$('#lessons-error').html(application.getAjaxError(loc('student.details.lessons.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#lessons-error').show();
		  		application.loading('#lessons-list', false);
			}
		});
	}
	
	function init () {
		// No errors
		$('#lessons-error').hide();
		// Initial value
		application.setCurrentMonth('lessons-month');
		// Loadings
		loadLessons();
	}
	
	return {
		init: init
	};
	
} ();

$(document).ready(Student.init);
