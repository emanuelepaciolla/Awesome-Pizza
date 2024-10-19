# Pizzeria Order Management System

## Descrizione
Questo progetto è un sistema di gestione degli ordini per una pizzeria. Utilizza Java, Spring Boot, SQL e Maven per gestire e testare gli ordini dei clienti.

## Tecnologie Utilizzate
- **Java**: Linguaggio di programmazione principale.
- **Spring Boot**: Framework per creare applicazioni stand-alone basate su Spring.
- **SQL**: Utilizzato per la gestione del database.
- **Maven**: Strumento di gestione delle dipendenze e build.

## Struttura del Progetto
- `src/main/java`: Contiene il codice sorgente dell'applicazione.
- `src/test/java`: Contiene i test unitari e di integrazione.
- `src/main/resources`: Contiene i file di configurazione e le risorse statiche.
- `src/test/resources`: Contiene i file di configurazione per i test.
- `pom.xml`: File di configurazione Maven.
- `README.md`: File di descrizione del progetto.
- 
## Avvio dell'Applicazione
Per avviare l'applicazione, eseguire il seguente comando Maven nella directory principale del progetto:
```sh
mvn spring-boot:run
```

## Sviluppi Futuri
- **Aggiunta della possibilità di avere più pizze in un ordine**: Implementare la funzionalità che permette ai clienti di aggiungere più pizze in un singolo ordine, gestendo le quantità e le varianti di ciascuna pizza.
- **Inserire un'interfaccia grafica**: Sviluppare un'interfaccia grafica user-friendly per migliorare l'esperienza utente, utilizzando tecnologie come JavaFX o un framework web come React per l'interazione con il sistema di gestione degli ordini.


## Documentazione API
La documentazione delle API è disponibile tramite Swagger. Puoi accedere alla documentazione completa delle API all'indirizzo:
[Swagger UI](http://localhost:8080/swagger-ui/index.html#)