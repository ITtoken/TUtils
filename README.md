# TUtils工具包的使用
  _提示: (点击工具类标题可以连接到源码)_

  >Tutils中加入了一下在Android编程中常用的工具类和一些我自己维护的Android常见的控件
  >> - 常用工具类: `AndroidUtils, In2Out, IntArrayUtils, MD5, PinYinUtil, SingleToast`
  >> - Android Widgets控件: `DragDownFresh, DragLayout, FileBrowser, QuickIndexListview, QuickIndexView, SlideItem, SlideMenu`
  
## 1. 常用工具类使用

  1. [AndroidUtils](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/AndroidUtils.java)
      - 双击事件: `doubleClick(long gapTime)`
      - 多击事件: `MuitiClick(long gapTime, int clickTimes)`
      - 获取屏幕尺寸:
          - `getScrennWidth(Context context)`
          - `etScrennHeight(Context context)`
  2. [In2Out](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/In2Out.java)
      - 读取输入流中的内容并返回: `getInputstreamInfo(InputStream in) throws IOException`
  3. [IntArrayUtils](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/IntArrUtils.java)
      - 读取数组中的最大/小值: `int getValueType(int[] intArr, int valueType)`
      - 对目标数组进行排序: `int[] getSort(int[] intArr, int arrLen)`
  4. [MD5](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/MD5.java)
      - `String encodingMD5(String password)`
  5. [PinyinUtils](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/PinyinUtil.java)
  
      > _这里引用了belerweb大神的[pinyin4j](https://github.com/belerweb/pinyin4j)_
      
      - 获取汉字的拼音: `String getPinyin(String hanyuString, HanyuPinyinCaseType caseType)`
      - 获取拼音的首字母: `char getFirstChar(String hanyuString)`
      - 判断字符串是否是以汉字开头的: `boolean isStartWithHY(String hanyuString)`
  6. [SingleToast](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/base/SingleToast.java)
      - 单例模式的Toast:
          - `showToast(Context context, String s)`
          - `showToast(Context context, int resId)`

## 2. Android Widgets模块的使用
## 1. [DragLayout](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/widgets/DragLayout.java)

> 该控件基于Android的`ViewDragHelper`

**1. 主要方法**

```java
	1. void open(boolean isSmooth): //打开侧滑面板
	2. void close(boolean isSmooth): //关闭侧滑面板
	3. boolean isMenuSHow(): //获取侧滑面板显示状态
	4. ViewGroup getMainViewGroup(): //获取主布局对象
	5. ViewGroup getMenuViewGroup(): //获取侧滑布局对象
	6. setDragStatListener(DragStatListener listener): //拖拽监听
```

**2. 接口**

> DragStatListener

```java
public interface DragStatListener {
	public void close(); //侧换面板关闭时调用

	public void open(); //侧换面板打开时调用

	public void draging(); //正在滑动时调用
};
```

## 2. [FileBrowser](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/widgets/FileBrowser.java)

> 该控件可以当做文件浏览器使用

**1. 主要方法**

```java
	1. List<String> getStringFiles(String path): //获取目录下所有文件的名字
	2. String goToBack(): //返回上一级(return 要返回的目录路径)
	3. void setFileOrDirOperateListener(FileOrDirOperateListener listener)//文件/文件夹操作监听
```

**2. 接口**
> FileOrDirOperateListener

```java
	public interface FileOrDirOperateListener {
		/**
		 * 
		 * @param currentPath
		 *            当前路径
		 * @param fileContent
		 *            如果是文件,则返回文件内容,否是为空内容("")
		 */
		public void onChoose(String fileName, String currentPath, String fileContent);
	}
```

## 3. [QuickIndexListView](https://github.com/ITtoken/TUtils/blob/master/src/com/tianjj/tutils/widgets/QuickIndexListview.java)

> 该控件将ListView和QuickIndexView进行了整合,可以直接像使用ListView一样简单的构造出`快速索引列表`,
>> _该控件基于[QuickIndexView](https://github.com/ITtoken/Tianjj/blob/master/TUtils/src/com/tianjj/tutils/widgets/QuickIndexView.java) 和 [QuickIndexHelper](https://github.com/ITtoken/Tianjj/blob/master/TUtils/src/com/tianjj/tutils/helper/QuickIndexHelper.java)_

**1. 主要方法**

```java
	1. void setItemText(String[] mItemTests): //设置条目内容，必须在调用，且在setAdapter之前
	2. void setAdapter(QuickListAdapter adapter): //设置QuickListView的条目内容
```

**2. 接口**
> QuickListAdapter

```java
public abstract class QuickListAdapter {
	/**
	 * 获取要显示的条目总数
	 * 
	 * @return
	 */
	public int getCount() {
		if (mOrderedList != null) {
			return mOrderedList.size(); //默认返回列表条目的大小
		}
		return 0;
	}

	/**
	 * 获取要显示的每个条目的View
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	public abstract View getView(int position, View convertView,
           ViewGroup parent, List<String> orderedList);
}
```

  _其他`Widgets`目前还正在更新验证..._
 
  _此工具包会一直更新,各位有什么意见或建议可以一起商讨,Email: `527623439@qq.com`_
