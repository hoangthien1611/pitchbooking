$(document).ready(function () {
    $('.msg-noti').fadeOut(7000);

    $(".navbar-notification").hover(resetNew);
});

function resetNew() {
    $.ajax({
        type: 'put',
        url: '/notification/resetNew',
        success: function (data) {
            if (data) {
                $(".badge-total-new").hide();
            }
        }
    });
}

function seeNotification(id) {
    $.ajax({
        type: 'put',
        url: '/notification/see/' + id,
        success: function (data) {
            if (data) {
                $(`#noti-${id}`).removeClass("notification-not-seen");
            }
        }
    });
}