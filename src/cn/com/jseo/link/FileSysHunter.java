package cn.com.jseo.link;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSysHunter implements LinkHunter{

	@Override
	public List<String> getAllSitLinks(String rootdir) throws Exception {
		String readFilePath=rootdir;
		 List<String> urlList=new ArrayList<String>();
		File file=new File(rootdir);
        String[] filelist = file.list();
        for (int i = 0; i < filelist.length; i++) {
                File readfile = new File(rootdir + "//" + filelist[i]);
                if (!readfile.isDirectory()) {
                	if(readfile.getName().endsWith(".html")){
//                        System.out.println("path=" + readfile.getPath());
//                        System.out.println("absolutepath="
//                                        + readfile.getAbsolutePath());
//                        System.out.println("name=" + readfile.getName());
                        String url=readfile.getAbsolutePath();
                        url= url.replace("E://xiongqi215.github.io//public//", "http://justin-x.cn/");
                        urlList.add(url);
                	}

                } else if (readfile.isDirectory()) {
                	urlList.addAll(getAllSitLinks(rootdir + "//" + filelist[i]));
                }
        }
		return urlList;
	}

	@Override
	public List<String> getAllJsLinks(String rootdir) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getAllCssLinks(String rootdir) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
public static void main(String[] args) throws Exception {
	FileSysHunter fsh=new FileSysHunter();
	 List<String> urlList=fsh.getAllSitLinks("E://xiongqi215.github.io//public");
	 System.out.println(urlList);
}
}
