package cn.com.jseo.link;

public enum LinkType {
     
	AHREF("a[href]"),SRC("[src]"),ALINK("link[href]");
	
	private String value;
	
	private LinkType(String value){
		this.value=value;
	}
	
	public  String getValue(){
		return this.value;
	}
}
