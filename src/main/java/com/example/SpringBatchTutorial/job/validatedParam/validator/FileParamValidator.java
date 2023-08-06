package com.example.SpringBatchTutorial.job.validatedParam.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class FileParamValidator implements JobParametersValidator {

    /**
     * 파일명 유효성 검증
     * @param parameters some {@link JobParameters} (can be {@code null})
     * @throws JobParametersInvalidException
     */
    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        String fileName = parameters.getString("fileName");

        if (!StringUtils.endsWithIgnoreCase(fileName, "csv")) {
            throw new JobParametersInvalidException("That is not csv file");
        }
    }

}
