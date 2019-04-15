package com.fhzm.common.file;

public class Term
{
	private String id;

	private String name;

	private String hiddenValue;

	private String displayValue;
	
	private String description;
	
	private Boolean ischoose;//是否被选中

	public Boolean getIschoose() {
		return ischoose;
	}

	public void setIschoose(Boolean ischoose) {
		this.ischoose = ischoose;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getHiddenValue()
	{
		return hiddenValue;
	}

	public void setHiddenValue(String hiddenValue)
	{
		this.hiddenValue = hiddenValue;
	}

	public String getDisplayValue()
	{
		return displayValue;
	}

	public void setDisplayValue(String displayValue)
	{
		this.displayValue = displayValue;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

}
