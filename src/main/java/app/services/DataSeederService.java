package app.services;


import app.Role;
import app.dtos.creating.CreateRateDTO;
import app.dtos.creating.CreateUserDTO;
import app.models.CafeReadModel;
import app.models.User;
import app.profiles.Profile;
import app.repositories.CafeReadModelRepository;
import app.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
@RequiredArgsConstructor
@Service
public class DataSeederService {
    Random random = new Random();
    private final CafeReadModelRepository cafeReadModelRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final RateService rateService;

    private final Faker faker = new Faker();

    public void seedUsers(){
        if(userRepository.countAllByRole(Role.USER) > 1000){
            userRepository.deleteAllInBatch(userRepository.findTop100ByRoleOrderByCreatedAtAsc(Role.USER));
        }
        List<CafeReadModel> cafeReadModels = cafeReadModelRepository.findAll();
        List<Profile> profiles = Arrays.stream(Profile.values()).toList();
        for (int i = 0; i < 100; i++){
            User user = seedSingleUser(i);
            Profile userProfile = profiles.get(random.nextInt(0, 5));
            user.setProfile(userProfile);
            userRepository.save(user);
            for (CafeReadModel cafeReadModel : cafeReadModels) {
                if(random.nextDouble(0, 1) < 0.6){
                    Integer atmosphereScore = generateScore(cafeReadModel.getAvgAtmosphereScore().doubleValue(), cafeReadModel.getStdDevAtmosphere().doubleValue(), userProfile.getAtmosphereBias());
                    Integer beverageScore = generateScore(cafeReadModel.getAvgBeverageScore().doubleValue(), cafeReadModel.getStdDevBeverage().doubleValue(), userProfile.getBeverageBias());
                    Integer serviceScore = generateScore(cafeReadModel.getAvgServiceScore().doubleValue(), cafeReadModel.getStdDevService().doubleValue(), userProfile.getServiceBias());
                    CreateRateDTO dto = new CreateRateDTO(
                            atmosphereScore,
                            beverageScore,
                            serviceScore);

                    rateService.createRate(dto, cafeReadModel.getCafe().getId(), user.getId());
                }

            }


        }

    }
    private User seedSingleUser(int i){
        return userService.createUser(
                new CreateUserDTO(
                faker.name().firstName(),
                faker.funnyName().name() + "@mail.com" + String.valueOf(i),
                faker.phoneNumber().phoneNumber(),
                "Test1234@",
                        faker.bool().bool(),
                        faker.bool().bool()

                )
        );

    }



    private Integer clamp(double value){
        if(value > 5){
            return 5;
        }
        if(value < 1){
            return 1;
        }
        return (int) value;
    }

    private Integer generateScore(double mean, double stdDev, double bias){
        double score = mean + (stdDev * random.nextGaussian()) + bias;
        return clamp(score);
    }
}
