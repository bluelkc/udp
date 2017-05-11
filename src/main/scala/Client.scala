package udp

import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress, SocketTimeoutException}

import udp.{End, Msg}
import Util._

/**
  * Created by kecen on 9/5/17.
  */
class Client {

  private val SIZE = 4096
  private val PORT = 10000
  private val PORT_AT_SERVER = 9999
  private val SERVER = "127.0.0.1"
  private val received = false;
  private val MAX_RETRY = 3

  def sendReq(msg : String, port : Int): String = {

    val udpSocket = new DatagramSocket(port)
    val address = new InetSocketAddress(SERVER, PORT_AT_SERVER)

    val message = Msg(msg)
    //val message = End()
    send(message, udpSocket, address)
    println("Sent \"" + message + "\" to server at " + address.getAddress)

    if (message.isInstanceOf[End]) {
      return "Sent to server End command."
    }

    val buffer = new Array[Byte](SIZE)
    val packet = new DatagramPacket(buffer, buffer.length)

    var received = false
    udpSocket.setSoTimeout(2000)
    var counter = 0

    while(!received) {
      try {
        receive(udpSocket, packet)
        received = true
      } catch {
        case e : SocketTimeoutException =>
          send(message, udpSocket, address)
          println("Resent messsage " + message.message + " to server")
      }
    }
    receive(udpSocket, packet)
    val res = deserialise(packet.getData)
    res match {
      case m : Msg =>
        println("received from server: " + m.message)
        m.message
      case _ => println("Invalid response.")
        "Invalid response."
    }
  }

  def sendEnd(port : Int) : Unit = {
    val udpSocket = new DatagramSocket(port)
    val address = new InetSocketAddress(SERVER, PORT_AT_SERVER)
val message = End()
    send(message, udpSocket, address)
    println("Sent End to server.")
  }
}
