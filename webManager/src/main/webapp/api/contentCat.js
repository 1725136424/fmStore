(function (window) {
    // 获取已经提交商品
    let getContentCatReq = (param, page, pageSize) => {
        if (!pageSize || !pageSize) {
            // 为传入默认获取 100条数据
            page = 1
            pageSize = 100
        }
        return post(`/contentCat/getContentCat?page=${page}&pageSize=${pageSize}`, param)
    }

    // 新建分类
    let buildContentReq = (param) => post('/contentCat/saveContentCat', param)

    // 删除分类
    let deleteContentReq = (param) => post('/contentCat/deleteContentCat', param)

    // 修改分类
    let editContentReq = (param) => post('/contentCat/editContentCat', param)

    window.getContentCatReq = getContentCatReq
    window.buildContentReq = buildContentReq
    window.deleteContentReq = deleteContentReq
    window.editContentReq = editContentReq
})(window);