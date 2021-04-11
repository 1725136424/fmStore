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
            title: '',
        },
        contentList: [],
        ids: [],
        categoryList: [],
        contentEntity: {
            categoryId: '',
            title: '',
            url: '',
            pic: '',
            status: '',
            sortOrder: ''
        }
    },
    methods: {
        async pageHandler(page) {
            this.pageNav.page = page
            let res = await getContentReq(this.searchKeywords, this.pageNav.page, this.pageNav.pageSize)
            this.contentList = res.rows
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
        async loadData() {
            if (this.categoryList.length > 0) return
          // 加载分类数据
            let res = await getAllCategory()
            this.categoryList = res
        },
        change(event) {
            let val = event.target.value
            console.log(val);
            if (val !== -1) {
                this.contentEntity.categoryId = val
            } else {
                this.contentEntity.categoryId = ''
            }
        },
        async uploadImage() {
            let formData = new FormData();
            let file = this.$refs.file
            formData.append('file', file.files[0])
            let res = await uploadImage(formData)
            console.log(res);
            if (res.url) {
                this.contentEntity.pic = res.url
            } else {
                alert("上传失败")
            }
        },
        async save() {
            let method
            if (this.contentEntity.id) {
                method = editContentReq
            } else {
                method = buildContentReq
            }
            let res = await method(this.contentEntity)
            if (res.isSuccess) {
                location.reload()
            } else {
                alert(res.message)
            }
        },
        isValid(event) {
            let isChecked = event.target.checked
            this.contentEntity.status = isChecked? 1: 0
        },
        async deleteContent() {
            if (this.ids.length === 0) {
                alert("请选择数据")
            } else {
                let res = await deleteContentReq(this.ids)
                if (res.isSuccess) {
                    location.reload()
                } else {
                    alert(res.message)
                }
            }
        },
        editContent(index) {
            let curContent = this.contentList[index]

            this.contentEntity = JSON.parse(JSON.stringify(curContent))
        },
        close() {
            this.contentEntity = {
                categoryId: '',
                    title: '',
                    url: '',
                    pic: '',
                    status: '',
                    sortOrder: ''
            }
        }
    },
    async created() {
        await this.pageHandler(1)
    }
})


