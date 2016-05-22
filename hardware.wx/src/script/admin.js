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
	category1Cur: -1,
	category2Cur: -1,
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
			}
		},'text');
	},
	loadCategory: function(p){
		vm.load(apiDomain+"admin/category.json",{"m.id": vm.userId, "m.token": vm.token, parent: p},function(list){
			if(p==-1) vm.category1=list;
			else vm.category2=list;
		});
	},
	load: function(url,data,fun,type){}
});

require(["domReady!", "mmRequest"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};
});