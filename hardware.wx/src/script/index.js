/**
 * New node file
 */

var goodsList={};

var vm = avalon.define({
	$id: "hardware",
	showFooter: true,//是否显示footer
	noInputing: true,//是否在输入
	tab: 'index',//当前页
	category1: [],//1级类别列表
	category2: [],//2级类别列表
	category2Show: false,//是否显示2级类别列表
	categoryCur: 0,//当前列表id
	goodsList: [],//商品列表
	subList: [],//颜色分类列表
	maskShow: false,//是否显示遮罩层
	subCur: subGoods,//当前颜色分类
	count: 1,//当前颜色分类数量
	goodsPages: 0,//商品总页码
	goodsPagesCur: 0,//当前商品页码
	goods: avalon.mix({quantity: 0},goods),//当前商品
	img: [],//图片列表
	goodsTab: 'buy',//商品页内的当前页
	receiverTab: 'old',//
	ob: 'name asc',
	cart: [],
	receiverList: [],
	receiver: avalon.mix({},receiver),
	user: {name: '', id: 0, token: '', point: 0, tel: ''},
//	user: {name: '韦畋君', id: 1, token: 'odhGWs5l6Xahi7XXf7Mh7wNYy-ZE', point: 0, tel: '18948754193'},
	payMethod: 5,
	note: '',
	orderId: '',//成功页面用订单编号
	orderStatus: 0,//成功页面用订单状态
	search: '',
	auth: auth,
	beforeAuth: '!/index',
	orderList: [],//订单列表
	orderShow: order,//展示订单
	orderDetail: [],//展示订单的详情
	orderPages: 0,//订单总页码
	orderPageCur: 0,//当前订单页码
	discountList: [],//满减列表
	discountCur: discount,//当前满减
	$computed: {
		total:{
			get: function(){
				var price=this.cart.reduce(function(p,c){
					p[c.act?0:1]+=c.price*c.count;
					return p;
				},[0,0]);
				for(var i=this.discountList.length-1;i>=0;i--){
					if(price[0]>=this.discountList[i].line){
						this.discountCur=this.discountList[i];
						break;
					}
					if(i==0) this.discountCur=discount;
				}
				return price[0]+price[1]-this.discountCur.reduce;
			}
		},
	},
	loadGoods: function(i){
		if(vm.tab=="list") vm.load(apiDomain+"goods/search.json",{category: vm.categoryCur, page: i, name: vm.search, ob: vm.ob},function(data){
			if(i==1) vm.goodsList=data.list;
			else data.list.forEach(function(o,i){
				vm.goodsList.push(o);
			});
			vm.goodsPages=data.pages;
			vm.goodsPageCur=i;
			if(i<data.pages) window.onscroll=scrollLoad;
		});
	},
	setOb: function(ob){
		vm.ob=ob;
		vm.loadGoods(1);
	},
	showSub: function(){
		vm.subCur=subGoods;
		vm.count=1;
	},
	showOrder: function(i){
		vm.orderShow=i;
		var detail=i.detail.split("||");
		vm.orderDetail=detail.map(function(s,i){
			var i=s.split(",,");
			return i[3]+"-"+i[4]+" x "+i[5];
		});
	},
	setSubCur: function(s){
		vm.subCur=avalon.mix({},s);
	},
	addCount: function(i){
		var count=vm.count+i;
		if(count>0) vm.count=count;
	},
	toggleMaskShow: function(){
		vm.maskShow=!vm.maskShow;
	},
	popupCat: function(){
		vm.category2Show=!vm.category2Show;
	},
	imgSrc: function(i,type){
		return i==0?"":apiDomain+"img/download.img?id="+i+(type?"&type="+type:"");
	},
	toggleGoods: function(t){
		vm.goodsTab = t;
	},
	toggleReceiver: function(t){
		vm.receiverTab=vm.receiverList.size()==0?"new":t;
	},
	quantity: function(el,q){
		var count=el.count+q;
		if(count>0) el.count=count;
	},
	removeGoods: function(i,id){
		vm.cart.splice(i,1);
	},
	order: function(){
		var total=vm.cart.map(function(i){
			return [i.id,i.subId,i.price,i.name,i.subName,i.count].join(",,");
		}).join("||");
		vm.load(apiDomain+"order/add.json",{detail: total, note: vm.note, price: vm.total, payMethod: vm.payMethod, "u.id": vm.user.id, "u.tel": vm.user.tel, "u.token": vm.user.token, "r.id": vm.receiver.id, "r.name": vm.receiver.name, "r.address": vm.receiver.address, "r.tel": vm.receiver.tel},function(data){
			if(data.result=='fail'){
				alert("订单提交失败，请稍后再试！");
			}else{
				vm.receiver = avalon.mix({},receiver);
				vm.note = '';
				vm.cart = [];
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
		vm.load(apiDomain+"rec/remove.json",{rid: i, id: vm.user.id, tel: vm.user.tel, token: vm.user.token},function(flag){
			loadReceiver();
		},'text');
	},
	login: function(){
		if(!(/^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\d{8}$/.test(vm.auth.tel))) alert("请输入正确的手机号");
		else if(!(/^\w{6,16}$/.test(vm.auth.pw))) alert("请输入6-16位数字字母或下划线的密码");
		else vm.load(apiDomain+"wx/login.json", vm.auth, function(data){
			if(data.id==0) alert("登陆失败");
			else{
				vm.user = data;
				localStorage.setItem("userinfo", JSON.stringify(data));
				location.hash = vm.beforeAuth;
			}
		});
	},
	register: function(){
		if(!(/^\S+$/.test(vm.auth.tel))) alert("请输入昵称");
		else if(!(/^1(3[0-9]|4[57]|5[0-35-9]|7[01678]|8[0-9])\d{8}$/.test(vm.auth.tel))) alert("请输入正确的手机号");
		else if(!(/^\w{6,16}$/.test(vm.auth.pw))) alert("请输入6-16位数字字母或下划线的密码");
		else if(vm.auth.pw!=vm.auth.repw) alert("2次密码输入不一致");
		else vm.load(apiDomain+"wx/register.json", vm.auth, function(data){
			if(data.id==0) alert("注册失败");
			else{
				vm.user = data;
				localStorage.setItem("userinfo", JSON.stringify(data));
				location.hash = vm.beforeAuth;
			}
		});
	},
	editpw: function(){
		if(!(/^\w{6,16}$/.test(vm.auth.pw))) alert("请输入6-16位数字字母或下划线的密码");
		else if(vm.auth.pw!=vm.auth.repw) alert("2次密码输入不一致");
		else vm.load(apiDomain+"wx/editpw.json", {token: vm.user.token, id: vm.user.id, pw: vm.auth.pw, tel: vm.user.tel}, function(data){
			if(!data) alert("修改失败");
			else location.hash = vm.beforeAuth;
		});
	},
	userinfo: function(){
		if(vm.user.id==0){
			vm.beforeAuth="!/order";
			location.hash="!/login";
		}else location.hash="!/order";
	},
	gotoCart: function(){
		if(vm.user.id==0){
			vm.beforeAuth="!/buy?id="+vm.goods.id+"&sub="+vm.subCur.id+"&count="+vm.count;
			location.hash="!/login";
		}else location.hash="!/buy?id="+vm.goods.id+"&sub="+vm.subCur.id+"&count="+vm.count;
	},
	loadOrder: function(i){
		vm.load(apiDomain+"order/list.json",{token: vm.user.token, id: vm.user.id, tel: vm.user.tel, page: i},function(data){
			vm.orderList=data.list;
			vm.orderPages=data.pages;
			vm.orderPageCur=i;
		});
	},
	load: function(url,data,fun,type){}
});

vm.$watch('receiverTab',function(a,b){
	if(a=='new' && b=='old') vm.receiver=avalon.mix({},receiver);
});

function check(){
	vm.noInputing=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(window.screen.availHeight/window.screen.availWidth>=1.5);
}

window.addEventListener('resize', check);
window.addEventListener('blur', check, true);
window.addEventListener('focus', check, true);

function loadCategory(p){//读取类别，true1级/false2级
	vm.load(apiDomain+"goods/category.json",{parent: p},function(list){
		if(p==-1) vm.category1=list;
		else vm.category2=list;
	});
}

function loadGoods(i,cb){
	if(goodsList[i]){
		vm.goods=goodsList[i];
		vm.subList=goodsList[i].sub;
		if(cb) cb(goodsList[i]);
	}
	else vm.load(apiDomain+"goods/detail.json",{id: i},function(data){
		goodsList[i]=data;
		vm.load(apiDomain+"goods/listSub.json",{parent: i},function(data){
			vm.goods=goodsList[i];
			vm.subList=goodsList[i].sub=data;
			if(cb) cb(goodsList[i]);
		});
	});
}

function loadReceiver(){
	vm.load(apiDomain+"rec/list.json", {id: vm.user.id, tel:vm.user.tel, token: vm.user.token}, function(list){
		vm.receiverList = list;
		vm.receiverTab=vm.receiverList.size()==0?"new":"old";
	});
}

function scrollLoad(){
	window.onscroll=null;
	return setTimeout(function(){
		if(document.body.clientHeight+62<100+document.body.scrollTop+window.screen.height) vm.loadGoods(vm.goodsPageCur+1);
		else window.onscroll=scrollLoad;
	},100);
}

function toggle(){
	vm.tab=this.path.substring(1);
	vm.showFooter=true;
	window.onscroll=null;
	if(vm.tab=='index'){
		loadCategory(-1);
	}else if(vm.tab=="list"){
		vm.goodsList=[];
		vm.categoryCur=this.query.id;
		loadCategory(this.query.list);
		vm.search='';
		vm.loadGoods(1);
	}else if(vm.tab=="detail"){
		vm.load(apiDomain+"img/list.json",{gid: this.query.id},function(data){
			vm.img=data;
			var mySwiper = new Swiper(".swiper-container",{
				autoplay: 5000,
				observer: true,
				pagination: '.swiper-pagination',
				autoplayDisableOnInteraction: false,
			});
		});
		loadGoods(this.query.id);
		vm.maskShow=false;
		vm.subCur=subGoods;
	}else if(vm.tab=="buy"){
		var query=this.query;
		var s=vm.cart.query("subId",query.sub);
		if(s){
			s.count+=parseInt(query.count,10);
		}else loadGoods(query.id, function(g){
			var sub=g.sub.query("id",query.sub);
			vm.cart.push({name: g.name, subName: sub.name, id: g.id, subId: sub.id, price: sub.price, count: parseInt(query.count,10), act: g.act});
		});
		loadReceiver();
		vm.load(apiDomain+"order/discount.json",function(data){
			vm.discountList=data;
		});
	}else if(vm.tab=="order"){
		vm.loadOrder(1);
	}else if(vm.tab=="login"){
		vm.auth = auth;
		vm.showFooter = false;
	}else if(vm.tab=="register"){
		vm.auth = auth;
		vm.showFooter = false;
	}else if(vm.tab=="editpw"){
		vm.auth = auth;
		vm.showFooter = false;
	}
}

require(["domReady!", "mmRequest", "mmRouter"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};

	var user=localStorage.getItem("userinfo");
	if(user) vm.user=JSON.parse(user);
//	avalon.post(apiDomain+"wx/login.json"+location.search,function(u){
//		vm.user = u;
//	});

	avalon.router.get('/index', toggle);
	avalon.router.get('/list', toggle);
	avalon.router.get('/detail', toggle);
	avalon.router.get('/buy', toggle);
	avalon.router.get('/success', toggle);
	avalon.router.get('/order', toggle);
	avalon.router.get('/login', toggle);
	avalon.router.get('/register', toggle);
	avalon.router.get('/editpw', toggle);

	avalon.history.start({});
});