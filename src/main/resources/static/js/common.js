
$(function(){
	var url = window.location.pathname;//加载面包
	var info =  $(".breadcrumb").attr("leval");
	if(!info){
		$.ajax({
			type:"get",
			url:"/menu/breadcrumb",
			data:{"url":url},
			dataType:"json",
			success:function(data){
				var vis="";
				// vis+='<li >主页</li>';
		       for(var i=0;i<data.length;i++){
		    	   var obj =data[i]; 
		    	   if(data[i].url){
		    		   vis+='<li > <a href="/'+data[i].url+'">'+data[i].title+'</a></li>';
		    	   }else{
		    		   vis+='<li >'+data[i].title+'</li>';
		    	   }
		       }
		       $(".breadcrumb").html(vis);
			}
		});
	}
	 $(".breadcrumb").css("width","1000px");
	/**
	 * 全局绑定表单继续ajax提交和验证
	 */
	var valid = $("#f").Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			$('#Validform_msg').hide();
			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			if(data.code == 1){
				if (data.url) {
                        cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
					setTimeout(function(){
						location.href = data.url;
					},data.wait*1000);
				}else{
					cthink.toast(data.msg,data.wait,'error');
					setTimeout(function(){
						$('.btn-primary').removeClass('disabled').prop('disabled',false);
					},3000);
					
				}
			}else{
				cthink.toast(data.msg,data.wait,'error');
				setTimeout(function(){
					$('.btn-primary').removeClass('disabled').prop('disabled',false);
				},3000);
			}
		}
	});


	var valid = $(".form").Validform({
    		tiptype:3,
    		label:".label",
    		showAllError:true,
    		ajaxPost:true,
    		callback:function(data){
    			$('#Validform_msg').hide();
    			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
    			if(data.code == 1){
    				if (data.url) {
                        cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
    					setTimeout(function(){
    						location.href = data.url;
    					},data.wait*1000);
    				}else{
    					cthink.toast(data.msg,data.wait,'error');
    					setTimeout(function(){
    						$('.btn-primary').removeClass('disabled').prop('disabled',false);
    					},3000);

    				}
    			}else{
    				cthink.toast(data.msg,data.wait,'error');
    				setTimeout(function(){
    					$('.btn-primary').removeClass('disabled').prop('disabled',false);
    				},3000);
    			}
    		}
    	});
	
	
	var valid = $("#fo").Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			location.href = data.url;
		}
	});
	//弹出框的form提交
	var valid = $("#f_layerOpen").Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			 parent.window.location.href = data.url;
			 var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
             parent.layer.close(index);
		}
	});
	
	//上传凭证的时候绑定的
	var valid = $("#f-img").Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			$('#Validform_msg').hide();
			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			if(data.code == 1){
					cthink.toast(data.msg,data.wait,'success');
					setTimeout(function(){
						$('.btn-primary').removeClass('disabled').prop('disabled',false);
						window.parent.location.reload(); //刷新父页面
						var index = parent.layer.getFrameIndex(window.name);  
						parent.layer.close(index);  //关闭当前弹出层
					},3000);
					
			}else{
				cthink.toast(data.msg,data.wait,'error');
				setTimeout(function(){
					$('.btn-primary').removeClass('disabled').prop('disabled',false);
				},3000);
			}
		}
	});
	
	
	
	var valid = $("#layerform").Validform({//弹框提交关闭弹框刷新父页面
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			$('#Validform_msg').hide();
			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			if(data.code == 1){
                    cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
					setTimeout(function(){
						layer.closeAll();
						parent.location.reload();
					},data.wait*1000);

			}else{
				cthink.toast(data.msg,data.wait,'error');
				setTimeout(function(){
					$('.btn-primary').removeClass('disabled').prop('disabled',false);
				},3000);
			}
		}
	});
	
	
	var valid = $("#layerformredirect").Validform({//弹框提交关闭弹框 并跳转到指定页面
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			$('#Validform_msg').hide();
			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			if(data.code == 1){
						cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
					setTimeout(function(){
						layer.closeAll();
						parent.location.href = data.url;
					},data.wait*1000);
		
					
			}else{
				cthink.toast(data.msg,data.wait,'error');
				setTimeout(function(){
					$('.btn-primary').removeClass('disabled').prop('disabled',false);
				},3000);
			}
		}
	});
	
	/**
	 * 关于安全登录
	 */
	$('#fpass').Validform({
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:false,
		beforeSubmit:function(){
			var url = $('#fpass').attr('action');
			var f = $('#fpass').serialize();
			var password = cthink.getUrlValue(f,'password');
			var password2 = cthink.getUrlValue(f,'password2');
			var md5Password = '';
			if(password){
				md5Password = hex_md5(password);
			}
			 
			if(password2 != null){
				var md5Password2 = hex_md5(password2);
				f = f.replace('password2='+password2,'password2='+md5Password2);
			}
			f = f.replace('password='+password,'password='+md5Password);
			$('.btn-success').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			$.post(url,f).success(function(data){
				if(data){
					if(data.code == 1){
						if (data.url) {
                                cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
							setTimeout(function(){
								location.href = data.url;
							},data.wait*1000);
						}else{
							cthink.toast('数据格式有误，请刷新重试！',3,'error');
							setTimeout(function(){
								$('.btn-primary').removeClass('disabled').prop('disabled',false);
								$('.btn-success').removeClass('disabled').prop('disabled',false);
							},3000);
						}
					}else{
						cthink.toast(data.msg,data.wait,'error');
						setTimeout(function(){
							$('.btn-primary').removeClass('disabled').prop('disabled',false);
							$('.btn-success').removeClass('disabled').prop('disabled',false);
						},3000);
					}
				}else{
					cthink.toast("存在数据缓存，请清理后重试！",3,'error');
				}	
					
			});
			return false;
		}
	});
	
	$("#layerform_psd").Validform({//弹框提交关闭弹框 并跳转到指定页面
		tiptype:3,
		label:".label",
		showAllError:true,
		ajaxPost:true,
		callback:function(data){
			$('#Validform_msg').hide();
			$('.btn-primary').addClass('disabled').attr('autocomplete','off').prop('disabled',true);
			if(data.code == 1){
                    cthink.toast(data.msg + ', ' + data.wait+'秒后页面将自动跳转~',data.wait,'success');
					setTimeout(function(){
						layer.closeAll();
						parent.location.href = data.url;
					},data.wait*1000);
			}else{
				cthink.toast(data.msg,data.wait,'error');
				setTimeout(function(){
					$('.btn-primary').removeClass('disabled').prop('disabled',false);
				},3000);
			}
		},
		beforeSubmit:function(){
			$("#newPassword").val(hex_md5($("#password").val()));
		}
	});
	
	
	
	/**
	 * 动作栏点击编辑、删除等操作的全局处理
	 */
	$('.ajax-post').click(function(){
		var url = $(this).attr('url');
		var target = $(this).attr('target-form');
		var tiptext = $(this).attr('tiptext');
		var checkval = '';
		$('tbody').find('.ids').each(function(i,o){
			if($(this).is(':checked')) {
				checkval += checkval?','+$(this).val():$(this).val();
			}
		});
		if(!checkval && $("#all").val() == "all"){
            checkval = "all";
		}
		if(tiptext==null || tiptext==''){
			tiptext='你确认要这么处理吗？';
		}

		if(checkval || $(this).hasClass('tabledel')){
			var btnlist  = ['确定','取消'];
			if($(this).hasClass('confirm')){
				layer.confirm(tiptext, {
					title:'信息',
				    btn: btnlist
				},
				function(index){
					layer.close(index);
					var loading = layer.load(1, {
						shade: [0.5,'#fff']
					});
					$.post(url,target+'='+checkval).success(function(data){
						if(data.code == 1){
							cthink.toast(data.msg,1,'success');
							layer.close(loading);
							setTimeout(function(){
								location.reload();
							},1000);
						}else{
							cthink.toast(data.msg,1,'error');
							layer.close(loading);
						}
					});
				});
			}else{
				var l = checkval.split(',');
				location.href = url+'?'+target+'='+l[0];
			}
		}
	});
	/**
	 * 动作栏点击编辑、删除等操作的全局处理
	 */
	$('.ajax-postToUrl').click(function(){
		var url = $(this).attr('url');
		var target = $(this).attr('target-form');
		var tiptext = $(this).attr('tiptext');
		if(tiptext==null || tiptext==''){
            tiptext='你确认要这么处理吗？';
		}
		var checkval = '';
		$('tbody').find('.ids').each(function(i,o){
			if($(this).is(':checked')) {
				checkval += checkval?','+$(this).val():$(this).val();
			}
		});
        var btnlist  =  ['确定','取消'];
		if(checkval || $(this).hasClass('tabledel')){
			if($(this).hasClass('confirm')){
				layer.confirm(tiptext, {
					title:'信息',
				    btn: btnlist
				},
				function(index){
					layer.close(index);
					var loading = layer.load(1, {
						shade: [0.5,'#fff']
					});
					$.post(url,target+'='+checkval).success(function(data){
						if(data.code == 1){
							cthink.toast(data.msg,1,'success');
							layer.close(loading);
							setTimeout(function(){
								location.href = url+'?'+target+'='+checkval;
							},1000);
						}else{
							cthink.toast(data.msg,1,'error');
							layer.close(loading);
						}
					});
				});
			}else{
				var l = checkval.split(',');
				location.href = url+'?'+target+'='+l[0];
			}
		}
	});
	
	
	/**
	 * 动作栏点击编辑、删除等操作的全局处理
	 */
	$('.ajax-postTip').click(function(){
		var url = $(this).attr('url');
		var tiptext=$(this).attr('tiptext');
		var target = $(this).attr('target-form');
		var checkval = '';
		$('tbody').find('.ids').each(function(i,o){
			if($(this).is(':checked')) {
				checkval += checkval?','+$(this).val():$(this).val();
			}
		});
		if(checkval || $(this).hasClass('tabledel')){
            var btnlist  = ['确定','取消'];
			if($(this).hasClass('confirm')){
				layer.confirm(tiptext, {
				  title:'信息',
				  btn: btnlist
				},
				function(index){
					layer.close(index);
					var loading = layer.load(1, {
						shade: [0.5,'#fff']
					});
					$.post(url,target+'='+checkval).success(function(data){
						if(data.code == 1){
							cthink.toast(data.msg,1,'success');
							layer.close(loading);
							setTimeout(function(){
								location.reload();
							},1000);
						}else{
							cthink.toast(data.msg,1,'error');
							layer.close(loading);
						}
					});
				});
			}else{
				var l = checkval.split(',');
				location.href = url+'?'+target+'='+l[0];
			}
		}
	
	});
	
	/**
	 * 动作栏点击编辑、删除等操作的全局处理
	 */
	$('.ajax-get').click(function(){
		var url = $(this).attr('url');
		
		var target = $(this).attr('target-form');
		var checkval = '';
		$('tbody').find('.ids').each(function(i,o){
			if($(this).is(':checked')) {
				checkval += checkval?','+$(this).val():$(this).val();
			}
		});
		if(checkval || $(this).hasClass('tabledel')){
            var btnlist  =  ['确定','取消'];
			if($(this).hasClass('confirm')){
                var tiptext='你确认要这么处理吗？';
				layer.confirm(tiptext, {
				  btn: btnlist
				},
				function(index){
					layer.close(index);
					var loading = layer.load(1, {
						shade: [0.5,'#fff']
					});
					location.href = url+'?'+target+'='+checkval;
				});
			}else{
				var l = checkval.split(',');
				location.href = url+'?'+target+'='+l[0];
			}
		}
	});
	/**
	 * 全局返回按钮事件
	 */
	$('.historygo').click(function(){
		javascript:history.go(-1);
	});
	/**
	 * 返回并刷新上个页面
	 */
	$('.historygorefresh').click(function(){
		self.location=document.referrer;
	});
	
	/**
	 * 全局checkbox效果
	 */
	$(".i-checks").iCheck({
		checkboxClass:"icheckbox_square-green",
		radioClass:"iradio_square-green"
	});
	
	/**
	 * 实现全选和反选的效果
	 */
	$(".check-all").on('ifClicked',function(event){
		$('.ids').filter("[disabled!='disabled']").iCheck('check');
	});
	$(".check-all").on('ifUnchecked',function(event){
		$('.ids').filter("[disabled!='disabled']").iCheck('uncheck');
	});
	/**
	 * 实现搜索块的隐藏和显示
	 */
	$('.search-toggle').click(function(){
		var display = $('.search-area').css('display');
		if(display == 'none'){
			$('.search-area').show(100);
			$(this).html('收起');
		}else{
			$('.search-area').hide(100);
			$(this).html('搜索');
		}
	});
	
});

/**
 * cthink js函数库
 */
var cthink = {
	/**
	 * 弹出消息，成功失败返回的结果在顶部居中显示
	 */
	toast:function(msg,t,type){
		toastr.options = {
		  "closeButton": true,
		  "debug": false,
		  "progressBar": true,
		  "positionClass": "toast-top-center",
		  "onclick": null,
		  "showDuration": "400",
		  "hideDuration": "1000",
		  "timeOut": t * 1000,
		  "extendedTimeOut": "1000",
		  "showEasing": "swing",
		  "hideEasing": "linear",
		  "showMethod": "fadeIn",
		  "hideMethod": "fadeOut"
		}
		var index = layer.load(1, {shade: [0.1,'#fff']});
		toastr[type](msg);
		setTimeout(function(){
			layer.close(index);
		},t*1000);
	},
	init_menu:function(){
		function e() {
	        var e = $("body > #wrapper").height() - 61;
	        $(".sidebard-panel").css("min-height", e + "px")
	    }
	    $("#side-menu").metisMenu(),
	    $(".right-sidebar-toggle").click(function() {
	        $("#right-sidebar").toggleClass("sidebar-open")
	    }),
	    $(".sidebar-container").slimScroll({
	        height: "100%",
	        railOpacity: .4,
	        wheelStep: 10
	    }),
	    $(".open-small-chat").click(function() {
	        $(this).children().toggleClass("fa-comments").toggleClass("fa-remove"),
	        $(".small-chat-box").toggleClass("active")
	    }),
	    $(".small-chat-box .content").slimScroll({
	        height: "234px",
	        railOpacity: .4
	    }),
	    $(".check-link").click(function() {
	        var e = $(this).find("i"),
	        a = $(this).next("span");
	        return e.toggleClass("fa-check-square").toggleClass("fa-square-o"),
	        a.toggleClass("todo-completed"),
	        !1
	    }),
	    $(function() {
	        $(".sidebar-collapse").slimScroll({
	            height: "100%",
	            railOpacity: .9,
	            alwaysVisible: !1
	        })
	    }),
	    $(".navbar-minimalize").click(function() {
	        $("body").toggleClass("mini-navbar"),
	        SmoothlyMenu()
	    }),
	    e(),
	    $(window).bind("load resize click scroll",
	    function() {
	        $("body").hasClass("body-small") || e()
	    }),
	    $(window).scroll(function() {
	        $(window).scrollTop() > 0 && !$("body").hasClass("fixed-nav") ? $("#right-sidebar").addClass("sidebar-top") : $("#right-sidebar").removeClass("sidebar-top")
	    }),
	    $(".full-height-scroll").slimScroll({
	        height: "100%"
	    }),
	    $("#side-menu>li").click(function() {
	        $("body").hasClass("mini-navbar") && NavToggle()
	    }),
	    $("#side-menu>li li a").click(function() {
	        $(window).width() < 769 && NavToggle()
	    }),
	    $(".nav-close").click(NavToggle),
	    /(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent) && $("#content-main").css("overflow-y", "auto")
	},
	subStatusForm:function(status){//提交不同状态form
    		$("#status").val(status);
    		$("#f").submit();
    },
	getUrlValue: function (url,name) {
         var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
         var r = url.match(reg);
         if (r!=null) return (r[2]); return null;
    },
    countryChange : function (obj){
        var id = $(obj).val();
        $.ajax({
            type:"get",
            url:'/common/getProvinceByPid',
            data:{"id":id},
            success:function(data){
                if(data != null ){
                    var html ="";
                    for(var i =0; i< data.length; i ++){
                            html+='<option value="'+data[i].id+'" >'+data[i].shortName+'</option>';
                    }
                    $("#province").empty();
                    $("#province").append(html);
                }else{
                    var msg =  "操作失败";
                    cthink.toast(msg,1,'error');
                }
            }
        });
    },
    removeImg:function(fileId,attachId,nameval){
        $("#"+fileId+nameval).remove();
        var attachids = $("#"+nameval).val();
        var attachArr = attachids.split(',');
        attachArr.splice($.inArray(attachId,attachArr),1);
        $("#"+nameval).val(attachArr.join(','));
	},
    addImage:function (attachId,nameval) {
        var attachArr = [];
        var attachids = $("#"+nameval).val();
        if(attachids){
            attachArr = attachids.split(',');
            attachArr.push(attachId);
        }else{
            attachArr.push(attachId);
        }
        var str = attachArr.join(',');
        $("#"+nameval).val(str);
    },
    messageRoleChange : function (obj){
             var id = $(obj).val();
             $.ajax({
                 type:"get",
                 url:'/common/getUserListByRoleId',
                 data:{"id":id},
                 success:function(data){
                     if(data != null ){
                         var html ="";
                         for(var i =0; i< data.length; i ++){
                             html+='<option value="'+data[i].uid+'" >'+data[i].nickname+'</option>';
                         }
                         $("#receiving").nextAll().remove();
                         $("#receiving").parent().append(html);
                     }else{
                         var msg =  "操作失败";
                         cthink.toast(msg,1,'error');
                     }
                 }
             });
     }
}