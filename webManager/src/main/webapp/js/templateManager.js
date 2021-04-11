Vue.component('v-select', VueSelect.VueSelect)
new Vue({
    el: "#app",
    data: {
        id: 0,
        page: 1,
        pageSize: 8,
        total: 0,
        maxPage: 4,
        searchKeywords: {
            name: ''
        },
        templateList: [],
        allBrands: [],
        selectedBrands: [],
        placeholder: '可以进行多选',
        allSpec: [],
        selectedSpecs: [],
        customAttributeItems: [],
        templateEntity: {
            id: '',
            name: '',
            specIds: '',
            brandIds: '',
            customAttributeItems: ''
        },
        templateIds: []
    },
    methods: {
        pageHandler(page) {
            this.page = page
            // 加载网络数据
            getTemplate(this.searchKeywords, this.page, this.pageSize)
                .then(res => {
                    console.log(res);
                    this.total = res.total
                    this.templateList = res.rows;
                })
                .catch(e => console.log(e))
        },
        formatStr(index, name) {
            let currentTemplate = this.templateList[index]
            let brand
            if (name === 'name') {
                brand = JSON.parse(currentTemplate.brandIds)
            } else if (name === 'specName'){
                brand = JSON.parse(currentTemplate.specIds)
            } else {
                if (currentTemplate.customAttributeItems) {
                    brand = JSON.parse(currentTemplate.customAttributeItems)
                }
            }

            let brandStr = ''
            if (brand) {
                brand.forEach((value, index) => {
                    if (index === 0) {
                        brandStr = value[name]
                    } else {
                        brandStr += ', ' + value[name]
                    }
                })
            }
            return brandStr
        },
        brandInput: function (value) {
        },
        specInput: function (value) {
        },
        getAssociation() {
            if (this.allBrands.length === 0 && this.allSpec.length === 0) {
                // 获取所有品牌数据
                getAllAssociation()
                    .then(res => {
                        this.allBrands = res[0].rows
                        this.allSpec = res[1].rows
                    })
                    .catch(e => console.log(e))
            }
        },
        saveTemplate() {
            let entity = this.templateEntity
            this.templateEntity.specIds = JSON.stringify(this.selectedSpecs)
            this.templateEntity.brandIds = JSON.stringify(this.selectedBrands)
            this.templateEntity.customAttributeItems = JSON.stringify(this.customAttributeItems)
            let method
            if (entity.id) {
                method =  editTemplate
            } else {
                method = saveTemplate
            }
            method(this.templateEntity)
                .then(res => {
                    if (res.isSuccess) {
                        this.pageHandler(1)
                        this.selectedSpecs = []
                        this.selectedBrands = []
                        this.customAttributeItems = []
                    } else {
                        alert(res.message)
                    }
                })
                .catch()
        },
        addAttribute() {
            let attrTemplate = {
                id: this.id++,
                attributeName: ''
            }
            this.customAttributeItems.push(attrTemplate)
        },
        removeAttribute(index) {
            this.customAttributeItems.splice(index, 1)
        },
        close() {
            this.selectedSpecs = []
            this.selectedBrands = []
            this.customAttributeItems = []
        },
        editTemplate(index) {
            let currentTemplate = this.templateList[index];
            this.templateEntity= currentTemplate;
            // 获取关联数据
            this.getAssociation()
            let brands = JSON.parse(currentTemplate.brandIds);
            let specs = JSON.parse(currentTemplate.specIds);
            let attribute = JSON.parse(currentTemplate.customAttributeItems);
            this.selectedBrands = brands
            this.selectedSpecs = specs
            this.customAttributeItems = attribute
        },
        check(event, id) {
            if (event.target.checked) {
                this.templateIds.push(id)
            } else {
                let spliceIndex = this.templateIds.indexOf(id)
                this.templateIds.splice(spliceIndex, 1)
            }
        },
        removeTemplate() {
            if (this.templateIds.length === 0) {
                alert("请选择数据删除")
            } else {
                deleteTemplate(this.templateIds)
                    .then(res => {
                        if (res.isSuccess) {
                            this.templateIds = []
                            this.pageHandler(1);
                        } else {
                            alert(res.message)
                        }
                    })
                    .catch(e => console.log(e))
            }
        }
    },
    created() {
        // 初始化品牌数据
        this.pageHandler(1)
    }
})