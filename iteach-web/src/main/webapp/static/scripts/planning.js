var Planning = function () {
	
	function create (start, end, isallday, pos) {
		alert(JSON.stringify({
			start: start,
			end: end,
			isallday: isallday,
			pos: pos
		}));
		return false;
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