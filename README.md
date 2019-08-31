# Introduction to "The Little Prince" 
(The Chinese introduction to this project is listed below that in English)

## Relevant Links
 - Download the apk：
 https://www.coolapk.com/apk/188955
 - Source code：
 https://github.com/zhaoyonghe/littleprince
 - Video tutorial (in Chinese)：
 https://v.qq.com/x/page/d0695277xij.html
 - Source code of backend:
 https://github.com/zhaoyonghe/littleprince-backendservice

## Our Team
Student ID| Name
---|---
1510193 | Yuyang Gao
1511477 | Wenda Zhang
1511479 | Yonghe Zhao

## Brief Introduction
- In daily life, users often need to view information by switching applications, which is very troublesome, affecting efficiency and mood.
- The application realizes the function of suspending the picture carrying information over all applications - picture-pasting, thus avoiding the trouble of switching applications back and forth.
- The application provides resident picture-pasting and screenshots buttons in the notification bar, enabling users to "paste" picture and screen at any time and anywhere.
- The application provides image browsing function, easy to switch albums, and provides powerful editing and sharing functions.
- The application provides the function of the picture bed. Users can upload pictures and get the URL of the picture in the picture bed. Users can easily open pictures through the URL and use the URL for inserting pictures on the network (such as Markdown).

# 贴图小王子文档

[TOC]

## 相关链接
 - 酷安商城链接：
 https://www.coolapk.com/apk/188955
 - github源码链接：
 https://github.com/zhaoyonghe/littleprince
 - 视频使用演示链接(软件介绍以及使用教学)：
 https://v.qq.com/x/page/d0695277xij.html
 - 服务器端代码
 https://github.com/zhaoyonghe/littleprince-backendservice

## 一、团队介绍
学号| 姓名
---|---
1510193 | 高钰洋
1511477 | 章文达
1511479 | 赵永赫



## 二、项目介绍
 - 在日常生活中，用户时常需要通过切换应用查看信息，这样十分麻烦，影响效率和心情。
 - 该应用实现了将携带信息的图片悬浮在所有应用之上——贴图的功能，从而避免了来回切换应用的麻烦。 
 - 该应用提供了通知栏常驻的贴图以及截图按钮，使得用户可以随时随地方便的贴图和截图。 
 - 该应用提供了图片浏览功能，可以便捷的切换相册，并且提供了功能强大的编辑以及分享功能。 
 - 该应用提供了图床的功能，用户可以上传图片，并且在图床中获取该图片的URL，用户可以通过URL方便的打开图片以及将URL用于网络上图片的插入（如Markdown）等需求。


## 三、技术架构

### 图库模块
![](http://wx1.sinaimg.cn/mw690/0060lm7Tly1fsgwb078goj30w70ncwgl.jpg)
图库分为两部分：**本地图片浏览**和**云图床**。
#### 本地图片浏览
 - 默认相册为Screenshots相册，若没有该相册，则任意指定一个存在的相册默认首个显示。使用ContentResolver接口访问ContentProvider提供的本地图片数据，读取相册下所有图片的路径，创建日期等必要数据。
 - 由于本地图片浏览的三列带标题图片显示效果是由Fragment内嵌套StickyGridHeadersGridView布局，再佐以UniversalImageLoader这一强大的多线程图片加载库实现的，需要采用适配器模式将ContentProvider提供的本地图片数据与显示所需要的数据形式匹配。所以，将单个图片信息存入封装类ImageItem中，并维护一个列表存储相册下所有图片信息，传入本地图片列表适配器。
 - 经过适配器的渲染，图片列表即可显示。
#### 云图床
 - 与本地图片浏览部分实现思路基本类似，从服务器处查得供访问的图片链接，单个图片信息封装入CloudImageItem中，存入列表，传入云图片列表适配器，经过渲染，图片列表即可显示。
另外，该图库无法实时监测图片的增删，故提供刷新功能。在实现中，本地图片刷新和切换相册使用相同接口refresh(bucketname)，该函数可以让列表显示名为bucketname相册中的图片。云图床刷新使用refreshcloud()，重新请求并加载网络图片，但由于UniversalImageLoader强大的缓存机制，当服务器端无图片变化时，不会做出下载图片的操作。调用这两类函数，可以实现本地相册与云图床的快捷切换。

### 编辑模块
#### 图片编辑EditImageActivity
从```ListActivity```中监听单击事件，通过
```
((BaseActivity) view.getContext()).imageSelected(selectImage);
```
 - 语句传入图片地址以及保存地址，进入```EditImageEctivity```，我们在```EditImageActivity```中进行图片的编辑。我们在图片编辑中借鉴使用了github中开源项目[ImageEditor](https://github.com/siwangqishiq/ImageEditor-Android)的源代码并进行了相应的修改。
 - 我们的界面由若干```View```与```Fragment```构成，分别拥有各自不同的功能，显示原图片View居于最下方，涂鸦剪切文字旋转等修改的View层“浮于”上方，其相应的功能在各自的Fragment中定义，当我们点击应用按钮时会将我们得修改应用于图片上，我们维护了修改列表以及修改次数变量用于实现```redo/undo```的功能，每次应用作为一个恢复/撤销的单位。
 - 我们维护若干变量记录当前修改次数```mOpTimes```，当前修改模式```mode```以及当前图片```MainBitmap```，当点击保存时会在我们的保存路径处新建文件并将当前图片```MainBitmap```写入文件。不保存直接退出时会对修改次数```mOpTimes```进行判断，若修改过则弹出提示，未修改过则直接退出
 - 页面最下方是菜单View，通过```mainMenuFragment```维护其功能，其中除了涂鸦，裁剪，旋转，文字等修改功能外，还有分享与上传功能，上传是我们图床的工能之一，可以将图片传到服务器上。

### 贴图模块
![](http://wx4.sinaimg.cn/mw690/0060lm7Tly1fsgwb0bee0j30wo0hvwgj.jpg)
可以对本地图片进行**贴图**。
**贴图，即把一张图片悬浮在所有应用之上。**
#### 悬浮窗实现
 - 实现该功能需要使用悬浮窗功能。安卓提供了几种实现悬浮窗的方法，但有些只能在应用内部悬浮，唯有请求悬浮窗权限才能做到全局的悬浮窗（退出应用后悬浮窗仍显示，但清除应用后不显示）。
 - 为图库中的每一张图片都加入长按事件，触发长按事件后，若是第一次使用贴图功能，应用会请求悬浮窗权限，用户同意后，以图片列表最外层的ConstraintLayout作为rootView（根视图，视图树的根节点），采用rootView的post()方法开启新线程。在这个线程中，如果有的话，首先关闭上一个悬浮图片，接下来在悬浮图片参数初始化器中，根据传入的图片宽高等信息，设置该图片在屏幕中显示的初始大小以及放大后的最大大小。规定，初始图片大小是该图片能够放入0.4倍屏幕宽高大小矩形的最大大小，放大后最大大小是该图片能够放入0.95倍屏幕宽高大小矩形的最大大小。由于图片大小和宽高比各异，这步处理非常重要。除此之外的一系列参数设定完毕后，使用安卓自带的WindowManager窗口管理类中的addView方法添加悬浮窗即可。
 - 当然，该功能的核心还是在于放缩效果的控制，但此处的实现难以简洁的描述而又并没有人想要了解，故在此不加陈述。

### 通知模块
![](http://wx3.sinaimg.cn/mw690/0060lm7Tly1fsgw0n0hw1j312b0npwfc.jpg)
通知模块由通知管理器、通知广播、截屏服务三部分构成。

 - 通知管理器 NotificationManager
通知管理器为单例模式，负责对常驻通知栏进行管理，包括常驻通知栏的生成、通知栏视图更新等。
 - 通知广播 NotificationBroadcast
通知广播负责接收大部分通知栏的动作：上一张图、下一张图、将当前图片贴图。接收到对应动作后会调用通知管理器的相应接口。
 - 截屏服务 CaptureService
截屏服务在应用开启时启动。该服务负责接收截屏动作并响应。

#### 通知管理器 NotificationManager
 - 初始化。通知管理器接收一个相册名，初始化相关变量。当相册改变后也会调用此接口重新初始化。
 -  生成通知栏布局框架。调用资源文件中的布局文件，添加各按键的 PendingIndent，包括对应的action。此为通知管理器的内部工具类。
 - 创建通知。设置通知栏的对应图片（运行中会改变）以及其他相关属性，调用notify方法发送通知。只有初始化之后才会调用此方法。
 - 更新通知。接受一个action参数，根据该参数决定如何切换当前图片，并更新通知栏图片，发送通知。
 - 贴图。调用贴图模块的接口，传递一个图片对象即可。

#### 通知广播 NotificationBroadcast  
- 接收广播。接收到Intent时会判断对应的action是否由自己处理。如果是左/右按键，会调用通知管理器的更新接口。为贴图则会调用贴图接口。

#### 截屏服务 CaptureService
 -  启动。在应用启动时会启动服务。
 - 监听广播。服务启动时会动态注册一个广播用于监听截屏事件。
 - 截屏接口。利用安卓的投射功能实现。

### 引导模块
![](http://wx4.sinaimg.cn/mw690/0060lm7Tly1fsgw0myxntj30sp0himxh.jpg)
引导模块使用了[AppIntro](https://github.com/apl-devs/AppIntro)库进行构建  。


#### 页面引导类 AppIntro
 - 利用安卓的 SharedPreferences 判断是否为第一次进入。如果为第一次，MainActivity会启动此类。
 - 页面的初始化与启动。向单页模板类 SampleSlide 传入静态 Fragment ，并收集对应实例。

#### 单页模板类 SampleSlide  
- 初始化。接收 Fragment 返回 SampleSlide 对象。



### 服务器模块


![](http://wx2.sinaimg.cn/mw690/0060lm7Tly1fsgwovo8b7j30ti0dugm4.jpg)
####移动端
 - 使用Apache旗下的HttpCore，HttpClient，HttpMIME工具包。HttpCore，HttpClient配合使用可以使用一些方法清晰的构建报文段而不需要显式编写。由于需要上传图片，故使用HttpMIME工具包中的MultipartEntity模块，以multipart/form-data格式上传图片。为提升用户体验，还加入了上传进度条。
#### 服务器
 - 使用Node.js搭建，监听端口3000。解析报文，保存图片至高清图片文件夹。之后缩略图生成器立即提取该图片，使用Node.js的封装库images将该图片缩放至边长为200像素，图像质量为原来的80%的缩略图，保存同名图片至缩略图文件夹。此举一般可将每张图片压缩几十倍。
#### RESTful API
 - 定义RESTful API ‘/smallinfo’，专门为移动端返回缩略图文件夹中的所有图片链接以供加载。移动端只需加载缩略图即可，相比加载原图可提升数十倍的加载速度。
由于原图与缩略图存储名称一致，文件夹不一致，因此从缩略图链接转换到原图链接十分容易。用户点击图床中的缩略图时，将直接弹出显示有原图下载链接的Dialog以供复制。





## 四、项目特点
#### 优点
一款实用的图片操作应用，该应用支持：
1. 贴图——将图库中一张图片悬浮在所有应用之上。应用图库，常驻通知栏浏览图片，一键快捷贴图，图片缩放自如。
2. 图库浏览。高效的相册切换机制，比手机自带图库更加快捷。
3. 编辑图片。对图片进行涂鸦，裁剪，旋转，添加文字，分享操作，更拥有快捷的撤销和重做功能。
4. 常驻通知栏截屏。踏雪无痕，无过场动画，简单提示，更加轻量级的截屏体验。
5. 图床：简单高效的免费图床，无需注册，直接上传，免费外链。

#### 缺点
1.	 机型适配不完全，在部分国产手机机型中会出现权限申请问题。
2.	 字体大小以及通知栏布局在某些手机上可能会不太和谐。
3.	 缩放贴图会出现图片抖动的问题。 
4.	 使用通知栏贴图时，由于该功能紧密依附于图库的根视图，因此只有该应用是点击Home键退出的才能成功调用通知栏贴图，使用Back键退出应用则不行。这是因为如果使用Back键退出应用，图库Activity会被终止，即执行到onDestroy为止，而使用Home键退出应用，图库Activity则只会被暂停，即执行到onStop。
5.	 在用户授权投屏权限后，在图库Activity的onCreate方法中仍会检查投屏权限，该操作会开启一个新的Activity。在极少数机型上，开启应用后，会看到一个空白页面弹出又收回。 
 


## 五、安装及使用
 - 在酷安商城中下载我们的应用程序安装包，安装成功后即可使用我们的应用。
 - 我们应用在第一次打开时会有新手教学功能。也可以通过视频链接观看视频演示。
