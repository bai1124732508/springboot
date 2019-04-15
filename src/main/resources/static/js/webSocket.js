    console.log("用户的id："+uid);
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
         var url = "ws://localhost:8088/websocket/"+uid;
         websocket = new WebSocket(url);
    }else {
        console.log("当前浏览器 Not support websocket");
        alert('当前浏览器 Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        console.log("WebSocket连接发生错误");
    };
    //连接成功建立的回调方法
    websocket.onopen = function () {
        console.log("WebSocket连接成功");
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var data = JSON.parse(event.data);
        console.log(data);
        var flag = false;
        if(data.type == 1){ //修改基本信息 更新logo 和平台名称
            if( data.platformName ){
                $(".logoSpan").text(data.platformName);
                flag = true;
            }
            if ( data.logoPath ){
                 $(".logoSvg").attr("src" ,data.logoPath);
                 flag = true;
            }
        }else if (data.type == 2){//有新增的消息
            flag = true;
            var msgType = data.msgType;
            var msgLabelDanger = $("#msgLabelDanger").text();
            ++msgLabelDanger;
            $("#msgLabelDanger").text(msgLabelDanger);
            var html = '<span class="msgRed">（'+msgLabelDanger+'）</span>';
            $("#msgType").empty();
            $("#msgType").append(html);
            if(msgType == '1' ){
                var val = $("#msgType1").attr("value");
                ++val;
                $("#msgType1").attr("value",val);
                console.log("val:"+val);
                html = '<span class="msgRed">（'+val+'）</span>';
                $("#msgType1").empty();
                $("#msgType1").append(html);
            }else if ( msgType == '2'  ) {
                var val = $("#msgType2").attr("value");
                ++val;
                $("#msgType1").attr("value",val);
                html = '<span class="msgRed">（'+val+'）</span>';
                console.log("val:"+val);
                $("#msgType2").empty();
                $("#msgType2").append(html);
            }
        }else if (data.type == 3){//删除消息的操作
            flag = true;
            var msgCount = data.msgCount;
            for (var key in msgCount) {
                var val = msgCount[key];
                console.log("val:"+val+"key:"+key);
                var html =  '';
                if(val > 0){
                    html = '<span class="msgRed">（'+val+'）</span>';
                } else {
                    html = '<span class="msgRed">（0）</span>';
                }
                if(key == 'all'){
                    $("#msgLabelDanger").text(val);
                    $("#msgType").empty();
                    $("#msgType").append(html);
                }else if(key == '1'){
                    $("#msgType1").attr("value",val);
                    $("#msgType1").empty();
                    $("#msgType1").append(html);
                }else if (key == '2'){
                    $("#msgType2").attr("value",val);
                    $("#msgType2").empty();
                    $("#msgType2").append(html);
                }
            }
        }
        if(!flag){
            setMessageInnerHTML(data.data);
        }
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        console.log("WebSocket连接关闭");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }
    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
        document.getElementById('message').innerHTML += innerHTML + '<br/>';
    }

    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }

    //发送消息
    function sendWebSocket(message) {
        websocket.send(message);
    }