package dev.abelab.smartpointer.infrastructure.api.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RequestValidator.class})
public @interface RequestValidated {
    String message()

    default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        RequestValidated[] value();
    }
}
