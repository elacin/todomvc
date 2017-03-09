package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom.html

object Footer {

  case class Props(
      filterLink: TodoFilter => VdomTag,
      onClearCompleted: Callback,
      currentFilter: TodoFilter,
      activeCount: Int,
      completedCount: Int
  )

  class Backend($ : BackendScope[Props, Unit]) {
    def clearButton(P: Props): VdomTagOf[html.Button] =
      <.button(
        ^.className := "clear-completed",
        ^.onClick --> P.onClearCompleted,
        "Clear completed",
        ^.visibility.hidden.when(P.completedCount == 0)
      )

    def filterLink(P: Props)(s: TodoFilter): VdomTagOf[html.LI] =
      <.li(
        P.filterLink(s)(
          s.title,
          (^.className := "selected").when(P.currentFilter == s)
        )
      )

    def render(P: Props): VdomTagOf[html.Element] =
      <.footer(
        ^.className := "footer",
        <.span(
          ^.className := "todo-count",
          <.strong(P.activeCount),
          s" ${if (P.activeCount == 1) "item" else "items"} left"
        ),
        <.ul(
          ^.className := "filters",
          TodoFilter.values.map(filterLink(P)).toTagMod
        ),
        clearButton(P)
      )
  }

  private val component =
    ScalaComponent
      .builder[Props]("Footer")
      .stateless
      .renderBackend[Backend]
      .build

  def apply(P: Props): VdomElement =
    component(P)
}
