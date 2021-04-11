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
            nickName: '',
            status: 0
        },
        sellerList: [],
        curSeller: {}
    },
    methods: {
        pageHandler(page) {
            this.pageNav.page = page
            // 加载网络数据
            getSellerReq(this.searchKeywords, this.pageNav.page, this.pageNav.pageSize)
                .then(res => {
                    console.log(res);
                    this.total = res.total
                    this.sellerList = res.rows;
                })
                .catch(e => console.log(e))
        },
        getDetail(index) {
            let curSeller = this.sellerList[index];
            this.curSeller = curSeller
        },
        optionSeller(status) {
            if (!status) return
            this.curSeller.status = status
            editSellerReq(this.curSeller)
                .then(res => {
                    if (res.isSuccess) {
                        this.curSeller = {}
                        this.pageHandler(1)
                    } else {
                        alert(res.message)
                    }
                })
        },
        close() {
            this.curSeller = {}
        },
    },
    created() {
        // 初始化品牌数据
        this.pageHandler(1)
    }
})

