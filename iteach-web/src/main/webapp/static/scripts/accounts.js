var Accounts = function () {

    function displayActions (id) {
        $('.account-actions').hide();
        $('#actions-' + id).show();
    }

    function disableUser (button, id) {
        application.confirmAndCall(
            loc('account.disable.prompt'),
            function () {
                changeUserState(button, id, 'disable');
            }
        );
    }

    function enableUser(button, id) {
        changeUserState(button, id, 'enable');
    }

    function changeUserState (button, id, state) {
        AJAX.put({
            loading: {
                el: button
            },
            url: 'admin/account/{0}/{1}'.format(id, state),
            successFn: function (data) {
                location.reload();
            }
        });
    }

    return {

        displayActions: displayActions,
        disableUser: disableUser,
        enableUser: enableUser

    };

} ();