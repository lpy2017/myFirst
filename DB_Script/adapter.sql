DROP TABLE IF EXISTS `ad_cluster`;
CREATE TABLE IF NOT EXISTS `ad_cluster` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ad_node`;
CREATE TABLE IF NOT EXISTS `ad_node` (
  `id` varchar(100) NOT NULL,
  `name` varchar(100) NOT NULL,
  `vm_id` varchar(100) DEFAULT NULL,
  `iaas_id` varchar(100) DEFAULT NULL,
  `cluster_id` varchar(100) NOT NULL,
  `ip` varchar(100) DEFAULT NULL,
  `cpu` int(11) DEFAULT NULL COMMENT '单位：个',
  `memory` double DEFAULT NULL COMMENT '单位：M',
  `disk` double DEFAULT NULL COMMENT '单位：G',
  `status` varchar(10) DEFAULT NULL COMMENT '0：未注册，1：已注册，2：健康，3：不健康，4：已申请',
  `os_version` varchar(100) DEFAULT NULL,
  `docker_version` varchar(100) DEFAULT NULL,
  `agent_version` varchar(100) DEFAULT NULL,
  `timestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ad_cluster_user`;
CREATE TABLE IF NOT EXISTS `ad_cluster_user` (
  `cluster_id` varchar(100) NOT NULL,
  `user_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ad_label`;
CREATE TABLE IF NOT EXISTS `ad_label` (
  `node_id` varchar(100) DEFAULT NULL,
  `key` varchar(100) DEFAULT NULL,
  `value` varchar(100) DEFAULT NULL,
  `type` VARCHAR(50) NULL DEFAULT NULL COMMENT '0：非层，1：静态层，2：动态层'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;