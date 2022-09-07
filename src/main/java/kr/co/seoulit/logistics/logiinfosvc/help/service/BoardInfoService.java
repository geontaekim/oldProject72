package kr.co.seoulit.logistics.logiinfosvc.help.service;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import kr.co.seoulit.logistics.logiinfosvc.help.to.boardTO2;

@Component
public interface BoardInfoService {

	public ArrayList<boardTO2> getBoardList();
	
	public ArrayList<boardTO2> getBoardList(String username);

	public void addContent(boardTO2 bean);	
	
	public boardTO2 getcontents(String seq);
	
	public void deleteContents(String seq);	
	
}
