import scala.sys.process._

object ITHelper {
  private var serverProcess: Process = null;

  def startServer(baseDir: String): Unit = {
    val file = s"${baseDir}/target/universal/stage/bin/financial-tracker"
    println(s"Starting server in ${file}...")
    serverProcess = Process(file, None).run()
    Thread sleep 7000
  }

  def shutdownServer(): Unit = {
    println("Shutting down server...")
    serverProcess.destroy
  }
}