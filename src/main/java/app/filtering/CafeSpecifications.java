package app.filtering;
import app.models.Cafe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CafeSpecifications {


    public static Specification<Cafe> booleanFilter(String fieldName, Boolean value) {
        return (root, query, cb) -> {
            if (value == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get(fieldName), value);
        };
    }

}
