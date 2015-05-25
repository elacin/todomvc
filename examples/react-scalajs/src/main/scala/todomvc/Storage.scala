package todomvc

import org.scalajs.dom
import upickle.{Reader, Writer}

object Storage {
  val storage: dom.ext.Storage = dom.ext.LocalStorage
  val namespace = "todos-react-scalajs"

  def write[T: Writer](data: T) =
    storage(namespace) = upickle.write(data)

  def read[T: Reader]: Option[T] =
    storage(namespace).map(s ⇒ upickle.read(s))
}
