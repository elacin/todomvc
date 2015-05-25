package todomvc

import japgolly.scalajs.react.extra.Broadcaster

import scala.collection.mutable

class TodoModel extends Broadcaster[Unit] {

  override protected def broadcast(a: Unit): Unit = {
    Storage.write(todoList)
    super.broadcast(a)
  }

  implicit final class TX[T](t: T){
    def ! = broadcast(())
    def by[U](f: T ⇒ U): (U, T) = (f(t), t)
  }

  private val todos = mutable.Map.empty[TodoId, TodoItem]

  private def updateStored(id: TodoId)(f: TodoItem ⇒ TodoItem) =
    todos.get(id).foreach(existing ⇒ todos(id) = f(existing))

  /* restore saved todos from local storage */
  Storage.read[Seq[TodoItem]].foreach {
    storedTodos ⇒ todos ++= storedTodos.map(t ⇒ (t.id, t))
  }

  def addTodo(title: String): Unit =
    (todos += (TodoItem(TodoId.random, title, completed = false) by (_.id))) !

  def clearCompleted(): Unit =
    todos.retain((id, todo) ⇒ !todo.completed) !

  def delete(id: TodoId): Unit =
    todos.remove(id) !

  def todoList: Seq[TodoItem] =
    todos.values.toSeq.sortBy(_.title)

  def toggleAll(checked: Boolean): Unit =
    todos.keys.foreach(updateStored(_)(_.copy(completed = checked))) !

  def toggleCompleted(id: TodoId): Unit =
    updateStored(id)(old ⇒ old.copy(completed = !old.completed)) !

  def update(id: TodoId, text: String): Unit =
    updateStored(id)(_.copy(title = text)) !
}
