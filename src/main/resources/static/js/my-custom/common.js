$(document).ready(function () {
    $('.msg-noti').fadeOut(7000);

    $(".navbar-notification").hover(resetNew);
});

function resetNew() {
    if ($(".badge-total-new").text()) {
        $.ajax({
            type: 'put',
            url: '/notification/resetNew',
            success: function (data) {
                if (data) {
                    $(".badge-total-new").remove();
                }
            }
        });
    }
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

function showAlertMessage(type, title, showConfirmButton, timer) {
    Swal.fire({
        type,
        title,
        showConfirmButton,
        timer
    })
}

function showAlertMessageAndReload(type, title, timer) {
    Swal.fire({
        type,
        title,
        showConfirmButton: true,
        timer
    }).then(() => {
        location.reload(true);
    })
}

function showConfirmDelete(callBackFn) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa!'
    }).then((result) => {
        if (result.value) {
            callBackFn
        }
    })
}