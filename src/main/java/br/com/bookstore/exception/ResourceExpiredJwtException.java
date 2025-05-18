package br.com.bookstore.exception;

public class ResourceExpiredJwtException extends RuntimeException {

	public ResourceExpiredJwtException(String message) {
        super(message);
    }
}
