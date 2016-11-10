import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//import java.awt.JButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class TextFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane,functionPane,buttonPane,optionsPane,textPane,goPanel;
	private myMongo myMongoClient;
	private DBCollection myCollection,outCollection;
	JTextField currentCount = new JTextField();
	//private BasicDBObject update;
	//ArrayList<HashMap<String,Object> > annotatedData = new ArrayList<HashMap<String,Object> >(100000);
	ArrayList<BasicDBObject> mp = new ArrayList<BasicDBObject>();
	BasicDBObject mp2 = new BasicDBObject();
	int count_per_tweet = 0;
	int skip_count = -1;
	String outFile = "outFile";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TextFrame frame = new TextFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TextFrame() {
		
		//mongo code
	myMongoClient = new myMongo();
	myCollection = myMongoClient.connect("localhost","27017");
	outCollection = myMongoClient.connect1("localhost","27017");
//	DBCursor cursor = myCollection.find();
	
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		buttonPane = new JPanel();
		optionsPane = new JPanel();
		functionPane = new JPanel();
		goPanel = new JPanel();
		textPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextArea Document = new JTextArea();
		JTextArea Document1 = new JTextArea();
		Document.setEditable(false);
		///Document1.setEditable(false);
		final JScrollPane scrolll = new JScrollPane(Document);
		final JScrollPane scroll2 = new JScrollPane(Document1);
//		Document.setText("Document");
		
		textPane.setLayout(new BoxLayout(textPane, BoxLayout.Y_AXIS));
		textPane.add(scrolll);
		textPane.add(scroll2);
		contentPane.add(textPane, BorderLayout.CENTER);
//		JButton button = new JButton("Read File");
//		button.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				JFileChooser chooser = new JFileChooser();
//				chooser.showOpenDialog(null);
//				File f = chooser.getSelectedFile();
//				String filename = f.getAbsolutePath();
//				try {
//					FileReader reader = new FileReader(filename);
//					BufferedReader br = new BufferedReader(reader);
//					Document.read(br, null);
//					br.close();
//					Document.requestFocus();
//				}
//				catch(Exception e1)
//				{
//					JOptionPane.showMessageDialog(null, e);
//				}
//			}
//		});
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
		JButton copy = new JButton("Copy");
		JButton done = new JButton("Add Annotation");
		JButton write = new JButton("Insert into Database");
		
		JButton next = new JButton("Show next");
		next.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				skip_count++;
				if(skip_count >= 0)
				{
					currentCount.setText(Integer.toString(skip_count));
					DBCursor cursor = myCollection.find().skip(skip_count).limit(1);
					if(cursor.hasNext()) {
						
						 DBObject o = cursor.next();
						 String tweet = (String) o.get("text");
						 Document.setText(tweet);
//							br.close();
//							Document.requestFocus();
//						System.out.println(cursor.next());
					}
				}
				else
				{
					System.out.println("Skip < 0");
				}
			}
		});
		
		JButton prev = new JButton("Show previous");
		prev.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(skip_count > 0)
					skip_count--;
				if(skip_count >= 0)
				{
					currentCount.setText(Integer.toString(skip_count));
					DBCursor cursor = myCollection.find().skip(skip_count).limit(1);
					if(cursor.hasNext()) {
						
						 DBObject o = cursor.next();
						 String tweet = (String) o.get("text");
						 Document.setText(tweet);
//							br.close();
//							Document.requestFocus();
//						System.out.println(cursor.next());
					}
				}
				else
				{
					System.out.println("Skip < 0");
				}
			}
		});
		
		copy.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub 
				StringSelection stringSelection = new StringSelection (Document.getSelectedText());
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
				clpbrd.setContents (stringSelection, null);
			}
		});
		
//		buttonPane.add(button);
		buttonPane.add(next);
		buttonPane.add(prev);
//		buttonPane.add(copy);
		buttonPane.add(done);
		buttonPane.add(write);
		optionsPane.setLayout(new BoxLayout(optionsPane, BoxLayout.Y_AXIS));
		
		
		JCheckBox check1 = new JCheckBox("Name");
		optionsPane.add(check1);
		JCheckBox check2 = new JCheckBox("Place");
	    optionsPane.add(check2);
	    JCheckBox check3 = new JCheckBox("Organisation");
	    optionsPane.add(check3);
	    JCheckBox check4 = new JCheckBox("Movie");
	    optionsPane.add(check4);
	    JCheckBox  check5 = new JCheckBox("Other");
	    optionsPane.add(check5);
		
		done.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				// Write To file in desired Format
				// Uncheck The Boxes
				StringSelection stringSelection = new StringSelection (Document.getSelectedText());
				Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
				clpbrd.setContents (stringSelection, null);
				
				String tweet = Document.getText();
				System.out.println(tweet);
				boolean name = false,place = false,org = false,movie =false,other = false;
				String copiedText = null;
				
				try{
					 copiedText = (String)clpbrd.getData(DataFlavor.stringFlavor);
				}catch(Exception e3)
				{
					e3.printStackTrace();
				}
				try(FileWriter fw = new FileWriter(outFile, true);
					    BufferedWriter bw = new BufferedWriter(fw);
					    PrintWriter out = new PrintWriter(bw))
					{
					    //more code
				if(check1.isSelected())
				{
					name = true;
				}
				if(check2.isSelected())
				{
					place = true;
				} 
				if(check3.isSelected())
				{
					org = true;
				}
				if(check4.isSelected())
				{
					movie = true;	
				}
				if(check5.isSelected())
				{
					other = true;	
				}
			} catch (IOException e2) {
				e2.printStackTrace();
				}
				//write into database
				BasicDBObject mp1 = new BasicDBObject();
				BasicDBObject mp3 = new BasicDBObject();
				BasicDBObject mp4 = new BasicDBObject();
				BasicDBObject mp5 = new BasicDBObject();
				BasicDBObject mp6 = new BasicDBObject();
				mp2.put("tweet", tweet);
//				mp1.clear();
//				mp.clear();
				if(name)
				{
					mp1.put("id", count_per_tweet);
					count_per_tweet++;
					mp1.put("type", "name");
					mp1.put("name",copiedText);
					mp.add(mp1);
				}
				if(place)
				{
					mp3.put("id", count_per_tweet);
					count_per_tweet++;
					mp3.put("type", "place");
					mp3.put("name",copiedText);
					mp.add(mp3);
				}
				if(org)
				{
					mp4.put("id", count_per_tweet);
					count_per_tweet++;
					mp4.put("type", "organisation");
					mp4.put("name",copiedText);
					mp.add(mp4);
				}
				if(movie)
				{
					mp5.put("id", count_per_tweet);
					count_per_tweet++;
					mp5.put("type", "movie");
					mp5.put("name",copiedText);
					mp.add(mp5);
				}
				if(other)
				{
					mp6.put("id", count_per_tweet);
					count_per_tweet++;
					mp6.put("type", "other");
					mp6.put("name",copiedText);
					mp.add(mp6);
				}
				mp2.put("Mentions",mp);
//				mp2.append("Mentions", mp);
				Document1.setText(null);
				for(int i=0;i<mp.size();++i)
				{
					BasicDBObject mp7 = mp.get(i);
					Document1.append(mp7.toString());
					Document1.append("\n");
					System.out.println("name = " + mp7.get("name"));
					System.out.println("type = " + mp7.get("type"));
				}
				check1.setSelected(false);
				check2.setSelected(false);
				check3.setSelected(false);
				check4.setSelected(false);
				check5.setSelected(false);
			}
		});
		
		write.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				//ArrayList<ArrayList<Object> > l1 = new ArrayList<ArrayList<Object> >();
				outCollection.insert(mp2);
				//annotatedData.add(skip_count,mp2);
				check1.setSelected(false);
				check2.setSelected(false);
				check3.setSelected(false);
				check4.setSelected(false);
				check5.setSelected(false);
				Document1.setText(null);
				mp.clear(); 
				mp2.clear();
				count_per_tweet = 0;
			}
		});
		functionPane.setLayout(new BoxLayout(functionPane, BoxLayout.Y_AXIS));
		functionPane.add(optionsPane);
		functionPane.add(buttonPane);
		
		
		JButton addButton = new JButton("goto");
		JTextField toPage = new JTextField();
		toPage.setText("0");
		goPanel.setLayout(new GridLayout());
		goPanel.add(addButton);
		goPanel.add(toPage);
		functionPane.add(goPanel);
		currentCount.setText(Integer.toString(skip_count));
		functionPane.add(currentCount);
		contentPane.add(functionPane, BorderLayout.WEST);
		
		addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int tweetNo = Integer.parseInt(toPage.getText());
				skip_count = tweetNo-1;
				next.doClick();
				
			}
		});
	}
	
	public class myMongo {
		  public DBCollection connect(String client,String port_no) {

			  MongoClient mongo = null;
		    try {

			/**** Connect to MongoDB ****/
			// Since 2.10.0, uses MongoClient
			 mongo = new MongoClient(client, Integer.parseInt(port_no));
		    } catch (UnknownHostException e) {
				e.printStackTrace();
			    } catch (MongoException e) {
				e.printStackTrace();
			    }
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("mydb");

			/**** Get collection / table from 'testdb' ****/
			// if collection doesn't exists, MongoDB will create it for you
			DBCollection table = db.getCollection("tweets");
			return table;
		    }
		  public DBCollection connect1(String client,String port_no) {

			  MongoClient mongo = null;
		    try {

			/**** Connect to MongoDB ****/
			// Since 2.10.0, uses MongoClient
			 mongo = new MongoClient(client, Integer.parseInt(port_no));
		    } catch (UnknownHostException e) {
				e.printStackTrace();
			    } catch (MongoException e) {
				e.printStackTrace();
			    }
			/**** Get database ****/
			// if database doesn't exists, MongoDB will create it for you
			DB db = mongo.getDB("mydb");

			/**** Get collection / table from 'testdb' ****/
			// if collection doesn't exists, MongoDB will create it for you
			DBCollection table = db.getCollection("annotated_tweets");
			return table;
		    }
		}
}
