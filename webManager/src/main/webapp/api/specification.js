(function (window) {
    // 获取品牌
    let getSpec = (param, page, pageSize) => {
        let url;
        if (pageSize && page) {
            url = `/specification/getSpecByExample?page=${page}&pageSize=${pageSize}`
        } else {
            url = '/specification/getSpecByExample'
        }
        if (!param) param = {}
        return  post(url, param)
    }

    // 保存规格与规格选型
    let saveSpec = (param) => post('/specification/saveSpec', param)

    // 修改规格与规格选型
    let editSpec = (param) => post('/specification/editSpec', param)

    // 获取规格相对应的规格选项数据
    let getSpecOptions = (param) => get('/specification/getSpecOptions', param)

    // 删除规格
    let deleteSpec = (param) => post('/specification/deleteSpec', param)
    window.getSpec = getSpec
    window.saveSpec = saveSpec
    window.editSpec = editSpec
    window.getSpecOptions = getSpecOptions
    window.deleteSpec = deleteSpec
})(window);