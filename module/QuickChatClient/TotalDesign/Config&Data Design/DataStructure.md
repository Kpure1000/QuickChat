# Data Structure

数据的结构



* DataManager
  * 高级的方法，暂时不能直接获取config
  * Private Config
    * UserInfo:当前用户信息
    * messageRecords:当前用户的消息记录
      * ChatObjectID:当前聊天对象ID
      * messageContents
        * senderID: 发送者ID
        * sendTime: 发送时间
        * isSent: 是否发送
        * isRead: 是否已读
        * content: 消息内容