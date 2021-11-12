package kz.komek.service.exceptions;

public enum ApiErrorType {
  E500_CONVERSATION_NOT_FOUND("Conversation not found"),
  E500_WATCHER_DUPLICATED("This observer has already been added to this chat"),
  E500_MESSAGE_NOT_FOUND("Message not found"),
  E500_READ_MESSAGE_NOT_FOUND("Read Message not found"),
  E500_WATCHER_NOT_FOUND("Watcher not found"),
  E401_UN_AUTHORIZED("User unauthorized");

  private final String text;

  ApiErrorType(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }

}
