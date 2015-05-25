package todomvc

import japgolly.scalajs.react.{ReactComponentB, _}
import japgolly.scalajs.react.vdom.prefix_<^._

object FooterC {
  case class Props(
     count:            Int,
     completedCount:   Int,
     current:          TodoFilter,
     onClearCompleted: ReactEvent ⇒ Unit
  )

  import scalaz.syntax.std.string._

  val component = ReactComponentB[Props]("todo_footer")
    .render {
      props ⇒
        val clearButton =
          <.button(^.id := "clear-completed", ^.onClick ==> props.onClearCompleted, "Clear completed")

        val filterLinks: TagMod = {
          def filterLink(s: TodoFilter): TagMod =
            <.li(<.a(^.href := s.link, ^.classSet("selected" → (props.current == s)), s.title))

          (TodoFilter.values map filterLink).reduceLeft(_ + " " + _)
        }

        <.footer(
          ^.id := "footer",
          <.span(^.id := "todo-count", <.strong(props.count), s"${"item".plural(props.count)} left"),
          <.ul(
            ^.id := "filters",
            filterLinks,
            Option(clearButton).filter(_ ⇒ props.completedCount > 0)
          )
        )
  }
}
