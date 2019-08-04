package com.xiaovvv.scaletrans;
import java.lang.String;
import java.lang.Integer;

public class ScaleThransform
{
	private int aScale,bScale;//将aScale进制转换为bScale进制
	private String afterText="";
	
	public void setParameter(int aS,int bS)
	{
		aScale=aS;
		bScale=bS;
	}
	public void changeTO(String changedStr)//转换的方法
	{
		Integer tStation=new Integer(1);
		String afterChangedText="";
		
		tStation=tStation.parseInt(changedStr,aScale);//先转化为十进制
		afterChangedText=tStation.toString(tStation,bScale);//再转化为bScale进制
		afterText=afterChangedText;
	}
	public String getTransformatnl()
	{
		return afterText;
	}
}
