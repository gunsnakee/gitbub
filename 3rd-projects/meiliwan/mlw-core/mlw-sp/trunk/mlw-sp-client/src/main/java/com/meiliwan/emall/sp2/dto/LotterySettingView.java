package com.meiliwan.emall.sp2.dto;


public class LotterySettingView{

	private Integer id;

    private String lotteryName;

    private String lotteryProduct;

    private Integer times;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLotteryName() {
		return lotteryName;
	}

	public void setLotteryName(String lotteryName) {
		this.lotteryName = lotteryName;
	}

	public String getLotteryProduct() {
		return lotteryProduct;
	}

	public void setLotteryProduct(String lotteryProduct) {
		this.lotteryProduct = lotteryProduct;
	}

	public Integer getTimes() {
		return times;
	}

	public void setTimes(Integer times) {
		this.times = times;
	}


}