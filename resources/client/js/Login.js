function postUserLogin() {
    console.log("Invoked postUserLogin() ");

    let url = "/users/AttemptLogin";
    let formData = new FormData(document.getElementById("LoginForm"));

    fetch(url, {
        method: "POST",
        body: formData,
    }).then(response => {
        return response.json();
    }).then(response => {
        if(response.hasOwnProperty("Error")) {
        alert(JSON.stringify(response));
    }else {
        Cookies.set("Token", response.Token);
        Cookies.set("Username", response.Username);
        window.open("Dashboard.html", "_self");
    }
});

}
