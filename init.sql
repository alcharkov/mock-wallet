CREATE TABLE if not exists account (
  id          INT,
  balance     INT
);

CREATE TABLE if not exists transaction (
   id         INT,
   afrom      INT,
   ato        INT,
   amount     INT
);
