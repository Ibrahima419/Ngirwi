# Guide Développeur - Ngirwi Medical

## Table des matières

1. [Vue d'ensemble](#1-vue-densemble)
2. [Architecture technique](#2-architecture-technique)
3. [Structure du projet](#3-structure-du-projet)
4. [Modèle de données](#4-modèle-de-données)
5. [Règles métier](#5-règles-métier)
6. [Gestion des rôles et sécurité](#6-gestion-des-rôles-et-sécurité)
7. [Composants clés du Frontend](#7-composants-clés-du-frontend)
8. [Services Backend](#8-services-backend)
9. [Génération de documents PDF](#9-génération-de-documents-pdf)
10. [Configuration et déploiement](#10-configuration-et-déploiement)
11. [Conventions de code](#11-conventions-de-code)
12. [Dépannage courant](#12-dépannage-courant)

---

## 1. Vue d'ensemble

### 1.1 Description du projet

Ngirwi Medical est une application de gestion médicale destinée aux établissements de santé au Sénégal. Elle permet la gestion complète du parcours patient : de l'enregistrement jusqu'à la facturation, en passant par les consultations, ordonnances et hospitalisations.

### 1.2 Fonctionnalités principales

| Module | Description |
|--------|-------------|
| Patients | Gestion des informations personnelles et dossiers médicaux |
| Consultations | Suivi des visites médicales et diagnostics |
| Ordonnances | Prescription de médicaments avec génération PDF |
| Hospitalisations | Gestion des séjours et fiches de surveillance |
| Facturation | Création de factures avec support Assurance/IPM |
| Certificats | Génération de certificats médicaux (repos, etc.) |

### 1.3 Contexte technique

L'application est construite sur le framework JHipster, combinant :
- Un backend Spring Boot (Java 17)
- Un frontend React avec TypeScript
- Une base de données PostgreSQL

---

## 2. Architecture technique

### 2.1 Architecture globale

```
┌─────────────────────────────────────────────────────────────────┐
│                        NAVIGATEUR                                │
│                    (React + TypeScript)                          │
└─────────────────────────────────────────────────────────────────┘
                              │
                              │ HTTP/REST (JSON)
                              │ JWT Authentication
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                     NGINX (Reverse Proxy)                        │
│              - Sert les fichiers statiques frontend              │
│              - Proxy vers le backend /api/*                      │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    SPRING BOOT BACKEND                           │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐    │
│  │ Controller│  │  Service  │  │ Repository│  │  Security │    │
│  │   (REST)  │──│  (Logic)  │──│   (JPA)   │  │   (JWT)   │    │
│  └───────────┘  └───────────┘  └───────────┘  └───────────┘    │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      POSTGRESQL                                  │
│                   Base de données                                │
└─────────────────────────────────────────────────────────────────┘
```

### 2.2 Stack technologique

#### Backend
- Java 17
- Spring Boot 2.7.x
- Spring Security avec JWT
- Spring Data JPA / Hibernate
- Liquibase (migrations de schéma)
- MapStruct (mapping DTO)
- OpenPDF (génération PDF côté serveur)

#### Frontend
- React 18
- TypeScript
- Redux Toolkit (gestion d'état)
- React Router v6
- Reactstrap (composants Bootstrap)
- @react-pdf/renderer (génération PDF côté client)
- react-jhipster (utilitaires JHipster)

#### Infrastructure
- PostgreSQL 14+
- Nginx
- Systemd (gestion des services)

---

## 3. Structure du projet

### 3.1 Backend (ngirwi-medical-backend-master)

```
src/main/java/sn/ngirwi/medical/
├── config/                 # Configuration Spring (Security, Database, etc.)
├── domain/                 # Entités JPA
│   └── enumeration/        # Enums (HospitalisationStatus, etc.)
├── repository/             # Interfaces Spring Data JPA
├── security/               # Configuration sécurité et JWT
│   ├── AuthoritiesConstants.java   # Définition des rôles
│   ├── DomainUserDetailsService.java
│   └── jwt/                # Gestion des tokens JWT
├── service/                # Logique métier
│   ├── dto/                # Data Transfer Objects
│   └── mapper/             # Mappers MapStruct
└── web/rest/               # Contrôleurs REST

src/main/resources/
├── config/
│   ├── application.yml             # Configuration principale
│   ├── application-dev.yml         # Configuration développement
│   ├── application-prod.yml        # Configuration production
│   └── liquibase/
│       ├── master.xml              # Fichier maître Liquibase
│       ├── changelog/              # Fichiers de migration
│       └── data/                   # Données initiales (CSV)
```

### 3.2 Frontend (ngirwi-medical-react-master)

```
src/main/webapp/app/
├── config/
│   ├── constants.ts        # Constantes (AUTHORITIES, etc.)
│   └── store.ts            # Configuration Redux
├── entities/               # Modules par entité
│   ├── bill/               # Facturation
│   ├── consultation/       # Consultations
│   ├── dossier-medical/    # Dossiers médicaux
│   ├── hospitalisation/    # Hospitalisations
│   ├── patient/            # Patients
│   ├── prescription/       # Ordonnances
│   └── surveillance-sheet/ # Fiches de surveillance
├── modules/
│   ├── account/            # Gestion du compte utilisateur
│   ├── administration/     # Administration
│   └── login/              # Authentification
├── shared/
│   ├── auth/               # Utilitaires d'authentification
│   ├── layout/             # Composants de mise en page
│   │   ├── header/
│   │   └── sidebar/
│   ├── model/              # Interfaces TypeScript
│   └── util/               # Utilitaires (dates, etc.)
└── routes.tsx              # Configuration des routes
```

---

## 4. Modèle de données

### 4.1 Entités principales

```
Patient
├── id: Long
├── firstName: String
├── lastName: String
├── birthday: LocalDate
├── birthplace: String
├── gender: Gender (MALE/FEMALE)
├── address: String
├── phone: String
├── cni: String (Carte Nationale d'Identité)
├── hospital: Hospital (ManyToOne)
└── Timestamps (dateCreated, dateUpdated, author)

DossierMedical (un par patient)
├── id: Long
├── motifConsultation: String
├── histoireMaladie: String
├── terrain: String
├── antecedantsPersonnels: String
├── antecedantsChirurgicaux: String
├── antecedantsFamiliaux: String
├── gynecoObstretrique: String
├── syndromique: String
├── dad, mom, siblings, descendants: String
└── patient: Patient (OneToOne)

Consultation
├── id: Long
├── dateTime: Instant
├── temperature, weight, tension: String
├── hypothesis: String
├── exams, treatment: String
├── patient: Patient (ManyToOne)
└── author: String

Prescription (Ordonnance)
├── id: Long
├── creationDate: Instant
├── consultation: Consultation (ManyToOne)
└── medecines: List<Medecine> (OneToMany)

Hospitalisation
├── id: Long
├── entryDate, releaseDate: Instant
├── status: HospitalisationStatus (ACTIVE/RELEASED/TRANSFERRED)
├── doctorName, roomNumber, bedNumber: String
├── reason, diagnosis, treatment: String
├── patient: Patient (ManyToOne)
└── surveillanceSheets: List<SurveillanceSheet>

Bill (Facture)
├── id: Long
├── date: Instant
├── total: Long
├── insurance, ipm: String (mutuellement exclusifs)
├── desc: String
├── patient: Patient (ManyToOne)
└── billElements: List<BillElement> (OneToMany)

BillElement
├── id: Long
├── name: String (intervention)
├── price: Long (prix unitaire)
├── percentage: Double (remise 0-100%)
├── quantity: Integer
└── bill: Bill (ManyToOne)
```

### 4.2 Relations clés

```
Hospital (1) ──────< (N) Patient
Patient  (1) ──────< (1) DossierMedical
Patient  (1) ──────< (N) Consultation
Patient  (1) ──────< (N) Hospitalisation
Patient  (1) ──────< (N) Bill
Consultation (1) ──< (N) Prescription
Hospitalisation (1) < (N) SurveillanceSheet
Bill (1) ─────────< (N) BillElement
```

---

## 5. Règles métier

### 5.1 Facturation - Exclusivité Assurance/IPM

Un patient ne peut avoir qu'un seul type de couverture par facture :
- Soit une Assurance (compagnies d'assurance privées)
- Soit une IPM (Institution de Prévoyance Maladie)

Cette règle est implémentée côté frontend avec une gestion d'état local pour éviter les conflits de synchronisation React.

```typescript
// Quand l'utilisateur sélectionne une assurance, l'IPM est vidé
const handleInsuranceChange = (e) => {
  setBlockAssurance(e.target.value);
  if (e.target.value !== '') {
    setBlockIPM(''); // Exclusivité
  }
};
```

### 5.2 Calcul du total facture

```
Total = Σ (prix_unitaire × quantité × (1 - remise/100))
```

Le total est arrondi à l'entier (pas de centimes en FCFA).

### 5.3 Affichage du total selon le mode

| Mode | Affichage |
|------|-----------|
| Création (NEW) | Calcul en temps réel |
| Édition (EDIT) | "Calculé à la sauvegarde" |
| Consultation (VOIR) | Total sauvegardé |

### 5.4 Hospitalisations

- Un patient ne peut avoir qu'une seule hospitalisation ACTIVE à la fois
- Le statut peut être : ACTIVE, RELEASED (sorti), TRANSFERRED (transféré)
- Les fiches de surveillance sont liées à l'hospitalisation

### 5.5 Dossier médical

- Un patient a un seul dossier médical (relation 1:1)
- Le dossier est créé lors de la première consultation
- Contient l'historique médical complet du patient

---

## 6. Gestion des rôles et sécurité

### 6.1 Rôles définis

| Rôle | Code | Accès |
|------|------|-------|
| Administrateur | ROLE_ADMIN | Accès complet |
| Médecin | ROLE_DOCTOR | Consultations, ordonnances, dossiers médicaux, certificats |
| Utilisateur | ROLE_USER | Patients, factures, hospitalisations |

### 6.2 Configuration Spring Security

Les rôles sont définis dans `AuthoritiesConstants.java` :

```java
public static final String ADMIN = "ROLE_ADMIN";
public static final String USER = "ROLE_USER";
public static final String DOCTOR = "ROLE_DOCTOR";
```

Les contrôleurs sont protégés par `@PreAuthorize` :

```java
@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.DOCTOR + "\")")
public class DossierMedicalResource { ... }
```

### 6.3 Vérification côté Frontend

```typescript
import { hasAnyAuthority } from 'app/shared/auth/private-route';
import { AUTHORITIES } from 'app/config/constants';

const isDoctor = hasAnyAuthority(account.authorities, [AUTHORITIES.DOCTOR]);
```

### 6.4 Token JWT

- Durée de vie : configurable dans `application.yml`
- Les rôles sont embarqués dans le token
- Après modification des rôles en base, l'utilisateur doit se reconnecter

---

## 7. Composants clés du Frontend

### 7.1 Structure d'un module entité

Chaque entité suit le pattern :

```
entities/[entity]/
├── [entity].tsx              # Liste principale
├── [entity]-detail.tsx       # Vue détaillée
├── [entity]-update.tsx       # Formulaire création/édition
├── [entity]-delete-dialog.tsx# Modal de suppression
├── [entity].reducer.ts       # Slice Redux
└── index.tsx                 # Routes du module
```

### 7.2 Modes d'affichage (pattern commun)

Les formulaires supportent trois modes déterminés par les paramètres de route :

```typescript
const { id } = useParams<'id'>();
const { idEdit } = useParams<'idEdit'>();
const isNew = id === undefined;

// Conditions d'affichage
if (isNew) { /* Mode création */ }
else if (idEdit === 'voir') { /* Mode consultation (lecture seule) */ }
else { /* Mode édition */ }
```

### 7.3 Gestion d'état avec Redux Toolkit

```typescript
// Slice type (entity.reducer.ts)
export const getEntity = createAsyncThunk(
  'entity/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IEntity>(requestUrl);
  }
);

// Utilisation dans le composant
const dispatch = useAppDispatch();
const entity = useAppSelector(state => state.entity.entity);
const loading = useAppSelector(state => state.entity.loading);
const updateSuccess = useAppSelector(state => state.entity.updateSuccess);

useEffect(() => {
  dispatch(getEntity(id));
}, [id]);

useEffect(() => {
  if (updateSuccess) {
    navigate('/entity');
  }
}, [updateSuccess]);
```

### 7.4 ValidatedForm (JHipster)

Formulaire avec validation intégrée :

```tsx
<ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
  <ValidatedField
    label="Nom"
    name="name"
    validate={{ required: { value: true, message: 'Ce champ est obligatoire.' } }}
  />
</ValidatedForm>
```

---

## 8. Services Backend

### 8.1 Pattern Service/Repository

```java
@Service
@Transactional
public class EntityService {
    
    private final EntityRepository entityRepository;
    private final EntityMapper entityMapper;
    
    public EntityDTO save(EntityDTO dto) {
        Entity entity = entityMapper.toEntity(dto);
        entity = entityRepository.save(entity);
        return entityMapper.toDto(entity);
    }
    
    @Transactional(readOnly = true)
    public Page<EntityDTO> findAll(Pageable pageable) {
        return entityRepository.findAll(pageable)
            .map(entityMapper::toDto);
    }
}
```

### 8.2 Requêtes natives PostgreSQL

Pour les requêtes avec paramètres optionnels (nullable), utiliser le pattern CAST :

```java
@Query(
    value = "SELECT * FROM table " +
            "WHERE (CAST(:param AS TYPE) IS NULL OR column = :param)",
    nativeQuery = true
)
Page<Entity> search(@Param("param") Type param, Pageable pageable);
```

Ce pattern est nécessaire car PostgreSQL ne peut pas inférer le type d'un paramètre NULL.

### 8.3 Multi-tenant (isolation par hôpital)

Les données sont filtrées par hôpital via `CurrentHospitalProvider` :

```java
Long hospitalId = currentHospitalProvider.getCurrentHospitalId().orElse(null);
return repository.search(..., hospitalId, pageable);
```

---

## 9. Génération de documents PDF

### 9.1 PDF côté Frontend (@react-pdf/renderer)

Utilisé pour : Factures, Ordonnances, Certificats médicaux

```tsx
import { Document, Page, Text, View, PDFDownloadLink } from '@react-pdf/renderer';

const MyDocument = (
  <Document>
    <Page>
      <View><Text>Contenu</Text></View>
    </Page>
  </Document>
);

<PDFDownloadLink document={MyDocument} fileName="document.pdf">
  Télécharger
</PDFDownloadLink>
```

### 9.2 PDF côté Backend (OpenPDF)

Utilisé pour des cas plus complexes nécessitant des données serveur.

```java
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

Document document = new Document();
PdfWriter.getInstance(document, outputStream);
document.open();
document.add(new Paragraph("Contenu"));
document.close();
```

### 9.3 Alignement du texte

- Titres et en-têtes : Centré
- Tableaux : Centré
- Paragraphes de texte : Justifié (`textAlign: 'justify'`)

---

## 10. Configuration et déploiement

### 10.1 Environnement de développement

#### Backend
```bash
cd ngirwi-medical-backend-master
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Frontend
```bash
cd ngirwi-medical-react-master
npm install
npm start
```

### 10.2 Build de production

#### Backend
```bash
./mvnw clean package -Pprod -DskipTests
# Génère target/ngirwi-medical-*.jar
```

#### Frontend
```bash
npm run build
# Génère target/classes/static/
```

### 10.3 Déploiement VPS

#### Structure sur le serveur
```
/opt/ngirwi-medical/
├── ngirwi-medical.jar    # Backend
└── frontend/             # Fichiers statiques
```

#### Configuration Nginx
```nginx
server {
    listen 80;
    server_name your-domain.com;
    
    location / {
        root /opt/ngirwi-medical/frontend;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

#### Service Systemd
```ini
[Unit]
Description=Ngirwi Medical Backend
After=network.target

[Service]
Type=simple
User=root
ExecStart=/usr/bin/java -jar /opt/ngirwi-medical/ngirwi-medical.jar --spring.profiles.active=prod
Restart=always

[Install]
WantedBy=multi-user.target
```

### 10.4 Base de données

#### Connexion PostgreSQL
```yaml
# application-prod.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ngirwi_medical
    username: postgres
    password: [password]
```

#### Migrations Liquibase
Les migrations sont automatiquement exécutées au démarrage. En cas de problème de checksum :
```sql
UPDATE databasechangelog SET md5sum = NULL WHERE id = 'changelog_id';
```

---

## 11. Conventions de code

### 11.1 Nommage

| Type | Convention | Exemple |
|------|------------|---------|
| Entité Java | PascalCase | `Hospitalisation` |
| Variable Java | camelCase | `patientId` |
| Constante | UPPER_SNAKE | `ROLE_ADMIN` |
| Composant React | PascalCase | `BillUpdate` |
| Fichier React | kebab-case | `bill-update.tsx` |
| URL API | kebab-case | `/api/dossier-medicals` |


---

## 12. Dépannage courant

### 12.1 Erreur 403 Forbidden

**Cause probable** : Rôle manquant ou mal configuré

**Vérifications** :
1. Vérifier les rôles de l'utilisateur dans `jhi_user_authority`
2. Vérifier que le rôle existe dans `jhi_authority`
3. Vérifier que `AuthoritiesConstants` correspond aux valeurs en base
4. Se déconnecter et reconnecter pour rafraîchir le token JWT

### 12.2 Liquibase checksum error

**Cause** : Modification d'un changelog déjà exécuté

**Solution** :
```sql
-- Option 1 : Mettre à jour le checksum
UPDATE databasechangelog SET md5sum = NULL WHERE id = 'changelog_id';

-- Option 2 : Réinitialiser (ATTENTION : perte de données)
DROP DATABASE ngirwi_medical;
CREATE DATABASE ngirwi_medical;
```

### 12.3 Frontend ne se met pas à jour

**Causes possibles** :
1. Cache navigateur : Ctrl+Shift+R pour forcer le rafraîchissement
2. Mauvais répertoire de déploiement
3. Nginx non rechargé après déploiement

### 12.4 PostgreSQL : erreur de type avec paramètres NULL

**Symptôme** : "could not determine data type of parameter"

**Solution** : Utiliser CAST explicite dans les requêtes natives
```sql
WHERE (CAST(:param AS BIGINT) IS NULL OR column = :param)
```

### 12.5 État incohérent après sauvegarde

**Cause** : Navigation avant fin de l'opération Redux

**Solution** : Utiliser `updateSuccess` pour déclencher la navigation
```typescript
useEffect(() => {
  if (updateSuccess) {
    navigate('/entity');
  }
}, [updateSuccess]);
```

---

## Annexes

### A. Commandes utiles

```bash
# Backend
./mvnw clean                    # Nettoyer
./mvnw compile                  # Compiler
./mvnw test                     # Tests
./mvnw spring-boot:run          # Lancer en dev

# Frontend
npm install                     # Installer dépendances
npm start                       # Lancer en dev
npm run build                   # Build production
npm run lint                    # Vérifier le code

# PostgreSQL
sudo -u postgres psql           # Connexion admin
\c ngirwi_medical               # Se connecter à la base
\dt                             # Lister les tables

# Serveur
systemctl status ngirwi-medical # Statut du service
journalctl -u ngirwi-medical -f # Logs en temps réel
nginx -s reload                 # Recharger Nginx
```

### B. Ressources

- Documentation JHipster : https://www.jhipster.tech/
- Spring Boot : https://spring.io/projects/spring-boot
- React : https://react.dev/
- Redux Toolkit : https://redux-toolkit.js.org/
- PostgreSQL : https://www.postgresql.org/docs/

---

*Document rédigé pour le projet Ngirwi Medical - Version 1.0*
