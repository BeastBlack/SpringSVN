# SpringSVN

SpringSVN to webowy klient SVN.

# Wymagania

SpringSVN wymaga do działania bazy danych MariaDB w wersji co najmniej 10.x. Aplikacja została wykonana w Java 8.

# IDE

SpringSVN został stworzony w IntelliJ IDEA 2019 z użyciem Maven 3.

# Deploy

Deploy aplikacji z użyciem Maven 3:

mvn clean package

# Instrukcja

1. Skonfiguruj dostęp do bazy danych w pliku application.propereties (nazwa bazy, nazwa uzytkownika i hasło).
2. Uruchom aplikację, przejdź do localhost:8080 (domyślny port). Użyj parametru uruchomieniowego java, jeżeli twoje repozytorium wymaga uwierzytelnienia z użyciem nazwy uzytkownika i hasła:

-Dsvnkit.http.methods=Basic

3. Wprowadź adres URI (http, https, file), nazwę i hasło uzytkownika SVN (jeśli trzeba), zostaw zaznaczenie przy wyborze dostępu anonimowego.
4. Jeśli wprowadzone dane są OK, otworzy się widok Repozytorium.
5. Zarejestruj konto administratora.
6. Wyłącz w opcjach dostęp anonimowy.
7. Wykonaj restart aplikacji.
8. Zaloguj się utworzonym kontem administratora.

# Rejestracja użytkowników

- Konto użytkownika (USER) może być zarejestorwane przez administratora lub przez każdego pod adresem localhost:8080/register.
- Kolejne konto administratora (ADMIN) może być zarejestorwane z poziomu zalogowonego użytkownika będącego administratorem.
- Konto użytkownika muszą być potwierdzone przez administratora w panelu Zarządzania użytkowników.
- Konto administratora nie musi zostać potwierdzone.
- Administrator może nadać lub odebrać uprawnienia administratora dowolnemu użytkownikowi.

# Integracja z systemem Bug-Trackingowym

- Włącz integrację w ustawieniach aplikacji.
- Podaj wzorzec (regex) identyfikatora incydentu, np.: dla BUG-123 będzie to: (BUG-)\d{1,}.
- Podaj treść HTML, na jaki zostanie podmieniony znaleziony identyfikator incydentu: <a href="http://bugtracksrv/link/@pattern">@pattern</a>.
