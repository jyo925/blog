<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp"%>

<div class="container">
    <form action="/auth/loginProc" method="post">
        <div class="form-group">
            <label for="username">Username:</label>
            <input name="username" type="text" class="form-control" placeholder="Enter username" id="username">
        </div>
        <div class="form-group">
            <label for="password">Password:</label>
            <input name="password" type="password" class="form-control" placeholder="Enter password" id="password">
        </div>
        <button id="btn-login" class="btn btn-primary">로그인</button>
        <a href="https://kauth.kakao.com/oauth/authorize?client_id=1a1c602e60bc0b66c3e1137a353c0669&redirect_uri=http://localhost:8080/auth/kakao/callback&response_type=code"><img src="/image/kakao_login_btn.png" height="39"/></a>
    </form>
    <br/><br/><br/><br/><br/><br/>

</div>

<%@ include file="../layout/footer.jsp"%>
