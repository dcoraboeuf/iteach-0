var Comments = function () {

	function formatTimestamp (timestamp) {
		return $.fullCalendar.formatDate (new Date(timestamp), i18n.timestampFormat, i18n);
	}
	
	function commentToHTML (comment) {
		var html = '';
		html += '<div class="comment well" id="comment-{0}">'.format(comment.id);
			html += '<div class="comment-header">';
				html += '<small class="comment-creation">{0}</small>'.format(formatTimestamp(comment.creation));
				if (comment.edition) {
					html += ' <small class="comment-edition">({0})</small>'.format(
						loc('comment.edition', formatTimestamp(comment.edition))
						);
				}
				html += '<span class="comment-actions pull-right">';
					html += '<i id="comment-edit-{0}" class="icon-edit"></i>'.format(comment.id);
					html += '<i id="comment-delete-{0}" class="icon-trash"></i>'.format(comment.id);
				html += '</span>';
			html += '</div>';
			html += '<div class="comment-content" id="comment-content-{0}">{1}</div>'.format(comment.id, comment.content);
		html += '</div>';
		return html;
	}
	
	function reloadComment (id) {
		loadComment (id, function (comment) {
			$('#comment-{0}'.format(id)).replaceWith(commentToHTML(comment));
		});
	}
	
	function loadComment (commentId, callbackFn) {
		// URL
		var url = $('#comments-url').val();
		$.ajax({
			type: 'GET',
			url: '{0}/{1}/HTML'.format(url, commentId),
			dataType: 'json',
			success: function (data) {
		  		application.loading('#comments-list-loading', false);
		  		callbackFn(data);
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		application.loading('#comments-list-loading', false);
		  		$('#comments-error').html(application.getAjaxError(loc('comments.preview.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#comments-error').show();
			}
		});
	}
	
	function preview () {
  		application.loading('#commentsPreview', true);
		$.ajax({
			type: 'POST',
			url: 'ui/comment/preview',
			contentType: 'application/json',
			data: JSON.stringify({
				format: 'HTML',
				content: $('#commentsContent').val()
			}),
			dataType: 'json',
			success: function (data) {
		  		application.loading('#commentsPreview', false);
		  		$('#commentsPreview').html(data.content);
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		application.loading('#commentsPreview', false);
		  		$('#comments-preview-error').html(application.getAjaxError(loc('comments.preview.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#comments-preview-error').show();
			}
		});
	}
	
	function deleteComment (id) {
		var url = $('#comments-url').val();
		application.confirmAndCall (loc('comment.delete.prompt'), function () {
			$.ajax({
				type: 'DELETE',
				url: '{0}/{1}'.format(url, id),
				dataType: 'json',
				success: function (data) {
					$('#comment-{0}'.format(id)).remove();
				},
				error: function (jqXHR, textStatus, errorThrown) {
					application.displayAjaxError(loc('comment.delete.error'), jqXHR, textStatus, errorThrown);
				}
			});
		});
	}
	
	function editComment (id) {
		var url = $('#comments-url').val();
		// Loads the full comment
		application.loading('#comments-list-loading', true);
		$.ajax({
			type: 'GET',
			url: '{0}/{1}/RAW'.format(url, id),
			dataType: 'json',
			success: function (data) {
				application.loading('#comments-list-loading', false);
				application.dialog({
					id: 'comments-dialog',
					title: loc('comment.edit'),
					width: 500,
					data: {
						commentsContent: data.content
					},
					open: function () {
						$('#comment-tab-content').tab('show');
						$('#comments-preview-error').hide();
					},
					submit: {
						name: loc('general.update'),
						action: function () {
							return submitEditComment(id);
						}
					}
				});
			},
			error: function (jqXHR, textStatus, errorThrown) {
				application.loading('#comments-list-loading', false);
				application.displayAjaxError(loc('comment.loading.error'), jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	function setCommentActions (comment) {
		$("#comment-delete-{0}".format(comment.id)).click(function () {
			deleteComment(comment.id);
		});
		$("#comment-edit-{0}".format(comment.id)).click(function () {
			editComment(comment.id);
		});
	}
	
	function appendComment (comment) {
		$('#comments-list').append(commentToHTML (comment));
		setCommentActions (comment);
	}
	
	/**
	 * Makes sure to reload the comment for proper formatting
	 */
	function prependComment (comment) {
		loadComment(comment.id, function (c) {
			$('#comments-list').prepend(commentToHTML (c));
		});
		setCommentActions (comment);
	}
	
	function loadCommentsWith (offset, count) {
		// Marks the lessons as being loading...
		application.loading('#comments-list-loading', true);
		// URL
		var url = $('#comments-url').val();
		// Loads the lessons
		$.ajax({
			type: 'GET',
			url: '{0}/list/150/HTML/{1}/{2}'.format(url, offset, count), // 150 is the maximum length for the summary text
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
		  		application.loading('#comments-list-loading', false);
		  		// For each comment
		  		for (var i in data.list) {
		  			var comment = data.list[i];
		  			appendComment(comment);
		  		}
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		$('#comments-error').html(application.getAjaxError(loc('comments.loading.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#comments-error').show();
		  		application.loading('#comments-list-loading', false);
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
		  		$('#comments-preview-error').hide();
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
	
	function submitEditComment (id) {
		// URL
		var url = $('#comments-url').val();
		// POST
		$.ajax({
			type: 'POST',
			url: '{0}/HTML'.format(url),
			contentType: 'application/json',
			data: JSON.stringify({
				id: id,
				content: $('#commentsContent').val()
			}),
			dataType: 'json',
			success: function (data) {
				reloadComment(id);
	  			$('#comments-dialog').dialog('close');
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	if (jqXHR.responseText && jqXHR.responseText != '') {
			  		$('#comments-dialog-error').html(jqXHR.responseText.htmlWithLines());
			  		$('#comments-dialog-error').show();
			  	} else {
			  		application.displayAjaxError (loc('comment.edit.error'), jqXHR, textStatus, errorThrown);
			  	}
			}
		});
		return false;
	}
	
	function init () {
		$('#comments-error').hide();
		loadComments();
		$('#comments-new').click(createComment);
		// Dialog -> preview
		$('#comment-tab-preview').on('show', function (e) {
			preview();
		})
	}
	
	return {
		init: init
	};
	
} ();

$(document).ready(Comments.init);