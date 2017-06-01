package com.tianjj.tutils.base;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class In2Out extends Base{

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