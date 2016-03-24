package cn.com.jseo.link;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 * 抓取网页链接信息
 * @author Justin
 *
 */
public class LinkHunter {
	private Log log = LogFactory.getLog(this.getClass());
 
 /**
  * 获取网站内所有站内链接
  * @param sitUrl
  * @return
  * @throws Exception
  */
 public List<String> getAllSitLinks(String sitUrl) throws Exception{
	   
	   List<String> urlList=new ArrayList<String>();
	   
	   Document doc = Jsoup.connect(sitUrl).get();
	   Elements links = doc.select(LinkType.AHREF.getValue());
	   
	     for (Element link : links) {
	    	 String ulr=link.attr("abs:href");
	    	 
	    	 if(ulr.indexOf(sitUrl)!=-1){
	    		 
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
 /**
  * 获取所有javascript 引用链接
  * @param sitUrl
  * @return
  * @throws Exception
  */
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

/**
 * 获取所有css 引用链接
 * @param sitUrl
 * @return
 * @throws Exception
 */
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
}
