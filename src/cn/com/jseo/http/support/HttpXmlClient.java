package cn.com.jseo.http.support;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.util.ArrayList;  
import java.util.HashMap;
import java.util.List;  
import java.util.Map;  
import java.util.Set;  
import org.apache.http.Header;
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.NameValuePair;  
import org.apache.http.ParseException;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.client.methods.HttpUriRequest;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.protocol.HTTP;  
import org.apache.http.util.EntityUtils;  
import org.apache.log4j.Logger;  

public class HttpXmlClient {
	static List list=new ArrayList();
	 private static Logger log = Logger.getLogger(HttpXmlClient.class);  
        
	 
	    public static List   test(String url, Map<String, String> params) throws Exception{
	    	 DefaultHttpClient httpclient = new DefaultHttpClient();  
		        String body = null;  
		          
		        HttpPost httpost = new HttpPost(url);  
		        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
		          
		        Set<String> keySet = params.keySet();  
		        for(String key : keySet) {  
		            nvps.add(new BasicNameValuePair(key, params.get(key)));  
		        }  
		          
		
		            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
		       
		          
		        HttpResponse response = sendRequest(httpclient, httpost);  
		        
		        HttpEntity entity = response.getEntity();  
		          
		        String charset = EntityUtils.getContentCharSet(entity);  
		      Header[] header=response.getHeaders("Location");
		           
		            body = EntityUtils.toString(entity);  
		            if("http://59.252.140.11:8999/MPMS/login.jsp?error=1".equals(header[0].getValue().trim().toString())){
//		            	for (Map.Entry<String, String> entry : params.entrySet()) {
//		            		   System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
//		            		  }
		            	list.add(params);
		            }
		            
		        System.out.println(header[0].getValue());
		        
		        
		        httpclient.getConnectionManager().shutdown();  
		        return list;
	    }
	    public static String post(String url, Map<String, String> params) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpPost post = postForm(url, params);  
	          
	        body = invoke(httpclient, post);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return body;  
	    }  
	      
	    public static String get(String url) {  
	        DefaultHttpClient httpclient = new DefaultHttpClient();  
	        String body = null;  
	          
	        log.info("create httppost:" + url);  
	        HttpGet get = new HttpGet(url);  
	        body = invoke(httpclient, get);  
	          
	        httpclient.getConnectionManager().shutdown();  
	          
	        return body;  
	    }  
	          
	      
	    private static String invoke(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	          
	        HttpResponse response = sendRequest(httpclient, httpost);  
	        String body = paseResponse(response);  
	          
	        return body;  
	    }  
	  
	    private static String paseResponse(HttpResponse response) {  
	        HttpEntity entity = response.getEntity();  
	          
	        String charset = EntityUtils.getContentCharSet(entity);  
	        log.info(charset);  
	      Header[] header=response.getHeaders("Location");
	        String body = null;  
	        try {  
	            body = EntityUtils.toString(entity);  
	            log.info(body);  
	        } catch (ParseException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return body;  
	    }  
	  
	    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
	            HttpUriRequest httpost) {  
	        HttpResponse response = null;  
	          
	        try {  
	            response = httpclient.execute(httpost);  
	        } catch (ClientProtocolException e) {  
	            e.printStackTrace();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        }  
	        return response;  
	    }  
	  
	    private static HttpPost postForm(String url, Map<String, String> params){  
	          
	        HttpPost httpost = new HttpPost(url);  
	        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
	          
	        Set<String> keySet = params.keySet();  
	        for(String key : keySet) {  
	            nvps.add(new BasicNameValuePair(key, params.get(key)));  
	        }  
	          
	        try {  
	            log.info("set utf-8 form entity to httppost");  
	            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
	        } catch (UnsupportedEncodingException e) {  
	            e.printStackTrace();  
	        }  
	          
	        return httpost;  
	    }  
	    public static void main(String[] args) throws Exception {
	    	
//	    	
	    	BufferedReader reader=new BufferedReader(new FileReader("d:/账户.txt"));
	    	String i;
	    	while((i=reader.readLine())!=null){
	    		String[] s=i.split(",");
	    		Map<String, String> params = new HashMap<String, String>();  
		    	params.put("j_username", s[0]);  
		    	params.put("j_password", s[1]);  
		    	      
		   HttpXmlClient.test("http://59.252.140.11:8999/MPMS/j_spring_security_check", params);  
		    	
	    	}
System.out.println(list);
		}
	}  
