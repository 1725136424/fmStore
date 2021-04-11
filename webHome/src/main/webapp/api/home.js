(function (window) {
    let getBanner = () => get('/content/getContentByCatFromRedis?categoryId=1')
    window.getBanner = getBanner
})(window);