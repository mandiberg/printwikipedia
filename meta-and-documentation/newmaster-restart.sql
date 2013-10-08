//FIRST FIND LAST ENTRY

USE printwiki;
SELECT * FROM printwiki.newmaster ORDER BY pkey desc LIMIT 1;


//THEN INSERT STRING VALUE OF page_title INTO QUERY BELOW
//SEEMS TO ONLY LIKE BEING RUN ON ONE LINE IN MYSQL VIA COMMAND LINE

USE printwiki;
INSERT INTO newmaster (page_id, page_title, rev_user_text, rev_comment, old_text) SELECT page.page_id, page.page_title, revision.rev_user_text, revision.rev_comment, text.old_text FROM `page`  LEFT JOIN revision ON (page.page_id = revision.rev_page)  LEFT JOIN text ON (revision.rev_id = text.old_id) WHERE page.page_namespace=0 AND page.page_title > "Replace_Me_with_last_entry" ORDER by page.page_title;
