(function (window) {
    let getItemReq = (param) => post("/search/getItem", param)

    window.getItemReq = getItemReq
})(window);