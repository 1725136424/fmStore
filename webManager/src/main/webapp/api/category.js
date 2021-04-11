(function (window) {
    // 获取分类
    let getCategoryReq = (param, page, pageSize) => post(`/category/getCategoryByExample?page=${page}&pageSize=${pageSize}`, param)

    // 保存分类
    let saveCategoryReq = (param) => post('/category/saveCategory', param)

    // 修改分类
    let editCategoryReq = (param) => post('/category/editCategory', param)

    // 删除分类
    let deleteCategoryReq = (param) => post('/category/deleteCategory', param)

    window.getCategoryReq = getCategoryReq
    window.saveCategoryReq = saveCategoryReq
    window.editCategoryReq = editCategoryReq
    window.deleteCategoryReq = deleteCategoryReq
})(window);