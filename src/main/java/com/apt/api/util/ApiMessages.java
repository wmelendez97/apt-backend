package com.apt.api.util;

public class ApiMessages {

	// Generic success messages
	public static final String SUCCESS_ACTION = "Request completed successfully";
	public static final String SUCCESS_CREATION = "Record created successfully";
	public static final String SUCCESS_UPDATE = "Record updated successfully";
	public static final String SUCCESS_ACTIVATION = "Record activated successfully";
	public static final String SUCCESS_INACTIVATION = "Record deactivated successfully";

	// Generic error messages
	public static final String ERROR_PROCESS = "Request could not be processed";
	public static final String ERROR_VALIDATION = "Request is invalid or incomplete";
	public static final String ERROR_INTERNAL = "Internal error occurred while processing the request";
	public static final String ERROR_DB = "Request could not be completed due to data integrity issue";
	public static final String ERROR_NOT_FOUND = "Requested resource not found";
	public static final String ERROR_DUPLICATE = "Record already exists";

	// External service errors
	public static final String ERROR_EXTERNAL_SERVICE = "Error consuming external service";
	public static final String ERROR_EXTERNAL_SERVICE_TIMEOUT = "Timeout while consuming external service";

	// Authentication / authorization errors
	public static final String ERROR_UNAUTHORIZED = "Unauthorized access";
	public static final String ERROR_FORBIDDEN = "Permission denied";

	// Specific JWT cases
	public static final String ERROR_TOKEN_EXPIRED = "Your current session has expired. Please log in again.";
	public static final String ERROR_TOKEN_INVALID = "Could not validate the request. Please log in again.";
	public static final String ERROR_TOKEN_MISSING = "Session not started. Please log in to continue.";

	// System user errors
	public static final String ERROR_USER_NOT_REGISTERED = "User is not registered in the system";
	public static final String ERROR_USER_INACTIVE = "User is inactive in the system";

	private ApiMessages() {
	}
}