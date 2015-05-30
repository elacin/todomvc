package todomvc

import org.scalajs.dom
import upickle.{Reader, Writer}

case class Storage(storage: dom.ext.Storage, namespace: String) {
  def write[T: Writer](data: T) =
    storage(namespace) = upickle.write(data)

  def read[T: Reader]: Option[T] =
    storage(namespace).map(s ⇒ upickle.read(s))
}
