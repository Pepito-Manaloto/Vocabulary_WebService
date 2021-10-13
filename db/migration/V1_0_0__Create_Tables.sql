USE `my_vocabulary`;

CREATE TABLE IF NOT EXISTS `foreign_language` (
  `id` tinyint(3) unsigned NOT NULL AUTO_INCREMENT,
  `language` varchar(30) CHARACTER SET utf8mb4 NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `vocabulary` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `foreign_id` tinyint(3) unsigned NOT NULL,
  `english_word` varchar(70) CHARACTER SET utf8mb4 NOT NULL,
  `foreign_word` varchar(100) CHARACTER SET utf8mb4 NOT NULL,
  `datein` datetime NOT NULL,
  `last_updated` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `english_foreign_unique` (`english_word`,`foreign_word`) USING BTREE,
  UNIQUE KEY `UKl9n22wm7vnnyron3n671vbtud` (`english_word`,`foreign_word`),
  KEY `english_word_index` (`english_word`,`id`),
  KEY `foreign_word_index` (`foreign_word`),
  KEY `foreign_id` (`foreign_id`),
  CONSTRAINT `FK9mbmc2msww0876oig00asx15l` FOREIGN KEY (`foreign_id`) REFERENCES `foreign_language` (`id`),
  CONSTRAINT `vocabulary_ibfk_1` FOREIGN KEY (`foreign_id`) REFERENCES `foreign_language` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1064 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
