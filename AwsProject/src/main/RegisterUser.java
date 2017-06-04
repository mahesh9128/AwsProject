package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class ListS3Objects
 */
@WebServlet("/RegisterUser")
public class RegisterUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterUser() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{
			
			System.out.println("Inside Register method !!");
			InputStream inputstr = request.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputstr, writer, "UTF-8");
			String input = writer.toString();
			JSONParser parserdet = new JSONParser();
			Object objdet = parserdet.parse(input);
			JSONObject jsonObj = (JSONObject) objdet;
			BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
			AmazonS3 s3client = new AmazonS3Client(basicCreds);
			
			if(((String)jsonObj.get("purpose")).equalsIgnoreCase("register")){
				System.out.println("register ");
				//Add user details to the user file
				GetObjectRequest objreq = new GetObjectRequest("mysamplecredential",
						"credentials.txt");
				S3Object object = s3client.getObject(objreq);
				S3ObjectInputStream objectContent = object.getObjectContent();
				IOUtils.copy(objectContent, new FileOutputStream("credentials.txt"));
				File file = new File("credentials.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufread = new BufferedReader(fileReader);
				String line=null;
				StringBuilder sb = new StringBuilder();
				line=bufread.readLine();
				boolean userExists=false;
				while (line != null)
				{
					if(line.split("_")[0].equalsIgnoreCase((String)jsonObj.get("regusername"))){
						userExists =true;
						break;
					}
				    String message = line;
				    sb.append(message);
				    sb.append(System.getProperty("line.separator"));
				    line=bufread.readLine();
				}
				if(userExists){
					response.getWriter().print("User exists!! Give some other name!!");
					return;
				}else{
					System.out.println("already present :"+sb.toString());
					if(line==null){
						sb.append((String)jsonObj.get("regusername")+"_"+(String)jsonObj.get("regpwd"));
					}
					System.out.println("already present later:"+sb.toString());
					FileWriter filewrite = new FileWriter(file, false);
					filewrite.write(sb.toString());
					filewrite.close();
					PutObjectResult result =  s3client.putObject(new PutObjectRequest(
			                 "mysamplecredential", file.getName() , file));
					
					
					Bucket myfirst = s3client.createBucket((String)jsonObj.get("regusername")+new Date().getTime());
					response.getWriter().print("User is registered!!");
					return;
				}
				
				/*	s3client.deleteObject(credentials.bucket_name, input);
				System.out.println("test is working : ");
				response.getWriter().print("success");*/
			}
			else if(((String)jsonObj.get("purpose")).equalsIgnoreCase("login")){
				System.out.println("login ");
				GetObjectRequest objreq = new GetObjectRequest("mysamplecredential",
						"credentials.txt");
				S3Object object = s3client.getObject(objreq);
				S3ObjectInputStream objectContent = object.getObjectContent();
				IOUtils.copy(objectContent, new FileOutputStream("/var/lib/tomcat7/upload/credentials.txt"));
				File file = new File("/var/lib/tomcat7/upload/credentials.txt");
				FileReader fileReader = new FileReader(file);
				BufferedReader bufread = new BufferedReader(fileReader);
				String line=null;
				line=bufread.readLine();
				System.out.println("line"+line);
				boolean userExists=false;
				while (line != null)
				{
					System.out.println("testing :  "+line.split("_")[0]+"_"+line.split("_")[1]);
					if(line.split("_")[0].equalsIgnoreCase((String)jsonObj.get("username")) && 
							line.split("_")[1].equalsIgnoreCase((String)jsonObj.get("pwd"))){
						userExists =true;
						break;
					}
				}
				if(!userExists){
					response.getWriter().print("fail");
					return;
				}
				return;
				
			}
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
			response.getWriter().print("Please try again!!");
			return;
		}
	}

}
