package answeraire
import akka.actor.ActorSystem
import anorm._
import anorm.SqlParser.scalar
import javax.inject.{Inject, Singleton}
import play.api.db.Database
import play.api.libs.concurrent.CustomExecutionContext
import play.api.{Logger}
import scala.concurrent.Future

@Singleton
class QuestionRepository @Inject()(database: Database,
  ec: DatabaseExecutionContext
) {

  private val logger = Logger(this.getClass)

  val parser: RowParser[Question] = Macro.namedParser[Question]

  def list(): Future[Iterable[Question]] = {
    Future {
      database.withConnection { implicit c =>
        SQL"select q.id, heading, subheading, question, answer from question q, answer a where a.id=q.answer_id".as(parser.*)
      }
    }(ec)
  }

  def get(questionId: Long): Future[Option[Question]] = {
    Future {
      database.withConnection { implicit c =>
        SQL"select q.id, heading, subheading, question, answer from question q, answer a where a.id=q.answer_id and q.id = $questionId".as(parser.singleOpt)
      }
    }(ec)
  }

  def create(data: Question): Future[Long] = {
    Future {
      logger.trace(s"create: data = $data")
      data.id
    }(ec)
  }

}
