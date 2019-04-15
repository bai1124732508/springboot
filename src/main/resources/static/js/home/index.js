 function edValueKeyPress(){
	 $("#Suggest").find("li").remove(); 
     var edValue = document.getElementById("ajaxtest");
     //获取输入框中的值
     var keyword = edValue.value;
     if(keyword!==null && keyword!=''){
  		 sendkeyword(keyword);
     }else{
    	 $("#SuggestLi").hide();
     }
}
function sendkeyword(keyword){
	$.ajax({
		url:"/user/keyword",
		type:"post",
		data:{"keyword":keyword},
		success: function(data) {
			if(data.menuList != null && data.menuList.length > 0) {//匹配的列表不为空  href='"+this.url+"'
				$("#SuggestLi").show(); 
				$("#Suggest").find("li").remove(); 
				$(data.menuList).each(function() {
					$("#Suggest").append("<li><a class='J_menuItem' name='menuList' onclick=\"openMenus('"+this.url+"','"+this.title+"')\" data_index='0'><i class='{$child.icon}'></i><span>"+this.title+"</span></a></li>");
				});
			}else{
				$("#SuggestLi").hide(); 
				$("#Suggest").find("li").remove(); 
			}
			//ul和li显示时触发的事件
			show();
		}
	});
}
function edValueKeyClick(){//当用户点击这个框时也进行判断
	var edValue = document.getElementById("ajaxtest");
     //获取输入框中的值
     var keyword = edValue.value;
     if(keyword!==null && keyword!=''){
  		 sendkeyword(keyword);
     }else{
    	 $("#SuggestLi").hide();
     }
}
function edValueKeyBlur(){//光标移除焦点事件
	setTimeout(function(){
		 $("#SuggestLi").hide();
	},100);
}
function edValueKeyFocus(){//光标获取焦点事件
	edValueKeyPress();
}
function check(){
	 $("#SuggestLi").hide();
}
function show(){//ul和li显示时触发的事件
	if(!$("#SuggestLi").is(':hidden')){//如果li 是显示状态就进行键盘事件监控
		var menuList =document.getElementsByName("menuList");
		var length=menuList.length;
		var index=0;
		var falg=0;
		  document.onkeydown=function(e){ //监控键盘事件
			 var cv= 0;
              e=e||window.event;  
              e.preventDefault();
              if(e.keyCode==40){//xia
            	  index++; 
            	  cv=index%length;
            	  if(falg==0){
            		  falg=1;
            		  cv=0;
            		  index=0;
            	  }
            	  $("#Suggest").children().css({'background-color':'#FFFFFF'});
            	  $("#Suggest").children().eq(cv).css({'background-color':'#C4C2C7'});
              }
              if(e.keyCode==38){//shang
            	  index--;
            	  cv=index%length;
        		  $("#Suggest").children().css({'background-color':'#FFFFFF'});
            	  $("#Suggest").children().eq(cv).css({'background-color':'#C4C2C7'});
              }
              if (e.keyCode == 13) {  //回车事件
            	  cv=index%length;
                  $("#Suggest").children().eq(cv).find('a').click();
              }
              if(e.keyCode == 8){//解决按backspace  按键不、管用的问题 
//               	  alert("backspace");
            	  var edValue = document.getElementById("ajaxtest");
         	      //获取输入框中的值
         	      var keyword = edValue.value;
         	      keyword=keyword.substring(0,keyword.length-1)
         	      edValue.value=keyword;
         	     if(keyword!==null && keyword!=''){//改变值之后再次获取keyCode 是否存在数据库中 
        	  		 sendkeyword(keyword);
        	     }else{
        	    	 $("#SuggestLi").hide();
        	     }
              }
              if(e.keyCode!=8 && e.keyCode!=13 && e.keyCode!=38 && e.keyCode!=40){
            	  //取消键盘监听事件
            	  	document.onkeydown=false;
              }
             
		  };
	}
}
    
//页面在点击li是所触发的事件
$(".J_menuItem").each(function(k) {
	if (!$(this).attr("data-index")) {
		$(this).attr("data-index", k)
	}
});
    
function openMenus(url,title){
	$("#ajaxtest").val(title);
	$("#SuggestLi").hide();
	var o= url;
	var m= title;
	var k= true;
	var l= title;
	$(".J_menuTab").each(
		function() {
			if ($(this).data("id") == o) {
				if (!$(this).hasClass("active")) {
					$(this).addClass("active").siblings(".J_menuTab").removeClass("active");
					gg(this);
					$(".J_mainContent .J_iframe").each(
						function() {
							if ($(this).data("id") == o) {
								$(this).show().siblings(".J_iframe").hide();
								return false
							}
						})
					}
				k = false;
				return false
			}
	});
	if (k) {
		var p = '<a href="javascript:;" class="active J_menuTab" data-id="'
				+ o +'"'
				+'>' 
				+ l + ' <i class="fa fa-times-circle"></i></a>';
		$(".J_menuTab").removeClass("active");
		var n = '<iframe class="J_iframe" name="iframe' + m
				+ '" width="100%" height="100%" src="' + o
				+ '" frameborder="0" data-id="' + o
				+ '" seamless></iframe>';
		$(".J_mainContent").find("iframe.J_iframe").hide().parents(
				".J_mainContent").append(n);
		$(".J_menuTabs .page-tabs-content").append(p);
		gg($(".J_menuTab.active"))
	}
	return false
}
function gg(n) {
	var o = ff($(n).prevAll()), q = ff($(n).nextAll());
	var l = ff($(".content-tabs").children().not(".J_menuTabs"));
	var k = $(".content-tabs").outerWidth(true) - l;
	var p = 0;
	if ($(".page-tabs-content").outerWidth() < k) {
		p = 0
	} else {
		if (q <= (k - $(n).outerWidth(true) - $(n).next().outerWidth(true))) {
			if ((k - $(n).next().outerWidth(true)) > q) {
				p = o;
				var m = n;
				while ((p - $(m).outerWidth()) > ($(".page-tabs-content")
						.outerWidth() - k)) {
					p -= $(m).prev().outerWidth();
					m = $(m).prev()
				}
			}
		} else {
			if (o > (k - $(n).outerWidth(true) - $(n).prev().outerWidth(
					true))) {
				p = o - $(n).prev().outerWidth(true)
			}
		}
	}
	$(".page-tabs-content").animate({
		marginLeft : 0 - p + "px"
	}, "fast")
}
function ff(l) {
	var k = 0;
	$(l).each(function() {
		k += $(this).outerWidth(true)
	});
	return k
}

function getprivilege(menuid,th,subsetUrl){
    var firstUrl = '';
	var groups = '';
	var json = JSON.parse(admin_menu);
	var strlim = '';
	$('.navbar-fixed-top').find('li').css('background','#2C8EFE');
	$(th).parent().css('background','#61ABFF');
	for(var i in json){
		var menu =json[i];
		if(menu.id==menuid){
			var groupStringList = menu.groupStringList;
			if(lang=='en_US'){
                groupStringList = menu.groupStringEnList;
			}
	    	for(var j in menu.groupStringList){
		    	    if(!groups){
                            groups=menu.groupStringList[0];
		    	    }
	    	  		var group ;
                        group=menu.groupStringList[j];
	    	  		//取group下的第一图标
	    	  		var groupicon ="";   // 去除图标
					/*for(var g in menu.ruleCustomList){
					  var childmenu =menu.ruleCustomList[g];
					  if(childmenu.groupId==group){
						  groupicon=childmenu.icon;
						  break;
					  }
					}*/
	    	  		if(groups==group){
	    	  			var limenu='<li class="active">';
			        	limenu+='<a href="#" >';
			        	limenu+= '<span  class="nav-label" >'+group+'</span><span class="fa arrow"></span></a>';
			        	limenu+= '<ul class="nav nav-second-level leftMenu">';
			        	for(var g in menu.ruleCustomList){
			        		var childmenu =menu.ruleCustomList[g];
			        		if(childmenu.groupId==group){
			        			if(g == 0){
			        				firstUrl = childmenu.url;
			        			}
			        			limenu+='<li url='+childmenu.url+'>';
			        			limenu+='<a class="J_menuItem" href="javascript:void(0)"  data-index="0"';
			        			limenu+="onclick=openMenusList('"+childmenu.url+"',this)";
			        			limenu+='>';
                                    limenu+=childmenu.title+'</a></li>';
			        		}
			        	}
			        	limenu += " </ul>";
			        	strlim += limenu;
	    	  		}
	    	  		else{
	    	  			var limenu='<li>';
			        	limenu+='<a href="#" >';
			        	limenu+= '<span  class="nav-label" >'+group+'</span><span class="fa arrow"></span></a>';
			        	limenu+= '<ul class="nav nav-second-level">';
			        	for(var g in menu.ruleCustomList){
			        		var childmenu =menu.ruleCustomList[g];
			        		if(g == 0){
		        				firstUrl = childmenu.url;
		        			}
			        		if(childmenu.groupId==group){
			        			var menuId = childmenu.id  ;//1535076601471183
                                limenu+='<li url='+childmenu.url+'>';
                                limenu+='<a class="J_menuItem" href="javascript:void(0)"  data-index="0"';
                                limenu+="onclick=openMenusList('"+childmenu.url+"',this)";
                                limenu+='>';
                                    limenu+=childmenu.title+'</a></li>';
			        		}
			        	}
			        	limenu += " </ul>";
			        	strlim += limenu;
	    	  		}
	    	}
		}
    }
	$("#side-menu").html(strlim);
	if(subsetUrl){ //主页的消息点进来的
          var li =  $('.leftMenu li');
          for(var i =0 ;i < li.length ; i++){
            if($(li[i]).attr("url") == 'message/receive_list'){
                if(subsetUrl == 'all'){
                    subsetUrl = $(li[i]).attr("url");
                }else{
                    subsetUrl = $(li[i]).attr("url") +"?search['type']="+subsetUrl;
                }
                console.log(subsetUrl);
                openMenusList(subsetUrl,$(li[i]).children('a'));
                break;
            }
          }
	}else{
	    openMenusList(firstUrl);
	}
    cthink.init_menu();
};
window.onload = function(){
	var json = JSON.parse(admin_menu);
	var strlim = '';
	if(json.length>0){
		var menu =json[0];
		//console.log(menu.groupStringList);
        var groupStringList = menu.groupStringList;

	    	for(var j in groupStringList){
	    		var group ;
                    group=menu.groupStringList[j];
	    		//console.log(group);
    			//取group下的第一图标
    	  		var groupicon ="";//去除图标
    	  		/*for(var g in menu.ruleCustomList){
	        		var childmenu =menu.ruleCustomList[g];
	        		if(childmenu.groupId == group){
	        			groupicon=childmenu.icon;
	        			break;
	        		}
    	  		}*/
	    		if(j==0){//第一个展开
		    		var limenu='<li class="active">';
		        	limenu+='<a href="#" >';
		        	limenu+= '<span  class="nav-label" >'+group+'</span><span class="fa arrow"></span></a>';
		        	limenu+= '<ul class="nav nav-second-level leftMenu">';
		        	for(var g in menu.ruleCustomList){
		        		var childmenu =menu.ruleCustomList[g];
		        		if(childmenu.groupId==group){
		        			limenu+='<li url='+childmenu.url+'>';
		        			limenu+='<a class="J_menuItem" href="javascript:void(0)"  data-index="0"';
		        			limenu+="onclick=openMenusList('"+childmenu.url+"',this)";
		        			limenu+='>';
                                limenu+=childmenu.title+'</a></li>';
		        		}
		        	}
		        	limenu += " </ul>";
		        	strlim += limenu;
	    		}else{//其余的闭合
		    		var limenu='<li >';
		        	limenu+='<a href="#" >';
		        	limenu+= '<span  class="nav-label" >'+group+'</span><span class="fa arrow"></span></a>';
		        	limenu+= '<ul class="nav nav-second-level leftMenu">';
		        	for(var g in menu.ruleCustomList){
		        		var childmenu =menu.ruleCustomList[g];
		        		if(childmenu.groupId==group){
		        			limenu+='<li url='+childmenu.url+'>';
		        			limenu+='<a class="J_menuItem" href="javascript:void(0)"  data-index="0"';
		        			limenu+="onclick=openMenusList('"+childmenu.url+"',this)";
		        			limenu+='>';
                                limenu+=childmenu.title+'</a></li>';
		        		}
		        	}
		        	limenu += " </ul>";
		        	strlim += limenu;
	    		}
	    	}
	}
	$("#side-menu").html(strlim);
    cthink.init_menu();
};


function openMenusList(url,th){
	if(th){
		$('#side-menu').find('li').css('background','#fff');
		$(th).parent().css({
			'background':'#F2F2F2',
		});
	}else{
	    $('.leftMenu li:first').css('background','#F2F2F2');
	}
	$('iframe').attr('src',url);
}

function fullScreen(){
	var docElm = document.documentElement;
	  if (docElm.requestFullscreen) {
	    docElm.requestFullscreen();
	  } else if (docElm.mozRequestFullScreen) {
	    docElm.mozRequestFullScreen();
	  } else if (docElm.webkitRequestFullScreen) {
	    docElm.webkitRequestFullScreen();
	  } else if (docElm.msRequestFullscreen) {
	    docElm.msRequestFullscreen();
	  }
}

function mininavbar(){
	if($('body').hasClass('mini-navbar') == false){
		$('body').addClass('mini-navbar');
	}else{
		$('body').removeClass('mini-navbar');
	}
}

function msgClick(type){
    var subsetUrl = type;
    var li = $(".admin_top_menu");
    for(var i =0 ;i < li.length ; i++){
        var id = $(li[i]).attr("id");
        if(id == 'message'){
            getprivilege($(li[i]).children('div').attr("id"),$(li[i]).children('div'),subsetUrl);
            //$(li[i]).children('div').click();
            break;
        }
    }
}


function logo_out(){
    layer.confirm('确定要退出登录吗？', {
      title: "确认提示", //不显示标题
      btn:['确定','取消'] //按钮
    }, function(){
        window.location.href ="login/logout";
    }, function(){
        layer.closeAll();
    });
}



function changePassword(){
       var html = $("#changepassword").html();
       layer.open({
          type: 1,
          title: '更改密码',
          shadeClose: true,
          area: ['40%', '30%'],
          content: html,
          btn: ['确定','取消'],
          yes: function(index, layero){
                $(layero).find("#layerform_psd").submit();
            }
        });
}

   