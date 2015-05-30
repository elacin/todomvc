package todomvc

import japgolly.scalajs.react._
import japgolly.scalajs.react.extra._
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object TodoApp extends JSApp {

  case class Page(s: TodoFilter)

  import router2._

  val routerConfig = RouterConfigDsl[Page].buildConfig { dsl =>
    import dsl._

    def filterRoute(s: TodoFilter): Rule =
      staticRoute(s.link, Page(s)) ~> render(TodoListC(s))

    val filterRoutes: Rule =
      TodoFilter.values map filterRoute reduce (_ | _)

    filterRoutes.notFound(redirectToPage(Page(TodoFilter.All))(Redirect.Replace))
  }

  val baseUrl = BaseUrl(dom.window.location.href.takeWhile(_ != '#'))

  @JSExport
  override def main() = {
    val router = Router(baseUrl, routerConfig.logToConsole)
    val routeC = router() render dom.document.getElementById("todoapp")
  }
}
