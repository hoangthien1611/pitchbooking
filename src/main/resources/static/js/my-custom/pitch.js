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
                $("#avatarImg").attr('height', 200);
                $("#avatarImg").attr('width', 250);
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
                if (data != null) {
                    $(`#select-pitch-type option[value='${pitchTypeId}']`).remove();
                    $("#number").val("");
                    if( $('#select-pitch-type').has('option').length == 0 ) {
                        $("#add-group-pitches-area").hide();
                    }
                    var markup = "<div class=\"snp-settings\" id='area-table-" + data.id + "'></div>"
                    console.log(data);
                    $(".pitches-content-page").append(markup);
                } else {
                    alert('Thêm thất bại!');
                }
            },
            error: function () {
                alert('Error! Không thêm được!');
            }
        });
    });

    $(document).on("click", ".del-price", function () {
        console.log('del row');
        $(this).parents("tr").remove();
    });
});

function addPrice(tableId) {
    tableId = 'table-' + tableId;
    var markup = "<tr><td class=\"sn-price-time-td\">"
        + "<div class=\"form-inline md-table\"><div class=\"input-group input-time-sm\">"
        + "<span class=\"input-group-addon\"><i class=\"fa fa-clock-o\"></i></span>"
        + "<input class=\"form-control input-sm\" placeholder=\"Bắt đầu\"></div>"
        + "<div class=\"input-group input-time-sm\">"
        + "<span class=\"input-group-addon\"><i class=\"fa fa-clock-o\"></i></span>"
        + "<input class=\"form-control input-sm\" placeholder=\"Kết thúc\"></div></div></td>"
        + "<td class=\"sn-price-day-picker\">"
        + "<label class=\"checkbox-inline\"><input type=\"radio\" name=\"groupDaysId\" class=\"margin-top-2\" value='1'> T2 - T6</label>"
        + "<label class=\"checkbox-inline\"><input type=\"radio\" name=\"groupDaysId\" class=\"margin-top-2\" value='2'> T7</label>"
        + "<label class=\"checkbox-inline\"><input type=\"radio\" name=\"groupDaysId\" class=\"margin-top-2\" value='3'> CN</label>"
        + "</td>"
        + "<td class=\"sn-price-price-td\">"
        + "<input min=\"0\" type=\"number\" class=\"form-control input-sm\"></td>"
        + "<td class=\"sn-price-delete-td\">"
        + "<a class=\"btn btn-default btn-sm del-price\" title=\"Xóa giá\"><i class=\"fa fa-remove\"></i></a>"
        + "</td></tr>";
    $(`table#${tableId} tbody`).append(markup);
}

function deletePitches(pitchesId) {
    var result = confirm('Bạn có chắc chắn muốn xóa?');
    if (result) {
        url = /group-specific-pitches/ + pitchesId;

        $.ajax({
            type: 'delete',
            url,
            success: function (data) {
                if (data == "SUCCESS") {
                    $("#area-table-" + pitchesId).remove();
                } else {
                    alert(data);
                }
            },
            error: function () {
                alert('Error! Có lỗi xảy ra!');
            }
        });
    }
}