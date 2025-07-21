# SiwBooks - Backend con Thymeleaf

Progetto sviluppato con **Spring Boot**, **MySQL**, **Google OAuth2** e interfaccia web basata su **Thymeleaf**.

---

## Funzionalità principali

- Registrazione e login (anche via Google)
- Gestione libri, autori e recensioni
- Modifica profilo e cambio password
- Validazione backend con messaggi utente
- Interfaccia HTML con Thymeleaf

---

## Configurazione locale

1. Crea un database MySQL:
   ```sql
   CREATE DATABASE siwbooks;
   ```

2. Copia il file di esempio:
   ```bash
   cp src/main/resources/application-local-example.properties src/main/resources/application-local.properties
   ```

3. Modifica `application-local.properties` inserendo:
   - le tue credenziali MySQL
   - un client ID/secret OAuth2 Google (facoltativo)

4. La riga seguente inizializza il database con dati test:
   ```properties
   spring.sql.init.mode=always
   ```
 Dopo il primo avvio, **commentala** o impostala su `never` per evitare duplicazioni.

---

## Avvio

Compila ed esegui:
```bash
mvn clean install
mvn spring-boot:run
```

Apri il browser su:
```
http://localhost:8080
```

---

## Tecnologie

- Java 17
- Spring Boot 3
- Spring MVC + Security + OAuth2
- Spring Data JPA
- MySQL
- Thymeleaf
- Maven

---
