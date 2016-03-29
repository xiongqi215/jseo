package cn.com.jseo.link;

import java.util.List;

public interface LinkHunter {

	/**
	  * 获取网站内所有站内链接
	  * @param sitUrl
	  * @return
	  * @throws Exception
	  */
	List<String> getAllSitLinks(String sitUrl) throws Exception;

	/**
	  * 获取所有javascript 引用链接
	  * @param sitUrl
	  * @return
	  * @throws Exception
	  */
	List<String> getAllJsLinks(String sitUrl) throws Exception;

	/**
	 * 获取所有css 引用链接
	 * @param sitUrl
	 * @return
	 * @throws Exception
	 */
	List<String> getAllCssLinks(String sitUrl) throws Exception;

}