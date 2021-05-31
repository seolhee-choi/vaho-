package site.itwill.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import site.itwill.dto.CartDTO;
import site.itwill.dto.ProductDTO;

public class CartDAO extends JdbcDAO{
	private static CartDAO _dao;
	 
	public CartDAO() {
		// TODO Auto-generated constructor stub
	}
	
	static {
		_dao=new CartDAO();
	}
	
	public static CartDAO getDao() {
		return _dao;	
	}
	
	public List<CartDTO> getemail(String email) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<CartDTO> productList=new ArrayList<CartDTO>();
		try {
			con=getConnection();
			String sql="select * from cart where login_mail=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, email);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				CartDTO cart=new CartDTO();
				cart.setLoginid(rs.getString("login_mail"));
				cart.setNo(rs.getInt("no"));
				cart.setItem(rs.getString("item"));
				cart.setPrice(rs.getString("price"));
				cart.setCount(rs.getInt("count"));
				cart.setDeliveryP(rs.getString("Delivery_P"));
				cart.setTotalP(rs.getString("Total_P"));
				cart.setDeliveryType(rs.getString("Delivery_type"));
				productList.add(cart);
			}
		} catch (Exception e) {
			System.out.println("[����]selectCategory() �޼ҵ� SQL ����"+e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return productList;
	}
	
	//����°���黷�� cart_manage_page,,,
		public List<CartDTO> selectCart() {
			Connection con=null;
			PreparedStatement pstmt=null;
			ResultSet rs=null;
			List<CartDTO> cartList=new ArrayList<CartDTO>();
			try {
				con=getConnection();
				String sql="select * from cart order by no";
				pstmt=con.prepareStatement(sql);
				
				rs=pstmt.executeQuery();
				
				while(rs.next()) {
					CartDTO cart=new CartDTO();
					cart.setLoginid(rs.getString("login_mail"));
					cart.setNo(rs.getInt("no"));
					cart.setItem(rs.getString("item"));
					cart.setCount(rs.getInt("count"));
					cart.setPrice(rs.getString("price"));
					cart.setTotalP(rs.getString("Total_P"));
					cart.setDeliveryP(rs.getString("delivery_P"));
					cart.setDeliveryType(rs.getString("Delivery_type"));
					cart.setDeliveryStartdate(rs.getString("delivery_startdate"));
					cart.setDeliveryArriveddate(rs.getString("delivery_arriveddate"));
					cartList.add(cart);
				}
			} catch (Exception e) {
				System.out.println("[����]selectCategory() �޼ҵ� SQL ����"+e.getMessage());
			} finally {
				close(con, pstmt, rs);
			}
			return cartList;
		}
	
	// ��ǰ ��ȣ�� ���� �޾�  ��ǰ ������ �˻��ϴ� �޼ҵ�
	public List<CartDTO> selectCategory(int no) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		List<CartDTO> productList=new ArrayList<CartDTO>();
		try {
			con=getConnection();
			String sql="select * from cart where no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, no);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				CartDTO cart=new CartDTO();
				cart.setNo(rs.getInt("no"));
				cart.setItem(rs.getString("item"));
				cart.setPrice(rs.getString("price"));
				cart.setCount(rs.getInt("count"));
				productList.add(cart);
			}
		} catch (Exception e) {
			System.out.println("[����]selectCategory() �޼ҵ� SQL ����"+e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return productList;
	}

	
	
	//�ִ��� ���� �� üũ
	public boolean selectCheck(int no) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		boolean isCheck=false;
		try {
			con=getConnection();
			String sql="select * from cart where no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, no);
			
			rs=pstmt.executeQuery();
			
			while(rs.next()) {
				isCheck=true;
			}
		} catch (Exception e) {
			System.out.println("[����]selectCategory() �޼ҵ� SQL ����"+e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return isCheck;
	}
	
	
	// ���� ���� ��ư ������ ������ ��ȯ�ϰ� ī�����ϴ� �޼ҵ� (�÷���)

	public int updateCartCount(int no,int Count) {
		Connection con=null;
		PreparedStatement pstmt=null;
		int rows=0;
		try {
			con=getConnection();
				
			String sql="update cart set count=count+? ,total_p=((count+?)*price) where no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, Count);
			pstmt.setInt(2, Count);
			pstmt.setInt(3, no);
				
			rows=pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]updateReadCount() �޼ҵ��� SQL ���� = "+e.getMessage());
		} finally {
			close(con, pstmt);
		}
		return rows;
	}
	
	
	// �ֹ����� ���� �޼ҵ�(�ֹ���ȣ, �̸��Ϸ�, ���� ��������)
	public void deliveryStatus(int status,String email) {
		Connection con=null;
		PreparedStatement pstmt=null;
		
		try {
			con=getConnection();
				
			String sql="update cart set DELIVERY_TYPE=? where login_mail=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, status);			
			pstmt.setString(2, email);
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]updateReadCount() �޼ҵ��� SQL ���� = "+e.getMessage());
		} finally {
			close(con, pstmt);
		}
	}


	//��ǰ �߰� �޼ҵ�
	public int insertCart(CartDTO insertCart) {
		Connection con=null;
		PreparedStatement pstmt=null;
		ResultSet rs=null;
		int rows=0;
		try {
			con=getConnection();
			String sql="insert into cart values(?,?,null,?,?,?,?,"+CartDTO.Shopping+",sysdate,sysdate+7)";
			pstmt=con.prepareStatement(sql);
			pstmt.setString(1, insertCart.getLoginid());
			pstmt.setInt(2, insertCart.getNo());
			pstmt.setInt(3, insertCart.getCount());
			pstmt.setString(4, insertCart.getPrice());
			pstmt.setString(5, insertCart.getDeliveryP());
			pstmt.setString(6, insertCart.getTotalP());
			rows=pstmt.executeUpdate();
			
		} catch (Exception e) {
			System.out.println("[����]selectCategory() �޼ҵ� SQL ����"+e.getMessage());
		} finally {
			close(con, pstmt, rs);
		}
		return rows;
	}
	
	// �� ��ǰ ������ �� ī��� �޼ҵ�
	
	// ��ǰ ��ȣ�� ���� �޾� ��ٱ��Ͽ��� ��ǰ �����ϴ� �޼ҵ� 
	public  int deleteCart(int no) {
		Connection con =null;
		PreparedStatement pstmt=null;
		int rows=0;
		try {
			con=getConnection();
			String sql="delete from cart where no=?";
			pstmt=con.prepareStatement(sql);
			pstmt.setInt(1, no);
			rows=pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("[����]deleteCustomer ���� ="+e.getMessage());
		}finally {
			close(con, pstmt);
		}
		return rows;
		
		
	}
	
	// ����� 10�� ������ �� - ī����
	
	
	// ��ٱ��� �߰��� ��¥ ������(select TO_CHAR(TO_DATE(?,'YYYYMMDD'), 'DY' )); �޾ƿ� �ݿ��ϴ� �޼ҵ�
	
	
}