CREATE TABLE `JGROUPSPING` (
  `own_addr` varchar(200) NOT NULL,
  `bind_addr` varchar(200) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `cluster_name` varchar(200) NOT NULL,
  `ping_data` blob,
  PRIMARY KEY (`own_addr`,`cluster_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
