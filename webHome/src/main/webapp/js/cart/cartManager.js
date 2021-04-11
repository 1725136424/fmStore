new Vue({
    el: '#app',
    data: {
        cartList: [],
        totalGoods: {
            totalPrice: 0,
            totalNum: 0
        }
    },
    methods: {
        async getCartList() {
            let res = await getCartListReq()
            console.log(res);
            this.cartList = res
        },
        changeNum(index, index1, num) {
            let curGoods = this.cartList[index]['orderItemList'][index1];
            if (curGoods.num > 1 || num > 0) {
                curGoods.num += num
                this.totalGoods.totalNum += num
                this.totalGoods.totalPrice += curGoods.price * num
            }
        },
        checkGoods(event, index, index1) {
            let isCheck = event.target.checked
            let curGoods = this.cartList[index]['orderItemList'][index1];
            if (isCheck) {
                this.totalGoods.totalNum += curGoods.num
                console.log(curGoods.num);
                this.totalGoods.totalPrice += curGoods.price * curGoods.num
            } else {
                this.totalGoods.totalNum -= curGoods.num
                this.totalGoods.totalPrice -= curGoods.price * curGoods.num

            }
        }
    },
    watch: {

    },
    async created() {
        await this.getCartList()
    }
})