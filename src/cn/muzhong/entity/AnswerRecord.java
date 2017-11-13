package cn.muzhong.entity;

public class AnswerRecord {
	private int id;
	private int truecount;//�����
	private int times;//��ʱ--��
	private int beyond;//����İٷ���
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTruecount() {
		return truecount;
	}
	public void setTruecount(int truecount) {
		this.truecount = truecount;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getBeyond() {
		return beyond;
	}
	public void setBeyond(int beyond) {
		this.beyond = beyond;
	}
	
}
