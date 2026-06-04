package com.example.eventify.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = NoPastEventsValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface NoPastEvents {
    String message() default "La fecha no puede ser anterior a hoy";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
