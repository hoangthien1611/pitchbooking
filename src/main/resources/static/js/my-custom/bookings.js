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
        alert("Vui lòng nhập đầy đủ thông tin phù hợp!");
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
                    alert('Lưu thành công');
                    if (id == 0) {
                        var btnDel = "<button type=\"button\" class=\"btn btn-danger btn-sm\" onclick=\"deleteBooking(" + index + "," + childPitchId + "," + data.id + ")\">"
                            + "Xóa"
                            + "</button>";
                        $(btnDel).insertBefore(`#${btnCloseForm}`);
                        $(`#id-${index}-${childPitchId}`).val(data.id);
                    }

                    var textInfo = "<div style=\"color: red\">Đã có người đặt, click để xem</div>";

                    $(`#${textId}`).html(textInfo);
                    $(`#${formId}`).hide();
                    $(`#${textId}`).show();
                } else {
                    alert("Lưu thất bại! Sân đã được đặt hoặc thời gian đặt không hợp lệ!");
                    location.reload(true);
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    }
}

function deleteBooking(index, childPitchId, bookingId) {
    var result = confirm('Bạn có chắc chắn muốn xóa?');
    if (result) {
        $.ajax({
            type: 'delete',
            url: '/booking/' + bookingId,
            success: function (data) {
                if (data) {
                    alert('Xóa thành công');
                    $(`#id-${index}-${childPitchId}`).val('0');
                    $(`#orderName-${index}-${childPitchId}`).val('');
                    $(`#orderPhone-${index}-${childPitchId}`).val('');
                    $(`#content-${index}-${childPitchId}`).val('');
                    $(this).remove();

                    var textInfo = "<div>Đang trống, click để cập nhật</div>";
                    var textId = "text-info-" + index + "-" + childPitchId;
                    $(`#${textId}`).html(textInfo);

                    var btnCloseForm = "btn-close-form-" + index + "-" + childPitchId;
                    $(`#${btnCloseForm}`).prev().remove();
                } else {
                    alert("Xóa thất bại!");
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    }
}

function delBooking(bookingId) {
    var result = confirm('Bạn có chắc chắn muốn xóa?');
    if (result) {
        $.ajax({
            type: 'delete',
            url: '/booking/' + bookingId,
            success: function (data) {
                if (data) {
                    alert('Xóa thành công');
                    removeRow(bookingId);
                } else {
                    alert("Xóa thất bại!");
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    }
}

function acceptBooking(bookingId) {
    $.ajax({
        type: 'patch',
        url: '/booking/' + bookingId,
        success: function (data) {
            if (data) {
                alert('Chấp nhận thành công');
                removeRow(bookingId);
            } else {
                alert("Chấp nhận thất bại!");
            }
        },
        error: function () {
            alert('Error! Có lỗi xảy ra!');
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
