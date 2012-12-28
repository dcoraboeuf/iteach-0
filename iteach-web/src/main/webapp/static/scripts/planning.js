var Planning = function () {
	
	function onSelect (start, end, allDay) {
		alert('onSelect');
		$("#planning-calendar").fullCalendar('unselect');
	}
	
	function create (config) {
		if (config.isallday) {
			return false;
		}
		var date = '{0}-{1}-{2}'.format(config.start.getFullYear(), config.start.getMonth() + 1, config.start.getDate());
		var startTime = formatTime(config.start);
		var endTime = formatTime(config.end);
		application.dialog({
			id: 'lesson-dialog',
			title: loc('lesson.new'),
			width: 500,
			data: {
				lessonDate: date,
				lessonFrom: startTime,
				lessonTo: endTime,
				lessonStudent: '',
				lessonLocation: ''
			},
			submit: {
				name: loc('general.create'),
				action: submitCreateLesson
			},
			cancel: config.onCancel
		});
	}
	
	function submitCreateLesson () {
		$.ajax({
			type: 'POST',
			url: 'ui/teacher/lesson',
			contentType: 'application/json',
			data: JSON.stringify({
				date: $('#lessonDate').val(),
				from: $('#lessonFrom').val(),
				to: $('#lessonTo').val(),
				student: $('#lessonStudent').val(),
				location: $('#lessonLocation').val()
			}),
			dataType: 'json',
			success: function (data) {
				if (data.success) {
					location.reload();
				} else {
					application.displayError(loc('lesson.new.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#lesson-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#lesson-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('lesson.new.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}

	function init () {
		// TODO Week-end enabled or not
		// TODO Basic or agenda mode
		var p = $("#planning-calendar").fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			weekends: true,
			defaultView: 'agendaWeek',
			selectable: true,
			selectHelper: true,
			select: onSelect
		});
	}

	return {
		init: init
	};

} ();

$(document).ready(Planning.init);