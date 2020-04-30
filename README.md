# Utils
工具类
contactutils 获取联系人的相关信息 头像 姓名 备注 等
ImageUtils
1 给bitmap增加高斯模糊
2 保存图片到本地并同步到系统相册

##RX操作符之错误处理（catch）<https://blog.csdn.net/nicolelili1/article/details/52152155>
#从onError通知中恢复发射数据。Catch操作符拦截原始Observable的onError通知，将它替换为其它的数据项或数据序列，让产生的Observable能够正常终止或者根本不终止

#1.onErrorReturn
是遇到onError的之后接管错误，之后onComplete

#2 onErrorResumeNext
让Observable在遇到错误时开始发射第二个Observable的数据序列。之后onComplete

#3 onExceptionResumeNext
和onErrorResumeNext类似，onExceptionResumeNext方法返回一个镜像原有Observable行为的新Observable，也使用一个备用的Observable，不同的是，如果onError收到的Throwable不是一个Exception，它会将错误传递给观察者的onError方法，不会使用备用的Observable
