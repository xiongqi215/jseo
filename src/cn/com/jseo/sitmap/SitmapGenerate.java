package cn.com.jseo.sitmap;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import cn.com.jseo.link.RemoteLinkHunter;

public class SitmapGenerate {
//	<?xml version="1.0" encoding="utf-8"?>
//	<!-- XML文件需以utf-8编码-->
//	<urlset>
//	<!--必填标签-->
//	    <url>
//	        <!--必填标签,这是具体某一个链接的定义入口，每一条数据都要用<url>和</url>包含在里面，这是必须的 -->
//	        <loc>http://www.yoursite.com/yoursite.html</loc>
//	        <!--必填,URL链接地址,长度不得超过256字节-->
//	        <lastmod>2009-12-14</lastmod>
//	        <!--可以不提交该标签,用来指定该链接的最后更新时间-->
//	        <changefreq>daily</changefreq>
//	        <!--可以不提交该标签,用这个标签告诉此链接可能会出现的更新频率 -->
//	        <priority>0.8</priority>
//	        <!--可以不提交该标签,用来指定此链接相对于其他链接的优先权比值，此值定于0.0-1.0之间-->
//	    </url>
//	    <url>
//	        <loc>http://www.yoursite.com/yoursite2.html</loc>
//	        <lastmod>2010-05-01</lastmod>
//	        <changefreq>daily</changefreq>
//	        <priority>0.8</priority>
//	    </url>
//	</urlset>

   
   public void generateSitMap(String dest,String... sitUrls) throws Exception{
	   RemoteLinkHunter hunter=new RemoteLinkHunter();
	 
	   List<String> allLinks=hunter.getAllSitLinks(sitUrls);
	   
	   Element root = DocumentHelper.createElement("urlset");  
	   root.addNamespace("xmlns", "http://www.sitemaps.org/schemas/sitemap/0.9");
	   root.addNamespace("mobile", "http://www.sitemaps.org/schemas/sitemap/0.9");
       Document document = DocumentHelper.createDocument(root);  
       SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
         String lastMod=sdf.format(new Date());
       for(String link:allLinks){
    	   Element url = root.addElement("url");  
    	   url.addElement("loc").setText(link);
    	   url.addElement("lastmod").addText(lastMod);
    	   url.addElement("changefreq").addText("daily");
    	   url.addElement("changefreq").addText("daily");
    	   url.addElement("mobile:mobile").addAttribute("type","pc,mobile");
       }
       //把生成的xml文档存放在硬盘上  true代表是否换行  
       OutputFormat format = new OutputFormat("    ",true);  
       format.setEncoding("UTF-8");//设置编码格式  
       XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(dest),format);  
     
       xmlWriter.write(document);  
       xmlWriter.close();  
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
	 
	 public static void main(String[] args) throws Exception {
		 SitmapGenerate generate=new SitmapGenerate();
		 generate.generateSitMap("E:/xiongqi215.github.io/public/sitemap.xml","http://justin-x.cn","http://justin-x.cn/page/2/");
	}
}
