var Accounts = function () {

    function displayActions (id) {
        $('#actions-' + id).toggle();
    }

    return {

        displayActions: displayActions

    };

} ();