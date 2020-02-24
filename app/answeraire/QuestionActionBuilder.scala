package answeraire

import javax.inject.Inject

import play.api.{Logger, MarkerContext}
import play.api.http.{FileMimeTypes, HttpVerbs}
import play.api.i18n.{Langs, MessagesApi}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

/**
  * A wrapped request for question resources.
  *
  * This is commonly used to hold request-specific information like
  * security credentials, and useful shortcut methods.
  */
trait QuestionRequestHeader
    extends MessagesRequestHeader
    with PreferredMessagesProvider
class QuestionRequest[A](request: Request[A], val messagesApi: MessagesApi)
    extends WrappedRequest(request)
    with QuestionRequestHeader


/**
  * The action builder for the Question resource.
  *
  * This is the place to put logging, metrics, to augment
  * the request with contextual data, and manipulate the
  * result.
  */
class QuestionActionBuilder @Inject()(messagesApi: MessagesApi,
                                  playBodyParsers: PlayBodyParsers)(
    implicit val executionContext: ExecutionContext)
    extends ActionBuilder[QuestionRequest, AnyContent]
    with HttpVerbs {

  override val parser: BodyParser[AnyContent] = playBodyParsers.anyContent

  type QuestionRequestBlock[A] = QuestionRequest[A] => Future[Result]

  private val logger = Logger(this.getClass)

  override def invokeBlock[A](request: Request[A],
                              block: QuestionRequestBlock[A]): Future[Result] = {
    // Convert to marker context and use request in block
    logger.trace(s"invokeBlock: ")

    val future = block(new QuestionRequest(request, messagesApi))

    future.map { result =>
      request.method match {
        case GET | HEAD =>
          result.withHeaders("Cache-Control" -> s"max-age: 100")
        case other =>
          result
      }
    }
  }
}

/**
  * Packages up the component dependencies for the question controller.
  *
  * This is a good way to minimize the surface area exposed to the controller, so the
  * controller only has to have one thing injected.
  */
case class QuestionControllerComponents @Inject()(
    questionActionBuilder: QuestionActionBuilder,
    questionResourceHandler: QuestionResourceHandler,
    actionBuilder: DefaultActionBuilder,
    parsers: PlayBodyParsers,
    messagesApi: MessagesApi,
    langs: Langs,
    fileMimeTypes: FileMimeTypes,
    executionContext: scala.concurrent.ExecutionContext)
    extends ControllerComponents

/**
  * Exposes actions and handler to the QuestionController by wiring the injected state into the base class.
  */
class QuestionBaseController @Inject()(pcc: QuestionControllerComponents)
    extends BaseController {
  override protected def controllerComponents: ControllerComponents = pcc

  def QuestionAction: QuestionActionBuilder = pcc.questionActionBuilder

  def questionResourceHandler: QuestionResourceHandler = pcc.questionResourceHandler
}
