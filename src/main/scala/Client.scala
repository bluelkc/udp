/**
  * Created by kecen on 9/5/17.
  */
package udp

import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress, SocketTimeoutException}
import Util._

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
    send(message, udpSocket, address)
    println("[CLIENT] Sent \"" + message + "\" to server at " + address.getAddress)

    val buffer = new Array[Byte](SIZE)
    val packet = new DatagramPacket(buffer, buffer.length)

    var flag = false
    udpSocket.setSoTimeout(2000)
    var counter = 0

    while(!flag) {
      try {
        receive(udpSocket, packet)
        flag = true
      } catch {
        case e : SocketTimeoutException =>
          if (counter < MAX_RETRY) {
            send(message, udpSocket, address)
            counter += 1
            println("[CLIENT] Resent messsage " + message.message + " to server")
          } else {
            flag = true
          }
      }
    }

    val res = deserialise(packet.getData)
    udpSocket.close()
    res match {
      case m : Msg =>
        println("[CLIENT] received from server: " + m.message)
        m.message
      case _ => println("[CLIENT] Invalid response.")
        "Invalid response."
    }
  }

  def sendEnd(port : Int) : Unit = {
    val udpSocket = new DatagramSocket(port)
    val address = new InetSocketAddress(SERVER, PORT_AT_SERVER)
    val message = End()
    send(message, udpSocket, address)
    println("[CLIENT] Sent End to server.")
    udpSocket.close()
  }
}
