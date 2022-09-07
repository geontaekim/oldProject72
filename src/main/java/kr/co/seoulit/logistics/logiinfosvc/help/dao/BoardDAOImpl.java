package kr.co.seoulit.logistics.logiinfosvc.help.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.security.auth.message.callback.PrivateKeyCallback.Request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeDetailTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.CodeTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.ImageTO;
import kr.co.seoulit.logistics.logiinfosvc.compinfo.to.LatLngTO;

/*
public class BoardDAOImpl implements BoardDAO {

	// SLF4J logger
	private static Logger logger = LoggerFactory.getLogger(BoardDAOImpl.class);

	// 싱글톤
	private static BoardDAO instance = new BoardDAOImpl();

	private BoardDAOImpl() {
	}

	public static BoardDAO getInstance() {
		
		if (logger.isDebugEnabled()) {
			logger.debug("@ BoardDAOImpl 객체접근");
		}
		
		return instance;
	}

	@Override
	public ArrayList<boardTO> selectBoardList() {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : selectCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<boardTO> boardList = new ArrayList<boardTO>();
		
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM BOARD ORDER BY SEQ_NUM ");		//코드넘버리스트 불러오기

			pstmt = conn.prepareStatement(query.toString());
			rs = pstmt.executeQuery();

			
			  while (rs.next()) {
			 
			 boardTO bean = new boardTO(); bean.setUsername(rs.getString("username"));
			  bean.setFrmTitle(rs.getString("frmtitle"));
			 bean.setFrmContents(rs.getString("frmcontents"));
			 bean.setErrnum(rs.getString("errnum"));
			 bean.setSeq_num(rs.getString("seq_num")); boardList.add(bean); }
			 

			return boardList;

	}

	@Override
	public void insertContent(boardTO bean) {

		if (logger.isDebugEnabled()) {
			logger.debug("BoardDAOImpl : insertContent 시작");
		}
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
		System.out.println(bean.getUsername());
			query.append("{call P_CRESEQ(?,?,?,?)}");
			pstmt = conn.prepareStatement(query.toString());	
			pstmt.setString(1,bean.getUsername() );
			pstmt.setString(2, bean.getFrmTitle());
			pstmt.setString(3, bean.getFrmContents());
			pstmt.setString(4, bean.getErrnum());

			rs = pstmt.executeQuery();

		} catch (Exception sqle) {

			throw new DataAccessException(sqle.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);

		}

	}
	public boardTO selectcontent(String seq) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : selectCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM BOARD WHERE SEQ_NUM=? ");		
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1,seq );
			rs = pstmt.executeQuery();
			boardTO bean = null;
			while (rs.next()) {

				bean = new boardTO();
				bean.setUsername(rs.getString("username"));
				bean.setFrmTitle(rs.getString("frmtitle"));
				bean.setFrmContents(rs.getString("frmcontents"));
				bean.setErrnum(rs.getString("errnum"));
				bean.setSeq_num(rs.getString("seq_num"));
				
			}

			return bean;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	
	public void deleteboardcontent(String seq) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : selectCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		

		try {
			conn = dataSourceTransactionManager.getConnection();
			StringBuffer query = new StringBuffer();
			query.append("delete from board where seq_num=?");		
			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1,seq );
			rs = pstmt.executeQuery();
			


		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	public ArrayList<boardTO> selectBoardList(String username) {

		if (logger.isDebugEnabled()) {
			logger.debug("CodeDAOImpl : selectCodeList 시작");
		}

		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		ArrayList<boardTO> boardList = new ArrayList<boardTO>();

		try {
			conn = dataSourceTransactionManager.getConnection();

			StringBuffer query = new StringBuffer();
			query.append("SELECT * FROM BOARD WHERE USERNAME=? ORDER BY SEQ_NUM ");		//코드넘버리스트 불러오기

			pstmt = conn.prepareStatement(query.toString());
			pstmt.setString(1,username);
			rs = pstmt.executeQuery();

			while (rs.next()) {

				boardTO bean = new boardTO();
				bean.setUsername(rs.getString("username"));
				bean.setFrmTitle(rs.getString("frmtitle"));
				bean.setFrmContents(rs.getString("frmcontents"));
				bean.setErrnum(rs.getString("errnum"));
				bean.setSeq_num(rs.getString("seq_num"));
				boardList.add(bean);
			}

			return boardList;

		} catch (SQLException e) {

			throw new DataAccessException(e.getMessage());

		} finally {

			dataSourceTransactionManager.close(pstmt, rs);
		}
	}
	
	
	
}
*/
