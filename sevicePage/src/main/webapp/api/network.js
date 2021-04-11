(function (w) {
    axios.defaults.baseURL  = 'http://localhost:8084'
    axios.defaults.timeout = '6000'

    // get请求
    let get = function (url, param) {
        return new Promise((resolve, reject) =>  {
            axios.get(url, {
                params: param
            })
                .then(res => {
                    let { data } = res
                    resolve(data)
                })
                .catch(e => reject(e))
        })
    }

    // post请求
    let post = function (url, param) {
        if (isArray(param)) {
            // 为数组
            param  = Qs.stringify({ idx: param}, {indices: false})
        }
        return new Promise((resolve, reject) => {
            axios.post(url, param)
                .then(res => {
                    let { data } = res
                    resolve(data)
                })
                .catch(e => reject(e))
        })
    }

    // all方法
    let all = function(...args) {
        return new Promise((resolve, reject) => {
            axios.all(args)
                .then(axios.spread(function (...res) {
                    resolve(res)
                }))
                .catch(e => reject(e))
        })
    }

    let isArray = function (obj) {
        return obj instanceof Array
    }

    window.get = get
    window.post = post
    window.all = all
})(window);