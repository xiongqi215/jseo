package cn.com.jseo.link;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.com.jseo.common.Config;
/**
 * 抓取网页链接信息
 * @author Justin
 *
 */
public class RemoteLinkHunter implements LinkHunter {
	private Log log = LogFactory.getLog(this.getClass());
	private String[] ignores=Config.get("url_ignore").split(",");
	private String sit_url=Config.get("sit_url");
	
	
	 public List<String> getAllSitLinks(String... sitUrl) throws Exception{
		 List<String> urlList=new ArrayList<String>();
		 for(String url:sitUrl){
			 List<String> urls=this.getAllSitLinks(url);
			 urlList.addAll(urls);
		 }
		return urlList;
		 
	 }
 /* (non-Javadoc)
 * @see cn.com.jseo.link.LinkHunter#getAllSitLinks(java.lang.String)
 */
 @Override
public List<String> getAllSitLinks(String sitUrl) throws Exception{
	   
	   List<String> urlList=new ArrayList<String>();
	 
	   String fileSuffix=Config.get("file_suffix");
	   Document doc = Jsoup.connect(sitUrl).get();
	   Elements links = doc.select(LinkType.AHREF.getValue());
	   
	     for (Element link : links) {
	    	 String ulr=link.attr("abs:href");
	    	 
	    	 if(ulr.indexOf(sit_url)!=-1){
	    		 
	    		 if(urlIgnore(ulr)){
	    			 log.info("get sit url: "+ulr+" text:"+link.text()+"[ ignore cause config ]");
	    			 continue;
	    		 }
	    		 ulr+=fileSuffix;
	    		 if(urlList.contains(ulr)){
	    			 log.info("get sit url: "+ulr+" text:"+link.text()+"[ ignore cause exist ]");
	    			 continue;
	    		 }
	    		 log.info("get sit url: "+ulr+" text:"+link.text());
	    		 urlList.add(ulr);
	    	 }
	     }
   	  log.info("get sit link finish, total : "+urlList.size());
	return urlList;
 }
 /* (non-Javadoc)
 * @see cn.com.jseo.link.LinkHunter#getAllJsLinks(java.lang.String)
 */
@Override
public List<String> getAllJsLinks (String sitUrl) throws Exception{
	 Document doc = Jsoup.connect(sitUrl).get();
	 Elements links = doc.select(LinkType.SRC.getValue());
	 
	 List<String> urlList=new ArrayList<String>();
	 for (Element link : links) {
    	 
    	 if(link.tagName().toLowerCase().equals("script")){
    		 String ulr=link.attr("abs:src");
    		 if(urlList.contains(ulr)){
    			 log.info("get javascript src: "+ulr+"[ ignore cause exist ]");
    			 continue;
    		 }
    		 log.info("get javascript src: "+ulr);
    		 urlList.add(ulr);
    	 }
     }
	 log.info("get javascript src finish, total : "+urlList.size());
	return urlList;
}

/* (non-Javadoc)
 * @see cn.com.jseo.link.LinkHunter#getAllCssLinks(java.lang.String)
 */
@Override
public List<String> getAllCssLinks (String sitUrl) throws Exception{
	 Document doc = Jsoup.connect(sitUrl).get();
	 Elements links = doc.select(LinkType.ALINK.getValue());
	 
	 List<String> urlList=new ArrayList<String>();
	 for (Element link : links) {
   	 
   	 if(link.attr("rel").toLowerCase().equals("stylesheet")){
   		String ulr=link.attr("abs:href");
   		 if(urlList.contains(ulr)){
   			 log.info("get css link: "+ulr+"[ ignore cause exist ]");
   			 continue;
   		 }
   		 log.info("get css link: "+ulr);
   		 urlList.add(ulr);
   	 }
   	 
    }
	 log.info("get css link finish, total : "+urlList.size());
	return urlList;
}

private boolean urlIgnore(String url){
	for(int i=0;i<ignores.length;i++){
		if(url.indexOf(ignores[i])!=-1){
			return true;
		}
	}
	return false;
}
}
