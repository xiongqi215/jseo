package cn.com.jseo.http.support;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.List;

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
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import cn.com.jseo.link.RemoteLinkHunter;

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
		log.info("requset result:");
		log.info(response.getStatusLine().toString());// 响应状态
		log.info("-----------------------------------");

		Header[] heard = response.getAllHeaders();// 响应头
		log.info("response heard:");
		for (int i = 0; i < heard.length; i++) {
			log.info(heard[i]);
		}
		log.info("-----------------------------------");
		HttpEntity entity = response.getEntity();// 响应实体/内容
		log.info("response content length:" + entity.getContentLength());
		log.info("response content:");
	
		log.info(EntityUtils.toString(entity));
		
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
			postMethod.setHeader("User-Agent", "curl/7.12.1");
			postMethod.setHeader("Host" , "data.zz.baidu.com ");
			postMethod.setHeader("Content-Type" , "text/plain;charset=UTF-8"); 
//			postMethod.setHeader("Content-Length" , String.valueOf(entity.getContentLength())); 
		    exctueRequest(postMethod); // 执行请求，获取HttpResponse对象
		
	}

	private HttpResponse exctueRequest(HttpRequestBase request){
	  HttpResponse response=null;
	  
	  try {
		
		  log.info("excute request:"+request.getURI());
		  if(request instanceof HttpPost){
			  log.info("request "+((HttpPost)request).getEntity().getContentType());
			  log.info("request entity:"+ EntityUtils.toString(((HttpPost)request).getEntity(),"UTF-8"));
			  
		  }
		   log.info("-----------------------------------");
		response=this.getClient().execute(request);//执行请求，获取HttpResponse对象
		showResponse(response);
		int statuscode = response.getStatusLine().getStatusCode();//处理重定向
		if((statuscode == HttpStatus.SC_MOVED_TEMPORARILY) || (statuscode == HttpStatus.SC_MOVED_PERMANENTLY)
				|| (statuscode == HttpStatus.SC_SEE_OTHER) || (statuscode == HttpStatus.SC_TEMPORARY_REDIRECT)){
			
			Header redirectLocation=response.getFirstHeader("Location");
			String newuri=redirectLocation.getValue();
		      if((newuri!=null)||(!newuri.equals(""))){
		         log.info("redirect to "+newuri);
		      request.setURI(new URI(newuri));
		      response=this.getClient().execute(request);
		      showResponse(response);
		   }else {
			   log.info("Invalid redirect");
	  }
	  
		}
	} catch (Exception e) {
		e.printStackTrace();
	} finally{
		releaseConnection(request);//释放连接
	}
	  return response;
	  
  }

	public static void main(String[] args) throws Exception {
		//测试百度主动推送
		String url="http://data.zz.baidu.com/urls?site=justin-x.cn&token=SP174q9CqvSyOjn7&type=original";
		HttpClientSupport client = new HttpClientSupport();
		
		 RemoteLinkHunter hunter=new RemoteLinkHunter();
		   List<String> allLinks=hunter.getAllSitLinks("http://justin-x.cn","http://justin-x.cn/page/2/");
		   StringBuffer buffer=new StringBuffer();
		   for(String s:allLinks){
			   buffer.append(s).append("%n");
		   }
		client.doPost(url, new StringEntity(String.format(buffer.toString())));

	}
}
