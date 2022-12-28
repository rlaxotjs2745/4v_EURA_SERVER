package com.eura.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.eura.web.base.BaseController;
import com.eura.web.model.MeetMapper;
import com.eura.web.model.UserMapper;
import com.eura.web.model.DTO.*;
import com.eura.web.service.*;
import com.eura.web.util.CONSTANT;

import lombok.RequiredArgsConstructor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@RestController
public class RestAPIController extends BaseController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final MeetMapper meetMapper;

    @Resource(name = "profileFileService")
    public ProfileFileService profileFileService;

    @Resource(name = "mailService")
    public MailService mailService;

    private final S3Service s3Service;

    private final MeetingService meetingService;

    @Value("${srvinfo}")
    public String srvinfo;

    @Value("${file.upload-dir}")
    private String filepath;

    @Value("${domain}")
    private String domain;

    @Value("${w3domain}")
    private String w3domain;

    @Value("${filedomain}")
    private String filedomain;
    
    /**
     * 로그인 확인
     * @param request
     * @param response
     * @param user_id
     * @return
     * @throws Exception
     */
    @PostMapping("/index")
    public ResultVO home(HttpServletRequest request, HttpServletResponse response, HttpServletRequest req) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("로그인 후 이용해주세요.");

        UserVO findUserVo = null;
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            findUserVo = (UserVO) flashMap.get("userVo");
        }
        if (getUserID(req) != null) {
            findUserVo = userService.findUserById(getUserID(req));
        }

        if (findUserVo != null) {// 로그인 필요
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("로그인 완료");
            return resultVO;
        }

        return resultVO;
    }

    /**
     * 회원가입
     * @param userVo
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping("/join_mail")
    public ResultVO join_mail(UserVO userVo, @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("입력 정보를 다시 확인해주세요.");

        if (userVo != null && userVo.getUser_id() != null && userVo.getUser_name() != null && userVo.getUser_pwd() != null) {
            UserVO findUser = userService.findUserById(userVo.getUser_id());
            if (findUser != null) {
                resultVO.setResult_str("이미 사용중인 아이디입니다.");
                return resultVO;
            } else {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(userVo.getUser_pwd());
                userVo.setUser_pwd(hashedPassword);
                userVo.setProfile_y(0);
                userVo.setTemp_pw_y(0);
                Integer idx_user = userService.join(userVo);
                if (file != null) {
                    UserVO findUserVO = userService.getUserProfileFile(idx_user); // 프로필 사진 정보

                    String _path = "/profile/" + idx_user + "/";
                    MeetingVO _frs = meetingService.saveFile(file, _path);
                    ProfileInfoVO profileInfo = new ProfileInfoVO();
                    profileInfo.setIdx_user(idx_user);
                    profileInfo.setFile_name(_frs.getFile_name());
                    profileInfo.setFile_path(_path);

                    if (findUserVO.getProfile_y() == 0) {
                        profileFileService.uploadProfileFile(profileInfo);
                        findUserVO.setProfile_y(1);
                        userService.addNewProfile(findUserVO); // 프로필 사진 DB 저장
                    } else if (findUserVO.getProfile_y() == 1) {
                        profileFileService.updateProfileFile(profileInfo);
                    }
                }

                userVo.setIdx_user(idx_user);

                // 메일 발송
                String authKey = mailService.generateAuthNo(6);
                String title = "[EURA] 회원가입 인증 메일";

                MailVO mailSendVO = new MailVO();
                mailSendVO.setIdx_user(idx_user);
                mailSendVO.setReceiver(userVo.getUser_id());
                mailSendVO.setTitle(title);
                mailSendVO.setContent(w3domain + "/login?confirm=true&email=" + userVo.getUser_id() + "&authKey=" + authKey);
                mailSendVO.setMail_type(0);

                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Calendar time = Calendar.getInstance();
                String now_date = fm.format(time.getTime());
                mailSendVO.setSendTime(now_date);

                mailService.insertJoinEmail(mailSendVO); // 메일 발송 기록

                userVo.setAuthKey(authKey);
                userService.updateAuthKey(userVo); // 인증키 저장

                MeetingVO meetingVO = new MeetingVO();
                meetingVO.setAuthKey(authKey);
                meetingVO.setTitle(title);
                meetingVO.setUser_email(userVo.getUser_id());
                meetingVO.setUser_name(userVo.getUser_name());
                meetingService.sendMail(meetingVO, null, 5);

                resultVO.setResult_code(CONSTANT.success);
                resultVO.setResult_str("가입에 성공했습니다");
            }
        }
        return resultVO;
    }

    /**
     * 회원가입 인증
     * @param email
     * @param authKey
     * @return
     * @throws Exception
     */
    @GetMapping("/signUpConfirm")
    public ResultVO signUpConfirm(@RequestParam("email") String email, @RequestParam("authKey") String authKey) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("메일 인증에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if (StringUtils.isNotEmpty(email) && StringUtils.isNotEmpty(authKey)) {
            UserVO userVO = new UserVO();
            userVO.setAuthKey(authKey);
            userVO.setUser_id(email);
            userService.updateAuthStatus(userVO);

            UserVO findUser = userService.findUserById(email);
            if (findUser.getUser_status() == 1) {
                // 참여자에서 본인이 있다면 참여자 정보와 연동
                meetMapper.chkMeetInviteChain(userVO);

                resultVO.setResult_str("메일 인증에 성공했습니다");
                resultVO.setResult_code(CONSTANT.success);
            }
        }
        return resultVO;
    }

    /**
     * 로그인
     * @param response
     * @param userVo
     * @return
     */
    @PostMapping(value = "/api_post_login")
    public ResultVO api_post_login(HttpServletRequest req, HttpServletResponse response, @RequestBody UserVO userVo) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail + "01");
        resultVO.setResult_str("로그인 정보를 다시 확인해주세요.");

        if (StringUtils.isNotEmpty(userVo.getUser_id()) || StringUtils.isNotEmpty(userVo.getUser_pwd())) {
            UserVO findUser = userService.findUserById(userVo.getUser_id());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (findUser != null) {
                if (findUser.getUser_status() == 1) {
                    if (findUser.getTemp_pw_y() == 0) {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getUser_pwd())) {
                            resultVO.setResult_code(CONSTANT.success + "01");
                            resultVO.setResult_str("로그인 되었습니다.");
                            ResponseCookie cookie = null;
                            if(userVo.getAutoLogin()==false){
                                cookie = ResponseCookie.from("user_id", findUser.getUser_id())
                                    .domain("eura.site")
                                    .secure(true)
                                    .path("/")
                                    .build();
                            }else{
                                cookie = ResponseCookie.from("user_id", findUser.getUser_id())
                                    .domain("eura.site")
                                    .secure(true)
                                    .path("/")
                                    .maxAge(60 * 60 * 24 * 30)  // 30일
                                    .build();
                            }
                            response.addHeader("Set-Cookie", cookie.toString());
                            findUser.setRemote_ip(getClientIP(req));
                            userMapper.putUserLoginHistory(findUser);
                        }
                    } else {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getTemp_pw())) {
                            resultVO.setResult_code(CONSTANT.success + "02");
                            resultVO.setResult_str("임시 비밀번호로 로그인 되었습니다.");
                            ResponseCookie cookie = null;
                            if(userVo.getAutoLogin()==false){
                                cookie = ResponseCookie.from("user_id", findUser.getUser_id())
                                    .domain("eura.site")
                                    .secure(true)
                                    .path("/")
                                    .build();
                            }else{
                                cookie = ResponseCookie.from("user_id", findUser.getUser_id())
                                    .domain("eura.site")
                                    .secure(true)
                                    .path("/")
                                    .maxAge(60 * 60 * 24 * 30)  // 30일
                                    .build();
                            }
                            response.addHeader("Set-Cookie", cookie.toString());
                            findUser.setRemote_ip(getClientIP(req));
                            userMapper.putUserLoginHistory(findUser);
                        }
                    }
                } else {
                    resultVO.setResult_code(CONSTANT.fail + "02");
                    resultVO.setResult_str("이메일 인증을 진행해주세요");
                }
            }
        }
        return resultVO;
    }

    /**
     * 회원 가입 검증
     * :: 안씀
     * @param req
     * @param session
     * @param userVo
     * @return
     */
    @PostMapping("/join_default")
    public ResultVO join_default(HttpServletRequest req, HttpSession session, @RequestBody UserVO userVo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("아이디를 다시 확인해 주세요.");

        /*
        if (StringUtils.isNotEmpty(userVo.getUser_name()) &&
            StringUtils.isNotEmpty(userVo.getUser_id()) &&
            StringUtils.isNotEmpty(userVo.getUser_pwd())) {
         */

        if (StringUtils.isNotEmpty(userVo.getUser_id())) {

            /*
            String regexId = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            Matcher matcherId = Pattern.compile(regexId).matcher(userVo.getUser_id());
            if (!matcherId.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("아이디는 이메일 형식이어야 합니다.");
                return resultVO;
            }

            Matcher matcherPw = Pattern.compile(CONSTANT.REGEXPW).matcher(userVo.getUser_pwd());
            if (userVo.getUser_pwd().length() < 10 || !matcherPw.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("비밀번호는 영문+숫자+특수문자 10자 이상으로 설정해주세요.");
                return resultVO;
            }
             */

            UserVO findUser = userService.findUserById(userVo.getUser_id());
            if (findUser != null) {
                resultVO.setResult_str("중복되는 아이디 입니다.");
                return resultVO;
            }
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("사용 가능한 아이디 입니다.");
        }
        return resultVO;
    }

    /**
     * 프로필 사진 변경
     * @param file
     * @param session
     * @param user_id
     * @return
     */
    @PostMapping("/reset_profile")
    public ResultVO reset_profile(HttpServletRequest req, HttpSession session) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("프로필 사진 초기화에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        UserVO findUserVO = getChkUserLogin(req);
        if(findUserVO==null){
            resultVO.setResult_str("로그인 후 이용해주세요.");
            return resultVO;
        }

        Integer rs = userMapper.resetProfile(findUserVO);
        if(rs == 1){
            if(srvinfo=="prod"){
                s3Service.delete("upload"+findUserVO.getFile_path() + findUserVO.getFile_name());
            }
            findUserVO.setProfile_y(0);
            userService.addNewProfile(findUserVO);
            resultVO.setResult_str("프로필 사진을 초기화했습니다.");
            resultVO.setResult_code(CONSTANT.success);
        }

        return resultVO;
    }

    @PostMapping("/modify_profile")
    public ResultVO modify_profile(HttpServletRequest req, @RequestParam("file") MultipartFile file, HttpSession session) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("프로필 사진 등록을 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if (file != null) {
            UserVO findUserVO = getChkUserLogin(req);
            if(findUserVO==null){
                resultVO.setResult_str("로그인 후 이용해주세요.");
                return resultVO;
            }

            String _path = "/profile/" + findUserVO.getIdx_user() + "/";
            if(srvinfo=="prod"){
                s3Service.delete("upload"+findUserVO.getFile_path() + findUserVO.getFile_name());
            }
            MeetingVO _frs = meetingService.saveFile(file, _path);
            ProfileInfoVO profileInfo = new ProfileInfoVO();
            profileInfo.setIdx_user(findUserVO.getIdx_user());
            profileInfo.setFile_name(_frs.getFile_name());
            profileInfo.setFile_path(_path);

            if (findUserVO.getProfile_y() == 0) {
                profileFileService.uploadProfileFile(profileInfo);
                findUserVO.setProfile_y(1);
                userService.addNewProfile(findUserVO);

                resultVO.setResult_str("프로필 사진을 등록했습니다.");
                resultVO.setResult_code(CONSTANT.success);

            } else if (findUserVO.getProfile_y() == 1) {
                if(findUserVO.getFile_path() == null){
                    profileFileService.uploadProfileFile(profileInfo);
                }else{
                    profileFileService.updateProfileFile(profileInfo);

                }

                resultVO.setResult_str("프로필 사진을 등록했습니다.");
                resultVO.setResult_code(CONSTANT.success);
            }
        }

        return resultVO;
    }

    /**
     * 회원정보 변경
     * @param user_id
     * @param userVO
     * @return ResultVO
     * @throws Exception
     */
    @PostMapping("/modify_myinfo")
    public ResultVO modify_myinfo(HttpServletRequest req, @RequestBody UserVO userVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("프로필 수정을 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        UserVO rs = getChkUserLogin(req);
        if(rs==null){
            resultVO.setResult_str("로그인 후 이용해주세요.");
            return resultVO;
        }

        userVO.setUser_id(getUserID(req));
        if (StringUtils.isNotEmpty(userVO.getUser_pwd())) {
            if (StringUtils.isEmpty(userVO.getUser_pwd_origin())) {
                resultVO.setResult_str("기존 비밀번호를 입력해주세요.");
                return resultVO;
            }
            Matcher matcherPw = Pattern.compile(CONSTANT.REGEXPW).matcher(userVO.getUser_pwd());
            if (userVO.getUser_pwd().length() < 10) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("10자 이상의 비밀번호만 사용할 수 있습니다.");
                return resultVO;
            }
            if (!matcherPw.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("영어, 숫자, 특수문자로 조합된 비밀번호만 사용가능합니다.");
                return resultVO;
            }

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String _pwd = "";
            if(rs.getTemp_pw_y()==1){
                _pwd = rs.getTemp_pw();
            }else{
                _pwd = rs.getUser_pwd();
            }
            if (passwordEncoder.matches(userVO.getUser_pwd_origin(), _pwd)) {
                String change_pwd = passwordEncoder.encode(userVO.getUser_pwd());
                userVO.setUser_pwd(change_pwd);
                userMapper.updateUserInfo(userVO);
                resultVO.setResult_str("프로필을 수정했습니다.");
                resultVO.setResult_code(CONSTANT.success);
            } else {
                resultVO.setResult_str("프로필을 수정할 수 없습니다.");
            }
        } else {
            userMapper.updateUserInfo(userVO);
            resultVO.setResult_str("새로운 비밀번호를 입력해주세요.");
            resultVO.setResult_code(CONSTANT.success);
        }

        return resultVO;
    }

    /**
     * 임시 비밀번호 발급
     * @param userVO
     * @return
     * @throws Exception
     */
    @PostMapping("/pw_find_mail")
    public ResultVO pw_find_mail(@RequestBody UserVO userVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("회원 정보를 다시 확인해주세요.");

        UserVO finduser = userService.findUserById(userVO.getUser_id());

        if (finduser != null) {
            String tempPW = mailService.getRamdomPassword();
            String title = "[EURA] 임시 비밀번호 발급 메일";

            MailVO mailSendVO = new MailVO();
            mailSendVO.setIdx_user(finduser.getIdx_user());
            mailSendVO.setReceiver(finduser.getUser_id());
            mailSendVO.setTitle(title);
            mailSendVO.setContent("임시 비밀번호 : " + tempPW);
            mailSendVO.setMail_type(1);

            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar time = Calendar.getInstance();
            String now_date = fm.format(time.getTime());
            mailSendVO.setSendTime(now_date);

            mailService.insertJoinEmail(mailSendVO);

            finduser.setTemp_pw_y(1);
            finduser.setTemp_pw_issue_dt(now_date);

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String hashedPassword = passwordEncoder.encode(tempPW);
            finduser.setTemp_pw(hashedPassword);

            userService.updateUserPw(finduser);

            MeetingVO meetingVO = new MeetingVO();
            meetingVO.setUser_email(finduser.getUser_id());
            meetingVO.setUser_name(finduser.getUser_name());
            meetingVO.setTemp_pw(tempPW);
            meetingVO.setTitle(title);
            meetingService.sendMail(meetingVO, null, 6);

            Map<String, Object> map = new HashMap<>();
            map.put("tempPW", tempPW);
            resultVO.setData(map);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("임시 비밀번호를 발급했습니다.");
        }
        return resultVO;
    }

    /**
     * 회원가입 인증 메일 다시 받기
     * @param userVo
     * @return
     * @throws Exception
     */
    @PostMapping("/remail_join")
    public ResultVO remail_join(@RequestBody UserVO userVo) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("인증 메일 발송을 실패했습니다.");

        UserVO findUser = userService.findUserById(userVo.getUser_id());

        if (findUser != null) {
            String authKey = mailService.generateAuthNo(6);
            String title = "[EURA] 회원가입 인증 메일";

            MailVO mailSendVO = new MailVO();
            mailSendVO.setIdx_user(findUser.getIdx_user());
            mailSendVO.setReceiver(findUser.getUser_id());
            mailSendVO.setTitle(title);
            mailSendVO.setContent(w3domain + "/login?confirm=true&email=" + findUser.getUser_id() + "&authKey=" + authKey);
            mailSendVO.setMail_type(0);

            SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Calendar time = Calendar.getInstance();
            String now_date = fm.format(time.getTime());
            mailSendVO.setSendTime(now_date);

            mailService.insertJoinEmail(mailSendVO);

            findUser.setAuthKey(authKey);
            findUser.setUser_status(0);
            userService.updateAuthKey(findUser);    // 인증키 저장

            MeetingVO meetingVO = new MeetingVO();
            meetingVO.setAuthKey(authKey);
            meetingVO.setTitle(title);
            meetingVO.setUser_email(findUser.getUser_id());
            meetingVO.setUser_name(findUser.getUser_name());
            meetingService.sendMail(meetingVO, null, 5);    // 메일 발송

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("인증 메일을 재발송했습니다.");
        }

        return resultVO;
    }

    /**
     * 내 정보 가져오기
     * @param userVo
     * @return ResultVO
     * @throws Exception
     */
    @PostMapping("/myinfo")
    public ResultVO getMyInfo(HttpServletRequest req) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("NO INFO");

        UserVO rs = getChkUserLogin(req);
        if(rs==null){
            resultVO.setResult_str("로그인 후 이용해주세요.");
            return resultVO;
        }
        if (rs != null) {
            Map<String, Object> _rs = new HashMap<String, Object>();
            _rs.put("idx_user", rs.getIdx_user());
            _rs.put("user_name", rs.getUser_name());
            _rs.put("user_phone", rs.getUser_phone());
            _rs.put("eq_type01", rs.getEq_type01());
            _rs.put("eq_type02", rs.getEq_type02());
            
            String _upic = "";
            if (StringUtils.isNotEmpty(rs.getFile_name())) {
                if(srvinfo.equals("dev")){
                    _upic = filedomain + "/pic?fnm=" + rs.getFile_path() + rs.getFile_name();
                }else{
                    _upic = filedomain + rs.getFile_path() + rs.getFile_name();
                }
            }
            _rs.put("user_pic", _upic);
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("OK");
        }
        return resultVO;
    }
}
