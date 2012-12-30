var Student = function () {
	
	function getLessonDate (lesson) {
		return $.fullCalendar.formatDate (new Date (lesson.date), i18n.dayOfMonthFormat, i18n);
	}
	
	function getLessonTimeSchedule (lesson) {
		return '{0} - {1}'.format(
				getLessonTime (lesson, lesson.from),
				getLessonTime (lesson, lesson.to)
			);
	}
	
	function getLessonTime (lesson, time) {
		return $.fullCalendar.formatDate (new Date (lesson.date + 'T' + time), i18n.simpleTimeFormat, i18n);
	}
		
	function getLessonSchedule (lesson) {
		return '{0} - {1}'.format (getLessonDate(lesson), getLessonTimeSchedule(lesson));
	}
	
	function loadLessons () {
		// Marks the lessons as being loading...
		application.loading('#lessons-list', true);
		// Student ID
		var id = $('#student-id').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: 'gui/student/{0}/lessons'.format(id),
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
		  		application.loading('#lessons-list', false);
		  		// Month
		  		$('#lessons-month').text(application.getMonth(new Date(data.date)));
		  		// For each lesson
		  		for (var i in data.lessons) {
		  			var lesson = data.lessons[i];
		  			$('#lessons-list').append('<div class="student-lesson" id="lesson-{0}"><div class="student-lesson-schedule">{1}</div><div class="student-lesson-location">{2}</div></div>'.format(
		  					lesson.id,
		  					getLessonSchedule(lesson),
		  					lesson.location));
		  		}
		  		// Hours for the month
		  		$('#lessons-month-hours').text(data.hours);
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
