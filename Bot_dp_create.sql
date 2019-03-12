CREATE TABLE users (
  user_id varchar(20) NOT NULL,
  bot_state varchar(45) NOT NULL DEFAULT '/start',
  phone_number varchar(45) DEFAULT NULL,
  city varchar(20) DEFAULT NULL,
  PRIMARY KEY (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
