var BaseDialog = jsloader.resolve("framework.base.wraper.BaseDialog");
var util = imports("framework.base.assist.util").getInstance();
var domutils = jsloader.resolve("framework.base.core.domutils");
var modalWindow = imports("framework.base.wraper.modalWindow").getInstance();

var UpdatePassDialog = function(){
	UpdatePassDialog.superclass.constructor.call(this);
	document.body.onunload = function() {
		if(!dialog.closed) {
			dialog.close(false);
		}			
		destroy();
	};
};
lang.extend(UpdatePassDialog, BaseDialog);

UpdatePassDialog.prototype.init = function(parent,data,fn,obj){
	UpdatePassDialog.superclass.init.call(this,parent,data,fn,obj);
	this.parent = parent;
	this.element = document.createElement("div");
	this.parent.appendChild(this.element);
	
	var template = domutils.doGet("js/urm/base/UpdatePassDialog.html");
	this.element.innerHTML = template;
	
	this.userOldPass = domutils.findElementByClassName(this.parent, "_oldPass");	
	this.userNewPass = domutils.findElementByClassName(this.parent, "_newPass");
	this.userConfirmPass = domutils.findElementByClassName(this.parent, "_confirmPass");
	
	this.btnOk = domutils.findElementByClassName(parent, "_OKButton");	
	this.btnCancel = domutils.findElementByClassName(parent, "_CancelButton");	
	
	this.addListener(this.btnOk, "click", this.doOkClick, this);
	this.addListener(this.btnCancel, "click", this.doCancelClick, this);
	this.node = data;
	
};

UpdatePassDialog.prototype.destroy =  function() {
	if(this.parent)
		domutils.destroyNode(this.element);
	this.element = null;	
	UpdatePassDialog.superclass.destroy.call(this);
};

UpdatePassDialog.prototype.doCancelClick = function(){
	this.closed = true;
	this.close(false);
};

UpdatePassDialog.prototype.doOkClick = function(){	
	var oldPwd = this.userOldPass.value;
	var newPwd = this.userNewPass.value;
	var confirmPwd = this.userConfirmPass.value;
	if(oldPwd==''){
		alert("�����������!");
		return;
	}
	if(newPwd==''){
		alert("������������!");
		return;
	}
	if(confirmPwd==''){
		alert("��ȷ������������!");
		return;
	}
	if(newPwd != confirmPwd){
		alert("��������ȷ�������벻һ��!");
		return;
	}
	var ret = util.remoteInvoke("UserService", "changePassword", [oldPwd, newPwd]);
 	if (ret.succeeded) {
 		alert("�޸�����ɹ�!");
 		this.userOldPass.value = "";
 		this.userNewPass.value = "";
 		this.userConfirmPass.value = "";
		this.closed = true;
		this.close(this, true); 		
 	}else{
 		alert(ret.result);
 		this.userOldPass.value = "";
 		this.userNewPass.value = "";
 		this.userConfirmPass.value = "";
 		this.close(this, false);
 	}
};
