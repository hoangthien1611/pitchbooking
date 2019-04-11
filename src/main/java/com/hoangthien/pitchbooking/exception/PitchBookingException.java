package com.hoangthien.pitchbooking.exception;

public class PitchBookingException extends RuntimeException {

    public PitchBookingException() {
    }

    public PitchBookingException(String message) {
        super(message);
    }

    public PitchBookingException(final String message, final Throwable cause){
        super(message, cause);
    }
}
