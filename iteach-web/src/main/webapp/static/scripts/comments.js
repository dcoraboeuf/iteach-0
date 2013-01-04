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
		  			var html = '';
		  			html += '<div class="comment well" id="{0}">'.format(comment.id);
		  				// FIXME Escape and format the message
		  				html += '<div class="comment-content">{0}</div>'.format(comment.content.htmlWithLines());
		  			html += '</div>';
		  			
		  			$('#comments-list').append(html);
		  		}
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
	
	function createComment () {
		application.dialog({
			id: 'comments-dialog',
			title: loc('comment.new'),
			width: 500,
			data: {
				commentContent: ''
			},
			submit: {
				name: loc('general.create'),
				action: submitCreateComment
			}
		});
	}
	
	function submitCreateComment () {
		// URL
		var url = $('#comments-url').val();
		// POST
		$.ajax({
			type: 'POST',
			url: url,
			contentType: 'application/json',
			data: JSON.stringify({
				id: 0,
				content: $('#commentsContent').val()
			}),
			dataType: 'json',
			success: function (data) {
				// FIXME Adds the comment to the list
				location.reload();
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#comments-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#comments-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('comment.new.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function init () {
		$('#comments-error').hide();
		loadComments();
		$('#comments-new').click(createComment)
	}
	
	return {
		init: init
	};
	
} ();

$(document).ready(Comments.init);