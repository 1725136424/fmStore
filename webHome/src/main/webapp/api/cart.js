(function (window) {
    let getCartListReq = () => get("/cart/getCartList")
    window.getCartListReq = getCartListReq
})(window);