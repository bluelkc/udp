/**
  * Created by kecen on 3/5/17.
  */

package udp

abstract sealed class MsgType
  case class Msg(message : String) extends MsgType
  case class End() extends MsgType
