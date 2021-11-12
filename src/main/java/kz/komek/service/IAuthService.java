package kz.komek.service;

import kz.komek.model.UserDto;

import java.util.List;

public interface IAuthService {

  List<UserDto> getUsers(Long convId);

}
