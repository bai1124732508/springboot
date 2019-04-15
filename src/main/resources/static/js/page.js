$(function(){
	//好像很实用的样子，后端的同学再也不用写分页逻辑了。
	laypage({
	    cont: 'page_Div',
	    pages: $("#pageinfo_pages").val(), //可以叫服务端把总页数放在某一个隐藏域，再获取。假设我们获取到的是18
	    skip:true,
	    skin: 'molv',
	    curr: function(){ //通过url获取当前页，也可以同上（pages）方式获取
	        var page = $("#pageinfo_pageNum").val();
	        return page;
	    }(), 
	    jump: function(e, first){ //触发分页后的回调
	        if(!first){ //一定要加此判断，否则初始时会无限刷新
	        	$("#pageinfo_pageNum").val(e.curr);
	        	$("#form").submit();
	        }
	        var totalCount = $('#totalcount').html();
	        $('#page_Div').find('.laypage_main').append(totalCount);
	    }
	});
})

function resetPageNum(){
	$("#pageinfo_pageNum").val(1);
}

function resetsubmitform(){
	resetPageNum();
	$("#form").submit();
	
	
}