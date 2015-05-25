package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.ext.KeyCode

object TodoListC {
  val model = new TodoModel

  case class Props(model: TodoModel, current: TodoFilter)

  case class Backend(t: BackendScope[Props, State]) extends OnUnmount {
    /** not sure if this is a good way to do it - any change in the
      * model immediately triggers a new rendering because of this */
    val unregister = t.props.model.register(_ ⇒ t.forceUpdate())
    onUnmountF(unregister)

    def handleNewTodoKeyDown(event: ReactKeyboardEventI) =
      if (event.nativeEvent.keyCode == KeyCode.enter) {
        Option(event.target.value).map(_.trim).filterNot(_.isEmpty).foreach{
          value ⇒
            event.target.value = ""
            t.props.model.addTodo(value)
        }
      }

    def edit(id: TodoId) =
      t.modState(_.copy(editing = Some(id)))

    def save(id: TodoId, text: String) = {
      cancel()
      t.props.model.update(id, text)
    }

    def cancel() =
      t.modState(_.copy(editing = None))
  }

  case class State(editing: Option[TodoId])

  val component = ReactComponentB[Props]("react-todos")
    .initialState(State(None))
    .backend(Backend)
    .render {
      case (props, state, backend) ⇒
        val todos           = props.model.todoList
        val filteredTodos   = todos filter props.current.accepts

        val activeCount     = todos count TodoFilter.Active.accepts
        val completedCount  = todos.length - activeCount

        val footer = FooterC.component.propsConst(
          FooterC.Props(
            count            = activeCount,
            completedCount   = completedCount,
            current          = props.current,
            onClearCompleted = _ ⇒ props.model.clearCompleted()
          )).build.apply()

        val main =
          <.section(
            ^.id := "main",
            <.input(
              ^.id        := "toggle-all",
              ^.`type`    := "checkbox",
              ^.checked   := activeCount == 0,
              ^.onChange ==> ((e: ReactEventI) ⇒ props.model.toggleAll(e.target.checked))
            ),
            <.ul(
              ^.id := "todo-list",
              filteredTodos.map(	todo ⇒
                TodoItemC.component.propsConst(
                  TodoItemC.Props(
                    id        = todo.id,
                    todo      = todo,
                    editing   = state.editing.contains(todo.id),
                    onToggle  = _ ⇒ props.model.toggleCompleted(todo.id),
                    onDestroy = _ ⇒ props.model.delete(todo.id),
                    onEdit    = _ ⇒ backend.edit(todo.id),
                    onSave    = s ⇒ backend.save(todo.id, s),
                    onCancel  = _ ⇒ backend.cancel()
                  )
                ).build.apply()
              )
            )
          )

        <.div(
          <.header(
            ^.id := "header",
            <.input(
              ^.id          := "new-todo",
              ^.placeholder := "What needs to be done?",
              ^.onKeyDown  ==> backend.handleNewTodoKeyDown,
              ^.autoFocus   := true
            )
          ),
          Some(main  ).filter(_ ⇒ todos.nonEmpty),
          Some(footer).filter(_ ⇒ todos.nonEmpty)
        )
  }.configure(OnUnmount.install)

  def apply(s: TodoFilter) =
    component.propsConst(new Props(model, s)).build.apply()
}
