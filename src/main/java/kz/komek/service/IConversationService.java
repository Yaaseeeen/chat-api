package kz.komek.service;

import kz.komek.model.ConversationDto;
import kz.komek.model.WatchingUsersDto;
import kz.komek.model.other.ConversationInfo;

import java.util.List;

public interface IConversationService {

  Long deleteConversation(Long id);

  List<ConversationDto> getConversationByUserId(Long userId);

  ConversationDto getConversation(Long id);

  List<WatchingUsersDto> getListConversationWatchers(Long convId);

  ConversationDto createConversation(ConversationDto dto);

  List<ConversationDto> getUnreadConversationByUserId(Long userId);

  Integer markConversationMessagesAsRead(Long id, Long userId);

  WatchingUsersDto saveUserWatchingConversation(WatchingUsersDto dto);

  Long deleteUserWatchingConversation(Long id);

  ConversationInfo getConversationInfo(Long convId);
}
