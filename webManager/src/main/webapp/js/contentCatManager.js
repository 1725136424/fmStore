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
            name: '',
        },
        contentCatList: [],
        ids: [],
        contentCatEntity: {
            name: ''
        }
    },
    methods: {
        async pageHandler(page) {
            this.pageNav.page = page
            let res = await getContentCatReq(this.searchKeywords, this.pageNav.page, this.pageNav.pageSize)
            this.contentCatList = res.rows
            this.pageNav.total = res.total
        },
        check(event, id) {
            if (event.target.checked) {
                this.ids.push(id)
            } else {
                let spliceIndex = this.ids.indexOf(id)
                this.ids.splice(spliceIndex, 1)
            }
        },
        async buildContentCat() {
            let method
            if (this.contentCatEntity.id) {
                method = editContentReq
            } else {
                method = buildContentReq
            }
            let res = await method(this.contentCatEntity)
            if (res.isSuccess) {
                location.reload()
            } else {
                alert(res.message)
            }
        },
        async deleteContentCat() {
            if (this.ids.length === 0) {
                alert("请选择数据进行删除")
            } else {
                let res = await deleteContentReq(this.ids)
                if (res.isSuccess) {
                    await this.pageHandler(1)
                    this.ids = []
                    this.contentCatEntity =  {
                        name: ''
                    }
                } else {
                    alert(res.message)
                }
            }
        },
        editContentCat(index) {
            let curContentCat = this.contentCatList[index]
            this.contentCatEntity = curContentCat
        },
        close() {
            this.contentCatEntity = {
                name: ''
            }
        }
    },
    async created() {
        // 获取顶级列表数据
        await this.pageHandler(1)
    }
})


