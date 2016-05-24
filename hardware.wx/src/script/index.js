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
	load: function(url,data,fun,type){}
});

function check(){
	vm.showFooter=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(window.screen.availHeight/window.screen.availWidth>=1.5);
}

window.addEventListener('resize', check);
window.addEventListener('blur', check, true);
window.addEventListener('focus', check, true);

function toggle(){
	vm.tab=this.path.substring(1);
	if(vm.tab=='index'){
		
	}else if(vm.tab=="list"){
		
	}else if(vm.tab=="detail"){
		
	}
}

require(["domReady!", "mmRequest", "mmRouter"], function() {
	vm.load=function(url,data,fun,type){
		avalon.post(url,data,fun,type||'json');
	};

	avalon.router.get('/index', toggle);
	avalon.router.get('/list/:id', toggle);
	avalon.router.get('/detail/:id', toggle);

	avalon.history.start({});
});