var Student = function () {
	
	function loadLessons () {
		// Marks the lessons as being loading...
		application.loading('#lessons-content', true);
	}
	
	function init () {
		loadLessons();
	}
	
	return {
		init: init
	};
	
} ();

$(document).ready(Student.init);
