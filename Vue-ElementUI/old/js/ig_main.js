function createMenuTable(moduleIndex,moduleName,menuList){
	if (menuList == null || menuList.length == 0) {
		return;
	}
	/*var table = '<table onmouseout="tableOut('+moduleIndex+');" cellspacing="0" id="menu'+moduleIndex+'" class="igMenu">';
	for(var i = 0;i < menuList.length; i++){
		var m = menuList[i];
		var tid = 'menu'+moduleIndex+':item'+i;
		table+='<tr style="cursor:pointer;" id="'+tid+'" onclick="loadMenuItem('+moduleIndex+',\''+m.url
			+'\',\''+m.id+'\',\''+m.name+'\',\''+moduleName+'\');" onmouseout="setHoverItem(false,this,'+moduleIndex+');" onmouseover="setHoverItem(true,this,'+moduleIndex+');">'
			+'<td class="igMenuItem owmc"><a>&nbsp;</a></td><td class="owmi"><img src="'+contextPath+m.icon+'" border="0" alt=""/></td>'
			+'<td class="igMenuItem owml"><a href="javascript:void(0)" onclick="return false;">'+m.name+'</a></td>'
			+'<td class="igMenuItem owmm"><a>&nbsp;</a></td></tr>';
	}
	table+='</table>';*/
	/*var table = '<ul onmouseout="tableOut('+moduleIndex+');" cellspacing="0" id="menu'+moduleIndex+'" class="menu-items">';
	for(var i = 0;i < menuList.length; i++){
		var m = menuList[i];
		var tid = 'menu'+moduleIndex+':item'+i;
		table+='<li style="cursor:pointer;" id="'+tid+'" onclick="loadMenuItem('+moduleIndex+',\''+m.url
			+'\',\''+m.id+'\',\''+m.name+'\',\''+moduleName+'\');" onmouseout="setHoverItem(false,this,'+moduleIndex+');" onmouseover="setHoverItem(true,this,'+moduleIndex+');">'
			+'<div class="menu-item"><img src="'+contextPath+m.icon+'" border="0" alt=""/>'
			+'<a class="menu-item-name" href="javascript:void(0)" onclick="return false;">'+m.name+'</a></div>'
			+'</li>';
	}
	table+='</ul>';
	return table;*/
	var table = '<ul onmouseout="tableOut('+moduleIndex+');" cellspacing="0" id="menu'+moduleIndex+'" class="menu-items">';
	for(var i = 0;i < menuList.length; i++){
		var m = menuList[i];
		var tid = 'menu'+moduleIndex+':item'+i;

		table+='<li style="cursor:pointer;" id="'+tid+'" onclick="loadMenuItem('+moduleIndex+',\''+m.url
			+'\',\''+m.id+'\',\''+m.name+'\',\''+moduleName+'\');" onmouseout="setHoverItem(false,this,'+moduleIndex+');" onmouseover="setHoverItem(true,this,'+moduleIndex+');">'
			+'<div class="menu-item">'
			+'<a class="menu-item-name" href="javascript:void(0)" onclick="return false;"><i class="'+m.icon+'"></i>'+m.name+'</a></div>'
			+'</li>';
	}
	table+='</ul>';
	return table;
}

function setHoverItem(flag,obj,index){
	if(flag){
		obj.style.background='#1ba3e3';
	}else{
		obj.style.background='none';
	}
}

var showingTable;

function showTableMenu(index){
	var menuTable=document.getElementById('menu'+index);
	if(showingTable!=null){
		if(menuTable!=showingTable){
			hideMenu();
			menuTable.style.visibility='visible';
		}
	}else{
		menuTable.style.visibility='visible';
	}

	var list=document.getElementById('lista'+index).parentNode;
	var x = list.offsetLeft+list.parentNode.offsetLeft-10 + 375;

	menuTable.style.left = x;
	menuTable.style.top = 49;
	showingTable = menuTable;
}

function tableOut(index){
	if(!checkToInMenuItem(index)){
		hideMenu();
	}
}

function hideMenu(){
	if(showingTable){
		showingTable.style.visibility='hidden';
		showingTable = null;
	}
}

function listOver(index){
	showTableMenu(index);
}
function listOut(index,event){
	if(!checkToInMenuItem(index,event)){
		hideMenu();
	}
}

//判断当前坐标是否在菜单上面
function checkToInMenuItem(index,event){
	var ev = event || window.event;
	var tar = ev.toElement;
	while(tar!=null){
		if(tar.id&&tar.id.indexOf('menu'+index)!=-1){
			return true;
		}
		tar = tar.parentNode;
	}
	return false;
}

function loadMenuItem(mindex,url,menuId,menuName,moduleName){
	//defaultTab.setTitle(moduleName+'&nbsp;>>&nbsp;'+menuName);
	document.getElementById("frmright").src=contextPath+url+'?menuId='+menuId;
	//defaultTab.setSrc(contextPath+url+'?menuId='+menuId);
	hideMenu();
	
	/*var old=document.getElementById('current');
	old==null||(old.id='');
	var src=document.getElementById('lista'+mindex);
	src.parentNode.id='current';*/
}