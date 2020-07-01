# Sign In

***ver1.0***

## Total

**User use this view-function to login in client.**

## Design

### Service Logic

1. ***从公有配置获取“输入ID”的记录***（登录成功ID的历史记录），**反馈给界面**
2. 界面输入ID，并调用sendID，间接调用网络层发送ID验证包
2.1 界面禁用“确认”按钮；同时该层添加消息任务，等待反馈
2.2 收到反馈后，**更新界面**（ID存在，进入下一面，否则提示错误，重新输入）
3. ***从私有配置获取该ID获取“记住密码”的配置***，以及密码，**反馈给界面**
4. 界面输入密码，调用sendPass，间接调用网络层发送密码验证包
4.1 界面禁用“确认”按钮；同时该层添加消息任务，等待反馈
4.2 收到反馈后，**界面更新**（密码正确，进入下一面，否则提示错误，重新输入）
5. 登录成功，刷新状态，***更新公有、私有配置的ID记录以及密码配置***

### Properties

```java
String idInputTmp// the temp of id, from input
String idListtmp// the temp of IDList, from public config
String passTmp// the temp of password, from input or private config
boolean rememberPass// pass record config from private config
```

### Methods

```java
void setCallBack(CallBack cb);// set interface of callback
String[] getIDList();// return list of id record
void inputID(String ID);// send id to network Layout
String getPassConfig();// return pass record, if none, null
void inputPass(String pass);// input pass
```

### Interfaces（Update UI mainly, avoid coupling）

```java
void OnGetIDConfig(String[] ids);// on function getting id record from local
void OnReceiveFeedBack(boolean feedback);// on function receive feed back, including ID and password
void OnGetPassConfig(String password);// on function getting passwprd config form local
```

### Usage&Step

1. layout of ID Input
construct->setCallBack->getIDList->inputID
2. layout of Password Input
getPassConfig->// TODO 写到这里了, 可能要把这几个类分开写

## UML

```PlantUML
@startuml
+class SignIn{
{field}
-String idInputTmp: the temp of id, from input
-String idListtmp: the temp of IDList, from public config
-String passTmp: the temp of password, from input or private config
-boolean rememberPass: pass record config from private config
{method}
+void setCallBack(CallBack cb): set interface of callback
+String[] getIDList(): return list of id record
+void inputID(String ID): send id to network Layout
+String getPassConfig(): return pass record, if none, null
+void inputPass(String pass): input pass
}
@enduml
```

```PlantUML
@startuml
+interface SignUpCallBack{
    void OnFeedBack()
}
@enduml
```
