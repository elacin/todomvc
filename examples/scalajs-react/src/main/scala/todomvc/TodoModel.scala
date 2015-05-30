package todomvc

import japgolly.scalajs.react.extra.Broadcaster

import scala.language.postfixOps

class TodoModel(storage: Storage) extends Broadcaster[Unit] {
  object State {
    var todos = Seq.empty[Todo]

    def mod(f: Seq[Todo] ⇒ Seq[Todo]): Unit = {
      todos = f(todos)
      broadcast(())
    }

    def modOne(Id: TodoId)(f: Todo ⇒ Todo): Unit =
      mod(_.map {
        case existing@Todo(Id, _, _) ⇒ f(existing)
        case other                   ⇒ other
      })
  }

  /* restore saved todos */
  storage.read[Seq[Todo]].foreach(existing ⇒ State.mod(_ ++ existing))

  override protected def broadcast(a: Unit): Unit = {
    storage.write(todoList)
    super.broadcast(a)
  }

  def addTodo(title: String): Unit =
    State.mod(_ :+ Todo(TodoId.random, title, completed = false))

  def clearCompleted(): Unit =
    State.mod(_.filterNot(_.completed))

  def delete(id: TodoId): Unit =
    State.mod(_.filterNot(_.id == id))

  def todoList: Seq[Todo] =
    State.todos

  def toggleAll(checked: Boolean): Unit =
    State.mod(_.map(_.copy(completed = checked)))

  def toggleCompleted(id: TodoId): Unit =
    State.modOne(id)(old ⇒ old.copy(completed = !old.completed))

  def update(id: TodoId, text: String): Unit =
    State.modOne(id)(_.copy(title = text))
}
