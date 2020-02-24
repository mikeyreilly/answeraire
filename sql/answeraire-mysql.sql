CREATE TABLE answer (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  answer varchar(8191) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;


INSERT INTO answer VALUES
(1,'answer 1'),
(2,'answer 2'),
(3,'answer 3'),
(4,'answer 4'),
(5,'answer 5');

CREATE TABLE question (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  heading varchar(255) DEFAULT NULL,
  subheading varchar(255) DEFAULT NULL,
  question varchar(255) NOT NULL,
  answer_id bigint(20) NOT NULL,
  PRIMARY KEY (id),
  KEY answer_id (answer_id),
  CONSTRAINT answer_fk FOREIGN KEY (answer_id) REFERENCES answer (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci;

INSERT INTO question VALUES
(1,'heading 1','subheading 1','question 1',1),
(2,'heading 2','subheading 2','question 2',2),
(3,'heading 3','subheading 3','question 3',3),
(4,'heading 4','subheading 4','question 4',4),
(5,'heading 5','subheading 5','question 5',5);


