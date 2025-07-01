package com.code_room.user_service.domain.Exception;

public class CvUploadException extends RuntimeException {

  private final String userMessage;
  private final String developerMessage;
  private final ErrorType errorType;

  public CvUploadException(ErrorType errorType) {
    super();
    this.errorType = errorType;

    switch (errorType) {
      case ONLY_CLIENT_CAN_UPLOAD:
        this.userMessage = "Only clients are allowed to upload a CV.";
        this.developerMessage = "Attempt to upload a CV from a non-client role.";
        break;
      case FILE_NOT_FOUND:
        this.userMessage = "CV file is missing.";
        this.developerMessage = "Received a null or non-existent file.";
        break;
      case INVALID_FILE_TYPE:
        this.userMessage = "Invalid file type. Only PDF files are accepted.";
        this.developerMessage = "Attempted to upload a file that is not a PDF.";
        break;
      case UPLOAD_FAILED:
        this.userMessage = "Failed to upload the CV.";
        this.developerMessage = "An exception occurred during file write or save.";
        break;
      default:
        this.userMessage = "CV upload failed.";
        this.developerMessage = "Unexpected error during CV upload.";
        break;
    }
  }

  public String getUserMessage() {
    return userMessage;
  }

  public String getDeveloperMessage() {
    return developerMessage;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public enum ErrorType {
    ONLY_CLIENT_CAN_UPLOAD,
    FILE_NOT_FOUND,
    INVALID_FILE_TYPE,
    UPLOAD_FAILED
  }
}
