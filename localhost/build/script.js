var Events = (function() {
    console.log("Модуль подключен");
    return {
        onButtonClick: function() {
            var username = document.getElementById('username');
            var password = document.getElementById('password');
            var result = document.getElementById('result');
            console.log("Проверка авторизации: ", username.value, password.value);
            if(username.value == "admin" && password.value == "123"){
                result.innerHTML = "Correct";
                document.title = "Authorization: Correct";
            }else{
                result.innerHTML = "Incorrect";
                document.title = "Authorization: Incorrect";
            }
        }
    }
}());