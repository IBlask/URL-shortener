CREATE TABLE USERS(
  user_id int not null AUTO_INCREMENT,
  username varchar(255) not null,
  password varchar(60) not null,
  PRIMARY KEY (user_id)
);

CREATE TABLE URL_TABLE(
  url_id int not null AUTO_INCREMENT,
  full_url varchar(1000) not null,
  short_url varchar(8) not null,
  redirects int not null DEFAULT 0,
  redirect_type smallint not null DEFAULT 302,
  user_id int not null,
  PRIMARY KEY (url_id)
);

