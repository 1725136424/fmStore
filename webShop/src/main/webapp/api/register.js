(function (window) {
    let registerReq = (param) => post('/seller/register', param)
    window.registerReq = registerReq
})(window);