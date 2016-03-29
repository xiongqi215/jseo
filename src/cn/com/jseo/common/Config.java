package cn.com.jseo.common;

import java.io.IOException;
import java.util.Properties;

/**
 * 配置类
 * @author Justin
 *
 */
public class Config {
  private  static final String CONFIG_FILE_NAME="config.properties";
  private static Properties property;
  
  static {
	  property=new Properties();
	  try {
		property.load(Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
	} catch (IOException e) {
		e.printStackTrace();
	}
    }
  
  public  static String get(String key){
	  return property.getProperty(key);
  }
}
