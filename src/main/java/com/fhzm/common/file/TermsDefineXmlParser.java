package com.fhzm.common.file;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import com.fhzm.common.RequestHandler;
import com.fhzm.common.RequestHelper;




/**
 * 解析Term文件
 * @author KID
 *
 */
public class TermsDefineXmlParser
{
	private static final String termFilePath = "/dictionary/Terms.xml";

	/**
	 * 将term文件的内容封装到rootMap中
	 * @return
	 * @throws DocumentException
	 */
	public static Map<String, Map<String, Term>> getMap() throws Exception
	{
		Map<String, Map<String, Term>> rootMap = new HashMap<String, Map<String, Term>>();
		
		SAXReader reader = new SAXReader();
		InputStream resource = RequestHelper.getResource(termFilePath);
		Document doc = reader.read(resource);
		Element root = doc.getRootElement();
		for(Iterator i = root.elementIterator("category"); i.hasNext();)
		{
			// 获取category id

			Element category = (Element)i.next();
			String id = category.attributeValue("id");
			Map<String, Term> categoryMap = new HashMap<String, Term>();

			for(Iterator j = category.elementIterator("term"); j.hasNext();)
			{
				Element foo1 = (Element)j.next();
				String termId = foo1.attributeValue("id");
				Term term = new Term();
				term.setId(foo1.attributeValue("id"));
				term.setName(foo1.attributeValue("name"));
				term.setHiddenValue(foo1.attributeValue("hiddenValue"));
				term.setDisplayValue(foo1.attributeValue("displayValue"));
				term.setDescription(foo1.attributeValue("description"));
				categoryMap.put(termId, term);
			}

			rootMap.put(id, categoryMap);
		}
		
		return rootMap;
	}
	
	/**
	 * 将term文件的内容封装到List中
	 * @return
	 * @throws DocumentException
	 */
	public static List<Term> getList(String categoryId) throws Exception
	{
		List<Term> categoryList = new ArrayList<Term>();
		
		SAXReader reader = new SAXReader();
		InputStream resource = RequestHelper.getResource(termFilePath);
		Document doc = reader.read(resource);
		Element root = doc.getRootElement();
		
		for(Iterator i = root.elementIterator("category"); i.hasNext();)
		{
			// 获取category id

			Element category = (Element)i.next();
			String id = category.attributeValue("id");
			if(id.equals(categoryId))
			{
				for(Iterator j = category.elementIterator("term"); j.hasNext();)
				{
					Element foo1 = (Element)j.next();
					Term term = new Term();
					term.setId(foo1.attributeValue("id"));
					term.setName(foo1.attributeValue("name"));
					term.setHiddenValue(foo1.attributeValue("hiddenValue"));
					term.setDisplayValue(foo1.attributeValue("displayValue"));
					categoryList.add(term);
				}
				
				break;
			}
		}
		
		return categoryList;
	}
}
