DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS GenreLine;
DROP TABLE IF EXISTS FRIENDSHIP;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS FILM;
DROP TABLE IF EXISTS RatingMPA;
DROP TABLE IF EXISTS Genre;

CREATE TABLE IF NOT EXISTS RatingMPA (
    RatingID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    Name varchar(10) NOT NULL UNIQUE,
    Description varchar(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS Film (
    FilmID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    Name varchar(200) NOT NULL,
    Description varchar(200) NOT NULL,
    ReleaseDate date NOT NULL,
    Duration int NOT NULL,
    Rate int,
    RatingID int NOT NULL REFERENCES RatingMPA (RatingID) ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS Users (
    UserID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    Login varchar(50) NOT NULL,
    Name varchar(200) NOT NULL,
    Email varchar(256) NOT NULL UNIQUE,
    Birthday date NOT NULL
);

CREATE TABLE IF NOT EXISTS Likes (
    LikeID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    UserID int NOT NULL REFERENCES Users (UserID),
    FilmID int NOT NULL REFERENCES Film (FilmID) ON DELETE CASCADE,
    UNIQUE (UserID, FilmID)
);

CREATE TABLE IF NOT EXISTS Friendship (
    FriendshipID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    UserID int NOT NULL REFERENCES Users (UserID),
    FriendID int NOT NULL REFERENCES Users (UserID),
    Status bool NOT NULL,
    UNIQUE (UserID, FriendID)
);

CREATE TABLE IF NOT EXISTS Genre (
    GenreID int GENERATED BY DEFAULT AS IDENTITY NOT NULL PRIMARY KEY,
    Name varchar(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS GenreLine (
    FilmID int NOT NULL REFERENCES Film (FilmID) ON DELETE CASCADE,
    GenreID int NOT NULL REFERENCES Genre (GenreID) ON DELETE RESTRICT
);

ALTER TABLE GenreLine ADD PRIMARY KEY (FilmID, GenreID);