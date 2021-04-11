(function (window) {
    // 获取分类
    let getCategoryReq = (param, page, pageSize) => {
        if (!page || !pageSize ) {
            page = 1
            pageSize = 100
        }
        return post(`/category/getCategoryByExample?page=${page}&pageSize=${pageSize}`, param)
    }

    // 上传图片
    let uploadImageReq = (param) => post('/upload/uploadImage', param)

    // 获取图片
    let getImageReq = (param) => get('/upload/getImage', param)

    // 删除图片
    let deleteImageReq = (param) => post('/upload/deleteImage', param)

    // 获取规格与规格选项集合
    let getSpecAndBrandReq = (param) => get('/template/getSpecAndBrand', param)

    // 保存商品
    let saveGoodReq = (param) => post('/goods/saveGoods', param)

    // 修改商品
    let editGoodsReq = (param) => post('/goods/editGoods', param)

    // 获取商品数据实体
    let getGoodsReq = (param) => get('/goods/getGoods', param)

    window.getCategoryReq = getCategoryReq
    window.uploadImageReq = uploadImageReq
    window.getImageReq = getImageReq
    window.deleteImageReq = deleteImageReq
    window.getSpecAndBrandReq = getSpecAndBrandReq
    window.saveGoodReq = saveGoodReq
    window.getGoodsReq = getGoodsReq
    window.editGoodsReq = editGoodsReq
})(window);