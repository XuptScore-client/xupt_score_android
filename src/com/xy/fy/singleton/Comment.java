package com.xy.fy.singleton;

public class Comment {
	private String name;// 回复人姓名
	private String date;// 回复日期
	private String time;// 回复时间
	private String content;// 回复内容

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "Comment [name=" + name + ", date=" + date + ", time=" + time
				+ ", content=" + content + "]";
	}

}
