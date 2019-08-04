package com.example.searchcity.entity;

/**封装城市信息的对象*/
public class CityEntity {
	/**该城市的id*/
	private int id;
	
	/**城市名称*/
	private String cityName;
	
	/**城市拼音*/
	private String cityPinyin;
	
	/**城市首字母*/
	private String shortName;
	
	public CityEntity(int id,String cityName,String pinyin,String shortName){
		this.id = id;
		this.cityName = cityName;
		this.cityPinyin = pinyin;
		this.shortName = shortName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityPinyin() {
		return cityPinyin;
	}

	public void setCityPinyin(String cityPinyin) {
		this.cityPinyin = cityPinyin;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@Override
	public String toString() {
		return cityName;
	}
	
	
}
