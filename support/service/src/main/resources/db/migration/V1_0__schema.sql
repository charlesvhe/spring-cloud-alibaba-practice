CREATE TABLE `config_meta` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` varchar(64) NOT NULL COMMENT '应用id',
  `code` varchar(64) NOT NULL COMMENT '编码',
  `property` varchar(64) NOT NULL COMMENT '接口属性',
  `column_name` varchar(64) NOT NULL COMMENT '列名',
  `description` varchar(128) NOT NULL DEFAULT '' COMMENT '说明',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) COMMENT='配置元信息表';

CREATE TABLE `config_item` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `app_id` varchar(64) NOT NULL COMMENT '应用ID',
  `meta_code` varchar(64) NOT NULL COMMENT '配置元信息编码',
  `parent_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '父配置项id',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `varchar1` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串1',
  `varchar2` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串2',
  `varchar3` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串3',
  `varchar4` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串4',
  `varchar5` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串5',
  `varchar6` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串6',
  `varchar7` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串7',
  `varchar8` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串8',
  `varchar9` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串9',
  `varchar10` varchar(512) NOT NULL DEFAULT '' COMMENT '字符串10',
  `text1` longtext COMMENT '长文本1',
  `text2` longtext COMMENT '长文本2',
  `text3` longtext COMMENT '长文本3',
  `decimal1` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT '数字1',
  `decimal2` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT '数字2',
  `decimal3` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT '数字3',
  `decimal4` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT '数字4',
  `decimal5` decimal(19,4) NOT NULL DEFAULT '0.0000' COMMENT '数字5',
  `datetime1` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日期1',
  `datetime2` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日期2',
  `datetime3` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '日期3',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) COMMENT='配置条目表';


INSERT INTO `config_meta` VALUES
(1,'platform','userType','code','varchar1','类型编码',10,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(2,'platform','userType','name','varchar2','类型名',20,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(3,'platform','userType','description','varchar3','说明',30,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(4,'platform','userType','img','varchar4','图标',40,'2018-05-10 17:54:31','2018-05-10 20:32:23'),

(11,'platform','benchmark','p1','varchar1','属性1',10,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(12,'platform','benchmark','p2','varchar2','属性2',20,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(13,'platform','benchmark','p3','varchar3','属性3',30,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(14,'platform','benchmark','p4','varchar4','属性4',40,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(15,'platform','benchmark','p5','varchar5','属性5',50,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(16,'platform','benchmark','p6','varchar6','属性6',60,'2018-05-10 17:54:31','2018-05-10 21:14:08'),
(17,'platform','benchmark','p7','varchar7','属性7',70,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(18,'platform','benchmark','p8','varchar8','属性8',80,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(19,'platform','benchmark','p9','varchar9','属性9',90,'2018-05-10 17:54:31','2018-05-10 20:32:23'),
(20,'platform','benchmark','p10','varchar10','属性10',100,'2018-05-10 17:54:31','2018-05-10 20:32:23');

INSERT INTO `config_item`(`id`, `app_id`, `meta_code`, `varchar1`, `varchar2`, `varchar3`, `varchar4`, `gmt_create`, `gmt_modified`) VALUES
(1,'platform','userType','user','普通用户','刚注册还未认证用户','1.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08'),
(2,'platform','userType','member','会员','已认证用户','2.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08'),
(3,'platform','userType','VIP1','铜牌会员','铜牌会员','3.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08'),
(4,'platform','userType','VIP2','银牌会员','银牌会员','4.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08'),
(5,'platform','userType','VIP3','金牌会员','金牌会员','5.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08'),
(6,'platform','userType','VVIP','VVIP','VVIP','6.jpg','2018-05-10 17:54:31','2018-05-10 21:14:08');

