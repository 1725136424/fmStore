(function (window) {
    let getUsername = () => get('/user/username')

    window.getUsername = getUsername
})(window);