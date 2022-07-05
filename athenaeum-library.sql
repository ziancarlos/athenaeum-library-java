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
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'athenaeum_library'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-07-05 14:23:47


CREATE VIEW `libarians` AS SELECT id, username, password, role, active, created_at FROM users WHERE role = 'libarian';

CREATE VIEW customers AS 
 SELECT
    users.id,
    users.username,
    users.password,
    users.role,
    users.phone_number,
    users.created_at,
    IF(SUM(IF(penalties.payment_status = 'unpaid', 1, 0)) = 0, 'not-blacklisted', 'blacklisted' ) AS blacklisted
FROM users
   LEFT JOIN penalties ON users.id = penalties.borrowed_book_borrowing_customer_id
   GROUP BY users.id
   HAVING users.role = 'customer';
   
   CREATE VIEW `admin` AS SELECT id, username, password, role, created_at FROM users WHERE role = 'admin';
   
   CREATE VIEW `borrowings_bookkeepings` AS SELECT double_entry_type, transaction_type, amount, purchasing_id, payment_date FROM bookkeepings WHERE penalty_id IS NULL AND purchasing_id IS NULL AND  borrowing_id IS NOT NULL;
   
   CREATE VIEW `penalties_bookkeepings` AS SELECT double_entry_type, transaction_type, amount, purchasing_id, payment_date FROM bookkeepings WHERE borrowing_id IS NULL AND purchasing_id IS NULL AND penalty_id IS NOT NULL;
   
   CREATE VIEW `purchasings_bookkeepings` AS SELECT double_entry_type, transaction_type, amount, purchasing_id, payment_date FROM bookkeepings WHERE borrowing_id IS NULL AND penalty_id IS NULL AND purchasing_id IS NOT NULL;
   
   CREATE VIEW purchasings_details AS
    SELECT 
        purchasings.id,
        purchasings.supplier_name,
        COUNT(purchasings_books_details.purchasing_id) AS total_books,
        SUM(purchasings_books_details.purchasing_price) AS total_amount,
        bookkeepings.payment_date
    FROM
        purchasings
            INNER JOIN
        purchasings_books_details ON purchasings.id = purchasings_books_details.purchasing_id
            INNER JOIN
        bookkeepings ON purchasings.id = bookkeepings.purchasing_id
    GROUP BY purchasings.id;
    
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