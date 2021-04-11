new Vue({
    el: '#app',
    data: {
        enterprise: {
            sellerId: '',
            password: '',
            nickName: '',
            name: '',
            telephone: '',
            addressDetail: '',
            linkmanName: '',
            linkmanQq: '',
            linkmanMobile: '',
            linkmanEmail: '',
        }
    },
    methods: {
        register: function () {
            registerReq(this.enterprise)
                .then(res => {
                    if (res.isSuccess) {
                        location.href = "/shoplogin.html"
                    } else {
                        alert(res.message)
                    }
                })
        }
    }
})