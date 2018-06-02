// new Vue({
//     el:"#nav",
//     data:function (){
//         return {
//             isLogin:true,
//             myCartCount:0,
//             user:{
//                 userName :"",
//                 password:""
//             }
//         }
//     },
//     created:function () {
//         this.getUserInfo();
//         this.getMyCartCount();
//     },
//     methods: {
//         toLogin:function () {
//             var self = this;
//             self.isLogin = false,
//             window.location.href="/login";
//         },
//         loginOut:function () {
//             var self = this;
//             self.$http.post("/loginOut").then(function (response) {
//                 var res = response.data;
//                 self.isLogin = true;
//                 window.location.href="/";
//             },function (response) {
//                 alert("请求失败！");
//             })
//         },
//         getUserInfo: function () {
//             var self = this;
//             self.$http.post("/user/getUserInfo").then(function (response) {
//                 var res = response.data;
//                 if(res.value){
//                     self.user = res.data;
//                     self.isLogin =false;
//                 } else {
//                     self.isLogin =true;
//                 }
//             },function (response) {
//                 alert("请求失败！");
//             })
//         },
//         getMyCartCount:function () {
//             var self = this;
//             self.$http.get("/cart/getCartProductCount").then(function (response) {
//                 var res = response.data;
//                 if(res.value){
//                     self.myCartCount = res.data;
//                 } else {
//                     self.myCartCount=0;
//                 }
//             },function (response) {
//                 alert("请求失败！");
//             })
//         },
//     }
// });