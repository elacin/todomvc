package todomvc

import java.util.UUID

case class TodoId(id: UUID)

object TodoId{
  def random = new TodoId(UUID.randomUUID)
}

case class Todo(id: TodoId, title: String, completed: Boolean)

sealed abstract class TodoFilter(val link: String, val title: String){
  def accepts(item: Todo): Boolean
}

object TodoFilter{
  object All extends TodoFilter("#/", "All"){
    override def accepts(item: Todo) = true
  }

  object Active extends TodoFilter("#/active", "Active"){
    override def accepts(item: Todo) = !item.completed
  }

  object Completed extends TodoFilter("#/completed", "Completed"){
    override def accepts(item: Todo) = item.completed
  }

  def values = Seq[TodoFilter](All, Active, Completed)
}

