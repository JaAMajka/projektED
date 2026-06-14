package app.mappers;


import app.dtos.creating.CreateRecommendationDTO;
import app.dtos.responding.ResponseRecommendationDTO;
import app.dtos.updating.UpdateRecommendationDTO;

import app.models.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    Recommendation toEntity(CreateRecommendationDTO dto);
    void updateRecommendationFromDto(UpdateRecommendationDTO dto, @MappingTarget  Recommendation recommendation);
    @Mapping(source = "cafe.name", target = "cafeName")
    @Mapping(source = "cafe.address", target = "cafeAddress")
    @Mapping(source = "user.name", target = "userName")
    ResponseRecommendationDTO toDto(Recommendation recommendation);

}
