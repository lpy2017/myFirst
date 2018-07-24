DROP PROCEDURE IF EXISTS getChildGnmk; 
DELIMITER ;;
CREATE PROCEDURE getChildGnmk (IN rootid VARCHAR(32)) 
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
jd_dm, 
_level_var 
FROM 
QX_GNMK_TREE 
WHERE 
fjd_dm = rootid; 
WHILE ROW_COUNT() > 0 
DO 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list SELECT 
a.jd_dm, 
_level_var 
FROM 
QX_GNMK_TREE a, 
temp_child_list b 
WHERE 
a.fjd_dm = b._id 
AND b._level = _level_var - 1; 
END WHILE; 
END; 
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS getParentGnmk; 
DELIMITER ;;
CREATE PROCEDURE getParentGnmk (IN rootid VARCHAR(32)) 
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
fjd_dm, 
_level_var 
FROM 
QX_GNMK_TREE 
WHERE 
jd_dm = rootid; 
WHILE ROW_COUNT() > 0 
DO 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list SELECT 
a.fjd_dm, 
_level_var 
FROM 
QX_GNMK_TREE a, 
temp_child_list b 
WHERE 
a.jd_dm = b._id 
AND b._level = _level_var - 1
AND a.jd_dm!='0';
END WHILE; 
END;
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS getAllParentGnmk; 
DELIMITER ;;
CREATE PROCEDURE getAllParentGnmk (IN rootid VARCHAR(32)) 
BEGIN 
DECLARE _level_var INT; 
 
DROP TABLE IF EXISTS temp_child_list; 
CREATE TABLE temp_child_list ( 
_id VARCHAR(32), 
_level INT 
); 
SET _level_var = 0; 
  INSERT INTO temp_child_list (_id, _level) 
SELECT 
jd_dm, 
_level_var 
FROM 
qx_gnmb_gnmk 
WHERE 
gnmb_dm in
       (select gnmb_dm
          from qx_gw_gnmb
         where gw_dm in
               (select gw_dm from qx_user_gw where userid = rootid))and jd_dm!='0'; 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list (_id, _level) SELECT
a.fjd_dm, 
_level_var 
FROM 
QX_GNMK_TREE a, 
temp_child_list b 
WHERE 
a.jd_dm in (select _id from temp_child_list); 
WHILE ROW_COUNT() > 0 
DO 
SET _level_var = _level_var + 1; 
INSERT INTO temp_child_list SELECT 
a.fjd_dm, 
_level_var 
FROM 
QX_GNMK_TREE a, 
temp_child_list b 
WHERE 
a.jd_dm in (select distinct _id from temp_child_list)
AND b._level = _level_var - 1
AND a.jd_dm!='0';
END WHILE; 
END;
;;
DELIMITER ;