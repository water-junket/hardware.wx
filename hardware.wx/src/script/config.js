/**
 * New node file
 */
avalon.config({//似乎这里开头用avalon还是用require效果一样
	baseUrl: 'lib'//基础路径
//	paths: {
//		avalon: '../avalon.mobile.min.js'
//	},
//	shim: {
//		avalon: {
//			exports: 'avalon'
//		}
//	},
	,debug: false
});

apiDomain="/";
//apiDomain="http://localhost:8080/";

var goods={id: 0, name: '', category1: 0, category2: 0, price: 0, dummyPrice: 999999, sales: 0, info: '', act: false};
var subGoods={id: 0, name: '', gid: 0, price: 0, img: '', status: 0, sales: 0};
var receiver={id: 0, name: '', address: '', tel: ''};
var auth={name: '', tel: '', pw: '', repw: ''};
var discount={line: 0, reduce: 0, id: 0};
var order={detail: '', note: '', orderTime: null, receive: '', endTime: null, uid: 0};

Array.prototype.query=function(key,value){
	for(var i=0;i<this.length;i++){
		if(this[i][key]==value) return this[i];
	}
}