var Schools = function () {
	
	// Private members
	
	function create () {
		application.dialog('school', {
			title: loc('school.new'),
			width: 500
		});
	}
	
	// Public members
	return {
		create: create
	};
	
} ();