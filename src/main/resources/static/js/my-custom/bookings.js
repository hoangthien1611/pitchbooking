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

    // $(".btn-close-form").on("click", function () {
    //     alert('close');
    //     $(this).parent('div.form-group').closest('form').hide();
    // });
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
    console.log('index:' + index + "-childPitchId:" + childPitchId);
    var formId = "form-" + index + "-" + childPitchId;
    var textId = "text-info-" + index + "-" + childPitchId;

    $(`#${formId}`).hide();
    $(`#${textId}`).show();
}
