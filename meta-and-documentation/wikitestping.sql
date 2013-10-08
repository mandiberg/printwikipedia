USE printwiki;

delimiter |
CREATE TRIGGER printwikiListener BEFORE INSERT ON newmaster
	FOR EACH ROW BEGIN
		INSERT INTO countertable VALUES(1);
	END;
|





SELECT page.page_id, page.page_title, revision.rev_user_text, revision.rev_comment, text.old_text
FROM `page` 
LEFT JOIN revision ON (page.page_id = revision.rev_page) 
LEFT JOIN text ON (revision.rev_id = text.old_id)
WHERE page.page_namespace=0
AND page.page_title > (SELECT page_title FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1)
ORDER by page.page_title
LIMIT 1;
