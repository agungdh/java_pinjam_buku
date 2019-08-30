-- mysqldump-php https://github.com/ifsnop/mysqldump-php
--
-- Host: 127.0.0.1	Database: java_pinjam_buku
-- ------------------------------------------------------
-- Server version 	5.5.5-10.3.16-MariaDB
-- Date: Fri, 30 Aug 2019 06:16:33 +0200

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(191) NOT NULL,
  `password` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `admin` VALUES (9,'admin','21232f297a57a5a743894a0e4a801fc3');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `admin` with 1 row(s)
--

--
-- Table structure for table `buku`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `buku` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `judul` varchar(191) NOT NULL,
  `pengarang` varchar(191) NOT NULL,
  `penerbit` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `buku`
--

LOCK TABLES `buku` WRITE;
/*!40000 ALTER TABLE `buku` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `buku` VALUES (1,'Novus Ordo Seclorum','Dunno','Gaktau Sapa Lupa'),(2,'Coding Is Funny (TAPI BOONG)','Sapa Yaaa','Gak Tau');
/*!40000 ALTER TABLE `buku` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `buku` with 2 row(s)
--

--
-- Table structure for table `peminjaman`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `peminjaman` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `id_siswa` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `tanggal_peminjaman` date NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id_buku` (`id_buku`),
  KEY `id_siswa` (`id_siswa`),
  CONSTRAINT `peminjaman_ibfk_1` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id`),
  CONSTRAINT `peminjaman_ibfk_2` FOREIGN KEY (`id_siswa`) REFERENCES `siswa` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `peminjaman`
--

LOCK TABLES `peminjaman` WRITE;
/*!40000 ALTER TABLE `peminjaman` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `peminjaman` VALUES (1,4,2,'2019-08-09','2019-08-09'),(3,5,1,'2019-08-09','2019-08-09'),(5,5,1,'2019-08-05','2019-08-24');
/*!40000 ALTER TABLE `peminjaman` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `peminjaman` with 3 row(s)
--

--
-- Table structure for table `siswa`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `siswa` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nama` varchar(191) NOT NULL,
  `kelas` varchar(191) NOT NULL,
  `nis` varchar(191) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `siswa`
--

LOCK TABLES `siswa` WRITE;
/*!40000 ALTER TABLE `siswa` DISABLE KEYS */;
SET autocommit=0;
INSERT INTO `siswa` VALUES (1,'Agung Sapto Margono Dh','MI15A','15753003'),(4,'Test Orang 1','MI15B','15753001'),(5,'Test Orang 2','MI15C','15753002');
/*!40000 ALTER TABLE `siswa` ENABLE KEYS */;
UNLOCK TABLES;
COMMIT;

-- Dumped table `siswa` with 3 row(s)
--

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on: Fri, 30 Aug 2019 06:16:33 +0200
