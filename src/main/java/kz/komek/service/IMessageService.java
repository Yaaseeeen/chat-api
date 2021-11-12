package kz.komek.service;

import kz.komek.model.MessagesDto;
import kz.komek.model.MessagesReadByUsersDto;

import java.util.List;

public interface IMessageService {

  MessagesDto getMessage(Long id);

  List<MessagesDto> getMessages(Long convId);

  Long deleteMessage(Long id);

  MessagesDto saveMessage(MessagesDto dto);

  MessagesReadByUsersDto markMessageAsRead(MessagesReadByUsersDto dto);

  Long unmarkMessageAsRead(Long id);

  Integer getCountUnreadMessages(Long userId);
}
