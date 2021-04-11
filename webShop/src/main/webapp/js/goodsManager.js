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
            goodsName: '',
            auditStatus: '',
        },
        goodsList: [],
        status: ['未申请', '申请中', '审核通过', '已驳回'],
        allCategory: [],
        ids: []
    },
    methods: {
        async pageHandler(page) {
            this.pageNav.page = page
            let res = await getCommitGoodsReq(this.searchKeywords, this.pageNav.page, this.pageNav.pageSize)
            this.goodsList = res.rows
            this.pageNav.total = res.total
        },
        change(event) {
            let val = parseInt(event.target.value)
            if (val !== -1) {
                this.searchKeywords.auditStatus = val
            } else {
                this.searchKeywords.auditStatus = ''
            }
        },
        getCategoryName(index, categoryClass) {
            let curGoods = this.goodsList[index]
            let curId = curGoods[`category${categoryClass}Id`]
            return this.formatCategory(curId)
        },
        formatCategory(categoryId) {
            // 获取分类数据
            let category = this.allCategory.filter(v => {
                return v.id === categoryId
            })[0]
            if (!category) return
            return category.name
        },
        async getAllCategory() {
            let res = await getAllCategoryReq()
            this.allCategory = res
        },
        check(event, id) {
            if (event.target.checked) {
                this.ids.push(id)
            } else {
                let spliceIndex = this.ids.indexOf(id)
                this.ids.splice(spliceIndex, 1)
            }
        },
        async deleteGoods() {
            let res = await deleteCommitGoodsReq(this.ids)
            if (res.isSuccess) {
                await this.pageHandler(1)
                // 获取所有分类数据
                await this.getAllCategory()
            } else {
                alert("删除失败")
            }
        }
    },
    async created() {
        // 获取顶级列表数据
        await this.pageHandler(1)
        // 获取所有分类数据
        await this.getAllCategory()
    }
})


