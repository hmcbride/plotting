import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.BasicAWSCredentials
import java.io.File



object AmazonS3FileUploadExample extends App {


  val bucketName = "testbuckethmb"          // specifying bucket name

  //file to upload
  val fileToUpload = new File("/Users/hughmcbride/IdeaProjects/plotting/ScatterChart.png")

  /* These Keys would be available to you in  "Security Credentials" of
      your Amazon S3 account */
  val AWS_ACCESS_KEY = "<your vaule>"
  val AWS_SECRET_KEY = "<your value>"


  val yourAWSCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
  val amazonS3Client = new AmazonS3Client(yourAWSCredentials)
  // This will create a bucket for storage
  amazonS3Client.createBucket(bucketName)
  amazonS3Client.putObject(bucketName, "testscatter.pg", fileToUpload)



}