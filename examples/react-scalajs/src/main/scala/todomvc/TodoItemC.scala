package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.ext.KeyCode

object TodoItemC {

  case class Props(
    id:        TodoId,
    todo:      TodoItem,
    editing:   Boolean,
    onToggle:  ReactEvent ⇒ Unit,
    onDestroy: ReactEvent ⇒ Unit,
    onEdit:    ReactEvent ⇒ Unit,
    onSave:    String     ⇒ Unit,
    onCancel:  ReactEvent ⇒ Unit)

  case class State(editText: String)

  case class TodoItemBackend(t: BackendScope[Props, State]){
    def handleSubmit(event: ReactEvent) =
      Option(t.state.editText.trim)
        .filter(_.nonEmpty)
        .foreach(t.props.onSave)

    def handleKeyDown(event: ReactKeyboardEvent) =
      event.nativeEvent.keyCode match {
        case KeyCode.escape ⇒
          t.modState(_.copy(editText = t.props.todo.title))
          t.props.onCancel(event)
        case KeyCode.enter ⇒
          handleSubmit(event)
        case _ ⇒ ()
      }

    def handleChange(event: ReactEventI) =
      t.modState(_.copy(editText = event.target.value))
  }

  val component = ReactComponentB[Props]("todoItem")
    .initialStateP(p ⇒ State(p.todo.title))
    .backend(TodoItemBackend)
    .render {
      (props, state, backend) ⇒
        <.li(
          ^.classSet(
            "completed" → props.todo.completed,
            "editing"   → props.editing
          ),
          <.div(
            ^.className := "view",
            <.input(
              ^.className := "toggle",
              ^.`type`    := "checkbox",
              ^.checked   := props.todo.completed,
              ^.onChange ==> props.onToggle
            ),
            <.label(props.todo.title, ^.onClick ==> props.onEdit),
            <.button(^.className := "destroy", ^.onClick ==> props.onDestroy)
          ),
          <.input(
            ^.ref       := "editField",
            ^.className := "edit",
            ^.onBlur    ==> backend.handleSubmit,
            ^.onChange  ==> backend.handleChange,
            ^.onKeyDown ==> backend.handleKeyDown,
            ^.value      := state.editText
          )
        )
    }
}
