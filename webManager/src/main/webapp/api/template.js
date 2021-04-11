(function (window) {
    // 获取品牌
    let getTemplate = (param, page, pageSize) => post(`/template/getTemplateByExample?page=${page}&pageSize=${pageSize}`, param)

    // 获取所有关联数据 -> 品牌 -> 规格
    let getAllAssociation = () => all(getBrand(), getSpec())

    // 保存模板
    let saveTemplate = (param) => post('/template/saveTemplate', param)

    // 修改模板
    let editTemplate = (param) => post('/template/editTemplate', param)

    // 删除模板
    let deleteTemplate = (param) => post('/template/deleteTemplate', param)
    window.getTemplate = getTemplate
    window.getAllAssociation = getAllAssociation
    window.saveTemplate = saveTemplate
    window.editTemplate = editTemplate
    window.deleteTemplate = deleteTemplate
})(window);