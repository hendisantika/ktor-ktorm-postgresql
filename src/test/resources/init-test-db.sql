CREATE TABLE if NOT EXISTS book
(
    id
    SERIAL
    PRIMARY
    KEY,
    NAME
    VARCHAR
(
    255
) NOT NULL
    );

-- Add some initial test data
INSERT INTO book (name)
VALUES ('The Great Gatsby');
INSERT INTO book (name)
VALUES ('To Kill a Mockingbird');
INSERT INTO book (name)
VALUES ('1984');