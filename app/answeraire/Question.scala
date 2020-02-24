package answeraire

final case class Question(
  id: Long,
  heading: String,
  subheading: String,
  question: String,
  answer: String
)
