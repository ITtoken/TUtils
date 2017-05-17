package com.tianjj.tutils.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class In2Out extends Base{
	/**
	 * 杈撳叆杈撳嚭娴佺殑瀵规帴
	 * 
	 * @param in
	 *            杈撳叆娴佸璞�
	 * @return 娴佷腑鐨勫瓧绗︿覆鍐呭
	 * @throws IOException
	 */
	public static String getInputstreamInfo(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte b[] = new byte[1024];
		int len = 0;
		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		String result = out.toString();

		return result;
	}
}