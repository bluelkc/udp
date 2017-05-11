/**
  * Created by kecen on 10/5/17.
  */
package udp

import java.io.ByteArrayOutputStream
import java.net.{DatagramPacket, DatagramSocket, InetSocketAddress}
import com.twitter.chill._

object Util {

  private val kryo = {
    val instantiator = new ScalaKryoInstantiator
    instantiator.setRegistrationRequired(false)
    val kryo = instantiator.newKryo()
    kryo.register(classOf[Msg])
    kryo.register(classOf[End])
    kryo.register(classOf[MsgType])
    kryo
  }

  def serialise(obj : Any) : Array[Byte] = {
    val baos = new ByteArrayOutputStream
    val output = new Output(baos)
    kryo.writeClassAndObject(output, obj)
    output.close()
    baos.toByteArray
  }

  def deserialise(bytes : Array[Byte]) : Any = {
    var flag = false
    while (!flag) {
      flag = true
      try {
        return kryo.readClassAndObject(new Input(bytes))
      } catch {
        case a: IndexOutOfBoundsException =>
          println("IndexOutOfBoundsException catched")
          flag = false
      }
    }
  }

  def send(message : MsgType, socket : DatagramSocket, addr : InetSocketAddress) : Unit = {
    val bytes = serialise(message)
    socket.send(new DatagramPacket(bytes, bytes.length, addr))
  }

  def receive(socket : DatagramSocket, packet : DatagramPacket) : Unit = {
    socket.receive(packet)
  }
}
