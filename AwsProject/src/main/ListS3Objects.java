package main;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

/**
 * Servlet implementation class ListS3Objects
 */
@WebServlet("/ListS3Objects")
public class ListS3Objects extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ListS3Objects() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		System.out.println("Inside List objects method !!");
		InputStream inputstr = request.getInputStream();
		StringWriter writer = new StringWriter();
		IOUtils.copy(inputstr, writer, "UTF-8");
		String input = writer.toString();
		String bucketName =null;
		BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
		AmazonS3 s3client = new AmazonS3Client(basicCreds);
		List<Bucket> listbuck = s3client.listBuckets();
		for(Bucket buckobj : listbuck){
			System.out.println("bucket name :"+buckobj.getName());
			if(buckobj.getName().contains(input)){
				bucketName=buckobj.getName();
				break;
			}
		}
		
		ObjectListing objlist = s3client.listObjects(bucketName);
		List<S3ObjectSummary> s3objList =    objlist.getObjectSummaries();
		if(s3objList.isEmpty()){
			response.getWriter().print("No Objects Stored!!");
			return;
		}
		StringBuilder strb = new StringBuilder();
		strb.append("<table class=\"table\">");
		strb.append("<thead><tr><th>Name</th><th>Size</th><th>Bucket Name</th></tr></thead>");
		strb.append("<tbody>");	
		for(S3ObjectSummary s3obj : s3objList){
			strb.append("<tr><td>"+s3obj.getKey()+"</td><td>"+s3obj.getSize()+"</td><td>"+s3obj.getBucketName()+"</td></tr>");
			System.out.println("test is working : "+s3obj.getKey());
		}
		strb.append("</tbody></table>");
		
		response.getWriter().print(strb.toString());
	}


}
