import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class Postgre {

	String dbname="m_17_2305021l";
	String username="m_17_2305021l";
	String password="2305021l";
    Connection connection = null;
    Statement statement = null;
    
    public Postgre()//constructor, used to connect to the database
    {
        try {
            connection= DriverManager.getConnection("jdbc:postgresql://yacata.dcs.gla.ac.uk:5432/"+dbname,username, password);          
      } 
        catch (SQLException e) {
        	System.err.println("Connection Failed!");
        	e.printStackTrace();
        	return;
        	}
        	if (connection != null) {
        	System.out.println("Connection successful");
        	}
        	else {
        	System.err.println("Failed to make connection!");
        	}
 }
    
    public void close()//used to disconnect from the database
    {
    	try {
    		connection.close();
    		System.out.println("Connection closed");
    		}
    		catch (SQLException e) {
    		e.printStackTrace();
    		System.out.println("Connection could not be closed – SQL exception");
    		}
    }
   
    public String getCourse()//used to implement Task 1
    /*View all courses and the instructor taking them, 
     * the capacity of the course, and the number of members currently booked on the course.
     */
    {
    	Statement stmt = null;
    	String query = "   SELECT course.name,course.maxplaces, instructor.instructorname,course.InstructorNumber,tempMemberCourse.id id, tempMemberCourse.cnt cnt  \r\n" + 
    			"    			 FROM course LEFT OUTER JOIN instructor\r\n" + 
    			"                 ON course.instructornumber=instructor.InstructorNumber\r\n" + 
    			"                 LEFT OUTER JOIN( \r\n" + 
    			"    			 SELECT m.id id, COUNT(bookingNumber) cnt \r\n" + 
    			"    			 FROM membercourse m  \r\n" + 
    			"    			 GROUP BY m.id \r\n" + 
    			"    			 ) tempMemberCourse  \r\n" + 
    			"    			 ON tempMemberCourse.id= course.id; ";
    	String allCourse="";
    	try {
    	 stmt = connection.createStatement();
    	 ResultSet rs = stmt.executeQuery(query);
    	 while (rs.next()) {
    		 String id = rs.getString("id");
    		 String maxplaces = rs.getString("maxplaces");
    		 String name = rs.getString("name");
    		 String instructornumber = rs.getString("instructornumber");
    		 String instructorname = rs.getString("instructorname");
    		 String cnt = rs.getString("cnt");
    		 String course=id + "\t\t"+name+ " \t" +maxplaces +"\t\t"+instructornumber+"\t\t\t"+instructorname+"\t\t"+cnt+"\n";
    		 allCourse=allCourse+course;
    		 }
    		 }
    		catch (SQLException e ) {
    		e.printStackTrace();
    		 System.err.println("error executing query " + query);
    		 }
    	close();//call close() to disconnect from the database
         return allCourse;
     }
    
    public String getMember()//used to implement Task 2
    /*View all members booked on a course 
     * i.e. member full name and ID and the name of the course for each course in your database.
     */
    {
    	Statement stmt = null;
    	String query = "SELECT member.membershipnumber,membername,membercourse.id,course.name\r\n" + 
    			" FROM member\r\n" + 
    			" INNER JOIN membercourse\r\n" + 
    			" ON member.MembershipNumber=membercourse.MembershipNumber\r\n" + 
    			" INNER JOIN course\r\n" + 
    			" ON membercourse.id=course.id\r\n" + 
    			" ORDER BY member.membershipnumber;";
    	String allMember="";
    	try {
    	 stmt = connection.createStatement();
    	 ResultSet rs = stmt.executeQuery(query);
    	 while (rs.next()) {
    		 String membershipnumber = rs.getString("membershipnumber");
    		 String membername = rs.getString("membername");
    		 String id = rs.getString("id");
    		 String coursename = rs.getString("name");
    		 String member=membershipnumber + " \t\t\t"+membername+ "\t\t" +id +"\t\t"+ coursename+"\n";
    		 allMember=allMember+member;
    		 }
    		 }
    		catch (SQLException e ) {
    		e.printStackTrace();
    		 System.err.println("error executing query " + query);
    		 }
    	close();
        return allMember;
     }
    
    public void book(int cId,String memberName)//used to implement Task 3
    //Book a given member on a course.
    {
    	int cnt=0;
    	int courseid=0;
    	int maxplaces=0;
    	int memberid=0;
    	int bookingNum=0;
    	Statement stmt = null;
    	 String query = "   SELECT course.name,course.maxplaces,tempMemberCourse.id id, tempMemberCourse.cnt cnt  \r\n" + 
    	 		"    			 FROM course \r\n" + 
    	 		"                LEFT OUTER JOIN( \r\n" + 
    	 		"    			 SELECT m.id id, COUNT(bookingNumber) cnt \r\n" + 
    	 		"    			 FROM membercourse m  \r\n" + 
    	 		"    			 GROUP BY m.id \r\n" + 
    	 		"    			 ) tempMemberCourse  \r\n" + 
    	 		"    			 ON tempMemberCourse.id= course.id;  ";
    	 //check the name of the course, capacity, course id and current bookings
    	try {
    	 stmt = connection.createStatement();
    	 ResultSet rs = stmt.executeQuery(query);
        	 while (rs.next()) {
        		 courseid = rs.getInt("id");
        		 maxplaces = rs.getInt("maxplaces");
        		 cnt = rs.getInt("cnt");
        		 if(cId == courseid)
          	       break;
        		 //if the course id is the same as the course id that the user entered, then exit the loop
        		 }
        	// print it for testing and tutors:
        	 System.out.println("Course ID: " +courseid+" Capacity: "+maxplaces+" Booking Status: "+cnt);
        	 cnt++;
        	 if(cnt>maxplaces)
        	 {
        		 JOptionPane.showMessageDialog(null, "Over maximum capacity, please change another course!", "Warning", JOptionPane.ERROR_MESSAGE);
        		 close();
        		 return;
        	 }
        	 String query1="SELECT MembershipNumber FROM member WHERE MemberNAME = '"+ memberName + "'";
        	 ResultSet rs1 = stmt.executeQuery(query1);
        	 int samename=0;
        	 String  memberidString;
        	 while (rs1.next()) {//find the membership number of this person
        		 memberid=rs1.getInt("MembershipNumber");
        		 samename++;
        		 if(samename>1)//if there are people with the same name, he or she needs to enter membership number
        		 {
        			 memberidString = JOptionPane.showInputDialog ("Because there are people with the same name, please enter membership number:").trim();
        			 memberid=Integer.parseInt(memberidString);
        			 break;
        		 }
        	 }
        	// print it for testing and tutors:
        	 System.out.println("Membership Number: "+memberid);
        	 String query2="SELECT Max(bookingnumber) FROM MemberCourse";
        	 ResultSet rs2 = stmt.executeQuery(query2);
        	 while (rs2.next()) {
        		 bookingNum=rs2.getInt("max");
        		 //find the booking number
        	 }
        	// print it for testing and tutors:
        	 System.out.println("Previous Booking Number: "+bookingNum);
        	 bookingNum++;
        	 System.out.println("Next Booking Number: "+bookingNum);
        	 String query3="INSERT INTO MemberCourse(MembershipNumber,ID, BookingNumber)\r\n" + 
        	 		"VALUES ("+memberid+", "+cId+", "+bookingNum+" );";
        	 stmt.executeUpdate(query3);//update the database to complete the booking
        	 JOptionPane.showMessageDialog(null, "The program runs successfully!");
    		 }
    		catch (SQLException e ) {
    			//if the user entered invalid information
    		 e.printStackTrace();
    		 System.err.println("error executing query " + query);
    		 JOptionPane.showMessageDialog(null, "Please input correct information!", "Warning", JOptionPane.ERROR_MESSAGE);
    		 close();
    		 return;
    		 } 
    	catch (NullPointerException e ) {
		 close();
		 return;
		 } 
    	    
    	close();
     }
}
    	

