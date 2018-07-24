set names utf8;

CREATE DATABASE IF NOT EXISTS `smartcd` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `smartcd`;

source master.sql

source resource_initdata.sql 

-- source adapter.sql

source quartz.sql

-- CREATE DATABASE IF NOT EXISTS `configcenter` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

-- USE `configcenter`;

-- source configcenter.sql