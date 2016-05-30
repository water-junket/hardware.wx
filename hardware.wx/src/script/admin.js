/**
 * New node file
 */

var vm = avalon.define({
	$id: "hardware",
	userName: "",
	pw: "",
	token: "",
	tab: "",
	goodsTab: "list",
	goodsPageCur: 1,
	goodsPages: 1,
	userId: 0,
	category1: [],
	category2: [],
	category2Temp: [],
	category1New: "",
	category2New: "",
	category1Cur: {},
	category2Cur: {},
	titleImg: {id:0, actualName:'', oname:''},
	normalImg: [],
	goodsList: [],
	goods: avalon.mix({},goods),
	oid: '',
	otype: -1,
	payMethod: -1,
	begin: '',
	end: '',
	orderList: [],
	orderPageCur: 1,
	orderPages: 1,
	$computed: {
	},
	login: function(){//登陆
		vm.load(apiDomain+"admin/login.json",{"m.name": vm.userName, "m.pw": vm.pw},function(r){
			if(r!="fail"){
				var info=r.split("||");
				vm.token=info[0];
				vm.userId=info[1];
			}
		},'text');
	},
	toggle: function(tab){//切换标签页
		vm.tab = tab;
		if(tab=='goods'){
			vm.loadCategory(-1);
		}
	},
	addCategory: function(t,p){//添加类别
		vm.load(apiDomain+"admin/addCategory.json",{"m.id": vm.userId, "m.token": vm.token, title: t, parent: p},function(flag){
			if(eval(flag)){
				vm.loadCategory(p);
				if(p==-1)vm.category1New="";
				else vm.category2New="";
			}
		},'text');
	},
	loadCategory: function(p,t){//读取类别
		vm.load(apiDomain+"admin/category.json",{"m.id": vm.userId, "m.token": vm.token, parent: p},function(list){
			if(p==-1) vm.category1=list;
			else if(!t) vm.category2=list;
			else vm.category2Temp=list;
		});
	},
	editCategory: function(c){//修改类别名称
		c.edit=true;
	},
	saveCategory: function(c){//保存类别名称修改
		vm.load(apiDomain+"admin/saveCategory.json",{"m.id": vm.userId, "m.token": vm.token, "o.id": c.id, "o.parent": c.parent, "o.name": c.name},function(flag){
			if(eval(flag)) c.edit=false;
		},'text');
	},
	statusCategory: function(c){//修改类别状态
		vm.load(apiDomain+"admin/statusCategory.json",{"m.id": vm.userId, "m.token": vm.token, "o.id": c.id, "o.parent": c.parent},function(flag){
			if(eval(flag)) c.status=!c.status;
		},'text');
	},
	cat: function(el){//选择类别
		if(el.parent==-1){
			vm.category1Cur=el;
			vm.loadCategory(el.id);
		}else{
			vm.category2Cur=el;
			vm.goodsToggle('list');
		}
	},
	goodsToggle: function(tab,el){//切换商品页
		vm.goodsTab=tab;
		if(tab=='add'){
			vm.goods=avalon.mix({},goods,{category1: vm.category1Cur.id, category2: vm.category2Cur.id});
			vm.loadCategory(vm.goods.category1,true);
		}else if(tab=='list'){
			loadGoods(vm.category2Cur.id, 1);
		}else if(tab=='edit'){
			vm.goods=avalon.mix({},goods,el);
			vm.loadCategory(vm.goods.category1,true);
		}else if(tab=='img'){
			vm.goods=avalon.mix({},goods,el);
			vm.loadImg();
		}
	},
	goodsCategory: function(a){//商品选择大类
		vm.loadCategory(vm.goods.category1,true);
	},
	saveGoods: function(){//保存商品：添加或修改
		var url=vm.goods.id==0?apiDomain+"admin/addGoods.json":apiDomain+"admin/editGoods.json";
		var param={"m.id": vm.userId, "m.token": vm.token, "g.name": vm.goods.name, "g.price": vm.goods.price, "g.dummyPrice": vm.goods.dummyPrice, "g.category1": vm.goods.category1, "g.category2": vm.goods.category2, "g.param": vm.goods.param, "g.info": vm.goods.info, "g.note": vm.goods.note};
		if(vm.goods.id!=0) avalon.mix(param,{"g.id": vm.goods.id});
		vm.load(url,param,function(flag){
			if(eval(flag)){
				alert("提交成功");
				vm.goodsToggle('list');
			}
		},'text');
	},
	pageGoods: function(i){//商品列表翻页
		loadGoods(vm.category2Cur.id, i);
	},
	statusGoods: function(c){//修改商品状态
		vm.load(apiDomain+"admin/statusGoods.json",{"m.id": vm.userId, "m.token": vm.token, "g.id": c.id},function(flag){
			if(eval(flag)) c.status=!c.status;
		},'text');
	},
	loadImg: function(){
		vm.load(apiDomain+"img/list.json",{"gid": vm.goods.id},function(data){
			if(data.title) vm.titleImg={id: data.title.id, actualName: data.title.actualName, oname: data.title.oname};
			else vm.titleImg={id: 0, actualName: '', oname: ''};
			vm.normalImg=data.normal;
		});
	},
	uploadImg: function(f){},
	removeImg: function(img){
		vm.load(apiDomain+"img/remove.json",{"m.id": vm.userId, "m.token": vm.token, iid: img.id, aname: img.actualName},function(flag){
			if(eval(flag)) vm.loadImg();
		},'text');
	},
	imgSrc: function(i){
		return apiDomain+"img/download.img?id="+i;
	},
	orderSearch: function(i){
		vm.load(apiDomain+"admin/listOrder.json",{"m.id": vm.userId, "m.token": vm.token, type: vm.otype, oid: vm.oid, payMethod: vm.payMethod, begin: vm.begin, end: vm.end, page: i},function(data){
			vm.orderList=data.list;
			vm.orderPages=data.pages;
			vm.orderPageCur=i;
		});
	},
	load: function(url,data,fun,type){}
});

function loadGoods(c2, i){//读取商品列表
	vm.load(apiDomain+"admin/listGoods.json",{"m.id": vm.userId, "m.token": vm.token, category: c2, page: i},function(data){
		vm.goodsList=data.list;
		vm.goodsPages=data.pages;
		vm.goodsPageCur=i;
	});
}

require(["domReady!", "mmRequest"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};
	vm.uploadImg=function(f){
		var fd=new FormData(document.forms.namedItem(f));
		avalon.upload(apiDomain+"img/upload.json",fd,{"m.id": vm.userId, "m.token": vm.token, gid: vm.goods.id},function(){
			vm.loadImg();
		},'text');
	};
});