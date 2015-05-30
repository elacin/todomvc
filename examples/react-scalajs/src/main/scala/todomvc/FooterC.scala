package todomvc

import japgolly.scalajs.react.{ReactComponentB, _}
import japgolly.scalajs.react.vdom.prefix_<^._
import scalaz.syntax.std.string._
import scalaz.syntax.std.list._

object FooterC {
  case class Props(
    count:            Int,
    completedCount:   Int,
    current:          TodoFilter,
    onClearCompleted: () ⇒ Unit
  )

  val component = ReactComponentB[Props]("todo_footer")
    .render {
      props ⇒
        def clearButton =
          <.button(^.id := "clear-completed", ^.onClick --> props.onClearCompleted(), "Clear completed")

        def filterLink(s: TodoFilter): TagMod =
          <.li(<.a(^.href := s.link, (props.current == s) ?= (^.className := "selected"), s.title))

        <.footer(
          ^.id := "footer",
          <.span(^.id := "todo-count", <.strong(props.count), s"${"item" plural props.count} left"),
          <.ul(
            ^.id := "filters",
            (TodoFilter.values map filterLink).toList.intersperse(" "),
            (props.completedCount > 0) ?= clearButton
          )
        )
  }
}
