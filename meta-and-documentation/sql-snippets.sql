// find last entry if done running (change the number)

SELECT * FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1;
SELECT * FROM printwiki.newmaster where pkey > 1383320 ORDER BY pkey desc LIMIT 1;


// check last pkey during running

USE printwiki;
SELECT count(pkey) FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1;



// logins

wikis-mac-mini:meta-and-documentation wiki$ mysql -u root -p
Enter password: 






//scraps

(SELECT page_title FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1)


INSERT INTO newmaster (page_id, page_title, rev_user_text, rev_comment, old_text) SELECT page.page_id, page.page_title, revision.rev_user_text, revision.rev_comment, text.old_text FROM `page`  LEFT JOIN revision ON (page.page_id = revision.rev_page)  LEFT JOIN text ON (revision.rev_id = text.old_id) WHERE page.page_namespace=0 AND page.page_title > (SELECT page_title FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1) ORDER by page.page_title LIMIT 10;



//

USE printwiki;

CREATE TRIGGER printwikiListener BEFORE INSERT ON newmaster
	FOR EACH ROW BEGIN
		INSERT INTO countertable VALUES(1);
	END;


SELECT page.page_id, page.page_title
FROM `page` 
WHERE page.page_namespace=0
AND page.page_title > "Articles_for_deletion/List_of_sovereign_states_in_1982"
LIMIT 10;


// to turn off

DROP TRIGGER printwikiListener;


//TRIGGER

CREATE TRIGGER printwikiListener BEFORE INSERT ON newmaster
	FOR EACH ROW BEGIN
		INSERT INTO countertable VALUES(1);
	END;
