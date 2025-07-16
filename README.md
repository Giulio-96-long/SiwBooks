# SiwBooks

**SiwBooks** è un'applicazione web completa per la gestione di libri, autori e recensioni. Sviluppata con **Spring Boot** e **Thymeleaf**, supporta autenticazione tramite credenziali o **Google OAuth2** e offre funzionalità differenziate per utenti e amministratori.

## Funzionalità principali

- **Login/registrazione** con credenziali o Google
- Gestione utenti con ruoli `USER` e `ADMIN`
- CRUD su:
  - Libri 📖
  - Autori ✍️
  - Recensioni 📝
- Visualizzazione delle recensioni per ciascun libro
- Interfaccia responsive con Bootstrap 5
- Validazione form lato server e lato client

## Tecnologie utilizzate

 Componente       Tecnologia                                       

Backend           Spring Boot 3.4.5, Spring Security, JPA, MySQL   
Frontend          Thymeleaf, Bootstrap 5, WebJars, Icons           
Autenticazione    Form-based + Google OAuth2                       
Validazione       Bean Validation (`javax.validation`)             
Template Engine   Thymeleaf + Thymeleaf Layout Dialect             

## Requisiti

- Java 17
- Maven
- MySQL (locale)
- Porta predefinita: `8081`

##  Configurazione

Modifica (se necessario) il file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/siwBooks
spring.datasource.username=root
spring.datasource.password=Giulio.96

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.mvc.hiddenmethod.filter.enabled=true

# OAuth2 Google
spring.security.oauth2.client.registration.google.client-id=...
spring.security.oauth2.client.registration.google.client-secret=...
spring.security.oauth2.client.registration.google.redirect-uri=http://localhost:8081/login/oauth2/code/google
```

## Dati iniziali (script SQL)

Alla prima esecuzione l'app popola automaticamente:

- **6 utenti** (`ADMIN`, `USER`)
- **6 autori** (Rowling, Orwell, Austen…)
- **6 libri reali**
- **24 recensioni esempio**

> Se hai disabilitato l'inizializzazione automatica, puoi eseguire manualmente lo script SQL di popolamento (vedi cartella `resources` o il dump fornito).

## Avvio dell'app

1. Crea il database:

   ```sql
   CREATE DATABASE siwBooks;
   ```

2. Clona il repository:

   ```bash
   git clone https://github.com/<tuo-utente>/SiwBooks.git
   cd SiwBooks
   ```

3. Avvia il backend:

   ```bash
   mvn spring-boot:run
   ```

4. Visita l’app:  
   [http://localhost:8081](http://localhost:8081)

## Utenti di test

Email               Password  Ruolo  

admin.email.it      123456    ADMIN  
user.email.it       123456    USER   
alice@example.com   123456    USER   

## Struttura del progetto

```
src
├── main
│   ├── java/com/example/siwbooks
│   │   ├── controller
│   │   ├── model
│   │   ├── repository
│   │   ├── service
│   │   └── security
│   └── resources
│       ├── templates/ (HTML Thymeleaf)
│       ├── static/    (Bootstrap, icone, CSS)
│       └── application.properties
```

## Sicurezza

- Accesso alle pagine admin limitato tramite `SecurityConfig`
- L'autenticazione può essere fatta via:
  - Form tradizionale
  - Google OAuth2

## Licenza

Questo progetto è distribuito a scopo didattico e personale. Nessuna licenza commerciale attiva.