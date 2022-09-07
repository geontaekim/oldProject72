package kr.co.seoulit.logistics.logiinfosvc.compinfo.to;

public class BoardTO {
	private int idx;
	private String title;
	private String contents;
	private int count;
	private String writer;
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContents() {
		return contents;
	}
	public void setContents(String contents) {
		this.contents = contents;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	@Override
	public String toString() {
		return "BoardTO [idx=" + idx + ", title=" + title + ", contents=" + contents + ", count=" + count + ", writer="
				+ writer + "]";
	}
}
