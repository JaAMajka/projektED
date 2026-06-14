# Krakow Cafes ☕

System rekomendacji kawiarni w Krakowie oparty na **collaborative filtering** (K-Means). Projekt na przedmiot *Eksploracja Danych*.

Backend w Spring Boot z asynchronicznym przeliczaniem statystyk przez RabbitMQ, modelem odczytu (read model) i silnikiem rekomendacji grupującym użytkowników w przestrzeni 3D ocen.

---

## Spis treści

- [Problem i rozwiązanie](#problem-i-rozwiązanie)
- [Stack technologiczny](#stack-technologiczny)
- [Architektura](#architektura)
- [Wymagania](#wymagania)
- [Uruchomienie](#uruchomienie)
- [Inicjalizacja danych (WAŻNE — kolejność)](#inicjalizacja-danych-ważne--kolejność)
- [Najważniejsze endpointy](#najważniejsze-endpointy)
- [Jak przetestować rekomendacje](#jak-przetestować-rekomendacje)
- [Struktura bazy](#struktura-bazy)
- [Ograniczenia](#ograniczenia)

---

## Problem i rozwiązanie

Collaborative filtering wymaga macierzy **użytkownik × przedmiot**. Mieliśmy tylko anonimowe oceny kawiarni z internetu (CSV) — bez informacji kto je wystawił.

Rozwiązanie:
1. Z CSV wyliczamy dla każdej kawiarni **średnią (μ)** i **odchylenie standardowe (σ)** w trzech wymiarach: napoje, obsługa, atmosfera — "matematyczny odcisk" kawiarni.
2. Generujemy syntetycznych użytkowników z **ukrytymi profilami** (bias preferencji) i realistycznym szumem Gaussa, odtwarzając brakującą macierz ocen.
3. K-Means grupuje użytkowników po guście; rekomendacje pochodzą z kawiarni wysoko ocenianych w klastrze, których użytkownik jeszcze nie zna.

---

## Stack technologiczny

| Warstwa | Technologia |
|---|---|
| Język / framework | Java, Spring Boot 3.5.13 |
| Baza danych | PostgreSQL 16 |
| Migracje | Flyway |
| Kolejka komunikatów | RabbitMQ |
| Autoryzacja | Spring Security + JWT (role USER / ADMIN) |
| Mapowanie DTO | MapStruct |
| Dokumentacja API | Swagger / OpenAPI (springdoc 2.8.x) |
| Konteneryzacja | Docker Compose |

---

## Architektura

### Read model + asynchroniczny przepływ

Statystyki kawiarni nie są liczone przy każdym zapytaniu. Trzyma je osobna tabela `cafe_read_model` (relacja 1:1 z `cafes`):

```
Nowa ocena ──> POST /rates ──> event do RabbitMQ ──> konsument przelicza μ/σ ──> cafe_read_model
```

Aplikacja czyta gotowe wartości, a kosztowne liczenie agregatów dzieje się w tle, poza ścieżką żądania użytkownika.

Kolejki:
- `MENU_ITEM_QUEUE` — przelicza średnie ceny per typ napoju + flagę `has_iced_items` + statystyki globalne.
- `RATE_QUEUE` — przelicza średnie ocen i odchylenia standardowe kawiarni.

### Silnik rekomendacji (K-Means)

- Przestrzeń 3D: `beverageScore`, `serviceScore`, `atmosphereScore`.
- `K = 4`, `MAX_ITERATIONS = 20`, stały seed (`Random(42)`) dla powtarzalności.
- Odległość euklidesowa; brakujące oceny uzupełniane średnią z read modelu (fallback).
- Wynik: kawiarnie wysoko oceniane przez resztę klastra, odrzucając te już ocenione przez użytkownika.
- Liczone **na żywo** przy każdym żądaniu (świadomy kompromis — patrz [Ograniczenia](#ograniczenia)).

### Generator danych (DataSeeder)

- 100 syntetycznych użytkowników, każdy z losowo przypisanym profilem.
- 5 profili z biasem: `CONNOISSEUR`, `FREELANCER`, `STUDENT`, `PERFECTIONIST`, `RANDOM_GUY`.
- Wzór oceny: `clamp( μ_kawiarni + bias_profilu + σ_kawiarni · gauss() , 1, 5 )`.
- Każdy użytkownik ocenia ~60% kawiarni (reszta zostaje jako kandydaci do rekomendacji).
- Przypisany profil zapisywany w kolumnie `users.profile` (NULL dla realnych użytkowników).

---

## Wymagania

- Docker + Docker Compose
- (opcjonalnie) klient REST: Postman albo wbudowany Swagger UI

---

## Uruchomienie

```bash
git clone <ADRES_REPO>
cd <KATALOG_PROJEKTU>
```

Utwórz plik `.env` w katalogu głównym (obok `docker-compose.yml`) z następującymi zmiennymi:

```env
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=<długi_losowy_sekret_min_256_bitów>
JWT_EXPIRATION=86400000
RABBITMQ_USER=guest
RABBITMQ_PASSWORD=guest
```

> `JWT_SECRET` musi być odpowiednio długim, losowym ciągiem (HS256 wymaga min. 256 bitów). `JWT_EXPIRATION` jest w milisekundach (tu: 24h).

Następnie:

```bash
docker compose up --build
```

Po starcie:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- RabbitMQ Management: `http://localhost:15672` (login/hasło jak `RABBITMQ_USER`/`RABBITMQ_PASSWORD`)
- PostgreSQL: `localhost:5432` (baza `krakow_cafes`)

Flyway uruchamia migracje automatycznie przy starcie — w tym migrację, która zasiewa `cafe_read_model` wartościami μ/σ z CSV.

Reset całości (czyści wolumeny i dane):
```bash
docker compose down -v
```

---

## Inicjalizacja danych (WAŻNE — kolejność)

Świeża baza wymaga **trzech kroków w tej kolejności**. Pominięcie któregokolwiek skutkuje pustymi rekomendacjami albo błędami `NullPointerException` na statystykach.

```
1. docker compose up           → Flyway: migracje + zasiew μ/σ do cafe_read_model (z CSV)
2. POST /admin/backfill        → dopełnia kolumny cenowe w read modelu (has_iced + ceny)
3. POST /admin/seed            → 100 użytkowników + ~3300 ocen → RabbitMQ przelicza read model
```

Dopiero po tych krokach `GET /api/recommendations/user/{id}` zwraca sensowne wyniki.

> Endpointy `/admin/**` wymagają tokenu **ADMIN** (patrz niżej).

---

## Najważniejsze endpointy

Pełna, interaktywna lista jest w Swaggerze (`/swagger-ui/index.html`). Poniżej najważniejsze do testowania.

### Autoryzacja (`auth-controller`)
```http
POST /auth/register           # rejestracja użytkownika
POST /auth/login              # logowanie -> zwraca JWT
```
Domyślne konto administratora (tworzone migracją przy starcie):
```json
{ "email": "admin@test.com", "password": "Admin@1234" }
```
W kolejnych żądaniach: nagłówek `Authorization: Bearer <token>`.

### Administracyjne (rola ADMIN)
```http
POST /admin/backfill          # dopełnia ceny w read modelu
POST /admin/seed              # generuje użytkowników i oceny
GET  /admin/export/rates      # eksport ocen z profilami (wejście dla analizy w R)
```

### Kawiarnie i wyszukiwanie (`cafe-controller`)
```http
GET    /cafes                              # filtrowanie po atrybutach (query params)
GET    /cafes?hasWifi=true&allowsPets=true # przykład filtra boolowskiego
GET    /cafes/all                          # wszystkie kawiarnie, bez filtra
GET    /cafes/{id}
POST   /cafes
PUT    /cafes/{id}
DELETE /cafes/{id}
```

Dostępne parametry filtrowania (`GET /cafes`, wszystkie typu boolean, opcjonalne, łączone warunkiem AND):
`hasWifi`, `allowsPets`, `sellsFood`, `allowsStudentsDiscounts`, `isLgbtqFriendly`, `hasToilet`, `hasTerrace`, `allowsIntake`, `allowsTakeaway`, `supportsCardPayments`.

### Zasoby zagnieżdżone pod kawiarnią
```http
GET|POST|PUT|DELETE /cafes/{cafeId}/items[/{id}]      # menu (menu-item-controller)
GET|POST|PUT|DELETE /cafes/{cafeId}/rates[/{id}]      # oceny (rate-controller)
GET|POST|PUT|DELETE /cafes/{cafeId}/schedules[/{id}]  # godziny otwarcia (schedule-controller)
```

### Rekomendacje (`recommendation-controller`)
```http
GET /api/recommendations/user/{userId}?limit=5   # personalizowane (K-Means)
GET /api/recommendations/{id}
POST /api/recommendations
DELETE /api/recommendations/{id}
```
`GET /api/recommendations/user/{userId}` zwraca listę rekomendowanych kawiarni dla danego użytkownika (collaborative filtering, K-Means).

---

## Jak przetestować rekomendacje

Po wykonaniu trzech kroków inicjalizacji:

1. **Zaloguj się** jako admin → skopiuj JWT.
2. **Wyeksportuj dane:** `GET /admin/export/rates` — zobaczysz oceny z przypisanymi profilami.
3. **Sprawdź rekomendacje** dla wybranego użytkownika:
   ```http
   GET /api/recommendations/user/1?limit=5
   ```
4. **Weryfikacja collaborative filtering:** wybierz dwóch użytkowników z tego samego klastra K-Means i porównaj ich rekomendacje — powinni dostawać podobne/te same kawiarnie, których jeszcze nie ocenili.

Analizę klastrów (wizualizacja w R) można odtworzyć na danych z `GET /admin/export/rates`.

---

## Struktura bazy

Główne tabele:

- `cafes` — kawiarnie i ich atrybuty (wifi, zwierzęta, taras, płatność kartą itd.)
- `cafe_read_model` — policzone statystyki (μ/σ ocen, średnie ceny, flagi) — relacja 1:1 z `cafes`
- `menu_items` — pozycje menu (typ napoju, rozmiar, cena, dodatki)
- `rates` — oceny (napoje / obsługa / atmosfera), unikalne per (user, cafe)
- `users` — użytkownicy, rola, kolumna `profile` (dla syntetycznych)
- `recommendations` — zapisane rekomendacje
- `schedules` — godziny otwarcia per dzień tygodnia
- `global_stats` — globalne średnie cen per typ napoju (singleton)

Typy ENUM: `beverage_type`, `available_size`, `weekday`.

---

## Ograniczenia

Świadome kompromisy (zakres projektu studenckiego, nie produkcja):

- **Rekomendacje liczone na żywo** przy każdym żądaniu. W produkcji: zadanie cykliczne (`@Scheduled`) lub wynik cache'owany.
- **Skalowanie RabbitMQ** — masowe seedowanie generuje tysiące wiadomości; przy dużym wolumenie warto byłoby użyć batch insertu + jednorazowego backfillu zamiast eventu per ocena.
- **Dane syntetyczne** nie reprezentują realnych klientów. Służą weryfikacji, że pipeline poprawnie wykrywa strukturę wprowadzoną przez profile (recovery test), a nie badaniu rynku.

---

*Projekt edukacyjny — Eksploracja Danych, 2026.*
