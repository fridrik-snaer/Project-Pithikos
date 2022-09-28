CREATE TABLE Words (
  id serial PRIMARY KEY,
  word varchar(255) NOT NULL,
  lang varchar(3) NOT NULL,
  rank int NOT NULL,
);

CREATE TABLE Quotes (
  id serial PRIMARY KEY,
  quote varchar(1023),
  reference varchar(255);
  lang varchar(3),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
);

CREATE TABLE Lessons (
  id serial PRIMARY KEY,
  lesson varchar(1023) NOT NULL,
  lang varchar(3) NOT NULL,
  lvl INT UNIQUE NOT NULL,
);

CREATE TABLE Users(
  id SERIAL PRIMARY KEY,
  email character varying(64) NOT NULL UNIQUE,
  username character varying(64) NOT NULL UNIQUE,
  password character varying(256) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP,
);

CREATE TABLE Relationship(
  sender_id INT, 
  receiver_id INT, 
  create_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
  FOREIGN KEY(sender_id) REFERENCES Users, 
  FOREIGN KEY(receiver_id) REFERENCES Users,
);

CREATE TABLE Stats(
  user_id INT,
  avg_wpm FLOAT,
  avg_acc FLOAT,
  tests_taken INT,
  FOREIGN KEY(user_id) REFERENCES Users(id),
);

CREATE TABLE Quote_Attempts(
  user_id INT,
  quote_id INT,
  time_start TIMESTAMP NOT NULL,
  time_finish TIMESTAMP NOT NULL,
  keystrokes INT,
  correct INT,
  daily BOOLEAN,
  completed BOOLEAN,
  FOREIGN KEY(user_id) references Users(id),
  FOREIGN KEY(quote_id) references Quotes(id),
  PRIMARY KEY(user_id, quote_id, time_start),
);

CREATE TABLE Random_Attempts(
  user_id INT,
  time_start TIMESTAMP NOT NULL,
  time_finish TIMESTAMP NOT NULL,
  keystrokes INT NOT NULL,
  correct INT NOT NULL,
  lang varchar(3) NOT NULL,
  mode varchar(255) NOT NULL,
  completed BOOLEAN NOT NULL,
  FOREIGN KEY(user_id) references Users(id),
  PRIMARY KEY(user_id, time_start)
); 