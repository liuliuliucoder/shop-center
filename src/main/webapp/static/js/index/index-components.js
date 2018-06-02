var components = function () {
    var app = {};
    app.index = {
        template: '#index',
        data: function () {
            return {
                loginORregister: true,
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                user: {
                    userName: "",
                    password: "",
                },
                keyword: null,
                categoryId: null,
                parentCategory: [],
                subCategory: [],
                threeCList: [],
                bagList: [],
                freshList: [],
                drinkList: []
            }
        },
        created: function () {
            this.getUserInfo();
            this.getMyCartCount();
            this.findCategory();
            this.findBagList();
            this.findThreeCList();
            this.findDrinkList();
            this.findFreshList();
        },
        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toRegister: function () {
                this.$router.go('/register');
                self.loginORregister = false;
                this.navShow = false;
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    this.$router.go('/');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    if (data.value) {
                        self.myCartCount = data.data;
                    } else {
                        self.myCartCount = 0;
                    }
                }.bind(this));
            },
            toMyAccount: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/account");
                }
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/myCart");
                }
            },
            searchByKeyword: function (q,e) {
                var self = this;
                if (q && q != null) {
                    self.keyword = q;
                }
                if (self.keyword != '' && self.keyword != null) {
                    this.$router.go('/product/list?keyword=' + self.keyword);
                }
            },
            searchByCategoryId: function () {
                var self = this;
                this.$router.go('/product/list?categoryId=' + self.categoryId);
            },
            findCategory: function () {
                var self = this;
                IndexApi.findCategoryBySub(function (data) {
                    self.subCategory = data.content || [];
                }.bind(this))
            },
            findBagList: function () {
                var self = this;
                IndexApi.findProductById({'categoryId': 3}, function (data) {
                    self.bagList = data.content || [];
                }.bind(this))
            },
            findFreshList: function () {
                var self = this;
                IndexApi.findProductById({'categoryId': 4}, function (data) {
                    self.freshList = data.content || [];
                }.bind(this))
            },
            findDrinkList: function () {
                var self = this;
                IndexApi.findProductById({'categoryId': 5}, function (data) {

                    self.drinkList = data.content || [];
                }.bind(this))
            },
            findThreeCList: function () {
                var self = this;
                IndexApi.findProductById({'categoryId': 2}, function (data) {
                    
                    self.threeCList = data.content || [];
                }.bind(this));
            },
            toDetail : function(productId){
                this.$router.go("product/detail?id="+productId);
            }

        }
    };
    app.myAccount = {
        template: '#myAccount',
        route: {
            data: function (transition) {
                var self = this;
                self.$router.go("/account/userCenter");
            }
        },
        data: function () {
            return {
                loginORregister: true,
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                user: {
                    userName: "",
                    password: "",
                },
            }
        },
        created: function () {
            this.getUserInfo();
            this.getMyCartCount();
        },
        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toRegister: function () {
                this.$router.go('/register');
                self.loginORregister = false;
                this.navShow = false;
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    this.$router.go('/');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/manager/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    
                    if (data.value) {
                        self.myCartCount = data.data;
                    } else {
                        self.myCartCount = 0;
                    }
                }.bind(this));
            },
            toMyAccount: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    self.$router.go("/");
                } else {
                    self.$router.go("/account");
                }
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/myCart");
                }
            },
            toUserCenter: function () {
                var self = this;
                self.$router.go("/account/userCenter");
            },
            toUpdatePassword: function () {
                var self = this;
                self.$router.go("/account/updatePassword");
            },
            toMyOrder:function () {
                var self = this;
                self.$router.go("/account/order");
            },
        }
    };
    app.myCart = {
        template: '#cart',
        route: {
            data: function (transition) {
                var self = this;
                var q = self.$route.query;
                self.query.id = q.id;
                self.query.count = q.count;
                self.getUserInfo();
                self.getMyCartCount();
                self.getMyCart();
                self.getTotalPrice();
            }
        },
        data: function () {
            return {
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                totalPrice:0,
                user: {
                    userName: "",
                    password: "",
                },
                query: {
                    productId: null,
                    count: null,
                },
                cartList: []
            }
        },

        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toIndex: function () {
                this.$router.go('/');
            },
            toMyAccount: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/account");
                }
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    this.$router.go('/');
                }.bind(this));
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    self.myCartCount = data.data;
                }.bind(this));
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == "" || null == self.user.userName) {
                    alert("用户未登录，请先登录");
                } else {
                    self.$router.go('/cart/list');
                }
            },
            toMyAccount: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/account");
                }
            },
            getMyCart: function () {
                var self = this;
                IndexApi.getMyCart(function (data) {
                    self.cartList = data.content;
                }.bind(this));
            },
            addCart: function () {
                var self = this;
                IndexApi.addCart(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getTotalPrice();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            addCount: function (productId, count) {
                var self = this;
                self.query.count = count + 1;
                self.query.productId = productId;
                IndexApi.updateCartCount(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getMyCartCount();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            minusCount: function (productId, count) {
                var self = this;
                self.query.count = count - 1;
                self.query.productId = productId;
                IndexApi.updateCartCount(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getMyCartCount();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            getTotalPrice:function () {
                var self = this;
                IndexApi.getTotalPrice(function (data) {
                    self.totalPrice = data.data;
                }.bind(this));
            },
            deleteOne: function (productId) {
                var self = this;
                IndexApi.deleteOne({'productId':productId}, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getTotalPrice();
                        self.getMyCartCount();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            deleteAll: function () {
                var self = this;
                var productIds = "";
                for (var i = 0; i < self.cartList.length; i++) {
                    productIds += self.cartList[i].productId;
                    productIds += ",";
                }
                IndexApi.deleteProduct(productIds, function (data) {
                    self.cartList = data.content;
                    self.getTotalPrice();
                    self.getMyCartCount();
                }.bind(this));
            },
            toProductDetail: function (productId) {
                var self = this;
                this.$router.go('/product/detail?id=' + productId);
            },
            toSettle: function (addressId) {
                var self = this;
                this.$router.go('/order/create');
            }
        }
    };
    app.userCenter = {
        template: '#userCenter',
        route: {
            data: function (transition) {
                this.getUserInfo();
            }
        },
        data: function () {
            return {
                user:{
                    userName: "",
                    password: "",
                    rePassword: "",
                    email: "",
                    nickName: "",
                    phone: "",
                    question:"",
                    answer:""
                }
            }
        },
        methods: {
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                    } else {
                        alert("获取用户信息失败");
                    }
                }.bind(this));
            },
            updateUserInfo:function () {
                var self = this;
                IndexApi.updateUserInfo(self.user,function (data) {
                    if (data.value) {
                        alert("修改成功");
                    } else {
                        alert("修改失败");
                    }
                }.bind(this));
            }
        }
    };
    app.updatePassword = {
        template: '#updatePassword',
        route:{
            data: function (transition) {
                var self = this;
                self.$router.go("/account/updatePassword");
            }
        },
        data: function () {
            return {
                user:{
                    password:"",
                    oldPassword:"",
                    rePassword:""
                }
            }
        },
        methods: {
            submit:function () {
                var self = this;
                if(self.user.password != self.user.rePassword){
                    alert("两次输入的密码不一致");
                    return;
                }
                IndexApi.resetPassword(self.user, function (data) {
                    if(!data.value){
                        alert(data.message);
                    } else {
                        alert("密码修改成功，请重新登录");
                        self.$router.go("/login");
                    }
                }.bind(this));
            },
        }
    };
    app.myOrder = {
        template: '#account_order',
        route: {
            data: function (transition) {
                var self = this;
                self.$router.go("/account/myOrder");
                self.getUserInfo();
            }
        },
        data: function () {
            return {
                user:{
                    password:"",
                    oldPassword:"",
                    rePassword:""
                }
            }
        },
        methods: {
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                    } else {
                        alert("获取用户信息失败");
                    }
                }.bind(this));
            },
        }
    };
    app.login = {
        template: '#login',
        data: function () {
            return {
                user: {
                    userName: "",
                    password: ""
                }
            }
        },
        methods: {
            loginMethod: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("用户名不能为空！");
                    return;
                } else if (self.user.password == '' || self.user.password == null) {
                    alert("密码不能为空！");
                    return;
                }
                IndexApi.loginAction(self.user, function (data) {
                    
                    if (data.value) {
                        this.$router.go("/");
                    } else {
                        this.$router.go("/login");
                    }
                }.bind(this));
            },
        }
    };
    app.managersLogin = {
        template: '#mlogin',
        data: function () {
            return {
                user: {
                    userName: "",
                    password: ""
                }
            }
        },
        methods: {
            managerLoginMethod: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("用户名不能为空！");
                    return;
                } else if (self.user.password == '' || self.user.password == null) {
                    alert("密码不能为空！");
                    return;
                }
                IndexApi.managerLoginAction(self.user, function (data) {
                    
                    if (data.value) {
                        this.$router.go("/managers/content");
                    } else {
                        this.$router.go("/managers/login");
                    }
                }.bind(this));
            },
        }
    };
    app.managersContent = {
        template: '#mContent',
        route:{
            data: function (transition) {
                var self = this;
                self.$router.go("/managers/content/productList");
            }
        },
        data: function () {
            return {
                loginORregister: true,
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                user: {
                    userName: "",
                    password: "",
                },
            }
        },
        created: function () {
            this.getUserInfo();
        },
        methods: {
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.managerToLogin();
                    this.$router.go('/managers/login');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            toProductList: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/managers/content");
                } else {
                    self.$router.go("/managers/content/productList");
                }
            },
            toOrderList: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/managers/content");
                } else {
                    self.$router.go("/managers/content/orderList");
                }
            },
        }
    };
    app.productList = {
        template: '#productList',
        data: function () {
            return {
                loginORregister: true,
                navShow: true,
                isLogined: true,
                user: {
                    userName: "",
                    password: "",
                },
                addImage:null,
                query: {
                    id: null,
                    status: null,
                    category: null,
                    pageNum: 1,
                    pageSize: 20
                },
                productForm: {
                    id: null,
                    productName: null,
                    subTitle: null,
                    price: null,
                    status: null,
                    count:0,
                    categoryId:null,
                    mainImage:null
                },
                productEditForm:{
                    id: null,
                    productName: null,
                    subTitle: null,
                    price: null,
                    status: null,
                    count:0,
                    categoryId:null,
                    mainImage:null
                },
                productList: [],
                statusList:[],
                categoryList:[]
            }
        },
        created: function () {
            this.getUserInfo();
            this.getProductList();
            this.getStatusList();
            this.getCategoryList();
        },
        methods: {
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    this.$router.go('/managers/login');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getProductList: function () {
                var self = this;
                IndexApi.getProductList(self.query, function (data) {
                    
                    self.productList = data.content;
                }.bind(this));
            },
            searchByConditions: function () {
                var self = this;
                IndexApi.searchByConditions(self.query, function (data) {
                    if(data.value){
                        self.productList = data.content;
                    } else{
                        alert(data.message);
                    }

                }.bind(this));
            },
            getStatusList: function () {
                var self = this;
                IndexApi.getStatusList(function (data) {
                    if(data.value){
                        self.statusList = data.content;
                    } else{
                        alert(data.message);
                    }

                }.bind(this));
            },
            getCategoryList:function () {
                var self = this;
                IndexApi.getCategoryList(function (data) {
                    if(data.value){
                        self.categoryList = data.content;
                    } else{
                        alert(data.message);
                    }

                }.bind(this));
            },
            saveOrUpdateProduct: function () {
                var self = this;
                self.productForm.mainImage = document.getElementById("addImage").value;
                IndexApi.saveOrUpdateProduct(self.productForm,function (data) {
                    if(data.value){
                        alert(data.message);
                        self.getProductList();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },


            delProduct: function (productId) {
                var self = this;
                self.query.id = productId;
                IndexApi.delProduct(self.query,function (data) {
                    if(data.value){
                        alert("删除成功！");
                        self.getProductList();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },

            queryProductDetail:function (id) {
                    var self = this;
                    this.$router.go('/product/detail?id=' + id);
                },
        }
    };
    app.orderList = {
        template: '#orderList',
        route:{
            data: function (transition) {
                var self = this;
                self.getOrderStatusList();
                self.getPayTypeList();
                self.$router.go("/managers/content/orderList");
            }
        },
        create:function () {
            var self = this;
            self.getOrderStatusList();
            self.getPayTypeList();
        },
        data: function () {
            return {
                loginORregister: true,
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                user: {
                    userName: "",
                    password: "",
                },
                orderList:[],
                query:{
                    id:"",
                    status:"",
                    payType:"",
                    created:"",
                },
                statusList:[],
                payTypeList:[]
            }
        },
        created: function () {
            this.getUserInfo();
            this.getOrderList();
        },
        methods: {
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    this.$router.go('/managers/login');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getOrderStatusList: function () {
                var self = this;
                IndexApi.getOrderStatusList(function (data) {
                    if(data.value){
                        self.statusList = data.content;
                    } else{
                        alert("获取订单状态列表失败");
                    }

                }.bind(this));
            },
            getPayTypeList: function () {
                var self = this;
                IndexApi.getOrderStatusList(function (data) {
                    if(data.value){
                        self.payTypeList = data.content;
                    } else{
                        alert("获取支付类型失败");
                    }

                }.bind(this));
            },
            getOrderList: function () {
                var self = this;
                IndexApi.getOrderList(self.query, function (data) {
                    if(data.value){
                        self.orderList = data.content;
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            searchOrderByConditions:function(){
                var self = this;
                IndexApi.searchOrderByConditions(self.query, function (data) {
                    if(data.value){
                        self.orderList = data.content;
                    } else{
                        alert(data.message);
                    }

                }.bind(this));
            }
        }
    };
    app.registerMethod = {
        template: '#register',
        data: function () {
            return {
                user: {
                    userName: "",
                    password: "",
                    rePassword: "",
                    email: "",
                    nickName: "",
                    phone: "",
                    question:"",
                    answer:""
                }
            }
        },
        methods: {
            register: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("用户名不能为空！");
                    return;
                }
                if (self.user.password == '' || self.user.password == null) {
                    alert("密码不能为空！");
                    return;
                }
                if (self.user.password != self.user.rePassword) {
                    alert("两次输入的密码不一致！");
                    return;
                }
                if (self.user.email == '' || self.user.email == null) {
                    alert("邮箱不能为空！");
                    return;
                }
                if (self.user.question == '' || self.user.question == null) {
                    alert("密保问题不能为空！");
                    return;
                }
                if (self.user.answer == '' || self.user.answer == null) {
                    alert("答案不能为空！");
                    return;
                }
                IndexApi.registerAction(self.user, function (data) {
                    
                    if (data.value) {
                        alert("注册成功");
                        this.$router.go("/login");
                    } else {
                        this.$router.go("/register");
                    }
                }.bind(this));
            }
        }
    };
    app.searchByKeyword = {
        template: '#list',
        route: {
            data: function (transition) {
                var self = this;
                var q = self.$route.query;
                self.query.keyword = q.keyword;
                this.getUserInfo();
                this.getMyCartCount();
                this.searchByKeyword();
            }
        },
        data: function () {
            return {
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                query: {
                    pageNum: 1,
                    pageSize: 10,
                    categoryId: null,
                    orderBy: null,
                    keyword: null
                },
                user: {
                    userName: "",
                    password: "",
                },
                productList: []
            }
        },

        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toRegister: function () {
                this.$router.go('/register');
                self.loginORregister = false;
                this.navShow = false;
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    this.$router.go('/manager');
                }.bind(this));
            },
            managerToLogin: function () {
                this.$router.go('/managers/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    
                    if (data.value) {
                        self.myCartCount = data.data;
                    } else {
                        self.myCartCount = 0;
                    }
                }.bind(this));
            },
            toMyAccount: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/account");
                }
            },
            searchByKeyword: function () {
                var self = this;
                IndexApi.searchByKeyword(self.query, function (data) {
                    
                    self.productList = data.content || [];
                }.bind(this));
            },
            searchByOrderBy: function (orderBy) {
                var self = this;
                self.query.orderBy = orderBy;
                IndexApi.searchByKeyword(self.query, function (data) {
                    
                    self.productList = data.content;
                }.bind(this));
            },
            toGetProductDetail: function (id) {
                var self = this;
                this.$router.go('/product/detail?id=' + id);
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == '' || self.user.userName == null) {
                    alert("请先登录");
                    self.$router.go("/");
                } else {
                    self.$router.go("/myCart");
                }
            },
        },
    };
    app.productDetail = {
        template: '#detail',
        route: {
            data: function (transition) {
                var self = this;
                var q = self.$route.query;
                self.query.id = q.id;
                this.getUserInfo();
                this.getMyCartCount();
                this.getProductDetail();
            }
        },
        data: function () {
            return {
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                user: {
                    userName: "",
                    password: "",
                },
                query: {
                    id: "",
                    count: 1,
                },
                productDetail: {}
            }
        },

        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    this.$router.go('/');
                }.bind(this));
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    
                    if (data.value) {
                        self.myCartCount = data.data;
                    } else {
                        self.myCartCount = 0;
                    }
                }.bind(this));
            },
            getProductDetail: function () {
                var self = this;
                IndexApi.getProductDetail(self.query, function (data) {
                    
                    self.productDetail = data.data;
                }.bind(this));
            },
            toAddCart: function (count, id, stock) {
                var self = this;
                if (self.user.userName == "" || null == self.user.userName) {
                    alert("用户未登录，请先登录");
                } else {
                    if (stock == 0) {
                        return;
                    } else {
                        self.$router.go('/cart/add?count=' + count + "&id=" + id);
                    }
                }
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == "" || null == self.user.userName) {
                    alert("用户未登录，请先登录");
                } else {
                    self.$router.go('/cart/list');
                }
            },
            add: function (count, stock) {
                var self = this;
                if (self.query.count < stock) {
                    self.query.count = count + 1;
                } else {
                    self.query.count = stock;
                }
            },
            min: function (count) {
                var self = this;
                if (self.query.count > 1) {
                    self.query.count = count - 1;
                } else {
                    self.query.count = 0;
                }
            }
        }
    };
    app.cart = {
        template: '#cart',
        route: {
            data: function (transition) {
                var self = this;
                var q = self.$route.query;
                self.query.id = q.id;
                self.query.count = q.count;
                self.getUserInfo();
                self.getMyCartCount();
                self.addCart();
                self.getTotalPrice();
            }
        },
        data: function () {
            return {
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                totalPrice:0,
                user: {
                    userName: "",
                    password: "",
                },
                query: {
                    productId: null,
                    count: null,
                },
                cartList: []
            }
        },

        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toIndex: function () {
                this.$router.go('/');
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    this.$router.go('/');
                }.bind(this));
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    self.myCartCount = data.data;
                }.bind(this));
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == "" || null == self.user.userName) {
                    alert("用户未登录，请先登录");
                } else {
                    self.$router.go('/cart/list');
                }
            },
            addCart: function () {
                var self = this;
                IndexApi.addCart(self.query, function (data) {
                    self.cartList = data.content || [];
                    self.getMyCartCount();
                    self.getTotalPrice();
                }.bind(this));
            },
            addCount: function (productId, count) {
                var self = this;
                self.query.count = count + 1;
                self.query.productId = productId;
                IndexApi.updateCartCount(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getTotalPrice();
                        self.getMyCartCount();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            minusCount: function (productId, count) {
                var self = this;
                self.query.count = count - 1;
                self.query.productId = productId;
                IndexApi.updateCartCount(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getTotalPrice();
                        self.getMyCartCount();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            deleteOne: function (productId) {
                var self = this;
                self.query.productId = productId;
                IndexApi.deleteOne(self.query, function (data) {
                    if(data.value){
                        self.cartList = data.content;
                        self.getMyCartCount();
                        self.getTotalPrice();
                    } else{
                        alert(data.message);
                    }
                }.bind(this));
            },
            deleteAll: function () {
                var self = this;
                var productIds = "";
                for (var i = 0; i < self.cartList.length; i++) {
                    productIds += self.cartList[i].productId;
                    productIds += ",";
                }
                IndexApi.deleteProduct(productIds, function (data) {
                    self.cartList = data.content;
                    self.getMyCartCount();
                    self.getTotalPrice();
                }.bind(this));
            },
            getTotalPrice:function () {
                var self = this;
                IndexApi.getTotalPrice(function (data) {
                    self.totalPrice = data.data;
                }.bind(this));
            },
            toProductDetail: function (productId) {
                var self = this;
                this.$router.go('/product/detail?id=' + productId);
            },
            toSettle: function (addressId) {
                var self = this;
                this.$router.go('/order/create');
            },
        }
    };
    app.order = {
        template: '#order',
        route: {
            data: function (transition) {
                var self = this;
                var q = self.$route.query;
                self.query.id = q.id;
                self.query.count = q.count;
                self.getUserInfo();
                self.getMyCartCount();
                self.createOrder();
                self.getOrderPrice();
            }
        },
        data: function () {
            return {
                navShow: true,
                isLogined: true,
                myCartCount: 0,
                orderPrice:0,
                user: {
                    userName: "",
                    password: "",
                },
                query: {
                    productId: null,
                    count: null,
                },
                orderVo: {
                    orderNo: null,
                    orderDetailVoList: [],
                    addressVo: {},
                },
                cartList: [],
                addressVo: {},
                orderNo:null,
            }
        },

        methods: {
            toLogin: function () {
                this.$router.go('/login');
                this.loginORregister = false;
                this.navShow = false;
            },
            toIndex: function () {
                this.$router.go('/');
            },
            loginOut: function () {
                var self = this;
                IndexApi.loginOut(function (data) {
                    self.getUserInfo();
                    self.getMyCartCount();
                    self.toIndex();
                }.bind(this));
            },
            getUserInfo: function () {
                var self = this;
                IndexApi.getUserInfo(function (data) {
                    
                    if (data.value) {
                        self.user = data.data;
                        self.isLogined = false;
                    } else {
                        self.isLogined = true;
                    }
                }.bind(this));
            },
            getMyCartCount: function () {
                var self = this;
                IndexApi.getMyCartCount(function (data) {
                    
                    self.myCartCount = data.data;
                }.bind(this));
            },
            toMyCart: function () {
                var self = this;
                if (self.user.userName == "" || null == self.user.userName) {
                    alert("用户未登录，请先登录");
                } else {
                    self.$router.go('/cart/list');
                }
            },
            toProductDetail: function (productId) {
                this.$router.go('/product/detail?id=' + productId);
            },
            createOrder: function (addressId) {
                var self = this;
                var addressId = 0;
                IndexApi.createOrder(addressId, function (data) {
                    self.orderVo = data.data;
                    self.cartList = data.content;
                    self.addressVo = self.orderVo.addressVo;
                    self.orderNo = self.orderVo.orderNo;
                }.bind(this));
            },
            getOrderPrice:function () {
                var self = this;
                IndexApi.getOrderPrice(self.orderVo.orderNo, function (data) {
                    self.orderPrice = data.data;
                }.bind(this));
            },
            toPay:function () {
                var self = this;
                this.$router.go("/result");
            },
        }
    };
    app.result = {
        template: '#result',
    },
    routerInit(app);
};

components();