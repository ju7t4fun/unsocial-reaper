var Script = function () {
    $().ready(function() {
        $("#register_form").validate({
            rules: {
                name: {
                    required: true,
                    minlength: 1,
                   // pattern: /^[a-zA-Z\u0400-\u04ff]+$/
                },
                surname: {
                    required: true,
                    minlength: 1
                },
                password: {
                    required: true,
                    minlength: 5
                },
                email: {
                    required: true,
                    email: true
                },
            },
            messages: {
                name: {
                    required: "Please enter your Name.",
                    minlength: "Your Name is too short.",
                    pattern: "Please enter correct Name."
                },
                address: {
                    required: "Please enter your Address.",
                    minlength: "Your Address must consist of at least 10 characters long."
                },
                surname: {
                    required: "Please enter your Surname.",
                    minlength: "Your surname is too short.",
                    pattern: "Please enter correct Surname."
                },
                password: {
                    required: "Please provide a password.",
                    minlength: "Your password must be at least 5 characters long."
                },
                email: "Please enter a valid email address.",
            },
            errorPlacement: function (error, element) {
                element.attr('title', error.text());
                $(".error").tooltip(
                    {
                        tooltipClass: "mytooltip",
                        placement:'top',
                        html:true
                    });
            }
        });
    });
}();