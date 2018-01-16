package com.xxl.job.core.util;

import com.xxl.job.core.enums.ProtocolEnum;
import com.xxl.job.core.rpc.codec.ResponseProtocolBytes;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Enumeration;

/**
 * httpclient util
 * @author xuxueli 2015-10-31 19:50:41
 */
public class HttpClientUtil {
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	/**
	 * post request
	 */
	public static byte[] postRequest(String reqURL, byte[] date, ProtocolEnum protocol) throws Exception {

		ResponseProtocolBytes result = new ResponseProtocolBytes();

		byte[] responseBytes = null;
		
		HttpPost httpPost = new HttpPost(reqURL);
		//CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpClient httpClient = HttpClients.custom().disableAutomaticRetries().build();	// disable retry

		try {
			// init post
			/*if (params != null && !params.isEmpty()) {
				List<NameValuePair> formParams = new ArrayList<NameValuePair>();
				for (Map.Entry<String, String> entry : params.entrySet()) {
					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}*/

			// timeout
			RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(10000)
                    .setSocketTimeout(10000)
                    .setConnectTimeout(10000)
                    .build();

			httpPost.setConfig(requestConfig);

			// data
			if (date != null) {
				httpPost.setEntity(new ByteArrayEntity(date, protocol.getContentType()));
			}
			// do post
			HttpResponse response = httpClient.execute(httpPost);

			HttpEntity entity = response.getEntity();
			if (null != entity) {
				responseBytes = EntityUtils.toByteArray(entity);
				EntityUtils.consume(entity);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw e;
		} finally {
			httpPost.releaseConnection();
			try {
				httpClient.close();
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}
		return responseBytes;
	}

	/**
	 * read bytes from http request
	 * @param request
	 * @return
	 * @throws IOException 
	 */
	public static final byte[] readBytes(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");
        int contentLen = request.getContentLength();
		InputStream is = request.getInputStream();
		if (contentLen > 0) {
			int readLen = 0;
			int readLengthThisTime = 0;
			byte[] message = new byte[contentLen];
			try {
				while (readLen != contentLen) {
					readLengthThisTime = is.read(message, readLen, contentLen - readLen);
					if (readLengthThisTime == -1) {
						break;
					}
					readLen += readLengthThisTime;
				}
				return message;
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
				throw e;
			}
		}
		return new byte[] {};
	}

	/**
	 * read bytes from http request
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static final String readJsonString(HttpServletRequest request) throws IOException {
		request.setCharacterEncoding("UTF-8");

		InputStream is = request.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		String line = null;
		StringBuffer content = new StringBuffer();
		while ((line = br.readLine()) != null) {
			content.append(line);
		}
		String reqStr = content.toString().trim();

		logger.info(reqStr);
		return reqStr;
	}

}
