// UEditor
let ue = UE.getEditor('container');


ue.ready(function () {
    ueditor("/upload/uploadImageUEditor")
})

// 自定义UEditor上传路径
function ueditor(basePath) {
    UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
    UE.Editor.prototype.getActionUrl = function (action) {
        if (action === 'uploadimage' || action === 'uploadscrawl') {
            return basePath;//此处写自定义的图片上传路径
        } else if (action === 'uploadvideo') {
            return 'http://a.b.com/video.php';
        } else {
            return this._bkGetActionUrl.call(this, action);
        }
    }
}

// vue核心内容
new Vue({
    el: '#app',
    data: {
        categoryList: [],
        curModelId: '',
        goodsImages: {
            id: '',
            color: '',
            url: ''
        },
        savedImages: [],
        specAndSpecOption: [],
        selectedSpecAndSpecOption: [],
        rowList: [],
        brandList: [],
        selectBrand: {},
        goodEntity: {
            goods: {
                goodsName: '',
                brandId: '',
                caption: '',
                category1Id: '',
                category2Id: '',
                category3Id: '',
                price: '',
                typeTemplateId: '',
                isEnableSpec: 1
            },
            goodsDesc: {
                introduction: '',
                specificationItems: [],
                itemImages: [],
                packageList: '',
                saleService: ''
            },
            items: []
        }
    },
    methods: {
        async getCategory(parentId) {
            let res = await getCategoryReq({parentId})
            if (res.rows.length === 0) return
            this.categoryList.push(res.rows)
        },
        async change(event, index) {
            // 切割数组
            if (index < this.categoryList.length) {
                this.categoryList.splice(index + 1)
            }
            let val = parseInt(event.target.value)
            if (val !== -1) {
                let curCate = this.categoryList[index]
                let res = curCate.filter(v => {
                    return v.id === val
                })[0]
                // 初始化分类id
                this.goodEntity.goods[`category${index + 1}Id`] = res.id
                // 初始化模板id
                this.goodEntity.goods.typeTemplateId = res.typeId
                this.curModelId = res.typeId
                // 获取对应模板的数据
                this.getSpecAndSpecOption(this.curModelId);
                // 获取当前分类子分类的数据
                await this.getCategory(val)
            } else {
                this.curModelId = 0
                this.specAndSpecOption = []
                this.brandList = []
                this.goodEntity.goods[`category${index + 1}Id`] = 0
                // 初始化模板id
                this.goodEntity.goods.typeTemplateId = 0
            }
        },
        async getSpecAndSpecOption(id) {
            let res = await getSpecAndBrandReq({id: id})
            // res --> 规格与规格选项  品牌
            this.specAndSpecOption = res.specAndSpecOption
            this.brandList = res.brand
        },
        async uploadImage() {
            let formData = new FormData();
            let $file = this.$refs.file;
            formData.append('file', $file.files[0]);
            formData.append('color', this.goodsImages.color);
            // 上传图片信息
            let res = await uploadImageReq(formData)
            if (res.id) {
                // 获取保存的图片展示
                let result = await getImageReq({id: res.id})
                this.goodsImages = result
            } else {
                alert("保存失败")
            }
        },
        async deleteImage(index) {
            let curImage = this.savedImages[index]
            let res = await deleteImageReq(curImage)
            if (res.isSuccess) {
                this.savedImages.splice(index, 1)
            } else {
                alert(res.message)
            }
        },
        saveImage() {
            let img = this.goodsImages
            this.savedImages.push(img);
            this.goodsImages = {
                id: '',
                color: '',
                url: ''
            }
        },
        check: function (event, spec, specOption) {
            let specId = spec.id
            let filterArray = this.selectedSpecAndSpecOption.filter(v => {
                return v.id === specId
            })[0]
            if (event.target.checked) {
                // 存在
                if (filterArray) {
                    filterArray["specificationOptions"].push(specOption)
                } else {
                    let obj = {}
                    Object.assign(obj, spec)
                    obj["specificationOptions"] = [specOption]
                    this.selectedSpecAndSpecOption.push(obj)
                }
            } else {
                // 删除数组
                let curArray = filterArray["specificationOptions"]
                let index = curArray.indexOf(specOption)
                curArray.splice(index, 1)
                if (curArray.length === 0) {
                    let index1 = this.selectedSpecAndSpecOption.indexOf(filterArray);
                    this.selectedSpecAndSpecOption.splice(index1, 1)
                }
            }
            this.createData();
        },
        createData() {
            let rowList = [
                {
                    spec: {},
                    price: 0,
                    num: 999999,
                    isDefault: 0
                }
            ]
            let selectSpec = this.selectedSpecAndSpecOption
            selectSpec.forEach(v1 => {
                let specName = v1.specName
                let specOptions = v1.specificationOptions
                let newList = []
                rowList.forEach(v2 => {
                    specOptions.forEach(v3 => {
                        let createList = JSON.parse(JSON.stringify(v2))
                        createList.spec[specName] = v3.optionName
                        newList.push(createList)
                    })
                })
                rowList = newList;
            })
            this.rowList = rowList;
        },
        select(event) {
            let val = parseInt(event.target.value)
            if (val !== -1) {
                // 选择品牌
                let selectBrand = this.brandList.filter(v => {
                    return v.id === val
                })[0]
                this.selectBrand = selectBrand
            } else {
                this.selectBrand = {}
            }
        },
        isAbleSpec(event) {
            if (event.target.checked) {
                this.goodEntity.goods.isEnableSpec = 0
            } else {
                this.goodEntity.goods.isEnableSpec = 1
            }
        },
        async saveGood() {
            this.goodEntity.items = this.rowList
            // 初始化商品介绍
            this.goodEntity.goodsDesc.introduction = ue.getContent()

            if (!this.goodEntity.goods.brandId) {
                alert("请填写品牌信息")
            }
            if (!this.goodEntity.goods.category1Id &&
                !this.goodEntity.goods.category2Id &&
                !this.goodEntity.goods.category3Id) {
                alert("请填写分类信息")
            }

            let method

            if (this.goodEntity.goods.id) {
                method = editGoodsReq
            } else {
                method = saveGoodReq
            }
            let res = await method(this.goodEntity)
            if (res.isSuccess) {
                window.location.href = "/admin/goods.html"
            } else {
                alert(res.message)
            }
        },
        getUrlParam(queryName) {
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
        async EchoData(goodsId) {
            // 获取商品数据实体
            let res = await getGoodsReq({id: goodsId})
            console.log(res);

            // 回显UEditor
            ue.ready(function () {
                ue.setContent(res.goodsDesc.introduction)
            })

            // 回显分类
            for (let j = 0; j < 3; j++) {
                // 回显分类
                let category1 = res.goods[`category${ j + 1 }Id`]
                let index1
                let selectedCat = this.categoryList[j].forEach((v, i) => {
                    if (v.id === category1) {
                        index1 = i
                    }
                })
                this.$refs[`category${index1}`][j].selected = true
                let curCategory = this.categoryList[j][index1]
                // 获取当前分类子分类的数据
                await this.getCategory(curCategory.id)
                if (j === 2) {
                    // 初始化模板id
                    this.curModelId = curCategory.typeId
                    // 获取对应模板的数据
                    this.getSpecAndSpecOption(this.curModelId);
                }
            }

            // 回显图片
            this.savedImages = JSON.parse(res.goodsDesc.itemImages)

            // 回显规格
            this.selectedSpecAndSpecOption = JSON.parse(res.goodsDesc.specificationItems)

            this.rowList = res.items
            this.rowList.forEach(v => {
                v.spec = JSON.parse(v.spec)
            })

            // 创建数据
            this.goodEntity = res
        },
        isChecked(specName, optionId) {
            let array = this.selectedSpecAndSpecOption.find(v => {
                return v.specName === specName
            })
            if (array) {
                let flag = false
                array.specificationOptions.forEach(v => {
                    if (v.id === optionId) {
                        flag = true
                    }
                })
                return flag
            }
        }
    },
    watch: {
        selectBrand(newValue, oldValue) {
            if (newValue.id) {
                this.goodEntity.goods.brandId = newValue.id
            } else {
                this.goodEntity.goods.brandId = ""
            }
        },
        selectedSpecAndSpecOption(newValue, oldValue) {

            this.goodEntity.goodsDesc.specificationItems = newValue
        },
        savedImages(newValue, oldValue) {
            this.goodEntity.goodsDesc.itemImages = newValue
        }
    },
    async created() {
        await this.getCategory(0)
        // 回显数据
        let goodsId = this.getUrlParam("goodsId")
        if (goodsId) {
            await this.EchoData(goodsId)
        }
    },
})