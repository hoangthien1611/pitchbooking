$(document).ready(function () {
    var password = document.getElementById("newPassword")
        , confirm_password = document.getElementById("confirmNewPassword");

    function validatePassword(){
        if(password.value != confirm_password.value) {
            confirm_password.setCustomValidity("Password không trùng!");
        } else {
            confirm_password.setCustomValidity('');
        }
    }

    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;

    $("#btnEditProfile").on("click", function () {
       $(".profile-edit").show();
       $(".profile-view").hide();
    });

    $("#btnProfileCancel").on("click", function () {
        $(".profile-edit").hide();
        $(".profile-view").show();
    });

    $("#show-profile").on("click", function () {
        $("#profile").css({'display':'block'});
        $("#changepass").css({'display':'none'});
        $("#show-profile").addClass("active");
        $("#show-change-pass").removeClass("active");
    });

    $("#show-change-pass").on("click", function () {
        $("#profile").css({'display':'none'});
        $("#changepass").css({'display':'block'});
        $("#show-change-pass").addClass("active");
        $("#show-profile").removeClass("active");
    });

    $("#btnChangePass").on("click", function () {

    });

});