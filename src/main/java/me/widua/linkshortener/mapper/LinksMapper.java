package me.widua.linkshortener.mapper;

import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.LinkModel;
import org.mapstruct.*;
import org.springframework.stereotype.Component;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING
)
@Component
public interface LinksMapper {
    LinkDTO linkModelToLinkDTO(LinkModel linkModel);


    @Mapping(target = "lastTimeUsed", expression = "java(java.time.LocalDateTime.now())")
    LinkModel linkDTOToLinkModel(LinkDTO linkDTO);

}
