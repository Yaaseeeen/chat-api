package kz.komek.model.enums;

public enum WatchingUserEnum {

  START_WATCHING_TIME("start_watching_time"),
  CONV_MESSAGES_ID("conv_messages_id"),
  ID("id"),
  USER_ID("user_id");

  private final String text;

  WatchingUserEnum(final String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }
}