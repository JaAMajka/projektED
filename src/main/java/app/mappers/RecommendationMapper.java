package app.mappers;


import app.dtos.creating.CreateRecommendationDTO;
import app.dtos.responding.ResponseRecommendationDTO;
import app.dtos.updating.UpdateRecommendationDTO;
import app.models.MenuItem;
import app.models.Recommendation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {
    @Mapping (target = "cafe", ignore = true)
    @Mapping( target = "user", ignore = true)
    Recommendation toEntity(CreateRecommendationDTO dto);
    void updateRecommendationFromDto(UpdateRecommendationDTO dto, @MappingTarget  Recommendation recommendation);
    ResponseRecommendationDTO toDto(Recommendation recommendation);

}
