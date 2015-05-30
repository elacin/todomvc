package todomvc

import japgolly.scalajs.react.extra.Broadcaster

import scala.collection.mutable
import scala.language.postfixOps

class TodoModel(storage: Storage) extends Broadcaster[Unit] {

  private val todos = mutable.Map.empty[TodoId, Todo]

  /* restore saved todos */
  storage.read[Seq[Todo]].foreach {
    storedTodos ⇒ todos ++= storedTodos.map(t ⇒ (t.id, t))
  }

  override protected def broadcast(a: Unit): Unit = {
    storage.write(todoList)
    super.broadcast(a)
  }

  implicit final class TX[T](t: T){
    def !() = broadcast(())
    def by[U](f: T ⇒ U): (U, T) = (f(t), t)
  }

  private def updateStored(id: TodoId)(f: Todo ⇒ Todo) =
    todos.get(id).foreach(existing ⇒ todos(id) = f(existing))

  def addTodo(title: String): Unit =
    (todos += (Todo(TodoId.random, title, completed = false) by (_.id))) !()

  def clearCompleted(): Unit =
    todos.retain((id, todo) ⇒ !todo.completed) !()

  def delete(id: TodoId): Unit =
    todos.remove(id) !()

  def todoList: Seq[Todo] =
    todos.values.toSeq

  def toggleAll(checked: Boolean): Unit =
    todos.keys.foreach(updateStored(_)(_.copy(completed = checked))) !()

  def toggleCompleted(id: TodoId): Unit =
    updateStored(id)(old ⇒ old.copy(completed = !old.completed)) !()

  def update(id: TodoId, text: String): Unit =
    updateStored(id)(_.copy(title = text)) !()
}
