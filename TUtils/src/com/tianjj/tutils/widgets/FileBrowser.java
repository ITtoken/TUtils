package com.tianjj.tutils.widgets;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.tianjj.tutils.R;
import com.tianjj.tutils.base.AndroidUtils;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowser extends ListView {

	private static final String TAG = "FileBrowser";
	private List<File> list;
	private Context context;
	private myAdapter myAdapter;
	private String currentPath;
	private FileOrDirOperateListener mListener;
	private String fileContent;
	private String fileName;

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

				if (position == 0) {
					// back to last level directory.
					goToBack();
				} else {
					// go into directory or read the file.
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
		File[] files = new File(path).listFiles();
		if (files == null) {
			Log.e(TAG, "Please check if the permission 'android.permission.READ_EXTERNAL_STORAGE'"
					+ "was used in manifest file!");
			return new ArrayList<File>();
		}
		return Arrays.asList(files);
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
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);

		float padding = AndroidUtils.getScrennHeight(context) / 50.0f;
		params.leftMargin = (int) padding;
		params.topMargin = (int) padding;
		params.rightMargin = (int) padding;
		params.bottomMargin = (int) padding;

		ImageView iv = new ImageView(context);
		TextView tv = new TextView(context);
		iv.setId(0);
		tv.setId(1);
		linearLayout.addView(iv, params);
		linearLayout.addView(tv, params);

		return linearLayout;
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
				AndroidUtils.showToast(context, "Permission deny!");
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

			tv.setTextSize(20);
			tv.setPadding(5, 5, 5, 5);

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

				String filepath = list.get(position - 1).toString();
				tv.setText(filepath.substring(filepath.lastIndexOf("/") + 1));
			}
			return ll;
		}

		private int setFileIcon(File file) {
			int drawableId = 0;
			String fileFormat = file.getName().substring(file.getName().lastIndexOf(".") + 1);
			if (file.getName().endsWith("." + fileFormat)) {
				drawableId = context.getResources().getIdentifier(fileFormat, "drawable", context.getPackageName());
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
	 * Get the all files' name in specific path.
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
	 * Back to last level.
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
		 * @param fileName
		 *            The file's name you chose.
		 * @param currentPath
		 *            The path of directory where you are.
		 * @param fileContent
		 *            It will be null if the file content is null, or will be
		 *            the content of the file.
		 */
		public void onChoose(String fileName, String currentPath, String fileContent);
	}

	public void setFileOrDirOperateListener(FileOrDirOperateListener listener) {
		this.mListener = listener;
	}
}
