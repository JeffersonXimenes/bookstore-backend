package br.com.bookstore.mapper;

import br.com.bookstore.model.dtos.BaseLabel;
import br.com.bookstore.model.entity.MainGenreEntity;
import br.com.bookstore.model.entity.SubGenreEntity;
import br.com.bookstore.model.entity.TypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BaseLabelMapper {

    BaseLabel toDto(TypeEntity typeEntity);
    BaseLabel toDto(MainGenreEntity mainGenreEntity);
    BaseLabel toDto(SubGenreEntity subGenreEntity);
}
