/**
 * New node file
 */

var vm = avalon.define({
	$id: "hardware",
	openid: "",
	showFooter:true,
	input: function(){
		vm.showFooter=(document.activeElement.tagName.toLowerCase()!="input")&&(document.activeElement.tagName.toLowerCase()!="textarea")&&(window.screen.availHeight/window.screen.availWidth>=1.5);
	}
});

window.addEventListener('onreize', vm.input);
