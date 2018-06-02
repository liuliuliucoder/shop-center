var routerInit = function(app) {
    var router = new VueRouter({});

    // router.redirect({
    //     '/': '/'
    // });

    router.map({
        '/': {
            component: app.index,
        }, '/login':{
            component: app.login,
        },'/managers/login':{
            component: app.managersLogin,
        },'/managers/content':{
            component: app.managersContent,
            subRoutes:{
                'productList':{
                    component:app.productList
                },
                'orderList':{
                    component:app.orderList
                }
            }
        },'/register':{
            component: app.registerMethod,
        },
        '/userCenter':{
            component:app.userCenter
        },
        '/account':{
            component: app.myAccount,
            subRoutes:
                {
                    '/userCenter':{
                        component:app.userCenter
                    }, '/updatePassword':{
                        component:app.updatePassword
                    },'/order':{
                    component:app.myOrder
                },
                }

        },'/product/list':{
            component: app.searchByKeyword,
        },'/product/detail':{
            component: app.productDetail,
        },'/cart/add':{
            component: app.cart,
        },'/order/create':{
            component: app.order,
        },'myCart':{
            component:app.myCart,
        },'result':{
            component:app.result,
        }
    });

    var App = Vue.extend({});
    router.start(App, '#index_content');
}
