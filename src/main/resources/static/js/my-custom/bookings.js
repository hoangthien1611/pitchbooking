$(document).ready(function() {

    showDate();

    $("#select-date-booking").on("change", function () {
        showDate();
    });

    $(".btn-show-date").on("click", function () {
        var url = document.location.href.split('?')[0];
        var date = $('#select-date-booking').val();
        window.location.href = url + "?date=" + date;
    });

});

function showDate() {
    var weekdays = ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"];
    var date = new Date($('#select-date-booking').val());
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    var weekday = date.getDay();
    var dateShow = weekdays[weekday] + ", ngày " + day + " tháng " + month + " năm " + year;
    $("#date-show").html(dateShow);
}

function openForm(index, childPitchId) {
    var formId = "form-" + index + "-" + childPitchId;
    var textId = "text-info-" + index + "-" + childPitchId;
    $(`#${formId}`).show();
    $(`#${textId}`).hide();
}

function closeForm(index, childPitchId) {
    var formId = "form-" + index + "-" + childPitchId;
    var textId = "text-info-" + index + "-" + childPitchId;

    $(`#${formId}`).hide();
    $(`#${textId}`).show();
}

function submitForm(index, childPitchId) {
    var formId = "form-" + index + "-" + childPitchId;
    var textId = "text-info-" + index + "-" + childPitchId;
    var btnCloseForm = "btn-close-form-" + index + "-" + childPitchId;
    var id = $(`#id-${index}-${childPitchId}`).val();
    var dateBookingString = $(`#dateBooking-${index}-${childPitchId}`).val();
    var fromTime = $(`#fromTime-${index}-${childPitchId}`).val();
    var toTime = $(`#toTime-${index}-${childPitchId}`).val();
    var orderName = $(`#orderName-${index}-${childPitchId}`).val();
    var orderPhone = $(`#orderPhone-${index}-${childPitchId}`).val();
    var content = $(`#content-${index}-${childPitchId}`).val();
    var cost = $(`#cost-${index}-${childPitchId}`).val();

    if (!orderName || !orderPhone || !content) {
        showAlertMessage('warning', 'Vui lòng nhập đầy đủ thông tin!', true, 10000);
    } else {
        var data = {
            id,
            dateBookingString,
            fromTime,
            toTime,
            orderName,
            orderPhone,
            content,
            childPitchId,
            cost
        }

        $.ajax({
            type: 'post',
            url: '/booking',
            data: data,
            success: function (data) {
                if (data) {
                    showAlertMessage('success', 'Lưu thành công', false, 1500);
                    if (id == 0) {
                        var btnDel = "<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"deleteBooking(" + index + "," + childPitchId + "," + data.id + ")\">"
                            + "Xóa"
                            + "</button>";
                        $(btnDel).insertBefore(`#${btnCloseForm}`);
                        $(`#id-${index}-${childPitchId}`).val(data.id);
                    }

                    var textInfo = "<div style=\"color: red; font-weight: bold\">Đã có người đặt</div>";

                    $(`#${textId}`).html(textInfo);
                    $(`#${formId}`).hide();
                    $(`#${textId}`).show();
                } else {
                    showAlertMessageAndReload('error', 'Lưu thất bại! Sân đã được đặt hoặc thời gian đặt không hợp lệ!', 10000);
                }
            },
            error: function () {
                showAlertMessage('error', 'Lỗi! Không thể lưu được!', true, 10000);
            }
        });
    }
}

function deleteBooking(index, childPitchId, bookingId) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa!'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: 'delete',
                url: '/booking/' + bookingId,
                success: function (data) {
                    if (data) {
                        showAlertMessage('success', 'Xóa thành công', false, 1500);
                        $(`#id-${index}-${childPitchId}`).val('0');
                        $(`#orderName-${index}-${childPitchId}`).val('');
                        $(`#orderPhone-${index}-${childPitchId}`).val('');
                        $(`#content-${index}-${childPitchId}`).val('');
                        $(this).remove();

                        var textInfo = "<div>Sân đang trống</div>";
                        var textId = "text-info-" + index + "-" + childPitchId;
                        $(`#${textId}`).html(textInfo);

                        var btnCloseForm = "btn-close-form-" + index + "-" + childPitchId;
                        $(`#${btnCloseForm}`).prev().remove();
                    } else {
                        showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không thể xóa được!', true, 10000);
                }
            });
        }
    })
}

function delBooking(bookingId) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa!'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: 'delete',
                url: '/booking/' + bookingId,
                success: function (data) {
                    if (data) {
                        showAlertMessage('success', 'Xóa thành công', false, 1500);
                        removeRow(bookingId);
                    } else {
                        showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không thể xóa được!', true, 10000);
                }
            });
        }
    })
}

function removeBooking(bookingId) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa!'
    }).then((result) => {
        if (result.value) {
            $.ajax({
                type: 'delete',
                url: '/booking/' + bookingId,
                success: function (data) {
                    if (data) {
                        showAlertMessage('success', 'Xóa thành công', false, 1500);
                        $(`#booking-card-${bookingId}`).remove();
                    } else {
                        showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không thể xóa được!', true, 10000);
                }
            });
        }
    })
}

function acceptBooking(bookingId) {
    $.ajax({
        type: 'patch',
        url: '/booking/' + bookingId,
        success: function (data) {
            if (data) {
                showAlertMessage('success', 'Chấp nhận thành công', false, 1500);
                removeRow(bookingId);
            } else {
                showAlertMessage('error', 'Chấp nhận thất bại!', true, 10000);
            }
        },
        error: function () {
            showAlertMessage('error', 'Lỗi! Không thể phê duyệt được!', true, 10000);
        }
    });
}

function removeRow(bookingId) {
    var total = parseInt($(".show-total-requests").text());
    total -= 1;
    $(`#request-${bookingId}`).remove();
    $(".show-total-requests").text(total);
    if (total == 0) {
        var tr = "<tr><td colspan=\"6\">Không có yêu cầu đặt sân nào</td>";
        $("#tbody-requests").append(tr);
    }
}
