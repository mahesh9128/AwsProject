package main;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class ListS3Objects
 */
@WebServlet("/UploadS3Objects")
public class UploadS3Objects extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean isMultipart;
	String fileName=null;
	String filecontent=null;
	String hashcode=null;
	String filecontentbytes=null;
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UploadS3Objects() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
		AmazonS3 s3client = new AmazonS3Client(basicCreds);
		try{
			System.out.println("Inside upload!!");

			// Check that we have a file upload request
			isMultipart = ServletFileUpload.isMultipartContent(request);
			java.io.PrintWriter out = response.getWriter();
			if( !isMultipart ){
				response.getWriter().print("Not uploaded");
				return;
			}

			// Create a new file upload handler
			DiskFileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List fileItems = upload.parseRequest(request);
			Iterator i = fileItems.iterator();
			Iterator itr = fileItems.iterator();
			String bucketname=null;

			while ( i.hasNext () ) {
				FileItem fil = (FileItem)i.next();
				System.out.println("inside for loop :");
				if (fil.isFormField() && fil.getFieldName().equalsIgnoreCase("bucket")){
					bucketname=fil.getString();
					System.out.println("bucket name :"+bucketname);
					List<Bucket> listbuck = s3client.listBuckets();
					for(Bucket buckobj : listbuck){
						System.out.println("bucket name :"+buckobj.getName());
						if(buckobj.getName().contains(bucketname)){
							bucketname=buckobj.getName();
							break;
						}
					}
				}
			}
			
			while ( itr.hasNext () ) {
				FileItem fi = (FileItem)itr.next();

				if (!fi.isFormField())	
				{
					fileName = fi.getName();
					System.out.println("file name :"+fileName);
					//File file = new File(fileName);
					File file = new File("/var/lib/tomcat7/upload/"+fileName);
					System.out.println("file permission :"+file.canWrite());
					System.out.println("file permission :"+file.getAbsolutePath());
					if(!file.canWrite()){
						file.setWritable(true);
						file.setExecutable(true);
						file.setReadable(true);
					}
					System.out.println("Bucketname  :"+bucketname);
					System.out.println("file permission :"+file.canWrite());
					fi.write( file );
					PutObjectResult result =  s3client.putObject(new PutObjectRequest(
							bucketname, file.getName(), file));
					response.getWriter().print("success");
					return;
				}
			}

		}
		catch(Exception ex){
			ex.printStackTrace();
			response.getWriter().print("failure");
		}
	}

}
