$(document).ready(function () {
    var password = document.getElementById("password")
        , confirm_password = document.getElementById("rePassword");

    function validatePassword(){
        if(password.value != confirm_password.value) {
            confirm_password.setCustomValidity("Password không trùng!");
        } else {
            confirm_password.setCustomValidity('');
        }
    }

    password.onchange = validatePassword;
    confirm_password.onkeyup = validatePassword;

   $(".btn-check-userName").on("click", function () {
       var userName = $("#userName").val();

       $.ajax({
           type: 'post',
           url: '/user/check-userName',
           data: {
               userName
           },
           success: function (data) {
               if (data) {
                   alert('Username này đã tồn tại. Vui lòng nhập username khác!');
                   ("#userName").focus();
               } else {
                   alert('Có thể sử dụng username này!');
               }
           },
           error: function () {
               alert('Đã có lỗi xảy ra!');
           }
       });
   });
});