new Vue({
    el: "#app",
    data: {
        id: 0,
        page: 1,
        pageSize: 8,
        total: 0,
        maxPage: 4,
        searchKeywords: {
            specName: ''
        },
        specList: [],
        specEntity: {
            specification: {
                specName: ''
            },
            specificationOptions: []
        },
        specId: [],
    },
    methods: {
        pageHandler(page) {
            this.page = page
            // 加载网络数据
            getSpec(this.searchKeywords, this.page, this.pageSize)
                .then(res => {
                    this.total = res.total
                    this.specList = res.rows;
                })
                .catch(e => console.log(e))
        },
        addSpecOpt: function () {
            // 添加规格实体 -》构造规格选项实体
            let specOpt = {
                idx: this.id++,
                optionName: '',
                orders: ''
            }
            this.specEntity.specificationOptions.push(specOpt)
        },
        removeSpecOpt: function (OptId) {
            let result = this.specEntity.specificationOptions.filter(v => {
                return v.id !== OptId
            })
            this.specEntity.specificationOptions = result
        },
        addSpec: function () {
            let method
            if (this.specEntity.specification.id) {
                method = editSpec
            } else {
                method = saveSpec
            }
            // 保存规格与规格选项
            method(this.specEntity)
                .then(res => {
                    if (res.isSuccess) {
                        this.specEntity = {
                            specification: {
                                specName: ''
                            },
                            specificationOptions: []
                        }
                        this.pageHandler(1);
                    } else {
                        alert(res.message)
                    }
                })
                .catch(e => console.log(e))
        },
        editSpec: function (index) {
            let currentSpec = this.specList[index];
            this.specEntity.specification = currentSpec;

            // 获取关联数据集合
            getSpecOptions( {id: currentSpec.id})
                .then(res => {
                    this.specEntity.specificationOptions = res;
                    this.specEntity.specificationOptions.forEach(v => {
                        v.idx = this.id++
                    })
                })
                .catch(e => console.log(e))
        },
        close: function () {
            this.specEntity = {
                specification: {
                    specName: ''
                },
                specificationOptions: []
            }
        },
        check(event, index) {
            if (event.target.checked) {
                this.specId.push(index)
            } else {
                let spliceIndex = this.specId.indexOf(index)
                this.specId.splice(spliceIndex, 1)
            }
        },
        deleteSpec() {
            if (this.specId.length === 0) {
                alert("请选择数据删除")
            } else {
                deleteSpec(this.specId)
                    .then(res => {
                        if (res.isSuccess) {
                            this.specId = []
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