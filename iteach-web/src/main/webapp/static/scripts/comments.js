var Comments = function () {
	
	var commentsSpan = 5;
	var totalComments = 0;

	function formatTimestamp (timestamp) {
		return $.fullCalendar.formatDate (new Date(timestamp), i18n.timestampFormat, i18n);
	}
	
	function commentContent (comment) {
		if (comment.summary) {
			return '{1} <span onclick="Comments.reloadComment({0});" class="comment-more" title="{3}">{2}</span>'.format(
				comment.id,
				comment.content,
				loc('comment.more'),
				loc('comment.more.tip'));
		} else {
			return comment.content;
		}
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
			html += '</div>';
			html += '<div class="comment-content" id="comment-content-{0}">{1}'.format(comment.id, commentContent(comment));
				html += ' <span class="comment-actions">';
					html += '<i id="comment-edit-{0}" class="icon-edit"></i>'.format(comment.id);
					html += '<i id="comment-delete-{0}" class="icon-trash"></i>'.format(comment.id);
				html += '</span>';
			html += '</div>';
		html += '</div>';
		return html;
	}
	
	function reloadComment (id) {
		loadComment (id, function (comment) {
			$('#comment-{0}'.format(id)).replaceWith(commentToHTML(comment));
			setCommentActions (id);
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
					totalComments--;
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
	
	function setCommentActions (id) {
		$("#comment-delete-{0}".format(id)).click(function () {
			deleteComment(id);
		});
		$("#comment-edit-{0}".format(id)).click(function () {
			editComment(id);
		});
	}
	
	function appendComment (comment) {
		$('#comments-list').append(commentToHTML (comment));
		setCommentActions (comment.id);
	}
	
	/**
	 * Makes sure to reload the comment for proper formatting
	 */
	function prependComment (comment) {
		loadComment(comment.id, function (c) {
			$('#comments-list').prepend(commentToHTML (c));
			setCommentActions (c.id);
		});
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
		  		// More button
		  		if (data.more) {
		  			$('#comments-list-more').show();
		  		} else {
		  			$('#comments-list-more').hide();
		  		}
		  		// Adjust the total
		  		totalComments += data.list.length;
			},
			error: function (jqXHR, textStatus, errorThrown) {
		  		$('#comments-error').html(application.getAjaxError(loc('comments.loading.error'), jqXHR, textStatus, errorThrown).htmlWithLines());
		  		$('#comments-error').show();
		  		application.loading('#comments-list-loading', false);
			}
		});
	}
	
	function loadComments () {
		loadCommentsWith (0, commentsSpan);
	}
	
	function loadMore () {
		loadCommentsWith (totalComments, commentsSpan);
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
		init: init,
		reloadComment: reloadComment,
		loadMore: loadMore
	};
	
} ();

$(document).ready(Comments.init);