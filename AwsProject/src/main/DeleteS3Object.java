package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class ListS3Objects
 */
@WebServlet("/DeleteS3Object")
public class DeleteS3Object extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteS3Object() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		try{
			InputStream inputstr = request.getInputStream();
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputstr, writer, "UTF-8");
			String input = writer.toString();
			BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
			AmazonS3 s3client = new AmazonS3Client(basicCreds);
			String bucketName=null;
			List<Bucket> listbuck = s3client.listBuckets();
			for(Bucket buckobj : listbuck){
				System.out.println("bucket name :"+buckobj.getName());
				if(buckobj.getName().contains(input.split("_")[1])){
					bucketName=buckobj.getName();
					break;
				}
			}

			s3client.deleteObject(bucketName, input.split("_")[0]);
			System.out.println("test is working : ");
			response.getWriter().print("success");
		}
		catch(Exception ex){
			ex.printStackTrace();
			response.getWriter().print("failure");
		}
	}

}
