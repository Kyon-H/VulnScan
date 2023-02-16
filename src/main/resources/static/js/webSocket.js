var webSocket = null;
var globalCallback = null;//定义外部接收数据的回调函数

//初始化websocket
function initWebSocket(url) {
  if ("WebSocket" in window) {
    webSocket = new WebSocket(url);//创建socket对象
    console.log(webSocket)
  } else {
    alert("该浏览器不支持websocket!");
  }
  //打开
  webSocket.onopen = function() {
    webSocketOpen();
  };
  //收信
  webSocket.onmessage = function(e) {
    webSocketOnMessage(e);
  };
  //关闭
  webSocket.onclose = function() {
    webSocketClose();
  };
  //连接发生错误的回调方法
  webSocket.onerror = function() {
    console.log("WebSocket连接发生错误");
  };
}

//连接socket建立时触发
function webSocketOpen() {

    const data = {
      type: "CONNECT",
      action: "OPEN"
    };
    sendSock(data, function() {});

  console.log("WebSocket连接成功");
}

//客户端接收服务端数据时触发,e为接受的数据对象
function webSocketOnMessage(e) {
    console.log("e.data:"+e.data);
  const data = JSON.parse(e.data);//根据自己的需要对接收到的数据进行格式化
  console.log("run callback");
  globalCallback(data);//将data传给在外定义的接收数据的函数，至关重要。
}

//发送数据
function webSocketSend(data) {
  webSocket.send(JSON.stringify(data));//在这里根据自己的需要转换数据格式
}

//关闭socket
function webSocketClose() {
  //因为我建立了多个socket，所以我需要知道我关闭的是哪一个socket，就做了一些判断。
  if (
    webSocket.readyState === 1 &&
    webSocket.url === "ws://1xx.xx.xx.xxx/ws"
  ) {
    webSocket.close();
    console.log("对话连接已关闭");
  }
}


//在其他需要socket地方调用的函数，用来发送数据及接受数据
function sendSock(agentData, callback) {
  globalCallback = callback;//此callback为在其他地方调用时定义的接收socket数据的函数，此关重要。
  //下面的判断主要是考虑到socket连接可能中断或者其他的因素，可以重新发送此条消息。
  switch (webSocket.readyState) {
    //CONNECTING：值为0，表示正在连接。
    case webSocket.CONNECTING:
      setTimeout(function() {
        webSocketSend(agentData, callback);
      }, 1000);
      break;
    //OPEN：值为1，表示连接成功，可以通信了。
    case webSocket.OPEN:
      webSocketSend(agentData);
      break;
    //CLOSING：值为2，表示连接正在关闭。
    case webSocket.CLOSING:
      setTimeout(function() {
        webSocketSend(agentData, callback);
      }, 1000);
      break;
    //CLOSED：值为3，表示连接已经关闭，或者打开连接失败。
    case webSocket.CLOSED:
      // do something
      break;
    default:
      // this never happens
      break;
  }
}
