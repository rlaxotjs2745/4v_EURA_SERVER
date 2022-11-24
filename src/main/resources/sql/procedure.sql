CREATE DEFINER=`eura_user`@`localhost` PROCEDURE `eura_db`.`prc_meet_invite`(
	IN `_idx_meeting` INT,
	IN `_user_email` VARCHAR(400)
)
BEGIN
	SET @IDXUSER = 0;
	IF EXISTS (SELECT IDX_USER FROM TB_USER WHERE USER_ID = _user_email) THEN
		SELECT IDX_USER INTO @IDXUSER FROM TB_USER WHERE USER_ID = _user_email;
	END IF;

	IF NOT EXISTS(SELECT IDX_MEETING_USER_JOIN FROM TB_MEETING_USER_JOIN WHERE USER_EMAIL=_user_email AND IDX_MEETING=_idx_meeting) THEN
		INSERT INTO TB_MEETING_USER_JOIN (
            IDX_USER, IDX_MEETING, USER_EMAIL
        ) VALUES (
            @IDXUSER, _idx_meeting, _user_email
        );
    END IF;
END;$$
DELIMITER ;

CREATE DEFINER=`eura_user`@`localhost` PROCEDURE `eura_db`.`prc_meet_chkRoomDupTime`(
	IN `_idx_meeting` INT,
	IN `_idx_user` INT
)
BEGIN
	SET @CNT = 0;
	SELECT MT_START_DT, MT_END_DT INTO @MTSTARTDT, @MTENDDT FROM TB_MEETING WHERE IDX_MEETING=_idx_meeting AND IDX_USER=_idx_user;
	SELECT COUNT(1) INTO @CNT FROM TB_MEETING WHERE IDX_USER=_idx_user AND DELETE_STAT=0 AND IDX_MEETING!=_idx_meeting
		AND ((MT_START_DT<=@MTSTARTDT AND MT_END_DT>=@MTSTARTDT) OR (MT_START_DT<=@MTENDDT AND MT_END_DT>=@MTENDDT));
	SELECT @CNT AS chkcnt;
END;$$
DELIMITER ;

CREATE DEFINER=`eura_user`@`%` PROCEDURE `eura_db`.`prc_meet_chkRoomDupDate`(
	IN `_idx_user` INT,
	IN `_mt_start_dt` VARCHAR(20),
	IN `_mt_end_dt` VARCHAR(20),
	IN `_idx_meeting` INT
)
BEGIN
	SET @CNT = 0;
	SELECT MT_START_DT, MT_END_DT INTO @MTSTARTDT, @MTENDDT FROM TB_MEETING WHERE IDX_MEETING=_idx_meeting;
	IF @MTSTARTDT != _mt_start_dt AND @MTENDDT != _mt_end_dt THEN
		SELECT COUNT(1) INTO @CNT FROM TB_MEETING WHERE IDX_USER=_idx_user AND DELETE_STAT=0
			AND ((MT_START_DT<=_mt_start_dt AND MT_END_DT>=_mt_start_dt) OR (MT_START_DT<=_mt_end_dt AND MT_END_DT>=_mt_end_dt));
	END IF;
	SELECT @CNT AS chkcnt;
END;$$
DELIMITER ;