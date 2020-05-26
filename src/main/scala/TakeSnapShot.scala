import java.io.{BufferedReader, File, FileOutputStream, InputStreamReader}

import com.amazonaws.{AmazonClientException, AmazonServiceException}
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import javafx.application.Application
import javax.imageio.ImageIO
import org.apache.commons.io.IOUtils
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.collections.ObservableBuffer
import scalafx.embed.swing.SwingFXUtils
import scalafx.geometry.Side
import scalafx.scene.chart.{NumberAxis, ScatterChart, XYChart}
import scalafx.scene.{Node, Scene}

object TakeSnapshot {
  def main(args: Array[String]) {
  //  TakeSnapshot.launch(classOf[TakeSnapshot], args: _*)
  }
}


class TakeSnapshot extends JFXApp {


  stage = new JFXApp.PrimaryStage {
    title.value = "Take a Snapshot"
    width = 640
    height = 400

    title = "ScatterChartDemo"
    val bufferedSource = io.Source.fromFile("src/insurance.csv")

    val xyseries =   bufferedSource.getLines().drop(1).map(x=> x.split(","))
                                                      .toList
                                                      .map(x => (x(2).toDouble,x(6).toDouble))
                                                      .toSeq

    bufferedSource.close

    val xseries = xyseries.unzip._1
    val yseries = xyseries.unzip._2

    val xmin = xseries.min
    val xmax = xseries.max +10

    val ymin = yseries.min
    val ymax = yseries.max + 5000

    scene = new Scene {
      root = new ScatterChart(NumberAxis("BMI", 0, xmax, 5), NumberAxis("Charges", 0, 65000, 5000)) {
        title = "Scatter Chart"
        legendSide = Side.Right
        data = ObservableBuffer(xySeries("Series 1", xyseries.take(80)))
      }
    }


  }

  ///////////


  val BUCKET_NAME = "testbuckethmb"
  val FILE_PATH = "/Users/hughmcbride/IdeaProjects/plotting/ScatterChart.png"
  val FILE_NAME = "scatterchart.png"
  val AWS_ACCESS_KEY = ""
  val AWS_SECRET_KEY = ""

  try {
    val awsCredentials = new BasicAWSCredentials(AWS_ACCESS_KEY, AWS_SECRET_KEY)
    val amazonS3Client = new AmazonS3Client(awsCredentials)

    // create a new bucket
    amazonS3Client.createBucket(BUCKET_NAME)

    // upload file
    val filex = new File(FILE_PATH)
    amazonS3Client.putObject(BUCKET_NAME, FILE_NAME, filex)

    // download file and read line by line
    val objx = amazonS3Client.getObject(BUCKET_NAME, FILE_NAME)
    val reader = new BufferedReader(new InputStreamReader(objx.getObjectContent()))
    var line = reader.readLine
    while (line!=null) {
      println(line)
      line = reader.readLine
    }

    // download file and write to local file system
    val obj = amazonS3Client.getObject(BUCKET_NAME, FILE_NAME)
    val bytes = IOUtils.toByteArray(obj.getObjectContent())
    val file = new FileOutputStream(FILE_NAME)
    file.write(bytes)

  } catch {
    case ase: AmazonServiceException => System.err.println("Exception: " + ase.toString)
    case ace: AmazonClientException => System.err.println("Exception: " + ace.toString)
  }



  //////////



  // Take the snapshot.
  takeSnapshot(stage.scene.root(), new File("ScatterChart.png"))

  // Take a snapshot of the specified node, writing it into the specified file. The dimensions of
  // the image will match the size of the node (and its child nodes) on the screen in pixels.
  def takeSnapshot(node: Node, file: File) {

    val image = node.snapshot(null, null)

    val bufferedImage = SwingFXUtils.fromFXImage(image, null)
    assert(bufferedImage ne null)

    ImageIO.write(bufferedImage, "png", file)
  }



  def xySeries(name: String, data: Seq[(Double, Double)]) =
    XYChart.Series[Number, Number](
      name,
      ObservableBuffer(data.map {case (x, y) => XYChart.Data[Number, Number](x, y)})
    )

}


