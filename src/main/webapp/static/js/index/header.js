// new Vue({
//     el:"#header",
//     data:function () {
//         return {
//             query:{
//                 keyword:null,
//                 orderBy:null,
//                 pageNum:1,
//                 pageSize:10,
//                 categoryId:null,
//             }
//         }
//     },
//     methods: {
//         search: function () {
//             var self = this;
//             self.$http.get("/product/list",{params:self.query}).then(function (response) {
//                 var res = response.data;
//                 if(res.value){
//                     console.log("搜索商品成功");
//                 } else {
//                     alert("搜索商品失败！");
//                 }
//             },function (response) {
//                 alert("请求失败！");
//             });
//         },
//     }
// });