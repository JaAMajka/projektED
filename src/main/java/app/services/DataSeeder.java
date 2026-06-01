package app.services;

import app.models.*;
import app.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;
    private final RateRepository rateRepository;
    private final PasswordEncoder passwordEncoder;

    private final Faker faker = new Faker(Locale.forLanguageTag("pl"));
    private final Random random = new Random();

    private static class CafeMetrics {
        final List<Integer> beverages = new ArrayList<>();
        final List<Integer> services = new ArrayList<>();
        final List<Integer> atmospheres = new ArrayList<>();

        double getMean(List<Integer> list) {
            if (list.isEmpty()) return 3.8;
            return list.stream().mapToInt(Integer::intValue).average().orElse(3.8);
        }

        double getStdDev(List<Integer> list, double mean) {
            if (list.size() <= 1) return 0.6;
            double sum = 0;
            for (int val : list) {
                sum += Math.pow(val - mean, 2);
            }
            return Math.sqrt(sum / (list.size() - 1));
        }
    }

    private enum UserProfile {
        OPTIMIST(0.7, false),
        CRITIC(-1.0, false),
        REALIST(0.0, false),
        POLARIZER(0.0, true);

        final double bias;
        final boolean isPolarizer;

        UserProfile(double bias, boolean isPolarizer) {
            this.bias = bias;
            this.isPolarizer = isPolarizer;
        }
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() > 0) {
            log.info("Baza danych zawiera już użytkowników. Pomijanie inteligentnego seedowania.");
            return;
        }

        Map<String, CafeMetrics> cafeMetricsMap = new HashMap<>();
        ClassPathResource resource = new ClassPathResource("CoffeeData - Rates.csv");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length < 4) continue;

                String cafeName = tokens[0].trim();
                cafeMetricsMap.putIfAbsent(cafeName, new CafeMetrics());
                CafeMetrics metrics = cafeMetricsMap.get(cafeName);

                if (!tokens[1].equalsIgnoreCase("null") && !tokens[1].isEmpty()) metrics.beverages.add(Integer.parseInt(tokens[1]));
                if (!tokens[2].equalsIgnoreCase("null") && !tokens[2].isEmpty()) metrics.services.add(Integer.parseInt(tokens[2]));
                if (!tokens[3].equalsIgnoreCase("null") && !tokens[3].isEmpty()) metrics.atmospheres.add(Integer.parseInt(tokens[3]));
            }
        } catch (Exception e) {
            log.error("Nie udało się załadować pliku CSV: {}", e.getMessage());
            return;
        }

        List<Cafe> dbCafes = cafeRepository.findAll();
        if (dbCafes.isEmpty()) {
            log.warn("Brak kawiarni w bazie! Upewnij się, że migracje Flyway lub inserty tabeli cafes zostały wykonane.");
            return;
        }

        log.info("Generowanie inteligentnego seeda ocen na podstawie realnych danych z Google Maps...");

        List<User> users = new ArrayList<>();
        Map<Long, UserProfile> userProfileAssignments = new HashMap<>();
        String defaultPasswordHash = passwordEncoder.encode("SecurePassword123");
        UserProfile[] profiles = UserProfile.values();

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            
            String email = faker.internet().emailAddress();
            if (userRepository.existsByEmail(email)) {
                email = i + "_" + email;
            }
            user.setEmail(email);
            user.setPhoneNumber(faker.phoneNumber().cellPhone());
            user.setPasswordHash(defaultPasswordHash);
            user.setStudent(random.nextBoolean());
            user.setPrefersCardPayment(random.nextBoolean());

            User savedUser = userRepository.save(user);
            users.add(savedUser);

            UserProfile assignedProfile = profiles[random.nextInt(profiles.length)];
            userProfileAssignments.put(savedUser.getId(), assignedProfile);
        }

        List<Rate> ratesToSave = new ArrayList<>();
        long rateIdCounter = 1; // Ręczny licznik identyfikatora encji

        for (User user : users) {
            UserProfile profile = userProfileAssignments.get(user.getId());

            int cafesToRateCount = random.nextInt(6, 15);
            List<Cafe> shuffledCafes = new ArrayList<>(dbCafes);
            Collections.shuffle(shuffledCafes);

            for (int k = 0; k < Math.min(cafesToRateCount, shuffledCafes.size()); k++) {
                Cafe cafe = shuffledCafes.get(k);
                CafeMetrics metrics = cafeMetricsMap.get(cafe.getName());

                int beverageScore, serviceScore, atmosphereScore;

                if (metrics != null) {
                    double bMean = metrics.getMean(metrics.beverages);
                    double bStd  = metrics.getStdDev(metrics.beverages, bMean);

                    double sMean = metrics.getMean(metrics.services);
                    double sStd  = metrics.getStdDev(metrics.services, sMean);

                    double aMean = metrics.getMean(metrics.atmospheres);
                    double aStd  = metrics.getStdDev(metrics.atmospheres, aMean);

                    beverageScore = calculateFinalScore(bMean, bStd, profile);
                    serviceScore = calculateFinalScore(sMean, sStd, profile);
                    atmosphereScore = calculateFinalScore(aMean, aStd, profile);
                } else {
                    beverageScore = calculateFinalScore(4.0, 0.6, profile);
                    serviceScore = calculateFinalScore(3.9, 0.7, profile);
                    atmosphereScore = calculateFinalScore(4.1, 0.5, profile);
                }

                Rate rate = new Rate();
                rate.setId(rateIdCounter++);
                rate.setCafe(cafe);
                rate.setAuthor(user);
                rate.setBeverageScore(beverageScore);
                rate.setServiceScore(serviceScore);
                rate.setAtmosphereScore(atmosphereScore);

                ratesToSave.add(rate);
            }
        }

        rateRepository.saveAll(ratesToSave);
        log.info("Zakończono sukcesem! Wygenerowano i powiązano {} ocen w bazie danych.", ratesToSave.size());
    }

    private int calculateFinalScore(double mean, double stdDev, UserProfile profile) {
        if (profile.isPolarizer) {
            return mean >= 3.8 ? (random.nextDouble() > 0.15 ? 5 : 4) : (random.nextBoolean() ? 1 : 2);
        }

        double rawScore = mean + (random.nextGaussian() * stdDev) + profile.bias;
        int finalScore = (int) Math.round(rawScore);
        
        return Math.max(1, Math.min(5, finalScore));
    }
}