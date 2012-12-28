var Planning = function () {
	
	function formatTimePart (n) {
		return n < 10 ? '0' + n : '' + n;
	}

	function formatTime (d) {
		var hours = formatTimePart(d.getHours());
		var minutes = formatTimePart(d.getMinutes());
		return hours + ':' + minutes;
	}
	
	function formatDate (d) {
		return '{0}-{1}-{2}'.format(d.getFullYear(), formatTimePart(d.getMonth() + 1), formatTimePart(d.getDate()));
	}
	
	function formatDateTime (d) {
		return '{0}T{1}'.format(formatDate(d), formatTime(d));
	}
	
	function fetchEvents (start, end, callback) {
		$.ajax({
			type: 'POST',
			url: 'ui/calendar/lessons',
			contentType: 'application/json',
			data: JSON.stringify({
				from: formatDateTime(start),
				to: formatDateTime(end)
			}),
			dataType: 'json',
			success: function (data) {
				callback(data.events);
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	application.displayAjaxError (loc('lesson.fetch.error'), jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	function onDelete (id) {
		application.confirmAndCall(
			loc('lesson.delete.prompt'),
			function () {
				$.ajax({
					type: 'DELETE',
					url: 'ui/teacher/lesson/{0}'.format(id),
					contentType: 'application/json',
					dataType: 'json',
					success: function (data) {
						if (data.success) {
							$("#planning-calendar").fullCalendar('refetchEvents');
							$('#lesson-dialog').dialog('close');
						} else {
							application.displayError(loc('lesson.delete.error'));
						}
					},
					error: function (jqXHR, textStatus, errorThrown) {
					  	application.displayAjaxError (loc('lesson.delete.error'), jqXHR, textStatus, errorThrown);
					}
				});
			}
		);
	}
	
	function onEdit (calEvent, jsEvent, view) {
		var date = calEvent.lesson.date;
		var startTime = calEvent.lesson.from;
		var endTime = calEvent.lesson.to;
		application.dialog({
			id: 'lesson-dialog',
			title: loc('lesson.edit'),
			width: 500,
			data: {
				lessonDate: date,
				lessonFrom: startTime,
				lessonTo: endTime,
				lessonStudent: calEvent.lesson.student.id,
				lessonLocation: calEvent.lesson.location
			},
			submit: {
				name: loc('general.update'),
				action: function () {
					return submitEditLesson (calEvent.id);
				}
			},
			open: function () {
				$('#lesson-dialog-delete').unbind('click');
				$('#lesson-dialog-delete').click(function () {
					onDelete(calEvent.id);
				});
				$('#lesson-dialog-delete').show();
			},
			close: function () {
				$("#planning-calendar").fullCalendar('unselect');
			}
		});		
	}
	
	function onSelect (start, end, allDay) {
		if (allDay) {
			$("#planning-calendar").fullCalendar('unselect');
		} else {
			var date = formatDate(start);
			var startTime = formatTime(start);
			var endTime = formatTime(end);
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
				open: function () {
					$('#lesson-dialog-delete').hide();
				},
				cancel: function () {
					$("#planning-calendar").fullCalendar('unselect');
				}
			});			
		}
	}
	
	function submitEditLesson (id) {
		$.ajax({
			type: 'PUT',
			url: 'ui/teacher/lesson/{0}'.format(id),
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
					$("#planning-calendar").fullCalendar('refetchEvents');
					$('#lesson-dialog').dialog('close');
				} else {
					application.displayError(loc('lesson.edit.error'));
				}
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#lesson-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#lesson-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('lesson.edit.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
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
					$("#planning-calendar").fullCalendar('refetchEvents');
					$('#lesson-dialog').dialog('close');
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
			// i18n
			firstDay: i18n.firstDay,
			dayNames: i18n.dayNames,
			dayNamesShort: i18n.dayNamesShort,
			monthNames: i18n.monthNames,
			monthNamesShort: i18n.monthNamesShort,
			buttonText: i18n.buttonText,
			timeFormat: i18n.timeFormat,
			columnFormat: i18n.columnFormat,
			titleFormat: i18n.titleFormat,
			axisFormat: i18n.axisFormat,
			// General appearance
			allDaySlot: false,
			minTime: 6,
			maxTime: 22,
			weekends: true,
			// Default view
			defaultView: 'agendaWeek',
			// Allowing selection (-> creation)
			selectable: true,
			selectHelper: true,
			select: onSelect,
			// Opening the lesson
			eventClick: onEdit,
			// Loading of events
			events: fetchEvents
		});
	}

	return {
		init: init
	};

} ();

$(document).ready(Planning.init);