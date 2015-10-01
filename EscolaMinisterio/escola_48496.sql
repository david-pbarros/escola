-- phpMyAdmin SQL Dump
-- version 4.1.6
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 02-Jun-2015 às 12:08
-- Versão do servidor: 5.6.16
-- PHP Version: 5.5.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `escola_48496`
--
CREATE DATABASE IF NOT EXISTS `escola_48496` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `escola_48496`;

-- --------------------------------------------------------

--
-- Estrutura da tabela `ajudante`
--

CREATE TABLE IF NOT EXISTS `ajudante` (
  `ID` varchar(255) NOT NULL,
  `DTULTIMAATUALIZA` date DEFAULT NULL,
  `GENERO` int(11) DEFAULT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `ULTIMADESIGNACAO` date DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `designacao`
--

CREATE TABLE IF NOT EXISTS `designacao` (
  `ID` varchar(255) NOT NULL,
  `DATA` date DEFAULT NULL,
  `DTULTIMAATUALIZA` date DEFAULT NULL,
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
  PRIMARY KEY (`ID`),
  KEY `FK_DESIGNACAO_semana_id` (`semana_id`),
  KEY `FK_DESIGNACAO_estudante_id` (`estudante_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `designacao_estudo`
--

CREATE TABLE IF NOT EXISTS `designacao_estudo` (
  `estudo_id` int(11) NOT NULL,
  `designacao_id` varchar(255) NOT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`designacao_id`,`estudo_id`),
  KEY `FK_designacao_estudo_estudo_id` (`estudo_id`),
  KEY `FK_designacao_estudo_designacao_id` (`designacao_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `estudante`
--

CREATE TABLE IF NOT EXISTS `estudante` (
  `ID` varchar(255) NOT NULL,
  `DESABILITADO` tinyint(1) DEFAULT '0',
  `DTULTIMAATUALIZA` date DEFAULT NULL,
  `GENERO` int(11) DEFAULT NULL,
  `NAOAJUDANTE` tinyint(1) DEFAULT '0',
  `NOME` varchar(255) DEFAULT NULL,
  `OBSERVACAO` varchar(255) DEFAULT NULL,
  `SALAULTIMADESIGNACAO` char(1) DEFAULT NULL,
  `ULTIMADESIGNACAO` date DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `estudo`
--

CREATE TABLE IF NOT EXISTS `estudo` (
  `NRESTUDO` int(11) NOT NULL,
  `DEMONSTRACAO` tinyint(1) DEFAULT '0',
  `DESCRICAO` varchar(255) DEFAULT NULL,
  `DISCURSO` tinyint(1) DEFAULT '0',
  `DTULTIMAATUALIZA` date DEFAULT NULL,
  `LEITURA` tinyint(1) DEFAULT '0',
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`NRESTUDO`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `itemprofile`
--

CREATE TABLE IF NOT EXISTS `itemprofile` (
  `ID` varchar(255) NOT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `ITEM` int(11) DEFAULT NULL,
  `profile_id` varchar(255) NOT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `FK_ITEMPROFILE_profile_id` (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `mesdesignacao`
--

CREATE TABLE IF NOT EXISTS `mesdesignacao` (
  `ID` varchar(255) NOT NULL,
  `ANO` int(11) DEFAULT NULL,
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `MES` int(11) DEFAULT NULL,
  `STATUS` char(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `profile`
--

CREATE TABLE IF NOT EXISTS `profile` (
  `DTULTIMAATUALIZA` datetime DEFAULT NULL,
  `ID` varchar(255) NOT NULL,
  `NOME` varchar(255) DEFAULT NULL,
  `DATAEXCLUSAO` date DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `profile`
--

INSERT INTO `profile` (`DTULTIMAATUALIZA`, `ID`, `NOME`, `DATAEXCLUSAO`) VALUES
('2015-06-02 01:57:09', 'profile_0', 'Administrador', NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `semanadesignacao`
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
  PRIMARY KEY (`ID`),
  KEY `FK_SEMANADESIGNACAO_mes_id` (`mes_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Estrutura da tabela `sequencia`
--

CREATE TABLE IF NOT EXISTS `sequencia` (
  `tabela` varchar(30) NOT NULL DEFAULT '',
  `sequencia` int(11) DEFAULT NULL,
  PRIMARY KEY (`tabela`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `sequencia`
--

INSERT INTO `sequencia` (`tabela`, `sequencia`) VALUES
('ajudante', 0),
('designacao', 0),
('estudante', 0),
('itemprofile', 0),
('mesdesignacao', 0),
('profile', 1),
('semanadesignacao', 0),
('usuario', 1);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `usuario` (`ID`, `BLOQUEADO`, `DTULTIMAATUALIZA`, `NOME`, `REINICIASENHA`, `SENHA`, `PROFILE_ID`, `DATAEXCLUSAO`) VALUES
('usuario_0', 0, NULL, 'admin', 0, '0DPiKuNIrrVmD8IUCuw1hQxNqZc=', 'profile_0', NULL);

--
-- Constraints for dumped tables
--

--
-- Limitadores para a tabela `designacao`
--
ALTER TABLE `designacao`
  ADD CONSTRAINT `FK_DESIGNACAO_estudante_id` FOREIGN KEY (`estudante_id`) REFERENCES `estudante` (`ID`),
  ADD CONSTRAINT `FK_DESIGNACAO_semana_id` FOREIGN KEY (`semana_id`) REFERENCES `semanadesignacao` (`ID`);

--
-- Limitadores para a tabela `designacao_estudo`
--
ALTER TABLE `designacao_estudo`
  ADD CONSTRAINT `FK_designacao_estudo_designacao_id` FOREIGN KEY (`designacao_id`) REFERENCES `designacao` (`ID`),
  ADD CONSTRAINT `FK_designacao_estudo_estudo_id` FOREIGN KEY (`estudo_id`) REFERENCES `estudo` (`NRESTUDO`);

--
-- Limitadores para a tabela `itemprofile`
--
ALTER TABLE `itemprofile`
  ADD CONSTRAINT `FK_ITEMPROFILE_profile_id` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`ID`);

--
-- Limitadores para a tabela `semanadesignacao`
--
ALTER TABLE `semanadesignacao`
  ADD CONSTRAINT `FK_SEMANADESIGNACAO_mes_id` FOREIGN KEY (`mes_id`) REFERENCES `mesdesignacao` (`ID`);

--
-- Limitadores para a tabela `usuario`
--
ALTER TABLE `usuario`
  ADD CONSTRAINT `FK_USUARIO_PROFILE_ID` FOREIGN KEY (`PROFILE_ID`) REFERENCES `profile` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
