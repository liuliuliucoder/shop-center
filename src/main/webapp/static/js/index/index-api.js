(function (root, $http) {
    var api = {};
    api.loginAction = function (query, func1) {
        $http.post("/user/loginAction", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.managerLoginAction = function (query, func1) {
        $http.post("/managers/loginAction", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.getProductList = function (query, func1, func2) {
        $http.get("/manages/product/list", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.getOrderList = function (query, func1, func2) {
        $http.get("/order/list", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };


    api.getStatusList = function ( func1, func2) {
        $http.post("/manages/product/getStatusList").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.getOrderStatusList = function ( func1, func2) {
        $http.post("/order/getOrderStatusList").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    api.getPayTypeList = function ( func1, func2) {
        $http.post("/order/getPayTypeList").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    api.getCategoryList = function ( func1, func2) {
        $http.post("/manages/product/getCategoryList").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.saveOrUpdateProduct = function (query,func1, func2) {
        $http.post("/manages/product/save",query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    
    api.delProduct = function (query,func1,func2) {
        $http.post("/manages/product/delProduct",query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    }

    api.searchByConditions = function (query, func1, func2) {
        $http.post("/manages/product/searchByConditions", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.searchOrderByConditions = function (query, func1, func2) {
        $http.post("/order/searchOrderByConditions", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.registerAction = function (query, func1) {
        $http.post("/user/registerAction", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.resetPassword = function (query, func1, func2) {
        $http.post("/user/resetPassword", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.loginOut = function (func1) {
        $http.post("/loginOut").then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.getUserInfo = function (func1, func2) {
        $http.post("/user/getUserInfo").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.updateUserInfo = function (query,func1, func2) {
        $http.post("/user/updateUserInfo",query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.getMyCartCount = function (func1) {
        $http.get("/cart/getCartProductCount").then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.getMyCart = function (func1) {
        $http.get("/cart/list").then(function (response) {
            func1(response.data);
        }, function (response) {
            console.error(response);
        });
    };

    api.searchByKeyword = function (query, func1, func2) {
        $http.get("/product/list", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.getProductDetail = function (query, func1, func2) {
        $http.get("/product/detail", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    api.addCart = function (query, func1, func2) {
        $http.get("/cart/add", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.updateCartCount = function (query, func1, func2) {
        $http.get("/cart/update", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.getTotalPrice = function (func1, func2) {
        $http.get("/cart/getTotalPrice").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.deleteProduct = function (query, func1, func2) {
        $http.get("/cart/deleteProduct", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.deleteOne = function (query, func1, func2) {
        $http.get("/cart/deleteOne", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.createOrder = function (query, func1, func2) {
        $http.get("/order/create", {params: query}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    api.getOrderPrice = function (query, func1, func2) {
        $http.post("/order/getOrderPrice", query).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.findCategoryForParentId = function (func1, func2) {
        $http.get("/category/find").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };
    api.findCategoryById = function (p, func1, func2) {
        $http.get("/category/findById", {params: p}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.findCategoryBySub = function (func1, func2) {
        $http.get("/category/findBySubCategory").then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };

    api.findProductById = function (p, func1, func2) {
        $http.get("/product/findProductById", {params: p}).then(function (response) {
            func1(response.data);
        }, function (response) {
            if (func2) func2(response);
        });
    };


    root.IndexApi = api;
})(window, Vue.http);