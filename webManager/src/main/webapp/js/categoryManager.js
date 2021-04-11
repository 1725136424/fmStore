Vue.component('v-select', VueSelect.VueSelect)
new Vue({
    el: "#app",
    data: {
        pageNav: {
            page: 1,
            pageSize: 8,
            total: 0,
            maxPage: 4,
        },
        searchKeywords: {
            parentId: '',
            name: ''
        },
        categoryList: [],
        navMap: {
            name: '顶级列表分类',
            children: {}
        },
        navList: ['顶级列表分类'],
        nextLevel: true,
        loadedCategoryList: [],
        loadedTotalList: [],
        loadedParentIdList: [],
        categoryEntity: {
            id: '',
            name: '',
            parentId: '',
            typeId: ''
        },
        selectTemplate: [],
        templateList: [],
        categoryIds: []
    },
    methods: {
        async pageHandler(page, parentId) {
            if (parentId === undefined) {
                parentId = this.searchKeywords.parentId
            }
            this.searchKeywords.parentId = parentId
            this.pageNav.page = page
            // 加载网络数据
            let res = await getCategoryReq(this.searchKeywords, this.pageNav.page, this.pageNav.pageSize)
            if (res.rows.length === 0) {
                this.nextLevel = false
                this.searchKeywords.parentId = this.loadedParentIdList[this.loadedParentIdList.length - 1]
            } else {
                this.pageNav.total = res.total
                this.categoryList = res.rows;
                this.loadedTotalList.push(res.total)
                if (!this.loadedParentIdList.includes(parentId)) {
                    this.loadedCategoryList.push(res.rows)
                    this.loadedParentIdList.push(parentId)
                }
            }
        },
        close() {
            this.categoryEntity = {
                id: '',
                name: '',
                parentId: '',
                typeId: ''
            }
        },
        async findChildren(id, index) {
            let curCategory = this.categoryList[index]

            this.getNavChildren(this.navMap).name = curCategory.name

            // 获取子集列表数据
            await this.pageHandler(1, id)

            if (!this.nextLevel) return
            this.navList.push(curCategory.name)

        },
        getNavChildren(data) {
            if (data.children.name) {
                return this.getNavChildren(data.children)
            } else {
                let obj = {
                    name: '',
                    children: {}
                }
                for (let key in obj) {
                    Vue.util.defineReactive(data.children, key, obj[key])
                }
                return data.children
            }
        },
        backLevel(index) {
            if (index !== this.loadedCategoryList.length - 1 && !this.nextLevel) {
                this.nextLevel = true
            }
            this.navList.splice(index + 1)
            this.loadedCategoryList.splice(index + 1)
            this.loadedTotalList.splice(index + 1)
            this.loadedParentIdList.splice(index + 1)
            this.categoryList = this.loadedCategoryList[this.loadedCategoryList.length - 1]
            this.pageNav.total = this.loadedTotalList[this.loadedTotalList.length - 1]
            this.searchKeywords.parentId = this.loadedParentIdList[this.loadedParentIdList.length - 1]
        },
        async addCategory() {
            // 获取模板数据
            let res = await getTemplate({}, 1, 100)
            this.templateList = res.rows
        },
        saveCategory() {
            let method
            if (this.categoryEntity.id) {
                method = editCategoryReq
            } else {
                method = saveCategoryReq
            }
            method(this.categoryEntity)
                .then(async res => {
                    if (res.isSuccess) {
                        await this.pageHandler(1)
                    } else {
                        alert(res.message)
                    }
                    this.categoryEntity = {
                        id: '',
                        name: '',
                        parentId: '',
                        typeId: ''
                    }
                })
        },
        async editCategory(index) {
            await this.addCategory()
            let curCategory = this.categoryList[index]

            let cutTemplate = this.templateList.filter(v => {
                return v.id === curCategory.typeId
            })
            this.selectTemplate = cutTemplate
            this.categoryEntity.id = curCategory.id
            this.categoryEntity.name = curCategory.name
        },
        check(event, id) {
            if (event.target.checked) {
                this.categoryIds.push(id)
            } else {
                let spliceIndex = this.categoryIds.indexOf(id)
                this.categoryIds.splice(spliceIndex, 1)
            }
        },
        deleteCategory() {
            if (this.categoryIds.length === 0) {
                alert('请选择一条数据进行删除')
            } else {
                deleteCategoryReq(this.categoryIds)
                    .then(async res => {
                        if (res.isSuccess) {
                            await this.pageHandler(1)
                            this.categoryIds = []
                        } else {
                            alert(res.message)
                        }
                    })
            }
        }
    },
    created() {
        // 获取顶级列表数据
        this.pageHandler(1, 0)
    },
    watch: {
        loadedParentIdList(newValue, oldValue) {
            if (newValue.length === 0) return
            this.categoryEntity.parentId = newValue[newValue.length - 1]
        },
        selectTemplate(newValue, oldValue) {
            if (newValue.length === 0) return
            this.categoryEntity.typeId = newValue[newValue.length - 1].id
        }
    }
})

