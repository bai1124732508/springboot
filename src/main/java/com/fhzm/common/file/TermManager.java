package com.fhzm.common.file;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TermManager {
	private static Logger log = LoggerFactory.getLogger(TermManager.class);

	private static Map<String, Map<String, Term>> rootMap;

	/**
	 * 根据categoryId获得category下面的所有term节点
	 * 
	 * @param categoryId
	 * @return
	 */
	public static Map<String, Term> getTermMap(String categoryId)
	{
		Map<String, Term> retMap = getRootMap().get(categoryId);
		if(retMap == null)
		{
			retMap = new HashMap<String, Term>();
			log.error("Term.xml中未找到categoryId = [" + categoryId + "]");
		}
		return retMap;
	}

	/**
	 * 根据categoryId获得category下面的所有term节点
	 * 
	 * @param categoryId
	 * @return
	 */
	public static List<Term> getTermList(String categoryId)
	{
		try
		{
			return TermsDefineXmlParser.getList(categoryId);
		}
		catch(Exception e)
		{
			log.error("读取Term.xml文件异常", e);
			return new ArrayList<Term>();
		}
	}

	/**
	 * 根据categoryId和termId返回一个term对象
	 * 
	 * @param categoryId
	 * @param TermId
	 * @return
	 */
	public static Term getTerm(String categoryId, String TermId)
	{
		return getTermMap(categoryId).get(TermId);
	}

	/**
	 * 根据categoryId和termId返回term的name值
	 * 
	 * @param categoryId
	 * @param TermId
	 * @return
	 */
	public static String getName(String categoryId, String TermId)
	{
		Term term = getTerm(categoryId, TermId);
		if(term == null)
		{
			return "";
		}
		else
		{
			return term.getName();
		}
	}

	/**
	 * 根据categoryId和termId返回term的name值 由于是在页面显示的，所以当name为空时，返回一个&nbsp;占位符
	 * 
	 * @param categoryId
	 * @param TermId
	 * @return
	 */
	public static String getHTMLName(String categoryId, String TermId)
	{
		Term term = getTerm(categoryId, TermId);
		String name = "&nbsp;";
		if(term != null && StringUtils.isNotEmpty(term.getName()))
			name = term.getName();
		return name;
	}

	/**
	 * 根据categoryId和termId返回term的HiddenValue值
	 * 
	 * @param categoryId
	 * @param TermId
	 * @return
	 */
	public static String getHiddenValue(String categoryId, String TermId)
	{
//		log.info("-----------getHiddenValue-----------");
//		log.info("categoryId = " + categoryId);
//		log.info("TermId = " + TermId);
//		log.info(getTerm(categoryId, TermId));
		return getTerm(categoryId, TermId).getHiddenValue();
	}

	/**
	 * 根据categoryId和termId返回term的DisplayValue值
	 * 
	 * @param categoryId
	 * @param TermId
	 * @return
	 */
	public static String getDisplayValue(String categoryId, String TermId)
	{
		return getTerm(categoryId, TermId).getDisplayValue();
	}

	/**
	 * 根据categoryId和termName返回term的HiddenValue值
	 * 
	 * @param categoryId
	 * @param name
	 * @return
	 */
	public static String getHiddenValueByName(String categoryId, String name)
	{
		List<Term> termList = getTermList(categoryId);
		for(Term term : termList)
		{
			if(name.equals(term.getName()))
			{
				return term.getHiddenValue();
			}
		}
		return "";
	}
	/**
	 * 根据categoryId和termName返回term的DisplayValue值
	 * @param categoryId
	 * @param name
	 * @return
	 */
	public static String getDisplayValueByName(String categoryId, String name)
	{
		List<Term> termList = getTermList(categoryId);
		for(Term term : termList)
		{
			if(name.equals(term.getName()))
			{
				return term.getDisplayValue();
        	}
		}
		return "";
	}
	/**
	 * 根据categoryId和termName返回term的id值
	 * 
	 * @param categoryId
	 * @param name
	 * @return
	 */
	public static String getIdByName(String categoryId, String name)
	{
		List<Term> termList = getTermList(categoryId);
		for(Term term : termList)
		{
			if(name.equals(term.getName()))
			{
				return term.getId();
			}
		}
		return "";
	}
	
	public static String getIdByHiddenValue(String categoryId, String hiddenValue)
	{
		List<Term> termList = getTermList(categoryId);
		for(Term term : termList)
		{
			if(hiddenValue.equals(term.getHiddenValue()))
			{
				return term.getId();
			}
		}
		return "";
	}

	/**
	 * 获得rootMap
	 * 
	 * @return
	 */
	public static Map<String, Map<String, Term>> getRootMap()
	{
		if(rootMap == null)
		{
			try
			{
				rootMap = TermsDefineXmlParser.getMap();
			}
			catch(Exception e)
			{
				rootMap = new HashMap<String, Map<String, Term>>();
				log.error("读取Term文件出错！", e);
			}
		}
		return rootMap;
	}
}
