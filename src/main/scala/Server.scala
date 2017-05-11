/**
  * Created by kecen on 3/5/17.
  */
package udp

import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress}
import scala.util.{Success, Failure}
import scala.concurrent._
import ExecutionContext.Implicits.global
import Util._

object Server {

  def run(): Unit = {

    val SIZE = 4096
    val PORT = 9999

    val udpSocket = new DatagramSocket(PORT)

    while(true) {

      val buffer = new Array[Byte](SIZE)
      val recv = new DatagramPacket(buffer, buffer.length)
      receive(udpSocket, recv)

      val f : Future[Any] = Future {
        val res = deserialise(recv.getData)
        res match {
          case e: End => {
            println("[SERVER] Server terminates.")
            udpSocket.close()
          }
          case m: Msg => {
            val reply = echoMsg(m.message)
            send(reply, udpSocket, new InetSocketAddress(recv.getAddress, recv.getPort))
            println("[SERVER] Sent reply to client at " + recv.getAddress + ": " + reply.message)
          }
          case _ => {
            println("[SERVER] Received something alien.")
          }
        }
      }
      f onComplete {
        case Success(res) => res
        case Failure(e) =>
          println("[SERVER] An error has occurred: " + e.getMessage)
          e.printStackTrace()
      }
    }
  }

  def echoMsg(input : String) : Msg = {
    Msg(input + ":echoed")
  }
}
