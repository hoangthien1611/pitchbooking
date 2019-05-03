$(document).ready(function () {

    $(".clear-check").on("click", function () {
        window.location.href = document.location.href.split('?')[0];
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
                if (willCheck) {
                    newAdditionalURL += tempArray[i] + "," + value;
                } else {
                    if (tempArray[i].split('=')[1].indexOf(',') > 0) {
                        var searchValue = tempArray[i].indexOf(',' + value) > 0 ? ("," + value) : (value + ",");
                        tempArray[i] = tempArray[i].replace(searchValue, "");
                    } else {
                        tempArray[i] = "";
                    }
                    newAdditionalURL += tempArray[i];
                }
            }
        }
    }
    var newParam = !willCheck ? "" : (existed ? "" : (temp + "" + param + "=" + value));
    var params = "?" + newAdditionalURL + newParam;
    window.location.href = baseURL + ((params.length > 1) ? params : "");
}