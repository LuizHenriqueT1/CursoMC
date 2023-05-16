package com.rick.cursomc.services.validation;
import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ClienteInsertValidation.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteInsert {
    String message() default "Erro de validação";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

