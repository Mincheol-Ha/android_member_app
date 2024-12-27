package mincheol.memberapplication.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MariaDBHelper {

    private static final String DB_URL = "";
    private static final String DB_USER = "";
    private static final String DB_PWD = "";
    private static final String DB_NAME = "clouds2024";

    //MariaDB Driver 초기화
    static {
        try{
            Class.forName("org.mariadb.jdbc.Driver");
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }



    //회원 가입 처리
    public boolean insertMember(String userid, String passwd,String name,String email){
        String sql="insert into member(userid, passwd, name, email) values (?,?,?,?)";
        try(Connection conn = getConnetion(); PreparedStatement pstmt=conn.prepareStatement(sql)){
            pstmt.setString(1,userid);
            pstmt.setString(2,passwd);
            pstmt.setString(3,name);
            pstmt.setString(4,email);

            int result = pstmt.executeUpdate();
            return result>0;
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //아이디 중복 확인
    public boolean useridCheck(String userid){
        String sql = "select from member where userid=?";
        try(Connection conn = getConnetion(); PreparedStatement pstmt=conn.prepareStatement(sql)){
            pstmt.setString(1,userid);
            ResultSet rs = pstmt.executeQuery();
            return rs.next(); //결과 집합의 행을 가르키는 포인터를 이동시킬 수 있는지
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    //회원 목록 조회 메서드
    public List<String> getAllUsers(){
        return null;
    }

    //로그인 확인
    //select mno from member where userid=?, password=?
    public boolean loginUser(String userid, String passwd){
        return false;
    }

    //데이터베이스 연결객체 가져오기
    private Connection getConnetion() throws SQLException {
        return DriverManager.getConnection(DB_URL,DB_USER,DB_PWD);
    }


}
