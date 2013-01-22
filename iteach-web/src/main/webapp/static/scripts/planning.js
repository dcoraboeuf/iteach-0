var Planning = function () {
	
	var initialization = true;
	
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
					from: application.formatDateTime(start),
					to: application.formatDateTime(end)
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
	
	function onSelect (start, end, allDay) {
		if (allDay) {
			$("#planning-calendar").fullCalendar('unselect');
		} else {
			var date = application.formatDate(start);
			var startTime = application.formatTime(start);
			var endTime = application.formatTime(end);
			Lessons.createLesson (
				date,
				startTime, endTime,
				function () {
					$("#planning-calendar").fullCalendar('unselect');
				},
				function () {
					$("#planning-calendar").fullCalendar('refetchEvents');
				});
		}
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
		init: init
	};

} ();

$(document).ready(Planning.init);