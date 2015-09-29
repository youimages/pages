package support.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import support.utils.out.Print;



public class HttpClientUtil {
	
	@SuppressWarnings("deprecation")
	private static HttpClient httpclient = new DefaultHttpClient();

	public static String getData(String url) {
		HttpGet httpGet =null;
		try {	
			httpGet = new HttpGet(url);
			HttpResponse httpResponseGet = httpclient.execute(httpGet);
			int codeGet = httpResponseGet.getStatusLine().getStatusCode();
			if (codeGet == 200) {
				HttpEntity entity = httpResponseGet.getEntity();
				return EntityUtils.toString(entity);
			}
		} catch (Exception e) {
			Print.out(url+"connect failed ");
			e.printStackTrace();
		}finally{
			httpGet.releaseConnection();
			//httpclient.getConnectionManager().shutdown();
		}
		return null;	
	}
}
