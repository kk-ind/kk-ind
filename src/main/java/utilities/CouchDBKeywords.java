package utilities;






import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fourspaces.couchdb.Database;

import com.fourspaces.couchdb.Document;

import com.fourspaces.couchdb.Session;


public class CouchDBKeywords{
 public static Session dbSession ;
	 
	 public static Database db;
	 public static void createDatabase(String dbName){

	       try{
		   dbSession = new Session("localhost", 5984);

	       db = dbSession.createDatabase(dbName);

	       if(db==null)

	           db = dbSession.getDatabase(dbName);
	       }
	       catch(Exception e)
	       {
	    	   System.out.println(e.toString());
	       }

	   }
	   
	   public static Document getDocument(String Status){
		   DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd,HH-mm-ss");
			 Date date = new Date();
			 System.out.println(dateFormat.format(date));
	       Document doc = new Document();

	       doc.setId(dateFormat.format(date).toString());
	       doc.put("Status", Status);

	       return doc;

	   }
	   public static void saveDocument(Document doc){

	       try {

	           db.saveDocument(doc);

	       } catch (Exception e) {

	       }

	   }
	   public static void deleteDocument(String id){

		   try{
	       Document d = db.getDocument(id);

	       System.out.println("Document 1: " + d);

	       db.deleteDocument(d);
		   }
		   catch(Exception e)
		   {
			   
		   }

	   }
	   public static int getTotalDocumentCount(){

	       int count = db.getDocumentCount();

	       System.out.println("Total Documents: " + count);

	       return count;

	   }
}