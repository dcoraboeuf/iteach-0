var Coordinates = function () {
	
	return {
		clear: function () {
			$('input[id^="coord_"]').val('');
		},
		getValues: function () {
			var map = {};
			$('input[id^="coord_"]').each(function (index, field) {
				map[field.getAttribute('name')] = field.value; 
			});
			return {map: map};
		}
	};
	
} ();