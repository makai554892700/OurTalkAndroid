### OurTalk Android 客户端代码
# 通讯核心使用[netty](https://netty.io/)
# 聊天气泡效果[参考](https://www.cnblogs.com/shiwei-bai/p/4913973.html)
    * 去[阿里巴巴矢量图标网站](https://www.iconfont.cn/) 搜索气泡 获取想要的图片，下载png文件，网站可以选择颜色和大小。气泡的方向则可以直接使用工具翻转。
    * 复制png文件至mipmap文件夹
    * 右键图片文件，选择 create 9-patch file...
    * 生成后直接打开生成的 .9.png 文件可编辑文件选择可拉伸位置，具体[参考](https://www.cnblogs.com/shiwei-bai/p/4913973.html)
    * 效果可通过xml预览
    * 使用代码

            <TextView
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="@drawable/ic_talk_left"
                android:text="@string/app_name"/>











