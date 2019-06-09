$(document).ready(function () {

    showDate('.select-date-booking', '.show-date');

    $(".clear-check").on("click", function () {
        window.location.href = document.location.href.split('?')[0];
    });

    $(".btn-keyword").on("click", function () {
        var keyword = $("#keyword").val();
        var newAdditionalURL = "";
        var tempArray = document.location.href.split("?");
        var baseURL = tempArray[0];
        var additionalURL = tempArray[1];
        var temp = "";
        if (additionalURL) {
            tempArray = additionalURL.split("&");
            for (var i = 0; i < tempArray.length; i++) {
                if (tempArray[i].split('=')[0] != 's') {
                    newAdditionalURL += temp + tempArray[i];
                    temp = "&";
                }
            }
        }

        var search = temp + "s=" + keyword;
        window.location.href = baseURL + "?" + newAdditionalURL + search;
    });

    $(".btn-close-booking-area").on("click", function () {
        $(".stadium-calendar").hide();
    });

    $(".btn-submit-booking").on("click", function () {
        var timeFrame = $("#timeBooking").val();
        var date = $("#dateBooking").val();
        var pitchesCostId = $("#pitchesCostIdBooking").val();
        var dates = date.split("/").reverse();
        date = dates.join("-");
        var orderName = $("#orderName").val();
        var orderPhone = $("#orderPhone").val();
        var content = $("#content").val();

        var data = {
            dateBookingString: date,
            fromTime: timeFrame.split("-")[0],
            toTime: timeFrame.split("-")[1],
            pitchesCostId,
            orderName,
            orderPhone,
            content
        }

        if (!orderName || !orderPhone) {
            showAlertMessage('warning', 'Vui lòng nhập đầy đủ thông tin!', true, 10000);
        } else {
            $.ajax({
                type: 'post',
                url: '/booking/for-user',
                data: data,
                success: function (data) {
                    if (data) {
                        if (data == "SUCCESS") {
                            showAlertMessage('success', 'Đặt sân thành công', false, 1500);
                        } else {
                            showAlertMessageAndReload('error', data, 10000);
                        }
                    } else {
                        showAlertMessage('error', 'Đã có lỗi xảy ra!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không thể đặt được sân!', true, 10000);
                }
            });
        }
    });

});

function goToUrl(inputId, param, value) {
    var willCheck = $(`#${inputId}`).prop("checked");

    var newAdditionalURL = "";
    var tempArray = document.location.href.split("?");
    var baseURL = tempArray[0];
    var additionalURL = tempArray[1];
    var temp = "";
    var existed = false;
    if (additionalURL) {
        tempArray = additionalURL.split("&");
        for (var i = 0; i < tempArray.length; i++) {
            if (tempArray[i].split('=')[0] != param) {
                newAdditionalURL += temp + tempArray[i];
                temp = "&";
            } else {
                existed = true;
                // add more value for param
                if (willCheck) {
                    newAdditionalURL += temp + tempArray[i] + "," + value;
                    temp = "&";
                } else {
                    //remove param
                    // multiple values for a single param
                    if (tempArray[i].split('=')[1].indexOf(',') > 0) {
                        var searchValue = tempArray[i].indexOf(',' + value) > 0 ? ("," + value) : (value + ",");
                        tempArray[i] = tempArray[i].replace(searchValue, "");
                        newAdditionalURL += temp + tempArray[i];
                        temp = "&";
                    }
                }
            }

        }
    }
    var newParam = !willCheck ? "" : (existed ? "" : (additionalURL ? ("&" + param + "=" + value) : (param + "=" + value)));
    var queries = "?" + newAdditionalURL + newParam;
    window.location.href = baseURL + ((queries.length > 1) ? queries : "");
}

function openBookingArea(pitchesCostId) {
    $(".stadium-calendar").hide();
    var bookingAreaId = "booking-area-" + pitchesCostId;
    $(`#${bookingAreaId}`).show();
}

function showDate(input, destination) {
    var weekdays = ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"];
    var date = new Date($(`${input}`).val());
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    var weekday = date.getDay();
    var dateShow = weekdays[weekday] + ", ngày " + day + " tháng " + month + " năm " + year;
    $(`${destination}`).html(dateShow);
}

function changeDate(pitchesCostId) {
    var input = "#select-date-booking-" + pitchesCostId;
    var destination = "#show-date-" + pitchesCostId;
    showDate(input, destination);
}

function checkPitches(pitchesCostId) {
    var inputDate = $(`#select-date-booking-${pitchesCostId}`).val();

    $.ajax({
        type: 'get',
        url: '/booking/get-booking-check-list',
        data: {
            pitchesCostId,
            date: inputDate
        },
        success: function (data) {
            if (data != null) {
                var html = "";
                if (data.length > 0) {
                    data.forEach(function (item, index) {
                        html += "<button type=\"button\" class=\"btn btn-sm btn-time-available-true" + (item.available ? " btn-success" : " btn-default disabled") + "\" style=\"margin-top:3px\""
                            + " onclick=\"showBookingModal(" + index + "," + pitchesCostId + ")\" "
                            + " id=\"btn-timeFrame-" + index + "-" + pitchesCostId + "\" "
                            + (item.available ? " data-toggle=\"modal\" data-target=\"#pitch-booking\">" : " >")
                            + "<i class=\"fa fa-clock-o\" aria-hidden=\"true\"></i>"
                            + "<span>" + item.timeFrame.fromTime + "-" + item.timeFrame.toTime + "</span>"
                            + "</button> &nbsp;"
                    });

                    $(`#btn-area-${pitchesCostId}`).html(html);
                    $(`#input-date-booking-${pitchesCostId}`).val(inputDate);
                } else {
                    html = "<h4 style='color: red;'>Không tìm thấy khung giờ để chọn!</h4>"
                    $(`#btn-area-${pitchesCostId}`).html(html);
                }

            } else {
                showAlertMessage('error', 'Đã có lỗi xảy ra!', true, 10000);
            }
        },
        error: function () {
            showAlertMessage('error', 'Lỗi! Không thể tìm được!', true, 10000);
        }
    });
}

function showBookingModal(index, pitchesCostId) {
    var date = new Date($(`#input-date-booking-${pitchesCostId}`).val());
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    var cost = $(`#input-cost-booking-${pitchesCostId}`).val();
    var pitchType = $(`#input-pitch-type-${pitchesCostId}`).val();
    var timeFrame = $(`#btn-timeFrame-${index}-${pitchesCostId}`).children("span").text();

    $("#timeBooking").val(timeFrame);
    $("#dateBooking").val(day + "/" + month + "/" + year);
    $("#pitchType").val(pitchType);
    $("#cost").val(cost);
    $("#pitchesCostIdBooking").val(pitchesCostId);
}
