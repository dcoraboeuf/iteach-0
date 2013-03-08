/**
 * TODO This class, in version 0.3, will host everything related to AJAX
 */

var AJAX = function () {

    /**
     * Performs a PUT request
     * @param config.*          See #call
     */
    function put (config) {
        call($.extend(config, { method: 'PUT' }));
    }

    /**
     * Performs an AJAX call
     * @param config.loading        Loading indicator configiration (see #showLoading)
     * @param config.method         HTTP method (default: 'POST')
     * @param config.url            URL to call (required)
     * @param config.responseType   Type of the response (default: 'json')
     * @param config.successFn      (data -> void) function that is called with the data returned by the AJAX call (required)
     * @param config.errorFn        (jqXHR, textStatus, errorThrown -> void) function that allows for the treatment of the error (default: #defaultAjaxErrorFn)
     */
    function call (config) {
        // Default settings
        var c = $.extend({
            method: 'POST',
            responseType: 'json',
            errorFn: defaultAjaxErrorFn
        }, config);
        // Starting to load
        showLoading(c.loading, true);
        // Performing the call
		$.ajax({
			type: c.method,
			url: c.url,
			dataType: c.responseType,
			success: function (data) {
			    // Loading ending
                showLoading(c.loading, false);
                // Success :)
		  		c.successFn(data);
			},
			error: function (jqXHR, textStatus, errorThrown) {
			    // Loading ending
                showLoading(c.loading, false);
                // Error :(
                c.errorFn(jqXHR, textStatus, errorThrown);
			}
		});
    }

    /**
     * Shows a loading indicator on a element.
     * @param config.el jQuery object or selector for the object which will contain the loading indicator
     * @param (optional, defaults to 'false') show Boolean that indicates if the the loading must be hidden or shown
     */
    function showLoading (config, hide) {

    }

    /**
     * Default error function for an AJAX error handling
     */
    function defaultAjaxErrorFn (jqXHR, textStatus, errorThrown) {
        // TODO 0.3 -> use Application instead of application
        application.displayError(getAjaxError(jqXHR, textStatus, errorThrown));
    }

    /**
     * Gets the AJAX error message
     */
	function getAjaxError (jqXHR, textStatus, errorThrown) {
		return '[{0}] {1}'.format(jqXHR.status, jqXHR.statusText);
	}

    return {
        // AJAX calls
        put: put,
        call: call,
        // Loading
        showLoading: showLoading,
        // Error management
        defaultAjaxErrorFn: defaultAjaxErrorFn,
        getAjaxError: getAjaxError
    };

} ();