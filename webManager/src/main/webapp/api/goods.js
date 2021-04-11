(function (window) {
    // 获取已经提交商品
    let getCommitGoodsReq = (param, page, pageSize) => {
        if (!pageSize || !pageSize) {
            // 为传入默认获取 100条数据
            page = 1
            pageSize = 100
        }
        return post(`/goods/getCommitGoods?page=${page}&pageSize=${pageSize}`, param)
    }

    // 获取所有分类
    let getAllCategoryReq = () => get('/category/getAllCategory')

    // 删除审核商品
    let deleteCommitGoodsReq = (param) => post('/goods/deleteCommitGoodsRel', param)

    // 通过审核商品
    let passCommitGoodsReq = (param) => post('/goods/passCommitGoods', param)

    let rollbackGoodsReq = (param) => post('/goods/rollbackGoods' ,param)

    window.getCommitGoodsReq = getCommitGoodsReq
    window.getAllCategoryReq = getAllCategoryReq
    window.deleteCommitGoodsReq = deleteCommitGoodsReq
    window.passCommitGoodsReq = passCommitGoodsReq
    window.rollbackGoodsReq = rollbackGoodsReq
})(window);