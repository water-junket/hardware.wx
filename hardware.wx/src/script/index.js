/**
 * New node file
 */

var goodsList={};

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
	goods: avalon.mix({quantity: 0},goods),
	img: [],
	buy: [],
	loadCategory: function(p){//读取类别
		vm.load(apiDomain+"goods/category.json",{parent: p},function(list){
			if(p==-1) vm.category1=list;
			else vm.category2=list;
		});
	},
	loadGoods: function(c,i){
		vm.load(apiDomain+"goods/list.json",{category: c, page: i},function(data){
			data.list.forEach(function(o,i){
				vm.goodsList.push(o);
				goodsList[o.id]=avalon.mix(o,goodsList[o.id]||{});
			});
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
	vm.showFooter=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(window.screen.availHeight/window.screen.availWidth>=1.5);
}

window.addEventListener('resize', check);
window.addEventListener('blur', check, true);
window.addEventListener('focus', check, true);

function loadGoods(i,cb){
	if(goodsList[i]){
		vm.goods=goodsList[i];
		if(cb) cb(vm.goods);
	}
	else vm.load(apiDomain+"goods/detail.json",{id: i},function(data){
		goodsList[i]=vm.goods=data;
		if(cb) cb(vm.goods);
	});
}

function toggle(){
	vm.tab=this.path.substring(1);
	if(vm.tab=='index'){
		vm.loadCategory(-1);
	}else if(vm.tab=="list"){
		vm.goodsList=[];
		vm.categoryCur=this.query.id;
		vm.loadGoods(vm.categoryCur, 1);
	}else if(vm.tab=="detail"){
		vm.load(apiDomain+"img/list.json",{gid: this.query.id},function(data){
			vm.img=data.normal;
			var mySwiper = new Swiper(".swiper-container",{
				autoplay: 5000,
				observer: true,
				pagination: '.swiper-pagination',
				autoplayDisableOnInteraction: false,
			});
		});
		loadGoods(this.query.id);
	}else if(vm.tab=="buy"){
		loadGoods(this.query.id, function(g){
			g.quantity++;
			goodsList[g.id]=g;
			if(vm.buy.indexOf(g)<0) vm.buy.push(g);
		});
	}
}

require(["domReady!", "mmRequest", "mmRouter"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};

	avalon.router.get('/index', toggle);
	avalon.router.get('/list', toggle);
	avalon.router.get('/detail', toggle);
	avalon.router.get('/buy', toggle);

	avalon.history.start({});
});