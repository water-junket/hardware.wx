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
	goodsTab: 'buy',
	receiverTab: 'old',
	buy: [],
	receiverList: [],
	receiver: avalon.mix({},receiver),
	user: {name: '韦畋君', id: 1, openid: 'odhGWs5l6Xahi7XXf7Mh7wNYy-ZE', point: 0},
	payMethod: 5,
	note: '',
	orderId: '',
	orderStatus: 0,
	search: '',
	$computed: {
		total:{
			get: function(){
				return this.buy.reduce(function(p,c){
					return p+c.price*c.quantity;
				},0);
			}
		}
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
	toggleGoods: function(t){
		vm.goodsTab = t;
	},
	toggleReceiver: function(t){
		vm.receiverTab=vm.receiverList.size()==0?"new":t;
	},
	quantity: function(el,q){
		el.quantity+=q;
		if(!el.quantity) el.quantity=1;
		goodsList[el.id].quantity = el.quantity;
	},
	removeGoods: function(i,id){
		vm.buy.splice(i,1);
		goodsList[id].quantity=0;
	},
	order: function(){
		var total=vm.buy.map(function(i){
			return [i.id,i.name,i.param,i.price,i.quantity].join("@*");
		}).join("@%");
		vm.load(apiDomain+"order/add.json",{detail: total, note: vm.note, price: vm.total, payMethod: vm.payMethod, uid: vm.user.id, openid: vm.user.openid, rid: vm.receiver.id, name: vm.receiver.name, address: vm.receiver.address, tel: vm.receiver.tel},function(data){
			if(data.result=='fail'){
				alert("订单提交失败，请稍后再试！");
			}else{
				vm.receiver = avalon.mix({},r);
				vm.note = '';
				vm.buy = [];
				vm.orderId = data.result;
				location.hash = "#!/success";
			}
		},'text');
	},
	chooseRec: function(r){
		vm.receiver = avalon.mix({},r);
	},
	removeRec: function(i){
		event.stopPropagation();
		if(vm.receiver.id==i) vm.receiver=avalon.mix({},receiver);
		vm.load(apiDomain+"rec/remove.json",{id: i, uid: vm.user.id, openid: vm.user.openid},function(flag){
			loadReceiver();
		},'text');
	},
	load: function(url,data,fun,type){}
});

vm.$watch('receiverTab',function(a,b){
	if(a=='new' && b=='old') vm.receiver=avalon.mix({},receiver);
});

function check(){
	vm.showFooter=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(window.screen.availHeight/window.screen.availWidth>=1.5);
}

window.addEventListener('resize', check);
window.addEventListener('blur', check, true);
window.addEventListener('focus', check, true);

function loadCategory(p){//读取类别
	vm.load(apiDomain+"goods/category.json",{parent: p},function(list){
		if(p==-1) vm.category1=list;
		else vm.category2=list;
	});
}

function loadGoods(i,cb){
	if(goodsList[i]){
		vm.goods=goodsList[i];
		if(cb) cb(goodsList[i]);
	}
	else vm.load(apiDomain+"goods/detail.json",{id: i},function(data){
		vm.goods=goodsList[i]=data;
		if(cb) cb(goodsList[i]);
	});
}

function loadReceiver(){
	vm.load(apiDomain+"rec/list.json", {uid: vm.user.id, openid: vm.user.openid}, function(list){
		vm.receiverList = list;
		vm.receiverTab=vm.receiverList.size()==0?"new":"old";
	});
}

function toggle(){
	vm.tab=this.path.substring(1);
	if(vm.tab=='index'){
		loadCategory(-1);
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
		if(this.query.id) loadGoods(this.query.id, function(g){
			g.quantity++;
			vm.buy = vm.buy.filter(function(i){
				return i.id!=g.id;
			});
			vm.buy.push(g);
		});
		loadReceiver();
	}
}

require(["domReady!", "mmRequest", "mmRouter"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};

//	avalon.post(apiDomain+"wx/login.json"+location.search,function(u){
//		vm.user = u;
//	});

	avalon.router.get('/index', toggle);
	avalon.router.get('/list', toggle);
	avalon.router.get('/detail', toggle);
	avalon.router.get('/buy', toggle);
	avalon.router.get('/success', toggle);

	avalon.history.start({});
});