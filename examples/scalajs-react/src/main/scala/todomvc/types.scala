package todomvc

import java.util.UUID

case class TodoId(id: UUID)

object TodoId{
  def random = new TodoId(UUID.randomUUID)
}

case class TodoItem(id: TodoId, title: String, completed: Boolean)

sealed abstract class TodoFilter(val link: String, val title: String){
  def accepts(item: TodoItem): Boolean
}

object TodoFilter{
  object All extends TodoFilter("#/", "All"){
    override def accepts(item: TodoItem) = true
  }

  object Active extends TodoFilter("#/active", "Active"){
    override def accepts(item: TodoItem) = !item.completed
  }

  object Completed extends TodoFilter("#/completed", "Completed"){
    override def accepts(item: TodoItem) = item.completed
  }

  def values = Seq[TodoFilter](All, Active, Completed)
}

