/**
  * Created by kecen on 3/5/17.
  */
package udp

import java.io.{ByteArrayOutputStream, ObjectOutputStream}
import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress}

import Util._
import udp.{End, Msg, MsgType}

import scala.util.{Success, Failure}
import scala.concurrent._
import ExecutionContext.Implicits.global


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
            println("Server terminates.")
            //udpSocket.close()
            System.exit(1)
          }
          case m: Msg => {
            if (m.message.contains("1")) {
              Thread.sleep(2000)
            }
            val reply = echoMsg(m.message)
            send(reply, udpSocket, new InetSocketAddress(recv.getAddress, recv.getPort))
            println("Sent reply to client at " + recv.getAddress + ": " + reply.message)
          }
          case _ => {
            println("Received something alien.")
          }
        }
      }
      f onComplete {
        case Success(res) => res /*match {
          case e: End => {
            println("Server terminates.")
            //udpSocket.close()
            System.exit(1)
          }
          case m: Msg => {
            val reply = echoMsg(m.message)
            send(reply, udpSocket, new InetSocketAddress(recv.getAddress, recv.getPort))
            println("Sent reply to client at " + recv.getAddress + ": " + reply.message)
          }
          case _ => {
            println("Received something alien.")
          }
        }*/
        case Failure(e) => println("An error has occurred: " + e.getMessage)
      }
    }
  }

  def echoMsg(input : String) : Msg = {
    return new Msg(input + ":echoed")
  }
}