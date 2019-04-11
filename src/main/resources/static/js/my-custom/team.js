$(document).ready(function () {
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
});