(function (window) {
    // 获取品牌
    let getSellerReq = (param, page, pageSize) => post(`/seller/getSellerByExample?page=${page}&pageSize=${pageSize}`, param)

    let editSellerReq = (param) => post('/seller/editSeller', param)

    window.getSellerReq = getSellerReq
    window.editSellerReq = editSellerReq
})(window);