$(document).ready(function () {
    showDate();

    $("#select-date-exchange").on("change", function () {
        showDate();
    });

    $('#btn-check-path').click(function () {
        var path = $("#input-path").val();
        if (!path) {
            $('.path-can-use').hide();
            $('.path-cannot-use').show();
            return;
        }
        $.ajax({
            type: 'get',
            url: '/team/check-path',
            data: {
                path
            },
            success: function (data) {
                if (!data) {
                    $('.path-can-use').show();
                    $('.path-cannot-use').hide();
                } else {
                    $('.path-can-use').hide();
                    $('.path-cannot-use').show();
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    });

    function readURL(input, destination) {

        if (input.files && input.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                $(`${destination}`).attr('src', e.target.result);
                $(`${destination}`).css('max-width', 300);
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

    $(".btn-search-exchange").on("click", function () {
        var keyword = $("#searchExchange").val();
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

    $(".btn-show-pitches").on("click", function () {
        var districtId = $("#select-district-exchange").val();
        var districtName = $("#select-district-exchange option:selected").text();
        $("#title-choose-pitch").text("Chọn sân bóng tại " + districtName);

        $.ajax({
            type: 'get',
            url: '/pitch/get-all/' + districtId,
            success: function (data) {
                var html = "";

                if (data && data.length) {
                    // html += "<div class=\"form-group\">"
                    //     + "<input type=\"text\" class=\"form-control\" id=\"input-search-pitch\" placeholder=\"Nhập tên sân bóng hoặc khu vực...\">"
                    //     + "</div>"
                     html += "<ul class=\"form-group stadium-list\">";

                    data.forEach(function (item, index) {
                        html += "<li class=\"stadium-entry-select ng-scope\" data-dismiss=\"modal\""
                            + "id=\"pitchItem-" +item.id  + "\" onclick=\"choosePitch(" + item.id + ")\">"
                            + "<a class=\"btn btn-default width-100p\">"
                            + "<b class=\"pitch-name-modal\" id=\"pitchNameSelected-" + item.id + "\">" + item.name + "</b>"
                            + "<br>"
                            + "<small>" + item.address + "</small>"
                            + "</a></li>";
                        });
                    html + "</ul>";
                } else {
                    html += "<ul class=\"form-group stadium-list\">"
                        + "<li class=\"stadium-entry-select ng-scope\">"
                        + "<a class=\"btn btn-default width-100p\">"
                        + "<b style=\"color: red\">Không tìm thấy sân nào ở quận / huyện này!</b>"
                        + "</a></li></ul>";
                }

                $("#modal-choose-pitch").html(html);
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    });

    $("#select-district-exchange").on("change", function () {
        $("#pitchIdInput").val("0");
        $("#showPitchName").text("Vui lòng chọn sân!");
    });

    $(document).on("keyup", "#input-search-pitch", function () {
        var nameArr = $(".pitch-name-modal").map(function() {
            return $(this).text();
        }).get();
        var keyWord = $("#input-search-pitch").val();
        var mappedNameArr = nameArr.filter(name => name.toLowerCase().indexOf(keyWord.toLowerCase()) > -1);

        console.log(nameArr);
        console.log(mappedNameArr);
    });

    $('input[name=hasPitch]').change(function(){
        var value = $( 'input[name=hasPitch]:checked' ).val();
        if (value == 0) {
            $("#pitchIdInput").val("");
            $("#showPitchName").text("Vui lòng chọn sân!");
            $(".show-choose-pitch").hide();
            $(".specific-area").show();
        } else {
            $("#areaInput").val("");
            $(".show-choose-pitch").show();
            $(".specific-area").hide();
        }
    });

    $(".btn-submit-invite").on("click", function () {
        var exchangeId = $("#exchangeId").val();
        var teamId = $("#teamSenderId").val();
        var message = $("#messageModal").val();

        var data = {
            exchangeId,
            teamId,
            message,
            type: 1
        }

        $.ajax({
            type: 'post',
            url: '/invitation',
            data: data,
            success: function (data) {
                if (data) {
                    alert('Gửi lời mời thành công');
                } else {
                    alert("Thất bại! Trận đấu này đã tìm được được đối hoặc thông tin không đúng!");
                    location.reload(true);
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    });

    $(".btn-submit-invite2").on("click", function () {
        var userId = $("#userId2").val();
        var teamId = $("#teamId2").val();
        var message = $("#messageModal2").val();
        var exchangeId = $("input[name='exchange2']:checked").val();

        var data = {
            exchangeId,
            userId,
            teamId,
            message,
            type: 2
        }

        console.log(data);

        $.ajax({
            type: 'post',
            url: '/invitation',
            data: data,
            success: function (data) {
                if (data) {
                    alert('Gửi lời mời thành công');
                } else {
                    alert("Thất bại! Trận đấu này đã tìm được được đối hoặc thông tin không đúng!");
                    location.reload(true);
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    })
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

function choosePitch(pitchId) {
    var pitchNamne = $(`#pitchNameSelected-${pitchId}`).text();

    $("#pitchIdInput").val(pitchId);
    $("#showPitchName").text(pitchNamne);
}

function deleteTeam(teamId) {
    var result = confirm('Bạn có chắc chắn muốn xóa?');
    if (result) {
        $.ajax({
            type: 'delete',
            url: '/team/' + teamId,
            success: function (data) {
                if (data) {
                    alert('Xóa thành công');
                    $(`#tr-team-${teamId}`).remove();
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

function openInvitation(exchangeId) {
    $("#exchangeId").val(exchangeId);
}
function openInvitation2(userId, teamId) {
    $("#userId2").val(userId);
    $("#teamId2").val(teamId);
}

function acceptInvitation(invitationId) {
    changeInvitaionStatus(invitationId, 1);
}

function refuseInvitation(invitationId) {
    changeInvitaionStatus(invitationId, 2);
}

function changeInvitaionStatus(id, status) {
    $.ajax({
        type: 'patch',
        url: '/invitation/' + id,
        data: {
            status
        },
        success: function (data) {
            if (data) {
                var txt = status == 1? 'Bạn đã chấp nhận' : 'Bạn đã từ chối';
                var color = status == 1? 'green' : 'red';

                $(`#dropdown-right-${id}`).remove();
                $(`.stt-text-${id}`).text(txt);
                $(`.stt-text-${id}`).css("color", color);
            } else {
                alert("Thay đổi trạng thái thất bại!");
            }
        },
        error: function () {
            alert('Error! Có lỗi xảy ra!');
        }
    });
}