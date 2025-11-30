# RHxManager

## 👥 Auteurs

* Lucas J.
* Pierre G.
* Maxime D.
* Armand P.
* Gabriel B.


## 🛠 Stack Technique

* **Langage** : Java 21
* **Framework** : Spring Boot 3.3.4
* **Outil de build** : Maven
* **Base de données** : MySQL
* **Frontend** : Thymeleaf (Rendu côté serveur), HTML5, CSS3
* **Sécurité** : Spring Security

## ⚙️ Prérequis

Avant de commencer, assurez-vous d'avoir installé :
* **Java Development Kit (JDK) 21**
* **Maven**
* **MySQL Server**

## 📦 Installation et Configuration

### 1. Cloner le projet
```bash
git clone https://github.com/lucasjmr/RHxManagerSpringBoot
cd RHxManagerSpringBoot
````

### 2\. Configuration de la Base de Données

1.  Ouvrez votre client MySQL (Workbench, CLI, ou autre).
2.  Exécutez le script SQL fourni dans le dossier : `data/database.sql`.
      * *Ce script crée la base de données `rhxmanagerspring`, les tables nécessaires, et un compte administrateur par défaut.*

### 3\. Configuration de l'application

Vérifiez le fichier `src/main/resources/application.properties`. Assurez-vous que le nom d'utilisateur et le mot de passe correspondent à votre installation MySQL locale :

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/rhxmanagerspring
spring.datasource.username=root
spring.datasource.password=VOTRE_MOT_DE_PASSE_MYSQL
```

### 4\. Lancer l'application

Lancez le projet via Maven :

```bash
mvn spring-boot:run
```

L'application sera accessible à l'adresse : `http://localhost:8080`.

## 🔑 Compte de Test (Connexion)

Le script de base de données initialise un compte Administrateur par défaut pour tester l'application :

  * **Nom d'utilisateur** : `aze`
  * **Mot de passe** : `aze`

> **Note** : Le mot de passe est chiffré en base de données via BCrypt.


## 🛡️ Règles de Sécurité

  * **Accès Public** : Page de connexion (`/login`), ressources statiques (CSS/JS).
  * **Accès Admin** : Toutes les routes de gestion (`/employees`, `/departments`, `/projects`, `/payslips`).
  * **Accès Utilisateur** : Les utilisateurs connectés accèdent au tableau de bord.
