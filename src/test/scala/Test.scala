/**
  * Created by kecen on 11/5/17.
  */

import org.scalatest._
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

  // test 1 & 2 aim to simulate concurrent client requests

  test("Client 1") {
    println("[TEST] Test Client 1 starts.")
    val index = 1
    val c = new Client()
    val msg = BASE_MSG + index
    val res = c.sendReq(msg, BASE_PORT + index)
    assert(res == BASE_MSG + index + ":echoed")
    println("[TEST] Test Client 1 ends.")
  }

  test("Client 2") {
    println("[TEST] Test Client 2 starts.")
    val index = 2
    val c = new Client()
    val msg = BASE_MSG + index
    val res = c.sendReq(msg, BASE_PORT + index)
    assert(res == BASE_MSG + index + ":echoed")
    println("[TEST] Test Client 2 ends.")
  }

  // test 3 aims to simulate End command and resending scheme

  test("Client 3") {
    println("[TEST] Test Client 3 starts.")
    val index =3
    val c = new Client()

    Thread.sleep(3000) // wait a safe margin for the first 2 tests to go through
    c.sendEnd(BASE_PORT + index)

    Thread.sleep(1000) // wait a safe margin for serve to terminate, then test the resending scheme
    val index2 = 4
    val c2 = new Client()
    val msg = BASE_MSG + index2
    val res = c2.sendReq(msg, BASE_PORT + index2)
    assert(res == "Invalid response.")
    println("[TEST] Test Client 3 ends.")
  }
}
