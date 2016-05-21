/**
 * New node file
 */

var vm = avalon.define({
	$id: "hardware",
	userName: "",
	pw: "",
	token: "",
	login: function(){}
});

require(["domReady!", "mmRequest"], function() {
	vm.login=function(){
		avalon.post();
	};
});