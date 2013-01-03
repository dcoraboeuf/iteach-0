var Coordinates = function () {
	
	function clear (prefix) {
		$('input[id^="{0}_coord_"]'.format(prefix)).val('');
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
		setValues: function (prefix, url) {
			clear(prefix);
			fetchCoordinates(url, function (map) {
				for (var name in map) {
					var id = prefix + '_coord_' + name;
					$('#' + id).val(map[name]);
				}
			});
		},
		getValues: function (prefix) {
			var map = {};
			$('input[id^="{0}_coord_"]'.format(prefix)).each(function (index, field) {
				map[field.getAttribute('name')] = field.value; 
			});
			$('textarea[id^="{0}_coord_"]'.format(prefix)).each(function (index, field) {
				map[field.getAttribute('name')] = field.value; 
			});
			return {map: map};
		}
	};
	
} ();