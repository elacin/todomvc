package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._
import org.scalajs.dom.ext.KeyCode

object TodoItemC {

  case class Props(
    id:        TodoId,
    todo:      TodoItem,
    editing:   Boolean,
    onToggle:  ()     ⇒ Unit,
    onDestroy: ()     ⇒ Unit,
    onEdit:    ()     ⇒ Unit,
    onSave:    String ⇒ Unit,
    onCancel:  ()     ⇒ Unit)

  case class State(editText: String)

  case class TodoItemBackend(t: BackendScope[Props, State]){
    def editFieldSubmit(event: ReactEvent) =
      Option(t.state.editText.trim)
        .filter(_.nonEmpty)
        .foreach(t.props.onSave)

    def editFieldKeyDown(event: ReactKeyboardEvent) =
      event.nativeEvent.keyCode match {
        case KeyCode.escape ⇒
          t.modState(_.copy(editText = t.props.todo.title))
          t.props.onCancel()
        case KeyCode.enter ⇒
          editFieldSubmit(event)
        case _ ⇒ ()
      }

    def editFieldChanged(event: ReactEventI) =
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
              ^.onChange --> props.onToggle()
            ),
            <.label(props.todo.title, ^.onClick --> props.onEdit()),
            <.button(^.className := "destroy", ^.onClick --> props.onDestroy())
          ),
          <.input(
            ^.ref        := "editField",
            ^.className  := "edit",
            ^.onBlur    ==> backend.editFieldSubmit,
            ^.onChange  ==> backend.editFieldChanged,
            ^.onKeyDown ==> backend.editFieldKeyDown,
            ^.value      := state.editText
          )
        )
    }
}
