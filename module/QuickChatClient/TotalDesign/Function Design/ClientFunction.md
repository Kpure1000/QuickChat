# The function of client with its methods relation

***version：1.0***

## Total&Abstract

**This is the Function Layout, between UI and Network, in oder to abstract the function of network layout, and sum up several functions considered by user.**

## Detail Design

### Sign Up

#### Service Logic

### Sign Out

1. 界面按钮监听，请求下线（若是由于网络断开引起的下线，则由服务器的任务线程处理）
2. 发送包
3. 刷新状态（isOnLine = false)

### Setting

#### Change Unser Name

1. 输入新用户名（界面输入）
2. send 用户名（网络层）
3. 等待反馈（消息队列）
4. 成功（界面反馈）

#### Change Password

1. 输入新密码（界面输入）
2. send 密码（网络层）
3. 等待反馈（消息队列）
4. 成功（界面反馈）

### Chat

#### Private Chat

1. 选择好友（界面输入，获取好友ID）
2. 输入（界面输入）
3. send 消息（网络层）

#### Grounp Chat

1. 选择群聊
2. 输入
3. send 消息
