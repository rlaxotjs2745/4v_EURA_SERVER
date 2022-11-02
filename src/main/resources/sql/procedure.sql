CREATE DEFINER=`eura_user`@`localhost` PROCEDURE `prc_meet_invite`(
	IN `_idx_meeting` INT,
	IN `_user_email` VARCHAR(400)
)
BEGIN
	SET @IDXUSER = 0;
	IF EXISTS (SELECT IDX_USER FROM TB_USER WHERE USER_ID = _user_email) THEN
		SELECT IDX_USER INTO @IDXUSER FROM TB_USER WHERE USER_ID = _user_email;
		INSERT INTO TB_MEETING_USER_JOIN (
            IDX_USER, IDX_MEETING, USER_EMAIL
        ) VALUES (
            @IDXUSER, _idx_meeting, _user_email
        );
	ELSE
		INSERT INTO TB_MEETING_USER_JOIN (
            IDX_USER, IDX_MEETING, USER_EMAIL
        ) VALUES (
            0, _idx_meeting, _user_email
        );
	END IF;
END;;

CREATE DEFINER=`eura_user`@`localhost` PROCEDURE `prc_meet_chkRoomDupTime`(
	IN `_idx_meeting` INT,
	IN `_idx_user` INT
)
BEGIN
	SET @CNT = 0;
	SELECT MT_START_DT, MT_END_DT INTO @MTSTARTDT, @MTENDDT FROM TB_MEETING WHERE IDX_MEETING=_idx_meeting AND IDX_USER=_idx_user;
	SELECT COUNT(1) INTO @CNT FROM TB_MEETING WHERE IDX_USER=_idx_user AND DELETE_STAT=0
		AND ((MT_START_DT<=@MTSTARTDT AND MT_END_DT>=@MTSTARTDT) OR (MT_START_DT<=@MTENDDT AND MT_END_DT>=@MTENDDT));
	SELECT @CNT AS chkcnt;
END;;

CREATE DEFINER=`eura_user`@`localhost` PROCEDURE `prc_meet_chkRoomDupDate`(
	IN `_idx_user` INT,
	IN `_mt_start_dt` VARCHAR(20),
	IN `_mt_end_dt` VARCHAR(20)
)
BEGIN
	SET @CNT = 0;
	SELECT COUNT(1) INTO @CNT FROM TB_MEETING WHERE IDX_USER=_idx_user AND DELETE_STAT=0
			AND ((MT_START_DT<=_mt_start_dt AND MT_END_DT>=_mt_start_dt) OR (MT_START_DT<=_mt_end_dt AND MT_END_DT>=_mt_end_dt));
	SELECT @CNT AS chkcnt;
END;;