-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: employeedata
-- ------------------------------------------------------
-- Server version	8.0.41

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `address`
--

DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `address` (
  `empid` int NOT NULL,
  `street` varchar(100) NOT NULL,
  `city_id` int DEFAULT NULL,
  `state_id` int DEFAULT NULL,
  `zip` varchar(10) NOT NULL,
  PRIMARY KEY (`empid`),
  KEY `city_id` (`city_id`),
  KEY `state_id` (`state_id`),
  CONSTRAINT `address_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`) ON DELETE CASCADE,
  CONSTRAINT `address_ibfk_2` FOREIGN KEY (`city_id`) REFERENCES `city` (`city_id`),
  CONSTRAINT `address_ibfk_3` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`),
  CONSTRAINT `address_ibfk_4` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `address`
--

LOCK TABLES `address` WRITE;
/*!40000 ALTER TABLE `address` DISABLE KEYS */;
/*!40000 ALTER TABLE `address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `city`
--

DROP TABLE IF EXISTS `city`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `city` (
  `city_id` int NOT NULL AUTO_INCREMENT,
  `city_name` varchar(50) NOT NULL,
  `state_id` int DEFAULT NULL,
  PRIMARY KEY (`city_id`),
  UNIQUE KEY `city_name` (`city_name`),
  KEY `state_id` (`state_id`),
  CONSTRAINT `city_ibfk_1` FOREIGN KEY (`state_id`) REFERENCES `state` (`state_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `city`
--

LOCK TABLES `city` WRITE;
/*!40000 ALTER TABLE `city` DISABLE KEYS */;
/*!40000 ALTER TABLE `city` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `demographics`
--

DROP TABLE IF EXISTS `demographics`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `demographics` (
  `empid` int NOT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `race` varchar(50) DEFAULT NULL,
  `DOB` date DEFAULT NULL,
  `mobile` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`empid`),
  CONSTRAINT `demographics_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `demographics`
--

LOCK TABLES `demographics` WRITE;
/*!40000 ALTER TABLE `demographics` DISABLE KEYS */;
/*!40000 ALTER TABLE `demographics` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `division`
--

DROP TABLE IF EXISTS `division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `division` (
  `ID` int NOT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `city` varchar(50) NOT NULL,
  `addressLine1` varchar(50) NOT NULL,
  `addressLine2` varchar(50) DEFAULT NULL,
  `state` varchar(50) DEFAULT NULL,
  `country` varchar(50) NOT NULL,
  `postalCode` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `division`
--

LOCK TABLES `division` WRITE;
/*!40000 ALTER TABLE `division` DISABLE KEYS */;
INSERT INTO `division` VALUES (1,'Technology Engineering','Atlanta','200 17th Street NW','','GA','USA','30363'),(2,'Marketing','Atlanta','200 17th Street NW','','GA','USA','30363'),(3,'Human Resources','New York','45 West 57th Street','','NY','USA','00034'),(999,'HQ','New York','45 West 57th Street','','NY','USA','00034');
/*!40000 ALTER TABLE `division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_division`
--

DROP TABLE IF EXISTS `employee_division`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_division` (
  `empid` int NOT NULL,
  `div_ID` int NOT NULL,
  PRIMARY KEY (`empid`,`div_ID`),
  KEY `div_ID` (`div_ID`),
  CONSTRAINT `employee_division_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`),
  CONSTRAINT `employee_division_ibfk_2` FOREIGN KEY (`div_ID`) REFERENCES `division` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_division`
--

LOCK TABLES `employee_division` WRITE;
/*!40000 ALTER TABLE `employee_division` DISABLE KEYS */;
INSERT INTO `employee_division` VALUES (7,1),(10,1),(1,999),(2,999),(3,999);
/*!40000 ALTER TABLE `employee_division` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_job_titles`
--

DROP TABLE IF EXISTS `employee_job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_job_titles` (
  `empid` int NOT NULL,
  `job_title_id` int NOT NULL,
  PRIMARY KEY (`empid`,`job_title_id`),
  KEY `job_title_id` (`job_title_id`),
  CONSTRAINT `employee_job_titles_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`),
  CONSTRAINT `employee_job_titles_ibfk_2` FOREIGN KEY (`job_title_id`) REFERENCES `job_titles` (`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_job_titles`
--

LOCK TABLES `employee_job_titles` WRITE;
/*!40000 ALTER TABLE `employee_job_titles` DISABLE KEYS */;
INSERT INTO `employee_job_titles` VALUES (7,100),(5,101),(4,102),(8,102),(9,102),(10,102),(14,103),(15,103),(11,200),(6,201),(12,201),(13,202),(2,900),(3,901),(1,902);
/*!40000 ALTER TABLE `employee_job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employees`
--

DROP TABLE IF EXISTS `employees`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employees` (
  `empid` int NOT NULL,
  `fname` varchar(255) DEFAULT NULL,
  `lname` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `HireDate` date DEFAULT NULL,
  `salary` double DEFAULT NULL,
  `ssn` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employees`
--

LOCK TABLES `employees` WRITE;
/*!40000 ALTER TABLE `employees` DISABLE KEYS */;
INSERT INTO `employees` VALUES (1,'Snoopy','Beagle','Snoopy@example.com','2022-08-01',45000,'111-11-1111'),(2,'Charlie','Brown','Charlie@example.com','2022-07-01',48000,'111-22-1111'),(3,'Lucy','Doctor','Lucy@example.com','2022-07-03',55000,'111-33-1111'),(4,'Pepermint','Patti','Peppermint@example.com','2022-08-02',98000,'111-44-1111'),(5,'Linus','Blanket','Linus@example.com','2022-09-01',43000,'111-55-1111'),(6,'PigPin','Dusty','PigPin@example.com','2022-10-01',33000,'111-66-1111'),(7,'Scooby','Doo','Scooby@example.com','1973-07-03',78000,'111-77-1111'),(8,'Shaggy','Rodgers','Shaggy@example.com','1973-07-11',77000,'111-88-1111'),(9,'Velma','Dinkley','Velma@example.com','1973-07-21',82000,'111-99-1111'),(10,'Daphne','Blake','Daphne@example.com','1973-07-30',59000,'111-00-1111'),(11,'bad','Bunny','Bugs@example.com','1934-07-01',26440.992000000002,'222-11-1111'),(12,'Daffy','Duck','Daffy@example.com','1935-04-01',16000,'333-11-1111'),(13,'Porky','Pig','Porky@example.com','1935-08-12',16550,'444-11-1111'),(14,'Elmer','Fudd','Elmer@example.com','1934-08-01',15500,'555-11-1111'),(15,'Marvin','Martian','Marvin@example.com','1937-05-01',28562.8,'777-11-1111');
/*!40000 ALTER TABLE `employees` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `job_titles`
--

DROP TABLE IF EXISTS `job_titles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `job_titles` (
  `job_title_id` int NOT NULL,
  `job_title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `job_titles`
--

LOCK TABLES `job_titles` WRITE;
/*!40000 ALTER TABLE `job_titles` DISABLE KEYS */;
INSERT INTO `job_titles` VALUES (100,'software manager'),(101,'software architect'),(102,'software engineer'),(103,'software developer'),(200,'marketing manager'),(201,'marketing associate'),(202,'marketing assistant'),(900,'Chief Exec. Officer'),(901,'Chief Finn. Officer'),(902,'Chief Info. Officer');
/*!40000 ALTER TABLE `job_titles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payroll`
--

DROP TABLE IF EXISTS `payroll`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payroll` (
  `payID` int NOT NULL,
  `pay_date` date DEFAULT NULL,
  `earnings` double DEFAULT NULL,
  `fed_tax` double DEFAULT NULL,
  `fed_med` double DEFAULT NULL,
  `fed_SS` decimal(7,2) DEFAULT NULL,
  `state_tax` double DEFAULT NULL,
  `retire_401k` decimal(7,2) DEFAULT NULL,
  `health_care` double DEFAULT NULL,
  `empid` int DEFAULT NULL,
  `fedss` double DEFAULT NULL,
  `retire401k` double DEFAULT NULL,
  PRIMARY KEY (`payID`),
  KEY `empid` (`empid`),
  CONSTRAINT `FKll9n6g0c6a3hyanfdd87ywhqu` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`),
  CONSTRAINT `payroll_ibfk_1` FOREIGN KEY (`empid`) REFERENCES `employees` (`empid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payroll`
--

LOCK TABLES `payroll` WRITE;
/*!40000 ALTER TABLE `payroll` DISABLE KEYS */;
INSERT INTO `payroll` VALUES (1,'2025-01-31',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1,NULL,NULL),(2,'2025-02-28',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1,NULL,NULL),(3,'2025-01-31',923.08,295.38,13.38,57.23,110.77,3.69,28.62,2,NULL,NULL),(4,'2025-02-28',923.08,295.38,13.38,57.23,110.77,3.69,28.62,2,NULL,NULL),(5,'2025-01-31',1057.69,338.46,15.34,65.58,126.92,4.23,32.79,3,NULL,NULL),(6,'2025-02-28',1057.69,338.46,15.34,65.58,126.92,4.23,32.79,3,NULL,NULL),(7,'2025-01-31',1884.62,603.08,27.33,116.85,226.15,7.54,58.42,4,NULL,NULL),(8,'2025-02-28',1884.62,603.08,27.33,116.85,226.15,7.54,58.42,4,NULL,NULL),(9,'2025-01-31',826.92,264.62,11.99,51.27,99.23,3.31,25.63,5,NULL,NULL),(10,'2025-02-28',826.92,264.62,11.99,51.27,99.23,3.31,25.63,5,NULL,NULL),(11,'2025-01-31',634.62,203.08,9.2,39.35,76.15,2.54,19.67,6,NULL,NULL),(12,'2025-02-28',634.62,203.08,9.2,39.35,76.15,2.54,19.67,6,NULL,NULL),(13,'2024-12-31',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1,NULL,NULL),(14,'2024-11-30',865.38,276.92,12.55,53.65,103.85,3.46,26.83,1,NULL,NULL),(15,'2024-12-31',1500,480,21.75,93.00,180,6.00,46.5,7,NULL,NULL);
/*!40000 ALTER TABLE `payroll` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `state`
--

DROP TABLE IF EXISTS `state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `state` (
  `state_id` int NOT NULL AUTO_INCREMENT,
  `state_name` varchar(50) NOT NULL,
  PRIMARY KEY (`state_id`),
  UNIQUE KEY `state_name` (`state_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `state`
--

LOCK TABLES `state` WRITE;
/*!40000 ALTER TABLE `state` DISABLE KEYS */;
/*!40000 ALTER TABLE `state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'employeedata'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-01 22:22:37
