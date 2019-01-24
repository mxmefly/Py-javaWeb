function callback(data){
	var dataObj=eval("("+data+")");
	if(dataObj.retCode=='0') {
		var header=document.getElementById('header');
		var innerHtml="<ul>";
		var innerTable = '';
		var list = dataObj.result;
		for(var i = 0;i < list.length; i++){
		innerHtml+='<li onmouseover="listOver('+i+')" onmouseout="listOut('+i+',event)"><a id="lista'+i+'" href="#">'+list[i].mdulName+'</a></li>';
		innerTable += createMenuTable(i,list[i].mdulName,list[i].menuList);
		}
		innerHtml=innerHtml.substring(0,innerHtml.length-"".length);
		innerHtml+='</ul>';
		header.innerHTML = innerHtml;
		document.getElementById('menuItem').innerHTML = innerTable;
		//initContent();

		//自动加载菜单第一个页面
		var firstMenu = list[0].menuList[0];
		loadMenuItem(0,firstMenu.url,firstMenu.id,firstMenu.name,list[0].name);
		}
};

function initModule(){
	var tem = [{}];
	var systemObj = {
		className:'AuthService',
		methodName:'getUserModuleAndMenu',
		params:lang.toJSONString(tem)
	};
	
	var getMenus = $.post(url, systemObj, 
			function(items){
				//callback(items);
				callback2(items);
		});
	getMenus.error(function(){ alert("获取菜单出现错误,请联系管理员."); });
};

function callback2(data){
	var dataObj = $.parseJSON(data);
	var innerMenuDom = '<ul class="nav sidebar-menu" id="sidebar-menu">';
	if(dataObj.retCode=='0') {
		var list = dataObj.result;

		for(var i = 0;i < list.length; i++){
			//先初始化菜单
			var menuDom = "";
			var moduleItem = "";
			var module = list[i];

			var menulist = module.menuList;
			if(menulist.length > 0){
				menuDom += '<ul class="submenu" style="display: none">';
				for(var j = 0; j < menulist.length; j++){
					var menu = menulist[j];
					menuDom += 	'<li onclick="loadMenuItem('+i+',\''+menu.url+'\',\''+menu.id+'\',\''+menu.name+'\',\''+module.mdulName+'\');">'
									+'<a href="javascript:void(0)" onclick="return false;">'
										+'<i class="menu-icon '+menu.icon+'"></i>'
										+'<span class="menu-text">'+menu.name+'</span>'
									+'</a>'
								+'</li>';
				}
				menuDom += '</ul>';
			}
			//再初始化module
			moduleItem += 	'<li class="module-item">'
								+'<a class="menu-dropdown" href="javascript:void(0)" onclick="return false;">'
									+'<i class="menu-icon '+module.icon+'"></i>'
									+'<span class="menu-text">'+module.mdulName+'</span>'
									+'<i class="menu-expand"></i>'
								+'</a>'
								+menuDom
							+'</li>'
			innerMenuDom += moduleItem;
		}
		innerMenuDom += '</ul>';

		document.getElementById('sidebar').innerHTML = innerMenuDom;

		InitiateSideMenu();

		//自动加载菜单第一个页面
		var firstMenu = list[0].menuList[0];
		loadMenuItem(0,firstMenu.url,firstMenu.id,firstMenu.name,list[0].name);

	}

}

function InitiateSideMenu() {

	$(".submenu li").click(function (e) {
		$(".submenu li").removeClass("active");
		$(this).addClass("active");
	})

	var b = $("#sidebar").hasClass("menu-compact");

	$("#sidebar-collapse").on('click', function () {
		if (!$('#sidebar').is(':visible'))
			$("#sidebar").toggleClass("hide");
		$("#sidebar").toggleClass("menu-compact");
		$(".sidebar-collapse").toggleClass("active");
		b = $("#sidebar").hasClass("menu-compact");

		if ($(".sidebar-menu").closest("div").hasClass("slimScrollDiv")) {
			$(".sidebar-menu").slimScroll({ destroy: true });
			$(".sidebar-menu").attr('style', '');
		}
		if (b) {
			$(".open > .submenu")
				.removeClass("open");
		} else {
			if ($('.page-sidebar').hasClass('sidebar-fixed')) {
				var position = (readCookie("rtl-support") || location.pathname == "/index-rtl-fa.html" || location.pathname == "/index-rtl-ar.html") ? 'right' : 'left';
				$('.sidebar-menu').slimscroll({
					height: 'auto',
					position: position,
					size: '3px',
					color: themeprimary
				});
			}
		}
	});

	$(".sidebar-menu").on('click', function (e) {
		var menuLink = $(e.target).closest("a");
		if (!menuLink || menuLink.length == 0)
			return;
		if (!menuLink.hasClass("menu-dropdown")) {
			if (b && menuLink.get(0).parentNode.parentNode == this) {
				var menuText = menuLink.find(".menu-text").get(0);
				if (e.target != menuText && !$.contains(menuText, e.target)) {
					return false;
				}
			}
			return;
		}
		var submenu = menuLink.next().get(0);
		if (!$(submenu).is(":visible")) {
			var c = $(submenu.parentNode).closest("ul");
			if (b && c.hasClass("sidebar-menu"))
				return;
			c.find("> .open > .submenu")
				.each(function () {
					if (this != submenu && !$(this.parentNode).hasClass("active"))
						$(this).slideUp(200).parent().removeClass("open");
				});
		}
		if (b && $(submenu.parentNode.parentNode).hasClass("sidebar-menu"))
			return false;
		$(submenu).slideToggle(200).parent().toggleClass("open");
		return false;
	});
}
function logout() {
	window.close();
};

var showEditPWWin = function() {
	
};

$(document).ready(function(){
	initModule();
});