var Schools = function () {
	
	// Private members
	
	function create () {
		var html = application.getDialog('school');
		// $(html).dialog({
		// 	modal: true,
		//	title: loc('school.new')
		// });
	}
	
	// Public members
	return {
		create: create
	};
	
} ();