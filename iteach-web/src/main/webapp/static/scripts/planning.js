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
		
	}

	function init () {
		var view = "week";           
		var DATA_FEED_URL = "php/datafeed.php";
		var op = {
			view: view,
			theme: 3,
			showday: new Date(),
			addHandler: create,
			//EditCmdhandler:Edit,
			//DeleteCmdhandler:Delete,
			//ViewCmdhandler:View,    
			//onWeekOrMonthToDay:wtd,
			//onBeforeRequestData: cal_beforerequest,
			//onAfterRequestData: cal_afterrequest,
			//onRequestDataError: cal_onerror, 
			autoload:true,
			//url: DATA_FEED_URL + "?method=list",  
			//quickAddUrl: DATA_FEED_URL + "?method=add", 
			//quickUpdateUrl: DATA_FEED_URL + "?method=update",
			//quickDeleteUrl: DATA_FEED_URL + "?method=remove"        
		};
		
		op.height = $('#home-row').height() - $('planning-panel-title').height() - 50;
		
		var p = $("#planning-calendar").bcalendar(op).BcalGetOp();
	}

	return {
		init: init
	};

} ();

$(document).ready(Planning.init);