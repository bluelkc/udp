/**
  * Created by kecen on 11/5/17.
  */

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures

import scala.concurrent._
import ExecutionContext.Implicits.global
import udp._

class Test extends FunSuite with BeforeAndAfterAll with ParallelTestExecution {

  override def beforeAll() = println("Testing Starts.")
  override def afterAll()  = println("Testing Ends.")

  val BASE_PORT = 10000
  val BASE_MSG = "Message from client no."

  val futureServer : Future[Unit] = Future {
    Server.run()
  }

  def futureClient(index : Int) : Future[String] = Future {
    val c = new Client()
    val msg = BASE_MSG + index
    val res = c.sendReq(msg, BASE_PORT + index)
    res
  }

  test("Client 1") {
    /*val client1 : Future[String] = futureClient(1)
    val client2 : Future[String] = futureClient(2)
    val client3 : Future[String] = futureClient(3)

    assert(client1.futureValue == (BASE_MSG + 1 + ":echoed"))
    assert(client2.futureValue == (BASE_MSG + 2 + ":echoed"))
    assert(client3.futureValue == (BASE_MSG + 3 + ":echoed"))*/
    println("Test Client 1 starts.")
    val index = 1
    val c = new Client()
    val msg = BASE_MSG + index
    val res = c.sendReq(msg, BASE_PORT + index)
    assert(res == BASE_MSG + index + ":echoed")
    println("Test Client 1 ends.")
  }

  test("Client 2") {
    /*val client1 : Future[String] = futureClient(1)
    val client2 : Future[String] = futureClient(2)
    val client3 : Future[String] = futureClient(3)

    assert(client1.futureValue == (BASE_MSG + 1 + ":echoed"))
    assert(client2.futureValue == (BASE_MSG + 2 + ":echoed"))
    assert(client3.futureValue == (BASE_MSG + 3 + ":echoed"))*/
    println("Test Client 2 starts.")
    val index = 2
    val c = new Client()
    val msg = BASE_MSG + index
    val res = c.sendReq(msg, BASE_PORT + index)
    assert(res == BASE_MSG + index + ":echoed")
    println("Test Client 2 ends.")
  }
}
