package com.tianjj.tutils.widgets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import com.tianjj.tutils.R;
import com.tianjj.tutils.base.AndroidUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowser extends ListView {

	private List<File> list;
	private Context context;
	private myAdapter myAdapter;
	private String currentPath;
	private FileOrDirOperateListener mListener;
	private String fileContent;
	private String fileName;
	private boolean isHiddenFileOrDir;

	private void init(final Context context) {
		currentPath = Environment.getExternalStorageDirectory().toString();
		list = getFiles(currentPath + "/");
		myAdapter = new myAdapter();

		setAdapter(myAdapter);
		setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (currentPath.length() <= 0) {
					currentPath = "/";
					return;
				}

				if (position == 0) {// 返回上一级
					goToBack();
				} else {// 进入目录或读取文件
					goAhead(context, position);
				}

				if (fileName == null) {
					fileName = "";
				}
				mListener.onChoose(fileName, currentPath, fileContent);
			}
		});
	}

	private List<File> getFiles(String path) {
		File file = new File(path);
		File[] listFiles = file.listFiles();
		if(listFiles == null && file.isDirectory()){
			listFiles = new File[]{};
		}
		
		List<File> list = Arrays.asList(listFiles);
		return list;
	}

	private String getInputstreamInfo(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte b[] = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		String result = out.toString();

		return result;
	}

	private LinearLayout generateLayout() {
		LinearLayout ll_parent = new LinearLayout(context);
		ll_parent.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams p_params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		float p_padding = AndroidUtils.getScrennHeight(context) / 50.0f;
		p_params.leftMargin = (int) p_padding;
		p_params.topMargin = (int) p_padding;
		p_params.rightMargin = (int) p_padding;
		p_params.bottomMargin = (int) p_padding;
		
		LinearLayout ll_child = new LinearLayout(context);
		ll_child.setOrientation(LinearLayout.VERTICAL);
		
		LinearLayout.LayoutParams c_params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);

		ImageView iv = new ImageView(context);
		TextView tv = new TextView(context);
		TextView tv_tip = new TextView(context);
		
		iv.setId(0);
		tv.setId(1);
		tv_tip.setId(2);
		
		ll_child.addView(tv,c_params);
		ll_child.addView(tv_tip,c_params);
		ll_parent.addView(iv, p_params);
		ll_parent.addView(ll_child, p_params);

		return ll_parent;
	}

	private void goAhead(final Context context, int position) {
		boolean isDir = list.get(position - 1).isDirectory();
		if (isDir) {
			currentPath = list.get(position - 1).toString();
			list = getFiles(currentPath + "/");
			fileContent = "";
		} else {
			try {
				fileName = list.get(position - 1).getName();
				FileInputStream fi = new FileInputStream(new File(currentPath + "/", fileName));
				fileContent = getInputstreamInfo(fi);
				if (fileContent == null) {
					fileContent = "";
				}
			} catch (FileNotFoundException e) {
				SingleToast.showToast(context, "权限不足");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		myAdapter.notifyDataSetChanged();
	}

	public FileBrowser(Context context) {
		super(context, null);
	}

	public FileBrowser(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	class myAdapter extends BaseAdapter {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout ll = null;
			if (convertView != null) {
				ll = (LinearLayout) convertView;
			} else {
				ll = generateLayout();
			}

			ImageView iv = (ImageView) ll.findViewById(0);
			TextView tv = (TextView) ll.findViewById(1);
			TextView tv_tip = (TextView) ll.findViewById(2);

			tv.setTextSize(20);
			tv.setPadding(5, 5, 5, 5);
			tv_tip.setTextColor(Color.GRAY);

			if (position == 0) {
				tv.setText("...");
				iv.setBackground(getResources().getDrawable(R.drawable.back));
			} else {
				File file = list.get(position - 1);
				int drawableId = 0;
				if (file.isDirectory()) {
					drawableId = R.drawable.folder;
				} else {
					drawableId = setFileIcon(file);
				}
				iv.setBackground(getResources().getDrawable(drawableId));

				if (list.get(position - 1).isFile()) {
					tv.setTextColor(Color.GREEN);
				} else {
					tv.setTextColor(Color.GRAY);
				}
				
				if(file.isDirectory() && file.isHidden()){
					tv_tip.setVisibility(View.VISIBLE);
					tv_tip.setTextSize(12);
					tv_tip.setText("隐藏文件(夹)可能会受到系统权限影响而打开失败");
				} else {
					tv_tip.setVisibility(View.GONE);
				}

				String filepath = list.get(position - 1).toString();
				tv.setText(filepath.substring(filepath.lastIndexOf("/") + 1));
			}
			return ll;
		}

		private int setFileIcon(File file) {
			int drawableId = 0;
			String fileFormat = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if (file.getName().endsWith("." + fileFormat)) {
				drawableId = context.getResources().getIdentifier(fileFormat, "drawable",
				context.getPackageName());
			} else {
				drawableId = R.drawable.file;
			}
			return drawableId == 0 ? R.drawable.file : drawableId;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return list.size() + 1;
		}
	};

	/**
	 * 获取目录下所有文件的名字
	 * 
	 * @param path
	 * @return
	 */
	public List<String> getStringFiles(String path) {
		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		return list;
	}

	/**
	 * 返回上一级
	 * 
	 * @return
	 */
	public String goToBack() {
		currentPath = currentPath.substring(0, currentPath.lastIndexOf("/"));
		if (currentPath.length() <= 0) {
			currentPath = "/";
			return null;
		}

		list = getFiles(currentPath + "/");
		myAdapter.notifyDataSetChanged();
		return currentPath;
	}

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

	public void setFileOrDirOperateListener(FileOrDirOperateListener listener) {
		this.mListener = listener;
	}
}
