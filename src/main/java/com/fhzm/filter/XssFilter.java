package com.fhzm.filter;
import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

@WebFilter(filterName="xssFilter",urlPatterns="/*")   
public class XssFilter implements Filter{  
  
    @Override  
    public void init(FilterConfig filterConfig) throws ServletException {  
    
    }  
  
    @Override  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)  
            throws IOException, ServletException {  
    
        XsslHttpServletRequestWrapper xssRequest = new XsslHttpServletRequestWrapper((HttpServletRequest)request);  
        chain.doFilter(xssRequest , response);   
    }  
      
    @Override  
    public void destroy() {  
          
    }  
      
} 