# FloatBallView

仿360实现了一个球形悬浮窗，拖动水波浪效果，可随意拖动，松开时自动靠边吸附。
双击可以打开一个底部栏，目前底部栏什么都没有加，只是一个样例。

## GIF
<img src="picture/example.gif"/>

##使用方法
在需要打开的地方插入：

                FloatViewManager fm=FloatViewManager.getInstance(MainActivity.this);
                fm.showFloatBallView();
