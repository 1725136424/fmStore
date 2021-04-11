let vue = new Vue({
    el: "#app",
    data: {
        page: 1,
        pageSize: 8,
        total: 0,
        maxPage: 4,
        searchKeywords: {
            name: '',
            firstChar: ''
        },
        brandList: [],
        addBrandData: {
            id: '',
            name: '',
            firstChar: ''
        },
        brandIds: []
    },
    methods: {
        pageHandler(page) {
            this.page = page
            // 加载网络数据
            getBrand(this.searchKeywords, this.page, this.pageSize)
                .then(res => {
                    this.total = res.total
                    this.brandList = res.rows;
                })
                .catch(e => console.log(e))
        },
        addBrand() {
            let method
            if (this.addBrandData.id) {
                method = editBrand
            } else {
                method = addBrand
            }
            // 添加品牌
            method(this.addBrandData)
                .then(res => {
                    if (res.isSuccess) {
                        // 清空数据
                        this.addBrandData = {
                            id: '',
                            name: '',
                            firstChar: ''
                        }
                        this.pageHandler(1);
                    } else {
                        alert(res.message)
                    }
                })
                .catch(e => console.log(e))
        },
        editBrand(index) {
            // 更新数据
            Object.assign(this.addBrandData, this.brandList[index])
        },
        check(event, id) {
            if (event.target.checked) {
                this.brandIds.push(id)
            } else {
                let spliceIndex = this.brandIds.indexOf(id)
                this.brandIds.splice(spliceIndex, 1)
            }
        },
        deleteBrand() {
            if (this.brandIds.length === 0) {
                alert("请选择数据进行删除")
            } else {
                deleteBrand( this.brandIds)
                    .then(res => {
                        if (res.isSuccess) {
                            // 清空数据
                            this.brandIds = []
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