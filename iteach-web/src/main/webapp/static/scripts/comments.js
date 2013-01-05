var Comments = function () {
	
	function commentToHTML (comment) {
		var html = '';
		html += '<div class="comment well" id="{0}">'.format(comment.id);
			// FIXME Escape and format the message
			html += '<div class="comment-content">{0}</div>'.format(comment.content.htmlWithLines());
		html += '</div>';
		return html;
	}
	
	function preview () {
		
	}
	
	function appendComment (comment) {
		$('#comments-list').append(commentToHTML (comment));
	}
	
	function prependComment (comment) {
		$('#comments-list').prepend(commentToHTML (comment));
	}
	
	function loadCommentsWith (offset, count) {
		// Marks the lessons as being loading...
		application.loading('#comments-list', true);
		// URL
		var url = $('#comments-url').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: '{0}/list/150/HTML/{1}/{2}'.format(url, offset, count), // 150 is the maximum length for the summary text
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
		  		application.loading('#comments-list', false);
		  		// For each comment
		  		for (var i in data.list) {
		  			var comment = data.list[i];
		  			appendComment(comment);
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
				commentsContent: ''
			},
			open: function () {
				$('#comment-tab-content').tab('show');
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
			url: '{0}/HTML'.format(url),
			contentType: 'application/json',
			data: JSON.stringify({
				id: 0,
				content: $('#commentsContent').val()
			}),
			dataType: 'json',
			success: function (data) {
				// Adds the comment to the list at the beginning
	  			prependComment(data);
	  			$('#comments-dialog').dialog('close');
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
		init: init,
		preview: preview
	};
	
} ();

$(document).ready(Comments.init);