/**
 * New node file
 */

var goods={id: '',name: '',category1: '',category2: '',price: '',dummyPrice: '',param: '',note: ''};

var vm = avalon.define({
	$id: "hardware",
	userName: "",
	pw: "",
	token: "",
	tab: "",
	goodsTab: "list",
	userId: 0,
	$computed: {
	},
	category1: [],
	category2: [],
	category2Temp: [],
	category1New: "",
	category2New: "",
	category1Cur: {},
	category2Cur: {},
	goodsList: [],
	goods: avalon.mix({},goods),
	login: function(){
		vm.load(apiDomain+"admin/login.json",{"m.name": vm.userName, "m.pw": vm.pw},function(r){
			if(r!="fail"){
				var info=r.split("||");
				vm.token=info[0];
				vm.userId=info[1];
			}
		},'text');
	},
	toggle: function(tab){
		vm.tab = tab;
		if(tab=='goods'){
			vm.loadCategory(-1);
		}
	},
	addCategory: function(t,p){
		vm.load(apiDomain+"admin/addCategory.json",{"m.id": vm.userId, "m.token": vm.token, title: t, parent: p},function(flag){
			if(eval(flag)){
				vm.loadCategory(p);
				if(p==-1)vm.category1New="";
				else vm.category2New="";
			}
		},'text');
	},
	loadCategory: function(p,t){
		vm.load(apiDomain+"admin/category.json",{"m.id": vm.userId, "m.token": vm.token, parent: p},function(list){
			if(p==-1) vm.category1=list;
			else if(!t) vm.category2=list;
			else vm.category2Temp=list;
		},'json');
	},
	editCategory: function(c){
		c.edit=true;
	},
	saveCategory: function(c){
		vm.load(apiDomain+"admin/saveCategory.json",{"m.id": vm.userId, "m.token": vm.token, "o.id": c.id, "o.parent": c.parent, "o.name": c.name},function(flag){
			if(eval(flag)){
				c.edit=false;
			}
		},'text');
	},
	statusCategory: function(c){
		vm.load(apiDomain+"admin/statusCategory.json",{"m.id": vm.userId, "m.token": vm.token, "o.id": c.id, "o.parent": c.parent},function(flag){
			if(eval(flag)){
				c.status=!c.status;
			}
		},'text');
	},
	cat: function(el){
		if(el.parent==-1){
			vm.category1Cur=el;
			vm.loadCategory(el.id);
		}else{
			vm.goodsTab='list';
			vm.category2Cur=el;
			vm.loadGoods(el.id);
		}
	},
	loadGoods: function(c2){
		vm.load(apiDomain+"admin/goods.json",{"m.id": vm.userId, "m.token": vm.token, category: c2},function(list){
			vm.goodsList=list;
		},'json');
	},
	goodsToggle: function(tab){
		vm.goodsTab=tab;
		if(tab=='add'){
			vm.goods=avalon.mix({},goods);
			vm.goods.category1=vm.category1Cur.id;
			vm.goods.category2=vm.category2Cur.id;
			vm.loadCategory(vm.goods.category1,false);
		}
	},
	load: function(url,data,fun,type){}
});

require(["domReady!", "mmRequest"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};
});