package answeraire

import javax.inject.Inject

import play.api.Logger
import play.api.data.Form
import play.api.libs.json.Json
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

case class QuestionFormInput(
  heading: String,
  subheading: String,
  question: String,
  answer: String,
)

/**
  * Takes HTTP requests and produces JSON.
  */
class QuestionController @Inject()(cc: QuestionControllerComponents)(
    implicit ec: ExecutionContext)
    extends QuestionBaseController(cc) {

  private val logger = Logger(getClass)

  private val form: Form[QuestionFormInput] = {
    import play.api.data.Forms._

    Form(
      mapping(
        "heading"    -> text,
        "subheading" -> text,
        "question"   -> text,
        "answer"     -> text
      )(QuestionFormInput.apply)(QuestionFormInput.unapply)
    )
  }

  def index: Action[AnyContent] = QuestionAction.async { implicit request =>
    logger.trace("index: ")
    questionResourceHandler.find.map { questions =>
      Ok(Json.toJson(questions))
    }
  }

  def process: Action[AnyContent] = QuestionAction.async { implicit request =>
    logger.trace("process: ")
    processJsonQuestion()
  }

  def show(id: String): Action[AnyContent] = QuestionAction.async {
    implicit request =>
      logger.trace(s"show: id = $id")
      questionResourceHandler.lookup(id).map { question =>
        Ok(Json.toJson(question))
      }
  }

  private def processJsonQuestion[A]()(
    implicit request: QuestionRequest[A]): Future[Result] = {
    logger.warn(s"request=$request")
    def failure(badForm: Form[QuestionFormInput]) = {
      Future.successful(BadRequest(badForm.errorsAsJson))
    }

    def success(input: QuestionFormInput) = {
      questionResourceHandler.create(input).map { question =>
        Created(Json.toJson(question)).withHeaders(LOCATION -> question.link)
      }
    }

    form.bindFromRequest().fold(failure, success)
  }
}
