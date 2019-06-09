$(document).ready(function() {
    $(document).on("click", ".btn-choose-map", function () {
        var long = $(".long").text();
        var lat = $(".lat").text();

        $("#longitude").val(long);
        $("#latitude").val(lat);
    });

    $("#fileAvatar").change(function() {
        if (this.files && this.files[0]) {
            var reader = new FileReader();

            reader.onload = function(e) {
                $("#avatarImg").attr('src', e.target.result);
                $("#avatarImg").css('max-width', 300);
            }

            reader.readAsDataURL(this.files[0]);
        }
    });

    $(document).on("click", "#a-add-pitches", function () {
       $("#form-add-pitches").show();
    });

    $(document).on("click", ".btn-add-pitches", function () {
        var pitchId = $(".pitch-id").text();
        var pitchTypeId = $('#select-pitch-type').val();
        var number = $("#number").val();
        $("#form-add-pitches").hide();

        $.ajax({
            type: 'post',
            url: '/group-specific-pitches',
            data: {
                pitchId: pitchId,
                pitchTypeId: pitchTypeId,
                number: number
            },
            success: function (data) {
                if (data) {
                    $(`#select-pitch-type option[value='${pitchTypeId}']`).remove();
                    $("#number").val("");
                    if( $('#select-pitch-type').has('option').length == 0 ) {
                        $("#add-group-pitches-area").hide();
                    }
                    var markup = "<div class=\"snp-settings\" id='area-table-" + data.id + "'>"
                        + "<div class=\"sn-settings-top row\">"
                        + "<div class=\"col-md-12 ng-scope\">"
                        + "<div class=\"sn-settings-bar\">"
                        + "<div class=\"pull-left sn-settings-name\">"
                        + "<span style=\"float: left;margin-top: 3px; margin-right:30px;\" id=\"pitches-span-" + data.id + "\">"
                        + "<i class=\"fa fa-check-square\"></i>"
                        + "&nbsp;Loại sân: " + data.pitchTypeName + " (" + data.number + " sân)</span>"
                        + "<div class=\"btn-group pull-right mobile-float-none\" style=\"margin-top:3px\">"
                        + "<a class=\"pointer add-price\" data-toggle=\"modal\" data-target=\"#addOrEditPrice\" onclick=\"addPrice(" + data.id +")\">"
                        + "<i class=\"fa fa-database\"></i> &nbsp;Thêm giá&nbsp;</a>&nbsp;"
                        + "<a class=\"pointer\" data-toggle=\"modal\" data-target=\"#changeNumberModal\" onclick=\"passPitchesIdToModal(" + data.id + "," + data.number+")\">"
                        + "&nbsp;<i class=\"fa fa-pencil\"></i> &nbsp;Thay đổi số lượng&nbsp;</a>&nbsp;"
                        + "<a class=\"pointer\" onclick=\"deletePitches(" + data.id +")\">"
                        + "&nbsp;<i class=\"fa fa-remove\"></i> &nbsp;Xóa</a>"
                        + "</div></div></div></div></div>"
                        + "<div class=\"stadium-number-prices\">"
                        + "<table class=\"table table-bordered text-left table-responsive stadium-number-price-table\" id=\"table-" +data.id +"\">"
                        + "<thead><tr>"
                        + "<th style=\"min-width:222px;\">Khung giờ</th>"
                        + "<th>Ngày trong tuần</th>"
                        + "<th>Giá (VND)</th>"
                        + "<th>Hành động</th>"
                        + "</tr></thead>"
                        + "<tbody></tbody></table>"
                        + "</div></div>"
                    $(markup).insertBefore(".bottom-pitches-right");
                } else {
                    showAlertMessage('error', 'Thêm thất bại!', true, 10000);
                }
            },
            error: function () {
                showAlertMessage('error', 'Lỗi! Không thêm được!', true, 10000);
            }
        });
    });

    $(document).on("click", ".del-price", function () {
        Swal.fire({
            title: 'Bạn có chắc chắn muốn xóa?',
            type: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Xóa!'
        }).then((result) => {
            if (result.value) {
                var costId = $(this).closest('tr').children('td.costId').text();
                var pitchesId = $(this).closest('tr').children('td.pitchesId').text();
                url = /specific-pitches-cost/ + costId;

                $.ajax({
                    type: 'delete',
                    url,
                    success: function (data) {
                        if (data) {
                            $(`#tr-${costId}`).remove();
                        } else {
                            showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                        }
                    },
                    error: function () {
                        showAlertMessage('error', 'Lỗi! Không xóa được!', true, 10000);
                    }
                });
            }
        })
    });

    $(document).on("click", ".btn-change-number", function () {
        var pitchesId = $("#pitchesIdChange").val();
        var numberChange = parseInt($("#numberChange").val());

        if (!numberChange && numberChange < 1) {
            showAlertMessage('warning', 'Vui lòng nhập vào số lượng hợp lệ!', true, 10000);
        } else {
            $.ajax({
                type: 'put',
                url: '/group-specific-pitches/change-number/' + pitchesId,
                data: {
                    number: numberChange
                },
                success: function (data) {
                    if (data) {
                        console.log(data);
                        var span = "<i class=\"fa fa-check-square\"></i>"
                            + "&nbsp;Loại sân: " + data.pitchTypeName + " (" + data.number + " sân)";
                        $(`#pitches-span-${data.id}`).html(span);
                    } else {
                        showAlertMessage('error', 'Thay đổi thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không thể thay đổi được!', true, 10000);
                }
            });
        }
    });

    $(document).on("click", ".btn-add-edit-cost", function () {
        var pitchesId = $("#pitchesIdForm").val();
        var costId = $("#costIdForm").val();
        var fromTime = $("#select-from-time").val();
        var toTime = $("#select-to-time").val();
        var price = $("#priceForm").val();
        var groupDaysId = $("input[name='groupDaysIdForm']:checked").val();
        var fromTimeInt = parseInt(fromTime.split(":")[0]);
        var toTimeInt = parseInt(toTime.split(":")[0]);

        var data = {
            groupSpecificPitchesId: pitchesId,
            fromTime: fromTime,
            toTime: toTime,
            cost: price,
            groupDaysId: groupDaysId
        };

        if (toTimeInt <= fromTimeInt) {
            showAlertMessage('warning', 'Vui lòng chọn khung giờ hợp lệ!', true, 10000);
        } else if (!groupDaysId) {
            showAlertMessage('warning', 'Vui lòng chọn ngày trong tuần!', true, 10000);
        } else if (costId == 0) {
            $.ajax({
                type: 'post',
                url: '/specific-pitches-cost',
                data: data,
                success: function (data) {
                    if (data) {
                        tableId = 'table-' + pitchesId;
                        var markup = generateTableRow(data);
                        $(`table#${tableId} tbody`).append(markup);
                    } else {
                        showAlertMessage('warning', 'Thêm giá thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('warning', 'Lỗi! Không thêm giá được!', true, 10000);
                }
            });
        } else {
            $.ajax({
                type: 'put',
                url: '/specific-pitches-cost/' + costId,
                data: data,
                success: function (data) {
                    if (data) {
                        var markup = generateTableRow(data);
                        $(`#tr-${costId}`).replaceWith(markup);
                    } else {
                        showAlertMessage('warning', 'Chỉnh sửa giá thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('warning', 'Lỗi! Không chỉnh giá được!', true, 10000);
                }
            });
        }
    });

    $(document).on("click", ".edit-price", function () {
        var pitchesId = $(this).closest('tr').children('td.pitchesId').text();
        var costId = $(this).closest('tr').children('td.costId').text();
        var fromTime = $(this).closest('tr').children('td.sn-price-time-td').find("input[name='fromTimeView']").val();
        var toTime = $(this).closest('tr').children('td.sn-price-time-td').find("input[name='toTimeView']").val();
        var daysId = $(this).closest('tr').children('td.daysId').text();
        var cost = $(this).closest('tr').children('td.sn-price-price-td').find("input[name='costView']").val();

        $("#pitchesIdForm").val(pitchesId);
        $("#costIdForm").val(costId);
        $("#select-from-time").val(fromTime);
        $("#select-to-time").val(toTime);
        $("#priceForm").val(cost);
        $(`input:radio[name=groupDaysIdForm][value=${daysId}]`).prop('checked', true);
        $(".title-price-form").html('Chỉnh sửa giá');
    });

    function generateTableRow(data) {
        return "<tr class='"+ data.id +"' id='tr-" + data.id + "'>"
            + "<td class=\"sn-price-time-td\">"
            + "<div class=\"form-inline md-table\"><div class=\"input-group input-time-sm\">"
            + "<span class=\"input-group-addon\"><i class=\"fa fa-clock-o\"></i></span>"
            + "<input class=\"form-control input-sm\" name=\"fromTimeView\" value='" + data.fromTime +"' readonly></div>"
            + "<span>&nbsp;&nbsp; - &nbsp;&nbsp;</span>"
            + "<div class=\"input-group input-time-sm\">"
            + "<span class=\"input-group-addon\"><i class=\"fa fa-clock-o\"></i></span>"
            + "<input class=\"form-control input-sm\" name=\"toTimeView\" value='" + data.toTime +"' readonly></div></div></td>"
            + "<td class=\"sn-price-day-picker\">" + data.groupDaysName + "</td>"
            + "<td class=\"sn-price-price-td\">"
            + "<input min=\"0\" type=\"number\" class=\"form-control input-sm\" name=\"costView\" value='"+data.cost+"' readonly></td>"
            + "<td class=\"sn-price-delete-td\">"
            + "<a class=\"btn btn-default btn-sm edit-price\" title=\"Chỉnh sửa giá\" data-toggle=\"modal\" data-target=\"#addOrEditPrice\">"
            + "<i class=\"fa fa-pencil\"></i></a>"
            + "&nbsp;<a class=\"btn btn-default btn-sm del-price\" title=\"Xóa giá\"><i class=\"fa fa-remove\"></i></a>"
            + "</td>"
            + "<td class=\"costId hide\">"+ data.id +"</td>"
            + "<td class=\"daysId hide\">"+ data.groupDaysId +"</td>"
            + "<td class=\"pitchesId hide\">" + data.groupSpecificPitchesId + "</td></tr>";
    }
});

function addPrice(pitchesId) {
    $("#pitchesIdForm").val(pitchesId);
    $("#costIdForm").val(0);
    $("#priceForm").val(0);
    $(".title-price-form").html('Thêm giá');
}

function deletePitches(pitchesId) {
    Swal.fire({
        title: 'Bạn có chắc chắn muốn xóa?',
        type: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: 'Xóa!'
    }).then((result) => {
        if (result.value) {
            url = /group-specific-pitches/ + pitchesId;

            $.ajax({
                type: 'delete',
                url,
                success: function (data) {
                    if (data) {
                        $("#add-group-pitches-area").show();
                        $("#area-table-" + pitchesId).remove();
                        if( $('#select-pitch-type').has('option').length == 0 ) {
                            $('#select-pitch-type').prepend($("<option></option>").val(data.pitchTypeId).text(data.pitchTypeName));
                        } else {
                            $("#select-pitch-type option").each(function () {
                                var value = $(this).val();
                                if (value > data.pitchTypeId) {
                                    $(this).before($("<option></option>").val(data.pitchTypeId).text(data.pitchTypeName));
                                    return false;
                                }
                            });
                        }

                    } else {
                        showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không xóa được!', true, 10000);
                }
            });
        }
    })
}

function passPitchesIdToModal(pitchesId, number) {
    $("#pitchesIdChange").val(pitchesId);
    $("#numberChange").val(number);
}

function deletePitch(pitchId) {
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
                url: '/pitch/management/' + pitchId,
                success: function (data) {
                    if (data) {
                        showAlertMessage('success', 'Xóa thành công', false, 1500);
                        $(`#tr-pitch-${pitchId}`).remove();
                    } else {
                        showAlertMessage('error', 'Xóa thất bại!', true, 10000);
                    }
                },
                error: function () {
                    showAlertMessage('error', 'Lỗi! Không xóa được!', true, 10000);
                }
            });
        }
    })
}