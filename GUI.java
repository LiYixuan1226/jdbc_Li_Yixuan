import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class GUI extends JFrame implements ActionListener {
	
	/** GUI JButtons */
	private JButton courseButton, memberButton;
	private JButton bookButton;
	private JTextField idIn, memberIn;
	private JTextArea display;
     
	public GUI() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Database System");
		setSize(1000, 500);
		display = new JTextArea();
		display.setFont(new Font("Courier", Font.PLAIN, 14));
		add(display, BorderLayout.CENTER);
		layoutTop();
		layoutBottom();
	}
	public void layoutTop() {
		JPanel top = new JPanel();
		courseButton = new JButton("Courses Details");
		courseButton.addActionListener(this);
		top.add(courseButton);
		memberButton = new JButton("Members Details");
		memberButton.addActionListener(this);
		top.add(memberButton);
		add(top, BorderLayout.NORTH);
	}

	/**
	 * adds labels, text fields and buttons to bottom of GUI
	 */
	public void layoutBottom() {
		// instantiate panel for bottom of display
		JPanel bottom = new JPanel();
		// add upper label, text field and button
		JLabel idLabel = new JLabel("Enter Course Id");
		bottom.add(idLabel);
		idIn = new JTextField(25);
		bottom.add(idIn);
		JPanel panel1 = new JPanel();
		bookButton = new JButton("Book");
		bookButton.addActionListener(this);
		bottom.add(panel1);
		// add middle label, text field and button
		JLabel nmeLabel = new JLabel("Enter Member Name");
		bottom.add(nmeLabel);
		memberIn = new JTextField(25);
		bottom.add(memberIn);
		bottom.add(bookButton);
		JPanel panel2 = new JPanel();
		bottom.add(panel2);
		// add lower label text field and button
		add(bottom, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()== courseButton)
		{
			 Postgre data = new Postgre();
			 String show="Course ID\t"+"Name\t\t"+"Capacity\t"+"Instructor Number\t"+"Instructor name\t\t"+"Booked Number\n";
			 display.setText(show+data.getCourse()); 
		}
		
		if (e.getSource()== memberButton)
		{
			 Postgre data = new Postgre();
			 String show="Membership Number\t"+"Member Name \t\t"+"Course ID\t"+"Course Name\n";
			 display.setText(show+data.getMember()); 
		}
		
		if (e.getSource()== bookButton)
		{
			 Postgre data = new Postgre();
			 String courseId=idIn.getText();
			 String memberName=memberIn.getText();
			 //if the user didn't enter any information before clicking the button
			 if(courseId.equals("")||memberName.equals(""))
			 {
				 JOptionPane.showMessageDialog(null, "Please input correct information!", "Warning", JOptionPane.ERROR_MESSAGE);
				 data.close();
				 return;
			 }
			 int cId = Integer.parseInt(courseId);//convert the data type of the course id (from String to Integer)
			 data.book(cId,memberName);
		}
	}
}
