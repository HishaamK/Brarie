/*function Username() {
    var Username;
    var decodedcookie = decodeURIComponent(document.cookie);
    var ca = decodedcookie.split(";");
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == "") {
            c = c.substring(1);
        }
        i
    }
} */

function getUsername() {
    var Username = document.cookie;
    document.getElementById("WelcomeBack").innerText = "Welcome Back, " + Username;
}
