package answeraire
import play.api.db.Database

import javax.inject._
import play.api._
import play.api.mvc._
import anorm._


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
class HomeController @Inject()(val controllerComponents: ControllerComponents,
  database: Database
     ,
  databaseExecutionContext: DatabaseExecutionContext
)
    extends BaseController {

  /**
   * Create an Action to render an HTML page.
   *
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
    */

  def index() = Action { implicit request: Request[AnyContent] =>
    database.withConnection { implicit c =>
      val heading: String = SQL(
  """
 select heading from question q, answer a where a.id=q.answer_id and q.id={question_id}
  """).on("question_id" -> 1).
        as(SqlParser.str("heading").single)
      Ok(answeraire.views.html.index(heading.toString))
    }
  }
}
