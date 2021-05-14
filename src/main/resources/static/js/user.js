let index;
index = {
    init: function () {
        // function(){} 대신 ()=>{} 사용하는 이유는 this를 바인딩하기 위함
        // function(){}으로 사용하면 this가 window 객체를 가리킴
        $("#btn-save").on("click", () => {
            this.save();
        });
        $("#btn-login").on("click", () => {
            this.login();
        });
    },

    save: function () {
        // alert("user의 svae 호출");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };

        // console.log(data);

        //ajax 통신을 이용해서 3개의 데이터를 json으로 변환하여 회원가입 insert 요청
        //ajax 호출시 default => 비동기 호출
        $.ajax({
            type: "POST",
            url: "/blog/api/user",
            data: JSON.stringify(data), //http body 데이터 - 자바스크립트 오브젝트를 JSON 문자열로 변환(Java가 이해할 수 있도록)
            contentType: "application/json; charset=utf-8",
            dataType: "json" //응답을 어떤 타입으로 받을지, 응답은 버퍼로 오기 때문에 기본적으로 모두 문자열 형태
        }).done(function (resp) { //응답 결과 문자열이 생긴게 json이라면 함수 파라미터로 자동 전달
            console.log(resp);
            alert("회원가입이 완료되었습니다.");
            location.href = "/blog";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },

    login: function () {
        // alert("user의 svae 호출");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
        };

        $.ajax({
            type: "POST",
            url: "/blog/api/user/login",
            data: JSON.stringify(data), 
            contentType: "application/json; charset=utf-8",
            dataType: "json" //응답을 어떤 타입으로 받을지, 응답은 버퍼로 오기 때문에 기본적으로 모두 문자열 형태
        }).done(function (resp) { //응답 결과 문자열이 생긴게 json이라면 함수 파라미터로 자동 전달
            console.log(resp);
            alert("로그인이 완료되었습니다.");
            location.href = "/blog";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
};

index.init()
