package at.htl.srsguard.service;

import at.htl.srsguard.error.FailedField;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ValidationService {

    @Inject
    Validator validator;

    public List<FailedField> validate(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (violations.isEmpty()) {
            return null;
        }

        return violations
                .stream()
                .map(o -> new FailedField(
                                o.getPropertyPath().toString(),
                                o.getInvalidValue() != null ? o.getInvalidValue().toString() : "",
                                o.getMessage()
                        )
                ).collect(Collectors.toList());
    }
}
