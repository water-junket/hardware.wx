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
	userPageCur: 1,
	userPages: 1,
	userId: 0,
	curUser: 0,
	curDiscount: 0,
	pointReduce: 0,
	category1: [],
	category2: [],
	category2Temp: [],
	category1New: "",
	category2New: "",
	category1Cur: {},
	category2Cur: {},
	imgs: [],
	hasTitle: false,
	goodsList: [],
	userList: [],
	subList: [],
	discountList: [],
	goods: goods,
	subGoods: subGoods,
	discount: discount,
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
		}else if(tab=='user'){
			loadUser(1);
		}else if(tab=='discount'){
			loadDiscount();
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
	statusSub: function(c){//修改颜色分类状态
		vm.load(apiDomain+"admin/statusSub.json",{"m.id": vm.userId, "m.token": vm.token, "s.id": c.id, "s.gid": vm.goods.id},function(flag){
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
		}else if(tab=='sublist'){
			if(el) vm.goods=avalon.mix({},goods,el);
			loadSubList();
		}else if(tab=='subadd'){
			vm.subGoods=avalon.mix({},subGoods,{gid: vm.goods.id});
		}else if(tab=='subedit'){
			vm.subGoods=avalon.mix({},el);
		}
	},
	goodsCategory: function(a){//商品选择大类
		vm.loadCategory(vm.goods.category1,true);
	},
	saveGoods: function(){//保存商品：添加或修改
		var url=vm.goods.id==0?apiDomain+"admin/addGoods.json":apiDomain+"admin/editGoods.json";
		var param={"m.id": vm.userId, "m.token": vm.token, "g.name": vm.goods.name, "g.category1": vm.goods.category1, "g.category2": vm.goods.category2, "g.info": vm.goods.info, "g.act": vm.goods.act};
		if(vm.goods.id!=0) avalon.mix(param,{"g.id": vm.goods.id});
		vm.load(url,param,function(flag){
			if(eval(flag)){
				alert("提交成功");
				vm.goodsToggle('list');
			}
		},'text');
	},
	saveSub: function(){//保存商品颜色分类：添加或修改
		var url=vm.subGoods.id==0?apiDomain+"admin/addSub.json":apiDomain+"admin/editSub.json";
		var param={"m.id": vm.userId, "m.token": vm.token, "s.name": vm.subGoods.name, "s.price": vm.subGoods.price, "s.gid": vm.goods.id};
		if(vm.goods.id!=0) avalon.mix(param,{"s.id": vm.subGoods.id});
		vm.load(url,param,function(flag){
			if(eval(flag)){
				alert("提交成功");
				vm.goodsToggle('sublist');
			}
		},'text');
	},
	pageGoods: function(i){//商品列表翻页
		loadGoods(vm.category2Cur.id, i);
	},
	pageUser: function(i){//用户翻页
		loadUser(i);
	},
	loadImg: function(){
		vm.load(apiDomain+"img/list.json",{"gid": vm.goods.id},function(data){
			vm.imgs=data;
			vm.hasTitle=data.some(function(i){
				return i.type==1;
			});
		});
	},
	uploadImg: function(f){},
	uploadImg1: function(f){},
	removeImg: function(img){
		vm.load(apiDomain+"img/remove.json",{"m.id": vm.userId, "m.token": vm.token, iid: img.id, aname: img.actualName},function(flag){
			if(eval(flag)) vm.loadImg();
		},'text');
	},
	removeImg1: function(sub){
		vm.load(apiDomain+"img/remove1.json",{"m.id": vm.userId, "m.token": vm.token, sid: sub},function(flag){
			if(eval(flag)) loadSubList();
		},'text');
	},
	imgSrc: function(i,n){
		return apiDomain+"img/download.img?id="+i+(n?"&type="+n:"");
	},
	orderSearch: function(i){
		vm.load(apiDomain+"admin/listOrder.json",{"m.id": vm.userId, "m.token": vm.token, type: vm.otype, oid: vm.oid, payMethod: vm.payMethod, begin: vm.begin, end: vm.end, page: i},function(data){
			vm.orderList=data.list;
			vm.orderPages=data.pages;
			vm.orderPageCur=i;
		});
	},
	chooseUser: function(id){
		vm.curUser = id;
		vm.pointReduce = 0;
	},
	minusPoint: function(i,p){
		if(vm.pointReduce==0 || vm.pointReduce>p) alert("请输入有效数字");
		else vm.load(apiDomain+"admin/minusPoint.json",{"m.id": vm.userId, "m.token": vm.token, uid: i, point: vm.pointReduce},function(flag){
			if(eval(flag)) loadUser(vm.userPageCur);
			vm.curUser=0;
		},'text');
	},
	addDiscount: function(){
		vm.load(apiDomain+"admin/addDiscount.json",{"m.id": vm.userId, "m.token": vm.token, "d.line": vm.discount.line, "d.reduce": vm.discount.reduce},function(flag){
			if(eval(flag)) loadDiscount();
			vm.discount=discount;
		},'text');
	},
	chooseDiscount: function(id){
		vm.curDiscount = id;
	},
	removeDiscount: function(id){
		vm.load(apiDomain+"admin/removeDiscount.json",{"m.id": vm.userId, "m.token": vm.token, did: id},function(flag){
			if(eval(flag)) loadDiscount();
		},'text');
	},
	saveDiscount: function(d){
		vm.load(apiDomain+"admin/editDiscount.json",{"m.id": vm.userId, "m.token": vm.token, "d.line": d.line, "d.reduce": d.reduce, "d.id": d.id},function(flag){
			if(eval(flag)){
				loadDiscount();
				vm.curDiscount = 0;
			}
		},'text');
	},
	resetPw: function(u){
		vm.load(apiDomain+"admin/resetPw.json",{"m.id": vm.userId, "m.token": vm.token, uid: u.id, tel: u.tel},function(flag){
			if(eval(flag)) alert("已重置");
		},'text');
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

function loadUser(i){//读取用户列表
	vm.load(apiDomain+"admin/listUser.json",{"m.id": vm.userId, "m.token": vm.token, page: i},function(data){
		vm.userList=data.list;
		vm.userPages=data.pages;
		vm.userPageCur=i;
	});
}

function loadSubList(){
	vm.load(apiDomain+"admin/listSub.json",{"m.id": vm.userId, "m.token": vm.token, gid: vm.goods.id},function(data){
		vm.subList=data;
	});
}

function loadDiscount(){
	vm.load(apiDomain+"admin/listDiscount.json",{"m.id": vm.userId, "m.token": vm.token},function(data){
		vm.discountList=data;
	});
}

require(["domReady!", "mmRequest"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};
	vm.uploadImg=function(f){
		var fd=new FormData(document.forms.namedItem(f));
		avalon.upload(apiDomain+"img/upload.json",fd,{"m.id": vm.userId, "m.token": vm.token, gid: vm.goods.id},function(flag){
			if(eval(flag)) vm.loadImg();
		},'text');
	};
	vm.uploadImg1=function(f){
		var fd=new FormData(document.forms.namedItem("sub"+f));
		avalon.upload(apiDomain+"img/upload1.json",fd,{"m.id": vm.userId, "m.token": vm.token, sid: f},function(flag){
			if(eval(flag)) loadSubList();
		},'text');
	};
});