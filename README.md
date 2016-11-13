# sharesdk
安卓平台分享sdk，目前支持微信好友，微信朋友圈，QQ好友，QQ空间，但是可以方便的向其中加入任意渠道的分享。

安卓平台上的分享sdk。使用方式：
1.使用默认UI
默认UI采用对话框+RecyclerView实现，用户自己提供UI布局，要求UI中需要提供一个RecyclerView，并且RecyclerView的item中需要指定一个TextView.
使用示例如下：
(1)提供分享对话框的布局,需要提供一个RecyclerView：
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="match_parent">
    <android.support.v7.widget.RecyclerView
        android:paddingBottom="8dp"
        android:paddingTop="15dp"
        android:id="@+id/rcv"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
</RelativeLayout>

(2)指定每个item的布局，必须要包含一个TextView
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="wrap_content"
    android:layout_height="wrap_content">
    <TextView
        android:id="@+id/txt"
        android:gravity="center"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        android:layout_centerInParent="true"/>
</RelativeLayout>

(3)指定取消按钮的布局
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical" android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <View
        android:id="@+id/seperator"
        android:layout_width="match_parent"
        android:layout_height="1dp"
        android:layout_marginTop="15dp"
        android:background="@android:color/darker_gray"/>
    <Button
        android:id="@+id/btn"
        android:background="#00000000"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textSize="18sp"
        android:textColor="#4c5ce3"
        android:text="取 消"
        android:paddingTop="8dp"
        android:gravity="center"/>
</LinearLayout>

(4)在需要分享的activity中创建一个ShareDialog,并设置分享参数，对于默认UI使用DefaultParams:
shareDlg = new ShareDialog(this);
shareDlg.setShareParams(new DefaultParams() {
  ...
  
ShareParams各个回调的说明：
  public int getPlatforms()：需要分享到哪些平台，分享多个平台就把各个平台的id按位或返回即可；
  public String getSummary()：分享的消息的摘要；
  public String getTitle()：分享的消息的标题；
  public String getTargetUrl()：点击分享的消息后跳转的url地址或者其他分享所需要的url
  public String getImageUrl()：分享的图片的url
  public String getAudioUrl()：分享的音频的url
  public abstract HashMap<Integer, String> getAppId()：各个平台的appid，键为平台id，值为对应平台的appid
  public Object getExtra()：额外的数据，比如要分享的文本等
  public abstract void onShareResult()：分享结果回调

DefaultParams继承ShareParams，并提供控制默认UI的相关回调：
  public abstract int getContentViewRes()：获取对话框content view的资源id
  public abstract int getRCV()：获取RecyclerView的资源id
  public int getStyle()：获取展示方式，水平列表，垂直列表或者网格方式
  public int getColumnCount()：以网格方式展示时，一行的展示列数
  public boolean showCancelView() ：是否展示取消按钮
  public abstract Map<Integer, Integer> getIconRes()：获取每个平台对应的图标资源id,键为平台id，值为对应的资源id
  public abstract View getItemContainer()：获取item view的容器view
  public abstract TextView getItemView()：获取item view
  public abstract View getBottomContainer()：获取底部取消按钮的容器view
  public abstract View getBottomView(View parent)：获取底部取消按钮的view
  public HashMap<Integer, String> getPlatformName()：获取平台的名字，键为平台id，值为对应的名称
  public int getDrawablePadding()：获取文字与图片之间的间距
  public RecyclerView.ItemDecoration getItemDecoration()：分割线的实现
  
2.不使用默认UI
  如果不使用默认UI，那么UI可以随具体需求任意处理，分享时只需要调用:
  Share.share(Activity activity, int platform, ShareParams p)即可完成向指定平台的分享；
  
3.增加、替换
  可以使用public int addShareImpl(ShareImpl impl)来增加一个分享的实现，函数返回新增实现的平台id；
  使用public void removeShareImpl(int platform)移除指定平台的分享实现；
  默认已经实现了QQ、空间、微信好友、微信朋友圈的分享实现，如果默认的这些实现不能满足需求，可以移除掉默认实现，把满足自己需求的实现添加进去即可。
