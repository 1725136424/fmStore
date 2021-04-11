(function (window) {
    // 获取已经提交商品
    let getContentReq = (param, page, pageSize) => {
        if (!pageSize || !pageSize) {
            // 为传入默认获取 100条数据
            page = 1
            pageSize = 100
        }
        return post(`/content/getContent?page=${page}&pageSize=${pageSize}`, param)
    }

    // 新建分类
    let buildContentReq = (param) => post('/content/saveContent', param)

    // 删除分类
    let deleteContentReq = (param) => post('/content/deleteContent', param)

    // 修改分类
    let editContentReq = (param) => post('/content/editContent', param)

    // 获取所有分类
    let getAllCategory = () => get('/contentCat/getAllCategory')

    // 上传图片
    let uploadImage = (param) => post('/upload/uploadImage', param)

    window.getContentReq = getContentReq
    window.buildContentReq = buildContentReq
    window.deleteContentReq = deleteContentReq
    window.editContentReq = editContentReq
    window.getAllCategory = getAllCategory
    window.uploadImage = uploadImage
})(window);