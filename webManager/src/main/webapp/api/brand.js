(function (window) {
    // 获取品牌
    let getBrand =  (param, page, pageSize) => {
        let url;
        if (pageSize && page) {
            url = `/brand/getBrandByExample?page=${page}&pageSize=${pageSize}`
        } else {
            url = '/brand/getBrandByExample'
        }
        if (!param) param = {}
       return  post(url, param)
    }

    // 添加品牌
    let addBrand =  (param) =>  post('/brand/addBrand', param)

    let editBrand =  (param) => post('/brand/editBrand', param)

    // 删除品牌
    let deleteBrand =  (param) => post('/brand/deleteBrand', param)


    window.getBrand = getBrand
    window.addBrand = addBrand
    window.deleteBrand = deleteBrand
    window.editBrand = editBrand
})(window);