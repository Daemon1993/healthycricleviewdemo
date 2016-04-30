###DHealthyProgressView
- ####类似健康指数的 齿轮进度条 给Paint设置setPathEffect 来实现进度的间隔化

具体效果

- 默认模式 每次都是重新开始绘制


![默认模式 每次都重新绘制](http://upload-images.jianshu.io/upload_images/831873-a0c4ff5b432a72ba.gif?imageMogr2/auto-orient/strip)
  
- 开启连续模式 增加模式 beginContinue(true)

![开启连续  beginContinue(true); ](http://upload-images.jianshu.io/upload_images/831873-97160320d4d09040.gif?imageMogr2/auto-orient/strip)


- 基本思路
因为在让进度里面存在别的控件 比如 文本 比如 ImageView
就让整个自定义View实现RelativeLayout 
然后根据当前View的长宽来绘制弧形
弧形分为底部全弧度   basePaint  画笔
上面的进度弧度         progressPaint 画笔