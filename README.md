###DHealthyProgressView

####类似健康指数的 齿轮进度条 给Paint设置setPathEffect 来实现进度的间隔化

具体效果

- 默认模式 每次都是重新开始绘制


![默认模式 每次都重新绘制](https://github.com/Daemon1993/healthycricleviewdemo/blob/master/gif/1.gif)
  
- 开启连续模式 增加模式 beginContinue(true)

![开启连续  beginContinue(true); ](https://github.com/Daemon1993/healthycricleviewdemo/blob/master/gif/3.gif)


- 基本思路

因为在让进度里面存在别的控件 比如 文本 比如 ImageView

就让整个自定义View实现RelativeLayout 
然后根据当前View的长宽来绘制弧形

弧形分为底部全弧度   basePaint  画笔

上面的进度弧度         progressPaint 画笔

初始化 用basePaint 绘制一个底部的进度 100%的弧度

然后 给 新设置的progress加一个动画  跟着动画的过程 来用 progressPaint 来绘制弧度 invalidate()方法通知draw 更新界面 直到动画完成 
 
代码不多  300行左右   感兴趣的 可以看看  


[简书地址  ](http://www.jianshu.com/p/355482f72c78)
