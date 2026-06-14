package app.mappers;
import app.dtos.creating.CreateRateDTO;
import app.dtos.responding.ResponseRateDTO;
import app.dtos.updating.UpdateRateDTO;
import app.models.Rate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RateMapper {
    Rate toEntity(CreateRateDTO dto);
    void updateRateFromDto(UpdateRateDTO dto, @MappingTarget Rate rate);
    @Mapping(source = "cafe.id", target = "cafeId")
    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "author.name", target = "authorName")
    @Mapping(source = "cafe.name", target = "cafeName")
    @Mapping(source = "cafe.address", target = "cafeAddress")
    ResponseRateDTO toDto(Rate menuItem);

}
