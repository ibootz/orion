package com.polaris.manage.web.advice;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.polaris.common.constant.ExceptionConstants;
import com.polaris.common.exception.ApiException;
import com.polaris.common.exception.AppMessage;
import com.polaris.common.exception.PolarisException;
import com.polaris.common.utils.JsonUtil;

/**
 * 专门用来处理带有RestController和Controller注解的controller层面的异常.
 * 异常匹配顺序是从上到下，匹配到合适的异常处理程序之后就不再向下匹配
 * 
 * @author John
 *
 */
@RestControllerAdvice(annotations = { RestController.class, Controller.class })
public class ExceptionHandleAdvice {

	private static final Logger LOGGER = LogManager.getLogger(ExceptionHandleAdvice.class);

	@Autowired
	private MessageSource messageSource;

	/**
	 * 处理Controller层主动抛出的API异常
	 */
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<AppMessage> handleApiException(ApiException e) {
		String message = messageSource.getMessage(e.getErrorKey(), e.getArgs(), null);
		if (StringUtils.isBlank(message)) {
			message = messageSource.getMessage(ExceptionConstants.API_EXCEPTION, null, null);
		}
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 400 - Spring validation Exception - 不符合验证规范
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(BindException.class)
	public ResponseEntity<AppMessage> handleBindException(BindException e) {
		BindingResult bindingResult = e.getBindingResult();
		String messageKey = ExceptionConstants.BAD_REQUEST;
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			messageKey = fieldError.getDefaultMessage();
		}
		String message = messageSource.getMessage(messageKey, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		appMessage.setMoreInfo(e.getMessage());
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 400 - Spring validation Exception - 不符合验证规范
	 * 
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<AppMessage>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		BindingResult bindingResult = e.getBindingResult();
		List<AppMessage> appMessages = new ArrayList<>();
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			String message = messageSource.getMessage(fieldError.getDefaultMessage(), null, null);
			AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
			appMessage.setMoreInfo(e.getMessage());
			appMessages.add(appMessage);
		}
		if (CollectionUtils.isEmpty(appMessages)) {
			String message = messageSource.getMessage(ExceptionConstants.BAD_REQUEST, null, null);
			AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
			appMessage.setMoreInfo(e.getMessage());
			appMessages.add(appMessage);
		}
		return new ResponseEntity<>(appMessages, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 400 - Bad Request - JSON序列化反序列化失败
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<AppMessage> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		String message = messageSource.getMessage(ExceptionConstants.BAD_REQUEST, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		appMessage.setMoreInfo(e.getMessage());
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 405 - Method Not Allowed
	 */
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<AppMessage> handleHttpRequestMethodNotSupportedException(
			HttpRequestMethodNotSupportedException e) {
		String message = messageSource.getMessage(ExceptionConstants.METHOD_NOT_ALLOWED, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		appMessage.setMoreInfo(e.getMessage());
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 415 - Unsupported Media Type
	 */
	@ExceptionHandler(HttpMediaTypeNotSupportedException.class)
	public ResponseEntity<AppMessage> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
		String message = messageSource.getMessage(ExceptionConstants.CONTENT_TYPE_NOT_SUPPORTED, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		appMessage.setMoreInfo(e.getMessage());
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 500 - 处理其他层抛过来的经过封装的异常，通过在AccessHandleAdvice中拦截封装，可以拦截绝大部分应用内抛出来的异常
	 * 
	 * @param e
	 * @return
	 */
	@ExceptionHandler(PolarisException.class)
	public ResponseEntity<AppMessage> handlePolarisException(PolarisException e) {
		String message = messageSource.getMessage(ExceptionConstants.APPLICATION_EXCEPTION, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		if ("null".equalsIgnoreCase(e.getMessage()) || StringUtils.isBlank(e.getMessage())) {
			appMessage.setMoreInfo("空指针异常");
		} else {
			appMessage.setMoreInfo(e.getMessage());
		}
		LOGGER.error(e.getMessage(), e);
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

	/**
	 * 500 - Internal Server Error 一些其他异常
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEntity<AppMessage> handleException(Exception e) {
		String message = messageSource.getMessage(ExceptionConstants.UNKNOWN_EXCEPTION, null, null);
		AppMessage appMessage = JsonUtil.fromJSON(message, AppMessage.class);
		appMessage.setMoreInfo(e.getMessage());
		LOGGER.error(e.getMessage(), e);
		return new ResponseEntity<>(appMessage, HttpStatus.valueOf(appMessage.getHttpStatus()));
	}

}
