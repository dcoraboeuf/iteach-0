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
		  			var html = '';
		  			html += '<div class="student-lesson" id="{0}">'.format(id);
		  				html += '<div class="student-lesson-time"><a href="gui/lesson/{0}">{1}</a></div>'.format(lesson.id, getLessonSchedule(lesson));
		  				html += '<div class="student-lesson-location">@ {0}</div>'.format(lesson.location);
		  			html += '</div>';
		  			
		  			$('#lessons-list').append(html);
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
