$(document).ready(function () {
    showDate();

    $("#select-date-exchange").on("change", function () {
        showDate();
    });

    $('#btn-check-path').click(function () {
        $('.path-can-use').show();
    });

    function readURL(input, destination) {

        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                $(`${destination}`).attr('src', e.target.result);
                $(`${destination}`).attr('height', 100);
                $(`${destination}`).attr('width', 150);
            }

            reader.readAsDataURL(input.files[0]);
        }
    }

    $("#fileLogoFilePath").change(function() {
        readURL(this, "#imageLogo");
    });

    $("#fileBannerFilePath").change(function() {
        readURL(this, "#imageTeam");
    });

    $(".btn-search-team").on("click", function () {
        var keyword = $("#inputSearch").val();
        var newAdditionalURL = "";
        var tempArray = document.location.href.split("?");
        var baseURL = tempArray[0];
        var additionalURL = tempArray[1];
        var temp = "";
        if (additionalURL) {
            tempArray = additionalURL.split("&");
            for (var i=0; i<tempArray.length; i++){
                if(tempArray[i].split('=')[0] != 's'){
                    newAdditionalURL += temp + tempArray[i];
                    temp = "&";
                }
            }
        }

        var search = temp + "s=" + keyword;
        window.location.href = baseURL + "?" + newAdditionalURL + search;
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
    var newParam = !willCheck ? "" : (existed ? "" : ( additionalURL? ("&" + param + "=" + value) : (param + "=" + value)));
    var queries = "?" + newAdditionalURL + newParam;
    window.location.href = baseURL + ((queries.length > 1) ? queries : "");
}

function showDate() {
    var weekdays = ["Chủ nhật", "Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7"];
    var date = new Date($('#select-date-exchange').val());
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();
    var weekday = date.getDay();
    var dateShow = weekdays[weekday] + ", ngày " + day + " tháng " + month + " năm " + year;
    $("#date-show").html(dateShow);
}