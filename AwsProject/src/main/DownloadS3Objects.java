package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
@WebServlet("/DownloadS3Objects")
public class DownloadS3Objects extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	String fileName=null;
	String filecontent=null;
	String hashcode=null;
	String filecontentbytes=null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DownloadS3Objects() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
		AmazonS3 s3client = new AmazonS3Client(basicCreds);
		try{
			
			InputStream inputstr = request.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputstr, writer, "UTF-8");
			String input = writer.toString();
			JSONParser parserdet = new JSONParser();
			Object objdet = parserdet.parse(input);
			JSONObject jsonObj = (JSONObject) objdet;
			PrintWriter out = response.getWriter();
			System.out.println("Download is working : "+(String)jsonObj.get("bucketname"));
			List<Bucket> listbuck = s3client.listBuckets();
			String bucketName=null;
			for(Bucket buckobj : listbuck){
				System.out.println("bucket name :"+buckobj.getName());
				if(buckobj.getName().contains((String)jsonObj.get("bucketname"))){
					bucketName=buckobj.getName();
					break;
				}
			}
			
			GetObjectRequest fileObj = new GetObjectRequest(bucketName,(String)jsonObj.get("filename")
					);
			S3Object object = s3client.getObject(fileObj);
			System.out.println("Download is working : "+object.getKey());
			S3ObjectInputStream objectContent = object.getObjectContent();
			IOUtils.copy(objectContent, new FileOutputStream("/var/lib/tomcat7/upload/"+(String)jsonObj.get("filename")));
			File file = new File("/var/lib/tomcat7/upload/"+(String)jsonObj.get("filename"));
			System.out.println(file.getAbsolutePath());
			System.out.println(file.getCanonicalPath());
			out.print(file.getAbsolutePath());
			/*FileInputStream fileinputstream = new FileInputStream(file);
			response.setContentType("application/octet-stream");
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=\""+input+"\"";
			response.setHeader(headerKey, headerValue);
			
			byte[] buffer = new byte[4096];
	        int bytesRead = -1;
	         
	        while ((bytesRead = fileinputstream.read()) != -1) {
	        	out.write(bytesRead);
	        }
	        fileinputstream.close();
	        out.close();  */
		}
		catch(Exception ex){
			ex.printStackTrace();
			response.getWriter().print("failure");
		}
	}

}
