package com.udacity.jwdnd.course1.cloudstorage.validation;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.validation.UserNameConstraint.UsernameValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserNameConstraint {

    String message() default "Username Already Exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class UsernameValidator implements ConstraintValidator<UserNameConstraint, String> {

        private final UserMapper userMapper;

        public UsernameValidator(UserMapper userMapper) {
            this.userMapper = userMapper;
        }

        @Override
        public void initialize(UserNameConstraint constraintAnnotation) {}

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
            return userMapper.getUser(s) == null;
        }
    }
}
