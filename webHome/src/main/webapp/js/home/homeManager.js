new Vue({
    el: '#app',
    data: {
        bannerList: [],
        keywords: ''
    },
    methods: {
        skip() {
            if (this.keywords) {
                window.location.href = `/search.html?keywords=${this.keywords}`
            }
        }
    },
    async created() {
        let res = await getBanner()
        console.log(res);
        this.bannerList = res
    }
})