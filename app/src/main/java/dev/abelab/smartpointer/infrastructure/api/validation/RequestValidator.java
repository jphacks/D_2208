package dev.abelab.smartpointer.infrastructure.api.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import dev.abelab.smartpointer.infrastructure.api.request.BaseRequest;

public class RequestValidator implements ConstraintValidator<RequestValidated, BaseRequest> {

    @Override
    public void initialize(RequestValidated constraintAnnotation) {}

    @Override
    public boolean isValid(final BaseRequest baseRequest, final ConstraintValidatorContext constraintValidatorContext) {
        baseRequest.validate();
        return true;
    }

}
