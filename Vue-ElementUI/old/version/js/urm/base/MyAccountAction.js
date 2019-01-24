var MyAccountAction = function() {
	this.text = "ÐÞ¸ÄÃÜÂë";
	this.id = "MyAccountAction";
};
lang.extend(MyAccountAction, "framework.webos.actions.AbstractTabAction");

MyAccountAction.prototype.execute = function(arg) {
	this.updatePass();
	return true;
};

MyAccountAction.prototype.close = function(arg) {
	return true;
};

MyAccountAction.prototype.destroy = function() {
	MyAccountAction.superclass.destroy.call(this);
};

MyAccountAction.prototype.updatePass = function(node){	
	var dialogFactory = null;
	if(!dialogFactory)
		dialogFactory = jsloader.resolve("framework.base.wraper.dialogFactory");
	
	this.dialogConfig = {};
	this.dialogConfig.title = "ÐÞ¸ÄÃÜÂë";
	this.dialogConfig.width = "430";
	this.dialogConfig.height = "200";	
	this.dialogConfig.fullName = "urm.base.UpdatePassDialog";
	dialogFactory.showDialog(this.dialogConfig, node, this.doAfterGenProc, this);
};
