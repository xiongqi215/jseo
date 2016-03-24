package cn.com.jseo.http.support;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class HttpClientSupport {
	private Log log = LogFactory.getLog(this.getClass());
    private HttpClient client;
    
	public HttpClient getClient() {
		 client = new DefaultHttpClient();// 获取HttpClient对象
		return client;
	}

	private void releaseConnection(HttpRequestBase request) {
		if (request != null) {
			request.releaseConnection();
		}
		this.client.getConnectionManager().shutdown();
	}

	private void showResponse(HttpResponse response) throws ParseException,
			IOException {
		log.debug("requset result:");
		log.debug(response.getStatusLine().toString());// 响应状态
		log.debug("-----------------------------------");

		Header[] heard = response.getAllHeaders();// 响应头
		log.debug("response heard:");
		for (int i = 0; i < heard.length; i++) {
			log.debug(heard[i]);
		}
		log.debug("-----------------------------------");
		HttpEntity entity = response.getEntity();// 响应实体/内容
		log.debug("response content length:" + entity.getContentLength());
		log.debug("response content:");
	
		log.debug(EntityUtils.toString(entity));
		
	}

	public void doGet(String uri) {// get方法提交
		HttpGet getMethod = null;
		getMethod = new HttpGet(uri);// 获取HttpGet对象，使用该对象提交get请求
	    exctueRequest(getMethod);
	}

	public void doPost(String uri,HttpEntity entity) {// post方法提交
		HttpPost postMethod = null;

			postMethod = new HttpPost(uri);
			
			postMethod.setEntity(entity);//设置请求实体，例如表单数据
		    exctueRequest(postMethod); // 执行请求，获取HttpResponse对象
		
	}

	private HttpResponse exctueRequest(HttpRequestBase request){
	  HttpResponse response=null;
	  
	  try {
		
		  log.debug("excute request:"+request.getURI());
		  if(request instanceof HttpPost){
			  log.debug("request "+((HttpPost)request).getEntity().getContentType());
			  log.debug("request entity:"+ EntityUtils.toString(((HttpPost)request).getEntity(),"UTF-8"));
			  
		  }
		   log.debug("-----------------------------------");
		response=this.getClient().execute(request);//执行请求，获取HttpResponse对象
		showResponse(response);
		int statuscode = response.getStatusLine().getStatusCode();//处理重定向
		if((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)){
			
			Header redirectLocation=response.getFirstHeader("Location");
			String newuri=redirectLocation.getValue();
		      if((newuri!=null)||(!newuri.equals(""))){
		         log.debug("redirect to "+newuri);
		      request.setURI(new URI(newuri));
		      response=this.getClient().execute(request);
		      showResponse(response);
		   }else {
			   log.debug("Invalid redirect");
	  }
	  
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		releaseConnection(request);//释放连接
	}
	  return response;
	  
  }
//   public InputStreamEntity createStreamEntity(ByteInputStream bis,Long EntitySize,ContentType type){
//	   InputStreamEntity  uefEntity=new InputStreamEntity(bos.newInputStream(),bos.size(),contentType);
//   }
	public static void main(String[] args) {
		HttpClientSupport client = new HttpClientSupport();
//		client.doGet("http://www.baidu.com/s?wd=HttpClient");
//		
//		List<NameValuePair> formparams = new ArrayList<NameValuePair>();// 设置表格参数
//		formparams.add(new BasicNameValuePair("usrname", "admin"));
//		formparams.add(new BasicNameValuePair("password", "123456"));
//		UrlEncodedFormEntity uefEntity = null;
//		try {
//			uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");//获取实体对象
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		try {
//		String soapRequestData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>"
//				                 + "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">"
//				                 + " <soap:Body>"
//				                + " <ns2:sayHello xmlns:ns2=\"http://hellowWorld.test.server.com/\">"
//			                + " <name>蛋蛋</name>"
//				                + " </ns2:sayHello>"
//				            + "</soap:Body>"
//				                 + " </soap:Envelope>";
//		byte[] b=soapRequestData.getBytes("utf-8");
//		InputStream is=new ByteInputStream(b, b.length);
//		ContentType contentType= ContentType.create("application/xml", Charset.forName("UTF-8"));
//		InputStreamEntity  uefEntity=new InputStreamEntity(is, b.length,contentType);
//		client.doPost("http://localhost:8080/ws/HelloWorld",uefEntity);
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		client.doGet("http://api.map.baidu.com/location/ip?ak=TceKupTf4WRoGoPBVbNhHYpV");
	}
}
