package kr.co.seoulit.logistics.logiinfosvc.help.service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kr.co.seoulit.logistics.logiinfosvc.help.dao.BoardDAO;
import kr.co.seoulit.logistics.logiinfosvc.help.to.boardTO2;

@Component
public class BoardInfoServiceImpl implements BoardInfoService {
	
	@Autowired
	private BoardDAO boardDAO;
	

	/*
	 * // SLF4J logger private static Logger logger =
	 * LoggerFactory.getLogger(BoardInfoServiceImpl.class);
	 */

	/*
	 * @Autowired private static BoardInfoService instance;
	 */

	@Override
	public ArrayList<boardTO2> getBoardList() {
		
		ArrayList<boardTO2> boardList = null;
		
			boardList = boardDAO.selectBoardList();
			
			System.out.println("boardList:"+boardList);
		return boardList;
	}
	public ArrayList<boardTO2> getBoardList(String username) {

		

	
		ArrayList<boardTO2> boardList = null;

			boardList = boardDAO.selectBoardList2(username);

		

		return boardList;
	}
	

		@Override
		public void addContent(boardTO2 bean) {
					
				// TODO Auto-generated method stub
			System.out.println("파사드에서 빈값:"+bean);
			boardDAO.insertContent(bean);	
				
		}
		
		public boardTO2 getcontents(String seq) {
			
			return boardDAO.selectcontent(seq);
			
		}
		
		public void deleteContents(String seq) {
			
			boardDAO.deleteboardcontent(seq);
			
		}
		
}
