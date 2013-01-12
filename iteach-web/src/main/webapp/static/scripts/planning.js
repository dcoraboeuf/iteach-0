var Planning = function () {
	
	var initialization = true;
	
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
	
	function completeEvents (events) {
		$.each (events, function (index, event) {
			event.url = 'gui/lesson/{0}'.format(event.id);
		});
	}
	
	function onEventChange (event, dayDelta, minuteDelta, revertFunc) {
		$.ajax({
			type: 'POST',
			url: 'ui/teacher/lesson/{0}/change'.format(event.id),
			contentType: 'application/json',
			data: JSON.stringify({
				dayDelta: dayDelta,
				minuteDelta: minuteDelta
			}),
			dataType: 'json',
			success: function (data) {
				// Nothing to do... Already displayed
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	revertFunc();
			  	application.displayAjaxError (loc('lesson.change.error'), jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	function fetchEvents (start, end, callback) {
		var setDate;
		if (initialization) {
			initialization = false;
			setDate = false;
		} else {
			setDate = true;
		}
		$.ajax({
			type: 'POST',
			url: 'gui/lesson/list',
			contentType: 'application/json',
			data: JSON.stringify({
				range: {
					from: formatDateTime(start),
					to: formatDateTime(end)
				},
				setDate: setDate
			}),
			dataType: 'json',
			success: function (data) {
				// Complete the events data
				completeEvents(data.events);
				// Displays the events
				callback(data.events);
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	application.displayAjaxError (loc('lesson.fetch.error'), jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	function deleteLesson (id) {
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
							// Going back to the planning
							location = "gui/home";
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
	
	function editLesson () {
		var id = $('#lesson-id').val();
		var student = $('#lesson-student').val();
		var date = $('#lesson-date').val();
		var startTime = $('#lesson-from').val();
		var endTime = $('#lesson-to').val();
		var location = $('#lesson-location').val();
		application.dialog({
			id: 'lesson-dialog',
			title: loc('lesson.edit'),
			width: 500,
			data: {
				lessonDate: date,
				lessonFrom: startTime,
				lessonTo: endTime,
				lessonStudent: student,
				lessonLocation: location
			},
			submit: {
				name: loc('general.update'),
				action: function () {
					return submitEditLesson (id);
				}
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
					location.reload();
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
		// Current date on initialization
		var currentDate = application.getCurrentDate();
		// TODO Week-end enabled or not
		// TODO Basic or agenda mode
		var p = $("#planning-calendar").fullCalendar({
			header: {
				left: 'prev,next today',
				center: 'title',
				right: 'month,agendaWeek,agendaDay'
			},
			// Current date
			year: currentDate.getFullYear(),
			month: currentDate.getMonth(),
			date: currentDate.getDate(),
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
			// Loading of events
			events: fetchEvents,
			// Resizing of an event
			editable: true,
			eventResize: onEventChange,
			eventDrop: function (event, dayDelta, minuteDelta, allDay, revertFunc) {
				onEventChange(event, dayDelta, minuteDelta, revertFunc);
			}
		});
	}

	return {
		init: init,
		deleteLesson: deleteLesson,
		editLesson: editLesson
	};

} ();

$(document).ready(Planning.init);