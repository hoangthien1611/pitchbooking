$(document).ready(function () {
    $("#btnEditProfile").on("click", function () {
       $(".profile-edit").show();
       $(".profile-view").hide();
    });

    $("#btnProfileCancel").on("click", function () {
        $(".profile-edit").hide();
        $(".profile-view").show();
    });
});