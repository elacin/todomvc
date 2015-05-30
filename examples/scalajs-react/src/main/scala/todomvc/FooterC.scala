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
          <.button(^.className := "clear-completed", ^.onClick --> props.onClearCompleted(), "Clear completed")

        def filterLink(s: TodoFilter): TagMod =
          <.li(<.a(^.href := s.link, (props.current == s) ?= (^.className := "selected"), s.title))

        def withSpaces(ts: TagMod*) =
          ts.toList.intersperse(" ")

        <.footer(
          ^.className := "footer",
          <.span(^.className := "todo-count", withSpaces(<.strong(props.count), "item" plural props.count, "left")),
          <.ul(
            ^.className := "filters",
            withSpaces(TodoFilter.values map filterLink),
            (props.completedCount > 0) ?= clearButton
          )
        )
  }
}
