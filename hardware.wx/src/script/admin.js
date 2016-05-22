/**
 * New node file
 */

var vm = avalon.define({
	$id: "hardware",
	userName: "",
	pw: "",
	token: "",
	tab: "",
	userId: 0,
	$computed: {
	},
	category1: [],
	category2: [],
	category1New: "",
	category2New: "",
	category1Cur: {},
	category2Cur: {},
	goodsList: [],
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
	loadCategory: function(p){
		vm.load(apiDomain+"admin/category.json",{"m.id": vm.userId, "m.token": vm.token, parent: p},function(list){
			if(p==-1) vm.category1=list;
			else vm.category2=list;
		});
	},
	cat: function(el){
		if(el.parent==-1){
			vm.category1Cur=el;
			vm.loadCategory(el.id);
		}else{
			vm.category2Cur=el;
			vm.loadGoods(el.id);
		}
	},
	loadGoods: function(c2){
		vm.load(apiDomain+"admin/goods.json",{"m.id": vm.userId, "m.token": vm.token, category: c2},function(list){
			vm.goodsList=list;
		});
	},
	load: function(url,data,fun,type){}
});

require(["domReady!", "mmRequest"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};
});