USE YOURDATABASENAME;
 CREATE  TABLE `YOURDATABASENAME`.`newmaster` (
   `pkey` INT NOT NULL AUTO_INCREMENT ,
   `page_id` INT(10) NULL ,
   `page_title` VARCHAR(255) NULL ,
   `rev_user_text` VARCHAR(255) NULL ,
   `rev_comment` TINYBLOB NULL ,
   `old_text` LONGTEXT NULL ,
   PRIMARY KEY (`pkey`) )
 ENGINE = MyISAM
 DEFAULT CHARACTER SET = utf8mb4;

INSERT INTO newmaster (page_id, page_title, rev_user_text, rev_comment, old_text)
SELECT page.page_id, page.page_title, revision.rev_user_text, revision.rev_comment, text.old_text
FROM `page` 
LEFT JOIN revision ON (page.page_id = revision.rev_page) 
LEFT JOIN text ON (revision.rev_id = text.old_id)
WHERE page.page_namespace=0
ORDER by page.page_title COLLATE utf8mb4_unicode_ci;
