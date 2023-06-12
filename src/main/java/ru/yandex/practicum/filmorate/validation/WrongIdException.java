package ru.yandex.practicum.filmorate.validation;

public class WrongIdException extends RuntimeException {
    public WrongIdException(String message) {
        super(message);
    }
}
