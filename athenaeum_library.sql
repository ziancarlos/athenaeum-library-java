CREATE DATABASE  IF NOT EXISTS `athenaeum_library` /*!40100 DEFAULT CHARACTER SET utf8mb3 */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `athenaeum_library`;
-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: athenaeum_library
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Temporary view structure for view `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!50001 DROP VIEW IF EXISTS `admin`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `admin` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `created_at`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `bookkeepings`
--

DROP TABLE IF EXISTS `bookkeepings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bookkeepings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `double_entry_type` enum('debit','kredit') NOT NULL,
  `transaction_type` enum('purchasing','fine','borrowing') NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `payment_date` timestamp NOT NULL,
  `purchasing_id` int DEFAULT NULL,
  `borrowing_id` int DEFAULT NULL,
  `penalty_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `purchasing_id` (`purchasing_id`),
  UNIQUE KEY `borrowing_id` (`borrowing_id`),
  UNIQUE KEY `penalty_id` (`penalty_id`),
  CONSTRAINT `bookkeepings_ibfk_1` FOREIGN KEY (`purchasing_id`) REFERENCES `purchasings` (`id`),
  CONSTRAINT `bookkeepings_ibfk_2` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`),
  CONSTRAINT `bookkeepings_ibfk_3` FOREIGN KEY (`penalty_id`) REFERENCES `penalties` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bookkeepings`
--

LOCK TABLES `bookkeepings` WRITE;
/*!40000 ALTER TABLE `bookkeepings` DISABLE KEYS */;
INSERT INTO `bookkeepings` VALUES (20,'kredit','purchasing',89.00,'2022-07-06 13:26:11',10,NULL,NULL),(21,'kredit','purchasing',131.00,'2022-07-06 13:27:36',11,NULL,NULL),(22,'kredit','purchasing',155.00,'2022-07-06 13:28:37',12,NULL,NULL),(23,'debit','borrowing',30.00,'2022-07-06 13:39:50',NULL,16,NULL),(24,'debit','borrowing',55.00,'2022-07-06 13:40:42',NULL,17,NULL),(25,'debit','borrowing',25.00,'2022-07-06 13:40:55',NULL,18,NULL),(26,'debit','borrowing',40.00,'2022-07-06 13:41:18',NULL,19,NULL);
/*!40000 ALTER TABLE `bookkeepings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `books`
--

DROP TABLE IF EXISTS `books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `books` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `category_id` int NOT NULL,
  `availability` enum('available','unavailable') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `books_ibfk_1` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `books`
--

LOCK TABLES `books` WRITE;
/*!40000 ALTER TABLE `books` DISABLE KEYS */;
INSERT INTO `books` VALUES (17,'Episode Three Mother',23,'available'),(18,'The Great Adventure Boxed Set',23,'unavailable'),(19,'PrimeTime',23,'available'),(20,'Real Views',23,'unavailable'),(21,'American Cultural',25,'unavailable'),(22,'Oxford handbook',25,'unavailable'),(23,'Carl Barks Disney',27,'unavailable'),(24,'The Transformation',25,'available'),(25,'We Spoke Out',28,'available'),(26,'Rich Dad, Poor Dad',26,'available'),(27,'Do Android Dream Of Electric Sheep?',27,'unavailable'),(28,'Are You There, Vodka?',29,'available');
/*!40000 ALTER TABLE `books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrowed_books`
--

DROP TABLE IF EXISTS `borrowed_books`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrowed_books` (
  `borrowing_id` int NOT NULL,
  `borrowing_customer_id` int NOT NULL,
  `book_id` int NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `status` enum('returned','broken','lost','on-going') NOT NULL,
  `end_date` timestamp NOT NULL,
  `returned_date` timestamp NULL DEFAULT NULL,
  KEY `borrowings_id` (`borrowing_id`),
  KEY `borrowings_customer_id` (`borrowing_customer_id`),
  KEY `book_id` (`book_id`),
  CONSTRAINT `borrowed_books_ibfk_1` FOREIGN KEY (`borrowing_id`) REFERENCES `borrowings` (`id`),
  CONSTRAINT `borrowed_books_ibfk_2` FOREIGN KEY (`borrowing_customer_id`) REFERENCES `users` (`id`),
  CONSTRAINT `borrowed_books_ibfk_3` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrowed_books`
--

LOCK TABLES `borrowed_books` WRITE;
/*!40000 ALTER TABLE `borrowed_books` DISABLE KEYS */;
INSERT INTO `borrowed_books` VALUES (16,28,18,15.00,'returned','2022-07-08 17:00:00','2022-07-06 13:40:25'),(16,28,20,15.00,'broken','2022-07-08 17:00:00',NULL),(17,34,21,15.00,'returned','2022-07-08 17:00:00','2022-07-06 13:41:01'),(17,34,22,25.00,'returned','2022-07-10 17:00:00','2022-07-06 13:40:58'),(17,34,19,15.00,'returned','2022-07-08 17:00:00','2022-07-06 13:41:04'),(18,31,18,15.00,'lost','2022-07-08 17:00:00',NULL),(18,31,27,10.00,'on-going','2022-07-07 17:00:00',NULL),(19,33,21,15.00,'lost','2022-07-08 17:00:00',NULL),(19,33,23,20.00,'on-going','2022-07-09 17:00:00',NULL),(19,33,22,5.00,'broken','2022-07-06 17:00:00',NULL);
/*!40000 ALTER TABLE `borrowed_books` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `borrowings`
--

DROP TABLE IF EXISTS `borrowings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `borrowings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `customer_id` int NOT NULL,
  `status` enum('on-going','completed') NOT NULL DEFAULT 'on-going',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `borrowings_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `borrowings`
--

LOCK TABLES `borrowings` WRITE;
/*!40000 ALTER TABLE `borrowings` DISABLE KEYS */;
INSERT INTO `borrowings` VALUES (16,28,'completed'),(17,34,'completed'),(18,31,'on-going'),(19,33,'on-going');
/*!40000 ALTER TABLE `borrowings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `borrowings_bookkeepings`
--

DROP TABLE IF EXISTS `borrowings_bookkeepings`;
/*!50001 DROP VIEW IF EXISTS `borrowings_bookkeepings`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `borrowings_bookkeepings` AS SELECT 
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `borrowing_id`,
 1 AS `payment_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categories` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(46) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (23,'Action And Adventure'),(24,'Classics'),(25,'Comic Book Or Graphic Novel'),(26,'Fantasy'),(27,'Historical Fiction'),(28,'Horror'),(29,'Literary Ficiton');
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!50001 DROP VIEW IF EXISTS `customers`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `customers` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `phone_number`,
 1 AS `created_at`,
 1 AS `blacklisted`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `libarians`
--

DROP TABLE IF EXISTS `libarians`;
/*!50001 DROP VIEW IF EXISTS `libarians`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `libarians` AS SELECT 
 1 AS `id`,
 1 AS `username`,
 1 AS `password`,
 1 AS `role`,
 1 AS `active`,
 1 AS `created_at`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `penalties`
--

DROP TABLE IF EXISTS `penalties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `penalties` (
  `id` int NOT NULL AUTO_INCREMENT,
  `penalty_type` enum('late','broken','lost') NOT NULL,
  `penalty_date` timestamp NOT NULL,
  `payment_status` enum('paid','unpaid') NOT NULL,
  `amount` decimal(15,2) NOT NULL,
  `borrowed_book_book_id` int NOT NULL,
  `borrowed_book_borrowing_customer_id` int NOT NULL,
  `borrowed_book_borrowing_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  KEY `borrowed_book_book_id` (`borrowed_book_book_id`),
  KEY `borrowed_book_borrowing_customer_id` (`borrowed_book_borrowing_customer_id`),
  KEY `borrowed_book_borrowing_id` (`borrowed_book_borrowing_id`),
  CONSTRAINT `penalties_ibfk_1` FOREIGN KEY (`borrowed_book_book_id`) REFERENCES `borrowed_books` (`book_id`),
  CONSTRAINT `penalties_ibfk_2` FOREIGN KEY (`borrowed_book_book_id`) REFERENCES `borrowed_books` (`book_id`),
  CONSTRAINT `penalties_ibfk_3` FOREIGN KEY (`borrowed_book_borrowing_customer_id`) REFERENCES `borrowed_books` (`borrowing_customer_id`),
  CONSTRAINT `penalties_ibfk_4` FOREIGN KEY (`borrowed_book_borrowing_id`) REFERENCES `borrowed_books` (`borrowing_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `penalties`
--

LOCK TABLES `penalties` WRITE;
/*!40000 ALTER TABLE `penalties` DISABLE KEYS */;
INSERT INTO `penalties` VALUES (25,'broken','2022-07-06 13:40:29','unpaid',20.00,20,28,16),(26,'lost','2022-07-06 13:41:32','unpaid',20.00,18,31,18),(27,'lost','2022-07-06 13:41:36','unpaid',30.00,21,33,19),(28,'broken','2022-07-06 13:41:39','unpaid',21.00,22,33,19),(29,'late','2022-07-08 17:00:00','unpaid',5.00,27,31,18),(30,'late','2022-07-09 17:00:00','unpaid',5.00,27,31,18),(31,'late','2022-07-10 17:00:00','unpaid',5.00,27,31,18),(32,'late','2022-07-11 17:00:00','unpaid',5.00,27,31,18),(33,'late','2022-07-12 17:00:00','unpaid',5.00,27,31,18),(34,'late','2022-07-10 17:00:00','unpaid',5.00,23,33,19),(35,'late','2022-07-11 17:00:00','unpaid',5.00,23,33,19),(36,'late','2022-07-12 17:00:00','unpaid',5.00,23,33,19),(37,'late','2022-07-13 17:00:00','unpaid',5.00,27,31,18),(38,'late','2022-07-13 17:00:00','unpaid',5.00,23,33,19);
/*!40000 ALTER TABLE `penalties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `penalties_bookkeepings`
--

DROP TABLE IF EXISTS `penalties_bookkeepings`;
/*!50001 DROP VIEW IF EXISTS `penalties_bookkeepings`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `penalties_bookkeepings` AS SELECT 
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `purchasing_id`,
 1 AS `payment_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `purchasings`
--

DROP TABLE IF EXISTS `purchasings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `supplier_name` varchar(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasings`
--

LOCK TABLES `purchasings` WRITE;
/*!40000 ALTER TABLE `purchasings` DISABLE KEYS */;
INSERT INTO `purchasings` VALUES (10,'Gramedia'),(11,'Buka Buku'),(12,'Periplus');
/*!40000 ALTER TABLE `purchasings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `purchasings_bookkeepings`
--

DROP TABLE IF EXISTS `purchasings_bookkeepings`;
/*!50001 DROP VIEW IF EXISTS `purchasings_bookkeepings`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `purchasings_bookkeepings` AS SELECT 
 1 AS `double_entry_type`,
 1 AS `transaction_type`,
 1 AS `amount`,
 1 AS `purchasing_id`,
 1 AS `payment_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `purchasings_books_details`
--

DROP TABLE IF EXISTS `purchasings_books_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchasings_books_details` (
  `id` int NOT NULL AUTO_INCREMENT,
  `purchasing_price` decimal(15,2) NOT NULL,
  `purchasing_id` int NOT NULL,
  `book_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `book_id` (`book_id`),
  KEY `purchasing_id` (`purchasing_id`),
  CONSTRAINT `purchasings_books_details_ibfk_1` FOREIGN KEY (`purchasing_id`) REFERENCES `purchasings` (`id`),
  CONSTRAINT `purchasings_books_details_ibfk_2` FOREIGN KEY (`book_id`) REFERENCES `books` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchasings_books_details`
--

LOCK TABLES `purchasings_books_details` WRITE;
/*!40000 ALTER TABLE `purchasings_books_details` DISABLE KEYS */;
INSERT INTO `purchasings_books_details` VALUES (6,19.00,10,17),(7,20.00,10,18),(8,30.00,10,19),(9,20.00,10,20),(10,30.00,11,21),(11,21.00,11,22),(12,20.00,11,23),(13,30.00,11,24),(14,30.00,11,25),(15,35.00,12,26),(16,30.00,12,27),(17,90.00,12,28);
/*!40000 ALTER TABLE `purchasings_books_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `purchasings_details`
--

DROP TABLE IF EXISTS `purchasings_details`;
/*!50001 DROP VIEW IF EXISTS `purchasings_details`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `purchasings_details` AS SELECT 
 1 AS `id`,
 1 AS `supplier_name`,
 1 AS `total_books`,
 1 AS `total_amount`,
 1 AS `payment_date`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(30) NOT NULL,
  `password` varchar(45) NOT NULL,
  `role` enum('customer','admin','libarian') NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `active` enum('active','unactive') DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone_number` (`phone_number`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (27,'admin','25f9e794323b453885f5181f1b624d0b','admin',NULL,NULL,'2022-07-06 13:21:52'),(28,'John Doe','25f9e794323b453885f5181f1b624d0b','customer','+6281220465201','active','2022-07-06 13:29:54'),(29,'Oliver John','25f9e794323b453885f5181f1b624d0b','customer','+628210273131','active','2022-07-06 13:30:07'),(30,'Mayline Oliver','25f9e794323b453885f5181f1b624d0b','customer','+62812312312311','active','2022-07-06 13:30:25'),(31,'Gordon Ramsay','25f9e794323b453885f5181f1b624d0b','customer','+6281220476291','active','2022-07-06 13:30:37'),(32,'John Collins','25d55ad283aa400af464c76d713c07ad','customer','+628122038162','active','2022-07-06 13:30:59'),(33,'Albert','25f9e794323b453885f5181f1b624d0b','customer','+628123123123','active','2022-07-06 13:31:24'),(34,'Ashley','25f9e794323b453885f5181f1b624d0b','customer','+6280123123123','active','2022-07-06 13:31:39'),(35,'Carlos','25f9e794323b453885f5181f1b624d0b','customer','+6282123123123','active','2022-07-06 13:31:53'),(36,'Collins Oliver','25f9e794323b453885f5181f1b624d0b','customer','+6281220478312','active','2022-07-06 13:32:06'),(37,'Patty','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'active','2022-07-06 13:32:41'),(38,'Olive Yew','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'active','2022-07-06 13:32:52'),(39,'Aida Bugg','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'unactive','2022-07-06 13:33:01'),(40,'Peg Legge','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'active','2022-07-06 13:33:09'),(41,'Teri Dactyl','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'unactive','2022-07-06 13:33:17'),(42,'Allie Grater','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'active','2022-07-06 13:33:26'),(43,'Maureen','25f9e794323b453885f5181f1b624d0b','libarian',NULL,'active','2022-07-06 13:33:33');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'athenaeum_library'
--
/*!50003 DROP PROCEDURE IF EXISTS `checkIfSpecifiedBorrowingIsCompleted` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `checkIfSpecifiedBorrowingIsCompleted`(in borrowingId INT)
BEGIN
	DECLARE bookBorrowedWithOngoingStatus  INT DEFAULT 0;
	SET bookBorrowedWithOngoingStatus = 0;
    SELECT COUNT(*) INTO bookBorrowedWithOngoingStatus  FROM borrowed_books WHERE borrowing_id = borrowingId AND status = 'on-going' ;
	
     IF (bookBorrowedWithOngoingStatus = 0) THEN
		UPDATE borrowings SET status = 'completed' WHERE id = borrowingId;
	END IF;
    
    
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Final view structure for view `admin`
--

/*!50001 DROP VIEW IF EXISTS `admin`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `admin` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`created_at` AS `created_at` from `users` where (`users`.`role` = 'admin') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `borrowings_bookkeepings`
--

/*!50001 DROP VIEW IF EXISTS `borrowings_bookkeepings`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_unicode_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `borrowings_bookkeepings` AS select `bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`borrowing_id` AS `borrowing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`penalty_id` is null) and (`bookkeepings`.`purchasing_id` is null) and (`bookkeepings`.`borrowing_id` is not null)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `customers`
--

/*!50001 DROP VIEW IF EXISTS `customers`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `customers` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`phone_number` AS `phone_number`,`users`.`created_at` AS `created_at`,if((sum(if((`penalties`.`payment_status` = 'unpaid'),1,0)) = 0),'not-blacklisted','blacklisted') AS `blacklisted` from (`users` left join `penalties` on((`users`.`id` = `penalties`.`borrowed_book_borrowing_customer_id`))) group by `users`.`id` having (`users`.`role` = 'customer') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `libarians`
--

/*!50001 DROP VIEW IF EXISTS `libarians`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `libarians` AS select `users`.`id` AS `id`,`users`.`username` AS `username`,`users`.`password` AS `password`,`users`.`role` AS `role`,`users`.`active` AS `active`,`users`.`created_at` AS `created_at` from `users` where (`users`.`role` = 'libarian') */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `penalties_bookkeepings`
--

/*!50001 DROP VIEW IF EXISTS `penalties_bookkeepings`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `penalties_bookkeepings` AS select `bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`purchasing_id` AS `purchasing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`borrowing_id` is null) and (`bookkeepings`.`purchasing_id` is null) and (`bookkeepings`.`penalty_id` is not null)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `purchasings_bookkeepings`
--

/*!50001 DROP VIEW IF EXISTS `purchasings_bookkeepings`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `purchasings_bookkeepings` AS select `bookkeepings`.`double_entry_type` AS `double_entry_type`,`bookkeepings`.`transaction_type` AS `transaction_type`,`bookkeepings`.`amount` AS `amount`,`bookkeepings`.`purchasing_id` AS `purchasing_id`,`bookkeepings`.`payment_date` AS `payment_date` from `bookkeepings` where ((`bookkeepings`.`borrowing_id` is null) and (`bookkeepings`.`penalty_id` is null) and (`bookkeepings`.`purchasing_id` is not null)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `purchasings_details`
--

/*!50001 DROP VIEW IF EXISTS `purchasings_details`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb3 */;
/*!50001 SET character_set_results     = utf8mb3 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `purchasings_details` AS select `purchasings`.`id` AS `id`,`purchasings`.`supplier_name` AS `supplier_name`,count(`purchasings_books_details`.`purchasing_id`) AS `total_books`,sum(`purchasings_books_details`.`purchasing_price`) AS `total_amount`,`bookkeepings`.`payment_date` AS `payment_date` from ((`purchasings` join `purchasings_books_details` on((`purchasings`.`id` = `purchasings_books_details`.`purchasing_id`))) join `bookkeepings` on((`purchasings`.`id` = `bookkeepings`.`purchasing_id`))) group by `purchasings`.`id` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-14 10:39:24
