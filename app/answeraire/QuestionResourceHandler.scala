package answeraire

import javax.inject.{Inject, Provider}
import play.api.libs.json._
import scala.concurrent.{ExecutionContext, Future}


/**
  * DTO for displaying question information.
  */
case class QuestionResource(
  id: String,
  link: String,
  heading: String,
  subheading: String,
  question: String,
  answer: String
)

object QuestionResource {
  /**
    * Mapping to read/write a QuestionResource out as a JSON value.
    */
    implicit val format: Format[QuestionResource] = Json.format
}


/**
  * Controls access to the backend data, returning [[QuestionResource]]
  */
class QuestionResourceHandler @Inject()(
    routerProvider: Provider[QuestionRouter],
    questionRepository: QuestionRepository)(implicit ec: DatabaseExecutionContext) {

  def create(questionInput: QuestionFormInput): Future[QuestionResource] = {
    val data = Question(999,
      questionInput.heading,
      questionInput.subheading,
      questionInput.question,
      questionInput.answer)
    // We don't actually create the question, so return what we have
    questionRepository.create(data).map { id =>
      createQuestionResource(data)
    }
  }

  def lookup(id: String): Future[Option[QuestionResource]] = {
    val questionFuture = questionRepository.get(id.toLong)
    questionFuture.map { maybeQuestion =>
      maybeQuestion.map { question =>
        createQuestionResource(question)
      }
    }
  }

  def find(): Future[Iterable[QuestionResource]] = {
    questionRepository.list().map { questionList =>
      questionList.map(question => createQuestionResource(question))
    }
  }

  private def createQuestionResource(p: Question): QuestionResource = {
    QuestionResource(p.id.toString, routerProvider.get.link(p.id), p.heading,
      p.subheading, p.question, p.answer)
  }

}
