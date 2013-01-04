var Comments = function () {
	
	function loadCommentsWith (offset, count) {
		// Marks the lessons as being loading...
		application.loading('#comments-list', true);
		// URL
		var url = $('#comments-url').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: '{0}/{1}/{2}'.format(url, offset, count),
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
		  		application.loading('#comments-list', false);
		  		// For each comment
		  		for (var i in data.list) {
		  			var comment = data.list[i];
//		  			var html = '';
//		  			html += '<div class="student-lesson" id="{0}">'.format(id);
//		  				html += '<div class="student-lesson-time"><a href="gui/lesson/{0}">{1}</a></div>'.format(lesson.id, getLessonSchedule(lesson));
//		  				html += '<div class="student-lesson-location">@ {0}</div>'.format(lesson.location);
//		  			html += '</div>';
//		  			
//		  			$('#comments-list').append(html);
		  		}
		  		$('#comments-list').append("TODO Loaded comments.");
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		$('#comments-error').html(application.getAjaxError(loc('comments.loading.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#comments-error').show();
		  		application.loading('#comments-list', false);
			}
		});
	}
	
	function loadComments () {
		loadCommentsWith (0, 10);
	}
	
	function init () {
		$('#comments-error').hide();
		loadComments();
	}
	
	return {
		init: init
	};
	
} ();

$(document).ready(Comments.init);