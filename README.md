# Restorani broneerimissüsteem

See on täislahenduslik (Full-stack) veebirakendus restorani laudade broneerimiseks. Süsteem võimaldab klientidel leida sobiva laua vastavalt eelistustele, näha laudade asetust interaktiivsel saaliplaanil ning sooritada broneering reaalajas.

## Rakenduse käivitamine

### Eeldused

* **Java 21**
* **Node.js** (24.1.0) ja **npm**
* **PostgreSQL**

### 1. Backend (Spring Boot)

1. Liigu kausta `src/main/java/ee/cgi/praktika/restaurant_reservation`.
2. Seadista andmebaasi ühendus failis `src/main/resources/application.properties`.
3. Käivita rakendus:
```bash
./mvnw spring-boot:run

```


*Käivitamisel täidab `DataInitializer` andmebaasi automaatselt testandmetega.*

### 2. Frontend (Vue 3)

1. Liigu kausta `frontend`.
2. Paigalda vajalikud sõltuvused:
```bash
npm install

```


3. Käivita arendusserver:
```bash
npm run dev

```



---

## Rakenduse käivitamine (Docker)

Kõige lihtsam ja soovitatavam viis rakenduse käivitamiseks on kasutada Dockerit. See käivitab automaatselt nii backendi, frontendi kui ka andmebaasi ühes võrgus.

1. Veendu, et **Docker Desktop** on arvutis käivitatud.
2. Ava terminal projekti juurkaustas.
3. Sisesta käsk:
   ```bash
   docker compose up --build

## Teostatud lahenduse kirjeldus

### Tehniline pinu (Tech Stack)

* **Backend**: Java 21, Spring Boot, Spring Data JPA, **PostgreSQL**.
* **Frontend**: Vue 3 (Composition API), Vite, `libphonenumber-js` teek.
* **Andmebaas**: PostgreSQL.
* Infrastruktuur: Docker, Docker Compose. 
* Testimine: JUnit 5

### Peamised funktsionaalsused

1. **Nutikas laua otsing (Scoring)**: Backend hindab laudu vastavalt eelistustele (nt täpne kohtade arv, aknapealne asukoht jne).
2. **Interaktiivne saaliplaan**: Laudade asetus kuvatakse dünaamiliselt koordinaatide abil ning nende olek (vaba/hõivatud) muutub reaalajas.
3. **Valideerimine**: Telefoninumbri kontroll `libphonenumber-js` abil ja sisestuse piiramine ainult numbritele.

---

## Arendusprotsessi märkmed

### Tööks kulunud aeg

* **Kokku**: 16h
* Planeerimine ja andmebaasi disain: 3h
* Backend loogika ja Scoring süsteem: 4h
* Frontend ja saaliplaani visualiseerimine: 5h
* Docker, testimine ja dokumentatsioon: 4h

### Märkus Git-i kasutamise kohta

* **Oluline selgitus**: Kuna ma ei märganud alguses punkti sagedaste *commit*-ide tegemise kohta, töötasin suurema osa ajast lokaalselt ilma muudatusi vahepeal salvestamata. Sellest tulenevalt on suurem osa funktsionaalsusest lisatud Giti ühe või paari suurema *commit*-ina projekti lõpufaasis.

### Keerukused ja lahendused

* **Aegade kattuvuse kontroll**: Tagamine, et ühte lauda ei saaks broneerida kattuvale ajale. Lahendasin selle SQL-päringuga, mis võrdleb ajavahemikke.
* **Telefoninumbri komponent**: Oli väljakutse luua komponent, mis puhastaks sisendi muudest sümbolitest peale numbrite, kuid säilitaks korrektse rahvusvahelise formaadi.
* Docker macOS-il: Esines takistus, kus terminal ei tuvastanud docker käsku (command not found). Lahendasin selle, lisades Dockeri bin-kausta tee käsitsi süsteemi PATH muutujasse (export PATH=$PATH:/Applications/Docker.app/Contents/Resources/bin).

### Tehisintellekti (AI) kasutus

Projekti arendamisel kasutasin peamiselt **tehisintellekti (AI)** abi (Gemini):

* AI-d kasutati koodis esinenud **vigade diagnoosimiseks ja parandamiseks**.
* Tehisintellekt aitas **optimeerida funktsionaalsust**, näiteks täiustada laudade Scoring-algoritmi ja luua keerukamaid Vue komponente.

---

## Tehtud eeldused

1. **Andmete säilitamine**: Rakendus eeldab töötavat PostgreSQL andmebaasi.
2. **Broneeringu reeglid**: Süsteem eeldab, et broneeringut ei saa teha minevikku ning külaliste arv ei tohi ületada laua mahutavust.
