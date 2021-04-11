new Vue({
    el: '#app',
    data: {
        requestParam: {
            keywords: '',
            page: 1,
            pageSize: 10
        },
        resultMap: {
            rows: [],
            total: '',
            totalPage: ''
        },
        pageHandler: {
            pageList: [],
            leftDot: true,
            rightDot: true,
            maxPageSize: 5
        },
        clickParam: {
            category: '',
            brand: '',
            price: '',
            spec: {},
        },
        price: ["0-500", "500-1000", "1000-1500", "1500-2000", "2000-25000", "2500-3000", "3000-*"]
    },
    methods: {
        getQueryValue(queryName) {
            let query = decodeURI(window.location.search.substring(1));
            let vars = query.split("&");
            for (let i = 0; i < vars.length; i++) {
                let pair = vars[i].split("=");
                if (pair[0] === queryName) {
                    return pair[1];
                }
            }
            return null;
        },
        skip() {
            if (this.keywords) {
                window.location.href = `/search.html?keywords=${this.keywords}`
            }
        },
        async getItem() {
            let newObj = JSON.parse(JSON.stringify(this.requestParam))
            Object.assign(newObj, this.clickParam)
            let res = await getItemReq(newObj)
            if (res) {
                this.resultMap.rows = res.rows
                this.resultMap.total = res.total
                this.resultMap.totalPage = res.totalPage
                Vue.set(this.resultMap, "categoryList", res.categoryList)
                Vue.set(this.resultMap, "brandList", res.specAndBrand.brandList)
                Vue.set(this.resultMap, "specList", res.specAndBrand.specList.specAndSpecOption)
            }
            this.createPage()
        },
        createPage() {
            this.pageHandler.pageList = []
            let star
            let end
            let maxPageSize = this.pageHandler.maxPageSize
            if (this.resultMap.totalPage <= maxPageSize) {
                this.pageHandler.leftDot = false
                this.pageHandler.rightDot = false
                star = 1
                end = this.resultMap.totalPage
            } else {
                if (this.requestParam.page <= Math.ceil(maxPageSize / 2)) {
                    this.pageHandler.leftDot = false
                    this.pageHandler.rightDot = true
                    star = 1
                    end = maxPageSize
                } else if (this.requestParam.page >= this.resultMap.totalPage - Math.floor(maxPageSize / 2)) {
                    this.pageHandler.leftDot = true
                    this.pageHandler.rightDot = false
                    star = this.resultMap.totalPage - maxPageSize + 1
                    end = this.resultMap.totalPage
                } else {
                    this.pageHandler.leftDot = true
                    this.pageHandler.rightDot = true
                    star = this.requestParam.page - Math.floor(maxPageSize / 2)
                    end = this.requestParam.page + Math.floor(maxPageSize / 2)
                }
            }
            for (let i = star; i <= end; i++) {
                this.pageHandler.pageList.push(i)
            }
        },
        async findByPage(page) {
            this.requestParam.page = page
        },
        hasPre() {
            if (this.requestParam.page <= 1) {
                return false
            } else {
                return true
            }

        },
        hasNext() {
            if (this.requestParam.page >= this.resultMap.totalPage) {
                return false
            } else {
                return true
            }

        },
        skipPage(direction) {
            if (direction === "prev") {
                if (this.hasPre()) {
                    this.requestParam.page -= 1
                }
            }else if (direction === "next") {
                if (this.hasNext()) {
                    this.requestParam.page += 1
                }
            }
        },
        confirmPage() {
            this.requestParam.page = parseInt(this.$refs.input.value)
        },
        addCategory(specName) {
            this.clickParam.category = specName
        },
        addBrand(brandName) {
            this.clickParam.brand = brandName
        },
        addSpec(specName, specOptName) {
            Vue.set(this.clickParam.spec, specName,  specOptName)
            console.log(this.clickParam);
        },
        addPrice(index) {
            this.clickParam.price = this.price[index]
        },
        check(name) {
            return !this.clickParam.spec[name]
        },
        removeCategory() {
            this.clickParam.category = ''
        },
        removeBrand() {
            this.clickParam.brand = ''
        },
        removePrice() {
            this.clickParam.price = ''
        },
        removeSpec(key) {
            Vue.delete(this.clickParam.spec, key)
        },
        skipItem(id) {
            window.location.href = `http://127.0.0.1:8086/${id}.html`
        }
    },
    async created() {
        // 获取参数
        this.requestParam.keywords = this.getQueryValue("keywords");
        // 发送请求获取数据
        await this.getItem()
    },
    watch: {
        'requestParam.page'(newValue, oldValue) {
            if (newValue) {
                this.getItem()
            }
        },
        clickParam: {
            handler(newValue, oldValue) {
                this.getItem();
            },
            deep: true
        }
    }
})