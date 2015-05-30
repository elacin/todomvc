package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra.OnUnmount
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom
import org.scalajs.dom.ext.KeyCode

object TodoListC {
  case class Props(model: TodoModel, current: TodoFilter)

  case class Backend(t: BackendScope[Props, State]) extends OnUnmount {
    /** I deliberately held the model out of the state to simulate that
      * it lives elsewhere - this handles the necessary signaling  */
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
    .render { (props, state, backend) ⇒
      val todos           = props.model.todoList
      val filteredTodos   = todos filter props.current.accepts

      val activeCount     = todos count TodoFilter.Active.accepts
      val completedCount  = todos.length - activeCount

      def footer = FooterC.component.propsRequired.build(
        FooterC.Props(
          count            = activeCount,
          completedCount   = completedCount,
          current          = props.current,
          onClearCompleted = props.model.clearCompleted
        )
      )

      def main =
        <.section(
          ^.className := "main",
          <.input(
            ^.className := "toggle-all",
            ^.`type`    := "checkbox",
            ^.checked   := activeCount == 0,
            ^.onChange ==> ((e: ReactEventI) ⇒ props.model.toggleAll(e.target.checked))
          ),
          <.ul(
            ^.className := "todo-list",
            filteredTodos.map(todo ⇒
              TodoItemC.component.propsRequired.build(
                TodoItemC.Props(
                    id        = todo.id,
                    todo      = todo,
                    editing   = state.editing.contains(todo.id),
                    onToggle  = () ⇒ props.model.toggleCompleted(todo.id),
                    onDestroy = () ⇒ props.model.delete(todo.id),
                    onEdit    = () ⇒ backend.edit(todo.id),
                    onSave    = s ⇒ backend.save(todo.id, s),
                    onCancel  = backend.cancel
                  )
              )
            )
          )
        )

      <.div(
        <.header(
          ^.className := "header",
          <.input(
            ^.className   := "new-todo",
            ^.placeholder := "What needs to be done?",
            ^.onKeyDown  ==> backend.handleNewTodoKeyDown,
            ^.autoFocus   := true
          )
        ),
        todos.nonEmpty ?= main,
        todos.nonEmpty ?= footer
      )
  }.configure(OnUnmount.install)

  def apply(s: TodoFilter, namespace: String = "todos-scalajs-react") = {
    val model = new TodoModel(Storage(dom.ext.LocalStorage, namespace))
    component.propsRequired.build(Props(model, s))
  }
}
