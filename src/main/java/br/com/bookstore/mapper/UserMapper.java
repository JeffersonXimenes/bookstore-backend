package br.com.bookstore.mapper;

import br.com.bookstore.model.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toEntity(String email, String name, String password);
}
