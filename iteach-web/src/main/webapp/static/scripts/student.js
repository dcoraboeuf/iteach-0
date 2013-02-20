var Student = function () {
		
	function getLessonSchedule (lesson) {
		return '{0} - {1} - {2}'.format (lesson.localizedDate, lesson.localizedFrom, lesson.localizedTo);
	}
	
	function loadLessons () {
		loadLessonsWith('gui/student/{0}/lessons');
	}
	
	function displayMonth (d) {
		$('#lessons-month').text ('{0} {1}'.format(
			application.getMonth(d),
			d.getFullYear()
		));
	}
	
	function loadLessonsWith (url) {
		// Marks the lessons as being loading...
		application.loading('#lessons-list', true);
		// Student ID
		var id = $('#student-id').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: url.format(id),
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
		  		application.loading('#lessons-list', false);
		  		// Month
		  		displayMonth(new Date(data.date));
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

	function disable () {
		// Student ID
		var id = $('#student-id').val();
        // Confirmation before going on
        application.confirmAndCall(
            loc('student.disable.prompt'),
            function () {
                location = 'gui/student/{0}/disable'.format(id);
            }
        );
	}
	
	function previousMonth () {
		loadLessonsWith('gui/student/{0}/lessons/previousMonth');
	}
	
	function nextMonth () {
		loadLessonsWith('gui/student/{0}/lessons/nextMonth');
	}
	
	function init () {
		// No errors
		$('#lessons-error').hide();
		// Initial value
		displayMonth (application.getCurrentDate());
		// Loadings
		loadLessons();
	}
	
	return {
		init: init,
		previousMonth: previousMonth,
		nextMonth: nextMonth,
		disable: disable
	};
	
} ();

$(document).ready(Student.init);
