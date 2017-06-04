package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

//import org.apache.tomcat.util.http.fileupload.IOUtils;

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

public class awsAccessClass {

	public static void main(String[] args){
		try{
			BasicAWSCredentials basicCreds = new BasicAWSCredentials(credentials.access_key, credentials.secret_key);
			AmazonS3 s3client = new AmazonS3Client(basicCreds);
			
			
			/*File file = new File("C:\\Users\\mahesh\\Desktop\\credentials.txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufread = new BufferedReader(fileReader);
			String line=null;
			String message=null;
			//FileWriter filewrite = new FileWriter(file, false);
			StringBuilder sb = new StringBuilder();
			line=bufread.readLine();
			boolean userExists=false;
			while (line != null)
			{
				if(line.split("_")[0].equalsIgnoreCase("new")){
					userExists =true;
					break;
				}
			     message = line;
				filewrite.write(message);
				filewrite.write("\n");
			    sb.append(message);
			    sb.append(System.getProperty("line.separator"));
			    line=bufread.readLine();
			}
			if(userExists){
				System.out.println("User exists!! Give some other name!!");
			}else{
				System.out.println("already present :"+sb.toString());
				if(line==null){
					sb.append("new"+"_"+"new");
				}
				System.out.println("already present later:"+sb.toString());
				FileWriter filewrite = new FileWriter(file, false);
				filewrite.write(sb.toString());
				filewrite.close();
				PutObjectResult result =  s3client.putObject(new PutObjectRequest(
		                 "mysamplecredential", file.getName() , file));
				
				
			}*/
			
			
			
			
			
			//Bucket myfirst = s3client.createBucket("mysamplecredential");
			File file = new File("C:\\Users\\mahesh\\Desktop\\credentials.txt");
			PutObjectResult result =  s3client.putObject(new PutObjectRequest(
                 "mysamplecredential", file.getName() , file));
			//s3client.deleteObject("mysamplecredential", "credentials.txt");
			//s3client.deleteBucket("mash1487791745732");
			/*s3client.deleteObject("mysamplecredential", "credentials.txt");
			System.out.println("test is working : ");*/
			
			/*GetObjectRequest request = new GetObjectRequest("mysamplebucketmahesh9128",
					"somefolder/Fantastic-Basketball-Wallpaper.jpg1487533255902");
			S3Object object = s3client.getObject(request);
			System.out.println("test is working : ");
			S3ObjectInputStream objectContent = object.getObjectContent();*/
		//	IOUtils.copy(objectContent, new FileOutputStream("M://test.jpg"));
			
			
			/*ObjectListing objlist = s3client.listObjects("mysamplebucketmahesh9128");
			List<S3ObjectSummary> s3objList =    objlist.getObjectSummaries();
			for(S3ObjectSummary s3obj : s3objList){
				System.out.println("test is working : "+s3obj.getKey());
			}*/
			
			
			
			
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

}
