package com.aaron.vocabularyapi.exception;

@Deprecated
//@Slf4j
//@ControllerAdvice
public class GlobalErrorHandlerOld
{
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ResponseError> handleAllExceptions(Exception e)
//    {
//        log.error("handleAllExceptions. Error.", e);
//
//        Throwable cause, resultCause = e;
//        while((cause = resultCause.getCause()) != null && resultCause != cause)
//        {
//            resultCause = cause;
//        }
//
//        return buildResponse(INTERNAL_SERVER_ERROR, resultCause.getMessage());
//    }
//
//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity<ResponseError> handleNotFoundException(NotFoundException e)
//    {
//        log.error("handleNotFoundException. Error.", e);
//
//        return buildResponse(NOT_FOUND, e.getMessage());
//    }
//
//    @ExceptionHandler({ BadRequestException.class, IllegalArgumentException.class })
//    public ResponseEntity<ResponseError> handleBadRequestException(Exception e)
//    {
//        log.error("handleBadRequestException. Error.", e);
//
//        return buildResponse(BAD_REQUEST, e.getMessage());
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ResponseError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e)
//    {
//        log.error("handleMethodArgumentNotValidException. Error.", e);
//
//        String errorMessages = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .collect(joining(" "));
//
//        return buildResponse(BAD_REQUEST, errorMessages);
//    }
//
//    private ResponseEntity<ResponseError> buildResponse(HttpStatus status, String message)
//    {
//        return ResponseEntity.status(status)
//                .body(ResponseError.builder()
//                        .message(message)
//                        .build());
//    }
}