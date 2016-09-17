/**
 * New node file
 */

var goodsList={};

var vm = avalon.define({
	$id: "hardware",
	showFooter: true,
	noInputing: true,
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
	//	user: {name: '', id: 0, token: '', point: 0, tel: ''},
	user: {name: '韦畋君', id: 1, token: 'odhGWs5l6Xahi7XXf7Mh7wNYy-ZE', point: 0, tel: '18948754193'},
	payMethod: 5,
	note: '',
	orderId: '',
	orderStatus: 0,
	search: '',
	auth: auth,
	beforeAuth: '!/index',
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
		vm.load(apiDomain+"order/add.json",{detail: total, note: vm.note, price: vm.total, payMethod: vm.payMethod, uid: vm.user.id, token: vm.user.token, rid: vm.receiver.id, name: vm.receiver.name, address: vm.receiver.address, tel: vm.receiver.tel},function(data){
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
		vm.load(apiDomain+"rec/remove.json",{rid: i, id: vm.user.id, token: vm.user.token},function(flag){
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
	vm.load(apiDomain+"rec/list.json", {id: vm.user.id, token: vm.user.token}, function(list){
		vm.receiverList = list;
		vm.receiverTab=vm.receiverList.size()==0?"new":"old";
	});
}

function toggle(){
	vm.tab=this.path.substring(1);
	vm.showFooter=true;
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
	}else if(vm.tab=="order"){
		vm.showFooter = false;
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