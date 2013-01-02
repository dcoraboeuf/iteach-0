var Coordinates = function () {
	
	function clear () {
		$('input[id^="coord_"]').val('');
	}
	
	function fetchCoordinates (url, callbackFn) {
		$.ajax({
			type: 'GET',
			url: url,
			contentType: 'application/json',
			dataType: 'json',
			success: function (data) {
				callbackFn(data.map);
			},
			error: function (jqXHR, textStatus, errorThrown) {
			  	application.displayAjaxError (loc('coordinates.get.error'), jqXHR, textStatus, errorThrown);
			}
		});
	}
	
	return {
		clear: clear,
		setValues: function (url) {
			clear();
			fetchCoordinates(url, function (map) {
				for (var name in map) {
					var id = 'coord_' + name;
					$('#' + id).val(map[name]);
				}
			});
		},
		getValues: function () {
			var map = {};
			$('input[id^="coord_"]').each(function (index, field) {
				map[field.getAttribute('name')] = field.value; 
			});
			$('textarea[id^="coord_"]').each(function (index, field) {
				map[field.getAttribute('name')] = field.value; 
			});
			return {map: map};
		}
	};
	
} ();