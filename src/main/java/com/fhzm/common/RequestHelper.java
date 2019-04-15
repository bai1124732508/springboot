package com.fhzm.common;

import java.io.InputStream;

public class RequestHelper
{
	public static String contextRealPath;
	
	public static InputStream is;

	public static String getContextRealPath()
	{
		if(contextRealPath==null){
			//ServletContext servletContext = ContextLoader.getCurrentWebApplicationContext().getServletContext();
			contextRealPath = RequestHelper.class.getClassLoader().getResource("").getPath();
		}
		return contextRealPath;
	}

	public static void setContextRealPath(String contextRealPath)
	{
		RequestHelper.contextRealPath = contextRealPath;
	}
	
	public static InputStream getResource(String url) throws Exception{
		 is=RequestHelper.class.getResourceAsStream(url);
		return is;
	}
	
	public static void setResource(InputStream is)
	{
		RequestHelper.is = is;
	}
	
	
}
