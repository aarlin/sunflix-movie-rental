# ************************************************************
# Sequel Pro SQL dump
# Version 4541
#
# http://www.sequelpro.com/
# https://github.com/sequelpro/sequelpro
#
# Host: cse305daa2.cpznraarn7vt.us-east-1.rds.amazonaws.com (MySQL 5.6.27-log)
# Database: Sunflix
# Generation Time: 2017-05-04 05:03:50 +0000
# ************************************************************


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table Account
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Account`;

CREATE TABLE `Account` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `DateOpened` date DEFAULT NULL,
  `Type` varchar(20) DEFAULT NULL,
  `Customer` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `Customer` (`Customer`),
  CONSTRAINT `Account_ibfk_1` FOREIGN KEY (`Customer`) REFERENCES `Customer` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Account` WRITE;
/*!40000 ALTER TABLE `Account` DISABLE KEYS */;

INSERT INTO `Account` (`Id`, `DateOpened`, `Type`, `Customer`)
VALUES
	(1,'2010-06-01','unlimited-2','444-44-4444'),
	(2,'2010-06-15','limited','222-22-2222');

/*!40000 ALTER TABLE `Account` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Actor
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Actor`;

CREATE TABLE `Actor` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `Name` varchar(20) NOT NULL,
  `Age` int(11) NOT NULL,
  `Gender` char(1) NOT NULL,
  `Rating` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Actor` WRITE;
/*!40000 ALTER TABLE `Actor` DISABLE KEYS */;

INSERT INTO `Actor` (`Id`, `Name`, `Age`, `Gender`, `Rating`)
VALUES
	(1,'Al Pacino',63,'M',5),
	(2,'Tim Robbins',53,'M',2);

/*!40000 ALTER TABLE `Actor` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table AppearedIn
# ------------------------------------------------------------

DROP TABLE IF EXISTS `AppearedIn`;

CREATE TABLE `AppearedIn` (
  `ActorId` int(11) NOT NULL DEFAULT '0',
  `MovieId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ActorId`,`MovieId`),
  KEY `MovieId` (`MovieId`),
  CONSTRAINT `AppearedIn_ibfk_1` FOREIGN KEY (`ActorId`) REFERENCES `Actor` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `AppearedIn_ibfk_2` FOREIGN KEY (`MovieId`) REFERENCES `Movie` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `AppearedIn` WRITE;
/*!40000 ALTER TABLE `AppearedIn` DISABLE KEYS */;

INSERT INTO `AppearedIn` (`ActorId`, `MovieId`)
VALUES
	(1,1),
	(2,1),
	(1,3);

/*!40000 ALTER TABLE `AppearedIn` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Customer
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Customer`;

CREATE TABLE `Customer` (
  `Id` varchar(11) NOT NULL DEFAULT '',
  `Email` varchar(40) DEFAULT NULL,
  `Rating` int(11) DEFAULT NULL,
  `CreditCardNumber` varchar(19) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  CONSTRAINT `Customer_ibfk_1` FOREIGN KEY (`Id`) REFERENCES `Person` (`SSN`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Customer` WRITE;
/*!40000 ALTER TABLE `Customer` DISABLE KEYS */;

INSERT INTO `Customer` (`Id`, `Email`, `Rating`, `CreditCardNumber`)
VALUES
	('111-11-1111','syang@cs.sunysb.edu',1,'1234-5678-1234-5678'),
	('222-22-2222','vicdu@cs.sunysb.edu',1,'5678-1234-5678-1234'),
	('333-33-3333','jsmith@ic.sunysb.edu',1,'2345-6789-2345-6789'),
	('444-44-4444','pml@cs.sunysb.edu',1,'6789-2345-6789-2345');

/*!40000 ALTER TABLE `Customer` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Employee
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Employee`;

CREATE TABLE `Employee` (
  `ID` int(11) NOT NULL DEFAULT '0',
  `SSN` varchar(11) DEFAULT NULL,
  `StartDate` date DEFAULT NULL,
  `HourlyRate` int(11) DEFAULT NULL,
  `ManagerStatus` tinyint(1) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `SSN` (`SSN`),
  CONSTRAINT `Employee_ibfk_1` FOREIGN KEY (`SSN`) REFERENCES `Person` (`SSN`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Employee` WRITE;
/*!40000 ALTER TABLE `Employee` DISABLE KEYS */;

INSERT INTO `Employee` (`ID`, `SSN`, `StartDate`, `HourlyRate`, `ManagerStatus`)
VALUES
	(1,'123-45-6789','2011-05-01',60,0),
	(2,'789-12-3456','2002-06-02',50,1);

/*!40000 ALTER TABLE `Employee` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Location
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Location`;

CREATE TABLE `Location` (
  `ZipCode` int(11) NOT NULL DEFAULT '0',
  `City` varchar(20) NOT NULL,
  `State` varchar(20) NOT NULL,
  PRIMARY KEY (`ZipCode`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Location` WRITE;
/*!40000 ALTER TABLE `Location` DISABLE KEYS */;

INSERT INTO `Location` (`ZipCode`, `City`, `State`)
VALUES
	(11790,'Stony Brook','NY'),
	(11794,'Stony Brook','NY'),
	(93536,'Los Angeles','CA');

/*!40000 ALTER TABLE `Location` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Movie
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Movie`;

CREATE TABLE `Movie` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `Name` varchar(20) NOT NULL,
  `Type` varchar(20) NOT NULL,
  `Rating` int(11) DEFAULT NULL,
  `DistrFee` int(11) DEFAULT NULL,
  `NumCopies` int(11) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Movie` WRITE;
/*!40000 ALTER TABLE `Movie` DISABLE KEYS */;

INSERT INTO `Movie` (`Id`, `Name`, `Type`, `Rating`, `DistrFee`, `NumCopies`)
VALUES
	(1,'The Godfather','Drama',5,10000,3),
	(2,'Shawshank Redemption','Drama',4,1000,2),
	(3,'Borat','Comedy',3,500,1);

/*!40000 ALTER TABLE `Movie` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table MovieQ
# ------------------------------------------------------------

DROP TABLE IF EXISTS `MovieQ`;

CREATE TABLE `MovieQ` (
  `AccountId` int(11) NOT NULL DEFAULT '0',
  `MovieId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`AccountId`,`MovieId`),
  KEY `MovieId` (`MovieId`),
  CONSTRAINT `MovieQ_ibfk_1` FOREIGN KEY (`AccountId`) REFERENCES `Account` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `MovieQ_ibfk_2` FOREIGN KEY (`MovieId`) REFERENCES `Movie` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `MovieQ` WRITE;
/*!40000 ALTER TABLE `MovieQ` DISABLE KEYS */;

INSERT INTO `MovieQ` (`AccountId`, `MovieId`)
VALUES
	(1,1),
	(2,2),
	(1,3),
	(2,3);

/*!40000 ALTER TABLE `MovieQ` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Order
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Order`;

CREATE TABLE `Order` (
  `Id` int(11) NOT NULL DEFAULT '0',
  `DateTime` datetime DEFAULT NULL,
  `ReturnDate` date DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Order` WRITE;
/*!40000 ALTER TABLE `Order` DISABLE KEYS */;

INSERT INTO `Order` (`Id`, `DateTime`, `ReturnDate`)
VALUES
	(1,'2011-09-11 10:00:00','2011-09-14'),
	(2,'2011-09-11 18:15:00',NULL),
	(3,'2011-09-12 09:30:00',NULL),
	(4,'2011-09-21 22:22:00',NULL);

/*!40000 ALTER TABLE `Order` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Person
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Person`;

CREATE TABLE `Person` (
  `SSN` varchar(11) NOT NULL DEFAULT '',
  `LastName` varchar(20) NOT NULL,
  `FirstName` varchar(20) NOT NULL,
  `Address` varchar(20) DEFAULT NULL,
  `ZipCode` int(11) DEFAULT NULL,
  `Telephone` varchar(12) DEFAULT NULL,
  PRIMARY KEY (`SSN`),
  KEY `ZipCode` (`ZipCode`),
  CONSTRAINT `Person_ibfk_1` FOREIGN KEY (`ZipCode`) REFERENCES `Location` (`ZipCode`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Person` WRITE;
/*!40000 ALTER TABLE `Person` DISABLE KEYS */;

INSERT INTO `Person` (`SSN`, `LastName`, `FirstName`, `Address`, `ZipCode`, `Telephone`)
VALUES
	('111-11-1111','Yang','Shang','123 Success Street',11790,'516-632-8959'),
	('123-45-6789','Smith','David','123 College Road',11790,'516-215-2345'),
	('222-22-2222','Du','Victor','456 Fortune Road',11790,'516-632-4360'),
	('333-33-3333','Smith','John','789 Peace Blvd.',93536,'315-443-4321'),
	('444-44-4444','Philip','Louis','135 Knowledge Lane',11794,'516-666-8888'),
	('789-12-3456','Warren','David','456 Sunken Street',11794,'631-632-9987');

/*!40000 ALTER TABLE `Person` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Rental
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Rental`;

CREATE TABLE `Rental` (
  `AccountId` int(11) NOT NULL DEFAULT '0',
  `CustRepId` int(11) NOT NULL DEFAULT '0',
  `OrderId` int(11) NOT NULL DEFAULT '0',
  `MovieId` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`AccountId`,`CustRepId`,`OrderId`,`MovieId`),
  KEY `CustRepId` (`CustRepId`),
  KEY `OrderId` (`OrderId`),
  KEY `MovieId` (`MovieId`),
  CONSTRAINT `Rental_ibfk_1` FOREIGN KEY (`AccountId`) REFERENCES `Account` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `Rental_ibfk_2` FOREIGN KEY (`CustRepId`) REFERENCES `Employee` (`ID`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `Rental_ibfk_3` FOREIGN KEY (`OrderId`) REFERENCES `Order` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `Rental_ibfk_4` FOREIGN KEY (`MovieId`) REFERENCES `Movie` (`Id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Rental` WRITE;
/*!40000 ALTER TABLE `Rental` DISABLE KEYS */;

INSERT INTO `Rental` (`AccountId`, `CustRepId`, `OrderId`, `MovieId`)
VALUES
	(1,1,3,3),
	(2,1,2,3),
	(1,2,1,1),
	(2,2,4,2);

/*!40000 ALTER TABLE `Rental` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table Users
# ------------------------------------------------------------

DROP TABLE IF EXISTS `Users`;

CREATE TABLE `Users` (
  `username` varchar(20) NOT NULL DEFAULT '',
  `password` varchar(20) DEFAULT NULL,
  `ssn` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;

INSERT INTO `Users` (`username`, `password`, `ssn`)
VALUES
	('customer','305','444-44-4444'),
	('custrep','305','123-45-6789'),
	('manager','305','789-12-3456');

/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;



/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
