let index;
index = {
    init: function () {
        $("#btn-save").on("click", () => {
            this.save();
        });
    },

    save: function () {
        // alert("user의 svae 호출");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        }

        // console.log(data);

        $.ajax().done().fail();
        //ajax 통신을 이용해서 3개의 데이터를 json으로 변환하여 회원가입 insert 요청

    }
};

index.init()
