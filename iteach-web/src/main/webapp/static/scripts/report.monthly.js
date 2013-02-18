var Report = function () {

    function change () {
        var totalDisplayed = $('#displayTotal').is(':checked');
        if (totalDisplayed) {
            $('#report .total').show();
        } else {
            $('#report .total').hide();
        }
    }

    function onChange() {
        var totalDisplayed = $('#displayTotal').is(':checked');
        $.post(
            'ui/profile/preference/REPORT_TOTAL_DISPLAYED/{0}'.format(totalDisplayed ? "true" : "false"),
            '',
            function (data) {
                if (data.success) {
                    change();
                }
            },
            'json'
        );
    }

    return {
        init: function () {
            $('#displayTotal').attr(
                'checked',
                $('#REPORT_TOTAL_DISPLAYED').val() == 'true'
                );
            $('#displayTotal').click(onChange);
            change();
        }
    };

} ();

$(document).ready(Report.init);