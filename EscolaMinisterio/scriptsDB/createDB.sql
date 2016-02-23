-- phpMyAdmin SQL Dump
-- version 4.0.4.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 23, 2016 at 08:14 PM
-- Server version: 5.6.11
-- PHP Version: 5.5.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Table structure for table `ajudante`
--

CREATE TABLE IF NOT EXISTS `ajudante` (
  `ID` varchar(255) NOT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `GENERO` int(11) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ULTIMADESIGNACAO` date DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  `EXCLUSAOLOGICA` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `designacao`
--

CREATE TABLE IF NOT EXISTS `designacao` (
  `ID` varchar(255) NOT NULL,
  `DATA` date DEFAULT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `FONTE` varchar(255) DEFAULT NULL,
  `NUMERO` int(11) DEFAULT NULL,
  `OBSFOLHA` varchar(255) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `SALA` varchar(255) DEFAULT NULL,
  `STATUS` char(1) DEFAULT NULL,
  `TEMA` varchar(255) DEFAULT NULL,
  `ajudante_id` varchar(255) DEFAULT NULL,
  `semana_id` varchar(255) NOT NULL,
  `estudante_id` varchar(255) NOT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  `TEMPO` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_DESIGNACAO_semana_id` (`semana_id`),
  KEY `FK_DESIGNACAO_estudante_id` (`estudante_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `designacao_estudo`
--

CREATE TABLE IF NOT EXISTS `designacao_estudo` (
  `estudo_id` int(11) NOT NULL,
  `designacao_id` varchar(255) NOT NULL,
  PRIMARY KEY (`designacao_id`,`estudo_id`),
  KEY `FK_designacao_estudo_estudo_id` (`estudo_id`),
  KEY `FK_designacao_estudo_designacao_id` (`designacao_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `estudante`
--

CREATE TABLE IF NOT EXISTS `estudante` (
  `ID` varchar(255) NOT NULL,
  `DESABILITADO` tinyint(1) DEFAULT '0',
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `GENERO` int(11) DEFAULT NULL,
  `NAOAJUDANTE` tinyint(1) DEFAULT '0',
  `NOME` varchar(255) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `SALAULTIMADESIGNACAO` char(1) DEFAULT NULL,
  `ULTIMADESIGNACAO` date DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  `EXCLUSAOLOGICA` tinyint(1) DEFAULT NULL,
  `EXCLUIDO` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `estudo`
--

CREATE TABLE IF NOT EXISTS `estudo` (
  `NRESTUDO` int(11) NOT NULL,
  `DEMONSTRACAO` tinyint(1) DEFAULT '0',
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `DISCURSO` tinyint(1) DEFAULT '0',
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `LEITURA` tinyint(1) DEFAULT '0',
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`NRESTUDO`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `itemprofile`
--

CREATE TABLE IF NOT EXISTS `itemprofile` (
  `ID` varchar(255) NOT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `ITEM` int(11) DEFAULT NULL,
  `profile_id` varchar(255) NOT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ITEMPROFILE_profile_id` (`profile_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `itemprofile`
--

INSERT INTO `itemprofile` (`ID`, `DTULTIMAATUALIZA`, `ITEM`, `profile_id`, `DATAEXCLUSAO`) VALUES
('itemprofile_0', '2015-06-05 22:15:45', 8, 'profile_0', NULL),
('itemprofile_1', '2015-06-05 22:15:45', 15, 'profile_0', NULL),
('itemprofile_10', '2015-06-05 22:15:47', 6, 'profile_0', NULL),
('itemprofile_11', '2015-06-05 22:15:48', 12, 'profile_0', NULL),
('itemprofile_12', '2015-06-05 22:15:48', 13, 'profile_0', NULL),
('itemprofile_13', '2015-06-05 22:15:48', 14, 'profile_0', NULL),
('itemprofile_14', '2015-06-05 22:15:48', 4, 'profile_0', NULL),
('itemprofile_15', '2015-06-05 22:15:48', 2, 'profile_0', NULL),
('itemprofile_16', '2015-06-05 22:15:49', 18, 'profile_0', NULL),
('itemprofile_17', '2015-06-05 22:15:49', 5, 'profile_0', NULL),
('itemprofile_18', '2015-06-05 22:15:49', 19, 'profile_0', NULL),
('itemprofile_19', '2015-06-05 22:15:49', 16, 'profile_0', NULL),
('itemprofile_2', '2015-06-05 22:15:46', 3, 'profile_0', NULL),
('itemprofile_3', '2015-06-05 22:15:46', 1, 'profile_0', NULL),
('itemprofile_4', '2015-06-05 22:15:46', 10, 'profile_0', NULL),
('itemprofile_5', '2015-06-05 22:15:46', 11, 'profile_0', NULL),
('itemprofile_6', '2015-06-05 22:15:46', 17, 'profile_0', NULL),
('itemprofile_7', '2015-06-05 22:15:47', 7, 'profile_0', NULL),
('itemprofile_8', '2015-06-05 22:15:47', 9, 'profile_0', NULL),
('itemprofile_9', '2015-06-05 22:15:47', 0, 'profile_0', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `mesdesignacao`
--

CREATE TABLE IF NOT EXISTS `mesdesignacao` (
  `ID` varchar(255) NOT NULL,
  `ANO` int(11) DEFAULT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `MES` int(11) DEFAULT NULL,
  `STATUS` char(1) DEFAULT NULL,
  `MELHOREMINISTERIO` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `profile`
--

CREATE TABLE IF NOT EXISTS `profile` (
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `ID` varchar(255) NOT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `profile`
--

INSERT INTO `profile` (`DTULTIMAATUALIZA`, `ID`, `NOME`, `DATAEXCLUSAO`) VALUES
('2015-06-11 10:56:06', 'profile_0', 'Administrador', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `semanadesignacao`
--

CREATE TABLE IF NOT EXISTS `semanadesignacao` (
  `ID` varchar(255) NOT NULL,
  `ASSEBLEIA` tinyint(1) DEFAULT '0',
  `DATA` date DEFAULT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `RECAPITULACAO` tinyint(1) DEFAULT '0',
  `SEMREUNIAO` tinyint(1) DEFAULT '0',
  `VISITA` tinyint(1) DEFAULT '0',
  `mes_id` varchar(255) NOT NULL,
  `VIDEOS` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `FK_SEMANADESIGNACAO_mes_id` (`mes_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sequencia`
--

CREATE TABLE IF NOT EXISTS `sequencia` (
  `tabela` varchar(30) NOT NULL DEFAULT '',
  `sequencia` int(11) DEFAULT NULL,
  PRIMARY KEY (`tabela`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `sequencia`
--

INSERT INTO `sequencia` (`tabela`, `sequencia`) VALUES
('ajudante', 0),
('designacao', 0),
('estudante', 0),
('itemprofile', 20),
('mesdesignacao', 0),
('profile', 1),
('semanadesignacao', 0),
('usuario', 1);

-- --------------------------------------------------------

--
-- Table structure for table `sincronismo`
--

CREATE TABLE IF NOT EXISTS `sincronismo` (
  `data` datetime NOT NULL,
  `usuario` varchar(50) CHARACTER SET latin1 NOT NULL,
  `situacao` varchar(1) CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`data`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `ID` varchar(255) NOT NULL,
  `BLOQUEADO` tinyint(1) DEFAULT '0',
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `REINICIASENHA` tinyint(1) DEFAULT '0',
  `SENHA` varchar(255) DEFAULT NULL,
  `PROFILE_ID` varchar(255) NOT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_USUARIO_PROFILE_ID` (`PROFILE_ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

--
-- Dumping data for table `usuario`
--
--user: admin / pass: adminEscola
INSERT INTO `usuario` (`ID`, `BLOQUEADO`, `DTULTIMAATUALIZA`, `NOME`, `REINICIASENHA`, `SENHA`, `PROFILE_ID`, `DATAEXCLUSAO`) VALUES
('usuario_0', 0, '2016-02-10 09:21:46', 'admin', 0, 'mc/rGXw2jhsCGyrIu09+EFarZdQ=', 'profile_0', NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
