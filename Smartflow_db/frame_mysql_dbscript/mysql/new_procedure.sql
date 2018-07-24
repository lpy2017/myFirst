DROP PROCEDURE IF EXISTS getParentOrg; 
DELIMITER ;;
CREATE PROCEDURE getParentOrg (IN rootid VARCHAR(32)) 
BEGIN 
DECLARE _level_var INT; 
 
DROP TABLE IF EXISTS temp_child_list; 
CREATE TABLE temp_child_list ( 
_id VARCHAR(32), 
_level INT 
); 
SET _level_var = 0; 
  INSERT INTO temp_child_list (_id, _level) VALUE(rootid,_level_var); 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list (_id, _level) 
SELECT 
sj_jg_dm, 
_level_var 
FROM 
dm_jg 
WHERE 
jg_dm = rootid; 
WHILE ROW_COUNT() > 0 
DO 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list SELECT 
a.sj_jg_dm, 
_level_var 
FROM 
dm_jg a, 
temp_child_list b 
WHERE 
a.jg_dm = b._id 
AND b._level = _level_var - 1; 
END WHILE; 
END; 
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS getChildOrg; 
DELIMITER ;;
CREATE PROCEDURE getChildOrg (IN rootid VARCHAR(32)) 
BEGIN 
DECLARE _level_var INT; 
 
DROP TABLE IF EXISTS temp_child_list; 
CREATE TABLE temp_child_list ( 
_id VARCHAR(32), 
_level INT 
); 
SET _level_var = 0; 
  INSERT INTO temp_child_list (_id, _level) VALUE(rootid,_level_var); 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list (_id, _level) 
SELECT 
jg_dm, 
_level_var 
FROM 
dm_jg 
WHERE 
sj_jg_dm = rootid; 
WHILE ROW_COUNT() > 0 
DO 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list SELECT 
a.jg_dm, 
_level_var 
FROM 
dm_jg a, 
temp_child_list b 
WHERE 
a.sj_jg_dm = b._id 
AND b._level = _level_var - 1; 
END WHILE; 
END; 
;;
DELIMITER ;