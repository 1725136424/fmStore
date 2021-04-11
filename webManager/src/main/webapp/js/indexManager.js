new Vue({
    el: '#app',
    data: {
        username: ''
    },
    methods: {
        getUserName() {
            getUsername()
                .then(res => {
                    this.username = res.username
                })
                .catch(e => console.log(e))
        }
    },
    created: function () {
        this.getUserName()
    }
})