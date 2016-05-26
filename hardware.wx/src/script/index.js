/**
 * New node file
 */

var vm = avalon.define({
	$id: "hardware",
	openid: "",
	showFooter:true,
	tab: 'index',
	category1: [],
	category2: [],
	categoryCur: 0,
	goodsList: [],
	goodsPages: 0,
	goodsPagesCur: 0,
	loadCategory: function(p){//读取类别
		vm.load(apiDomain+"goods/category.json",{parent: p},function(list){
			if(p==-1) vm.category1=list;
			else vm.category2=list;
		});
	},
	loadGoods: function(c,i){
		vm.load(apiDomain+"goods/list.json",{"m.id": vm.userId, "m.token": vm.token, category: c, page: i},function(data){
			vm.goodsList=vm.goodsList.concat(data.list);
			vm.goodsPages=data.pages;
			vm.goodsPageCur=i;
		});
	},
	imgSrc: function(i){
		return apiDomain+"img/download.img?id="+i;
	},
	load: function(url,data,fun,type){}
});

function check(){
	vm.showFooter=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(document.body.clientHeight/document.body.clientWidth>=1.5);
}

window.addEventListener('resize', check);
window.addEventListener('blur', check, true);
window.addEventListener('focus', check, true);

function toggle(){
	vm.tab=this.path.substring(1);
	if(vm.tab=='index'){
		vm.loadCategory(-1);
	}else if(vm.tab=="list"){
		vm.goodsList=[];
		vm.categoryCur=this.query.id;
		vm.loadGoods(vm.categoryCur, 1);
	}else if(vm.tab=="detail"){
		
	}
}

require(["domReady!", "mmRequest", "mmRouter"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};

	avalon.router.get('/index', toggle);
	avalon.router.get('/list', toggle);
	avalon.router.get('/detail', toggle);

	avalon.history.start({});
});