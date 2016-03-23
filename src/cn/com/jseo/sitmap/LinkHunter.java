package cn.com.jseo.sitmap;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class LinkHunter {
 public static void main(String[] args) throws IOException {
	 
//	 Validate.isTrue(args.length == 1, "usage: supply url to fetch");
     String url = "http://justin-x.cn";
     print("Fetching %s...", url);

     Document doc = Jsoup.connect(url).get();
     Elements links = doc.select("a[href]");
     Elements media = doc.select("[src]");
     Elements imports = doc.select("link[href]");

     print("\nMedia: (%d)", media.size());
     for (Element src : media) {
         if (src.tagName().equals("img"))
             print(" * %s: <%s> %sx%s (%s)",
                     src.tagName(), src.attr("abs:src"), src.attr("width"), src.attr("height"),
                     trim(src.attr("alt"), 20));
         else
             print(" * %s: <%s>", src.tagName(), src.attr("abs:src"));
     }

     print("\nImports: (%d)", imports.size());
     for (Element link : imports) {
         print(" * %s <%s> (%s)", link.tagName(),link.attr("abs:href"), link.attr("rel"));
     }

     print("\nLinks: (%d)", links.size());
     for (Element link : links) {
         print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
     }
 }
 public List<String> getAllLinks(String sitUrl) throws Exception{
	   
	   List<String> urlList=new ArrayList<String>();
	   
	   Document doc = Jsoup.connect(sitUrl).get();
	   Elements links = doc.select("a[href]");
	   
	   
	     for (Element link : links) {
//	         print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
//	    	 System.out.println(link.attr("abs:href")+"："+trim(link.text(),35));
	    	 String ulr=link.attr("abs:href");
	    	 if(ulr.indexOf(sitUrl)!=-1){
	    		 if(urlList.contains(ulr)){
	    			 continue;
	    		 }
	    		 urlList.add(ulr);
	    	 }
	     }
	return urlList;
 }
 
 private static void print(String msg, Object... args) {
     System.out.println(String.format(msg, args));
 }

 private static String trim(String s, int width) {
     if (s.length() > width)
         return s.substring(0, width-1) + ".";
     else
         return s;
 }
}
