import java.sql.*;
public class FeeService {
 public String payFee(String regNo,String amountText) throws Exception {
  if(regNo==null||regNo.trim().isEmpty()) throw new IllegalArgumentException("Registration number is required.");
  double amount;
  try { amount=Double.parseDouble(amountText.trim()); }
  catch(NumberFormatException e){ throw new IllegalArgumentException("Enter a valid amount.",e); }
  if(amount<=0) throw new IllegalArgumentException("Amount must be greater than zero.");
  String sql="INSERT INTO fees(reg_no,amount) VALUES (?,?)";
  try(Connection c=FeeDatabase.getConnection(); PreparedStatement ps=c.prepareStatement(sql)){
   ps.setString(1,regNo.trim()); ps.setDouble(2,amount); ps.executeUpdate();
  } catch(SQLException e){ throw new Exception("Unable to save fee payment.",e); }
  return String.format("Payment of %.2f recorded for %s.",amount,regNo.trim());
 }
}
