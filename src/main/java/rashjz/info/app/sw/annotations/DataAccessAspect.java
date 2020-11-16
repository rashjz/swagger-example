package rashjz.info.app.sw.annotations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
@AllArgsConstructor
public class DataAccessAspect {

    @Around("@annotation(dataAccessEvent)")
    public Object forParamName(ProceedingJoinPoint joinPoint, DataAccessEvent dataAccessEvent)
            throws Throwable {
        Object proceed = joinPoint.proceed();
        if (checkResponse(proceed)) {
            Integer paramValue = getParamValue(joinPoint, dataAccessEvent.paramName());
            log.info("parameter is {}", paramValue);
        }
        return proceed;
    }

    private int findParamIdx(ProceedingJoinPoint joinPoint, String paramName) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        for (int i = 0; i < parameterNames.length; i++) {
            if (parameterNames[i].equals(paramName)) {
                return i;
            }
        }
        throw new IllegalArgumentException("No such parameter : " + paramName);
    }

    private Integer getParamValue(ProceedingJoinPoint joinPoint, String paramName) {
        int packetIdParamIdx = findParamIdx(joinPoint, paramName);
        try {
            return (Integer) joinPoint.getArgs()[packetIdParamIdx];
        } catch (ClassCastException classCastException) {
            throw new IllegalArgumentException("Parameter : " + paramName + " cannot be cast to String",
                    classCastException);
        }
    }

    private boolean checkResponse(Object response) {
        if (response instanceof ResponseEntity) {
            ResponseEntity<?> responseEntity = (ResponseEntity<?>) response;
            HttpStatus statusCode = responseEntity.getStatusCode();
            return statusCode.is1xxInformational() || statusCode.is2xxSuccessful();
        }
        return true;
    }
}
