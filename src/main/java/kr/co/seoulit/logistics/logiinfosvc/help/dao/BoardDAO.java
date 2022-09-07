package kr.co.seoulit.logistics.logiinfosvc.help.dao;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;

import kr.co.seoulit.logistics.logiinfosvc.help.to.boardTO2;
@Mapper
public interface BoardDAO {

	public ArrayList<boardTO2> selectBoardList();
	
	public ArrayList<boardTO2> selectBoardList2(String username);

	public void insertContent(boardTO2 bean);
	
	public boardTO2 selectcontent(String seq);
	
	public void deleteboardcontent(String seq);

	
	
}
