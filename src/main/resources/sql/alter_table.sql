ALTER TABLE `TB_MEETING` CHANGE `IS_LIVE` `IS_LIVE` TINYINT(1)  NULL  DEFAULT 0  COMMENT '미팅 시작 여부 - 0:아니오, 1:예';
ALTER TABLE `TB_MEETING` CHANGE `MT_STATUS` `MT_STATUS` TINYINT(1)  NULL  DEFAULT 0  COMMENT '미팅룸 상태 - 0:비공개, 1:공개, 2:취소, 3:삭제';