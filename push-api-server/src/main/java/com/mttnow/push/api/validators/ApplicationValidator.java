package com.mttnow.push.api.validators;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.ErrorMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@Component
public class ApplicationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Application.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty","Application name is empty!");
        ValidationUtils.rejectIfEmpty(errors,"mode","mode.empty", "Application mode not set!");
    }

    public void validateApplicationMultipartRequestParams(MultipartFile file, APNSChannel apnsChannel, Application appForSaving) throws PushApplicationException {
        ErrorMessage message = new ErrorMessage();

        BindingResult result = new BeanPropertyBindingResult(appForSaving, "application");
        validate(appForSaving, result);
        if (result.hasErrors()) {
            message.setError("Please fill in required fields!");
            if (result.hasFieldErrors()) {
                Map<String, String> errors = new HashMap<String, String>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errors.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                message.setFieldErrors(errors);

            }
            throw new PushApplicationException("Please fill in required fields!", message);
        }

        if (apnsChannel != null && file == null) {
            message.setError("Certificate file missing!");
            throw new PushApplicationException(message);
        }
    }
}
