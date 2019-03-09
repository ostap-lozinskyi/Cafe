$(document).ready(function () {
    $('.radio-inline').click(function () {
        var element = document.getElementsByClassName('radio-inline');
        var i;

        for (i = 0; i < element.length; i++) {
            if (element[i].classList.contains("checked")) {
                element[i].classList.remove("checked");
            }
        }

        $(this).addClass("checked");
    });
});