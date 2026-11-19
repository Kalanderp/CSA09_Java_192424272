import java.sql.*;
public final class FeeDatabase {
 private FeeDatabase() {}
 public static Connection getConnection() throws SQLException {
  String url=System.getenv("FEE_DB_URL"), user=System.getenv("FEE_DB_USER"), pwd=System.getenv("FEE_DB_PASSWORD");
  if(url==null||user==null||pwd==null) throw new SQLException("Set FEE_DB_URL, FEE_DB_USER and FEE_DB_PASSWORD.");
  return DriverManager.getConnection(url,user,pwd);
 }
}
