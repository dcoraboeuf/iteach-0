var Planning = function () {
	
	function formatTimePart (n) {
		return n < 10 ? '0' + n : '' + n;
	}
	
	function formatTime (d) {
		var hours = formatTimePart(d.getHours());
		var minutes = formatTimePart(d.getMinutes());
		return hours + ':' + minutes;
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
		var view = "week";
		var op = {
			view: view,
			theme: 3,
			showday: new Date(),
			addHandler: create,
			autoload:true
		};
		
		op.height = $('body').height() - 150;
		
		var p = $("#planning-calendar").bcalendar(op).BcalGetOp();
	}

	return {
		init: init
	};

} ();

$(document).ready(Planning.init);