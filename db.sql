DROP DATABASE IF EXISTS text_board;
CREATE DATABASE text_board;
use text_board;

DROP DATABASE IF EXISTS text_board;
CREATE DATABASE text_board;
use text_board;

CREATE TABLE article (
   id INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
   regDate DATETIME NOT NULL,
   updateDate DATETIME NOT NULL,
   title CHAR(100) NOT NULL,
   `body` TEXT NOT NULL
   );

   SELECT * FROM article;

   INSERT INTO article
   SET regDate =NOW(),
   updateDate = NOW(),
   title = CONCAT("제목", RAND()),
   `body` = CONCAT("내용", RAND());