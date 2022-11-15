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

    private final MeetingService meetingService;

    @Value("${file.upload-dir}")
    private String filepath;

    @Value("${domain}")
    private String domain;

    @Value("${w3domain}")
    private String w3domain;

    // public static final String REGEXPW = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";
    
    /**
     * 로그인 확인
     * 
     * @param request
     * @param response
     * @param user_id
     * @return
     */
    @PostMapping("/index")
    public ResultVO home(HttpServletRequest request, HttpServletResponse response, @CookieValue(name = "user_id", required = false) String user_id) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("로그인이 필요합니다.");

        UserVO findUserVo = null;
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if (flashMap != null) {
            findUserVo = (UserVO) flashMap.get("userVo");
        }
        if (user_id != null) {
            findUserVo = userService.findUserById(user_id);
        }

        if (findUserVo != null) {// 로그인 필요
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("로그인이 되어있습니다.");
            return resultVO;
        }

        return resultVO;
    }

    /**
     * 회원가입
     * 
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

                // 참여자에서 본인이 있다면 참여자 정보와 연동
                userVo.setIdx_user(idx_user);
                meetMapper.chkMeetInviteChain(userVo);

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
     * 
     * @param email
     * @param authKey
     * @return
     */
    @GetMapping("/signUpConfirm")
    public ResultVO signUpConfirm(@RequestParam("email") String email, @RequestParam("authKey") String authKey) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("메일 인증에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if (email != null && authKey != null) {
            UserVO userVO = new UserVO();
            userVO.setAuthKey(authKey);
            userVO.setUser_id(email);
            userService.updateAuthStatus(userVO);

            UserVO findUser = userService.findUserById(email);
            if (findUser.getUser_status() == 1) {
                resultVO.setResult_str("메일 인증에 성공했습니다");
                resultVO.setResult_code(CONSTANT.success);
            }
        }
        return resultVO;
    }

    /**
     * 로그인
     * 
     * @param response
     * @param userVo
     * @return
     */
    @PostMapping(value = "/api_post_login")
    public ResultVO api_post_login(HttpServletRequest req, HttpServletResponse response, @RequestBody UserVO userVo) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail + "01");
        resultVO.setResult_str("로그인 정보를 다시 확인해주세요.");

        if (userVo.getUser_id() != null || userVo.getUser_pwd() != null) {
            UserVO findUser = userService.findUserById(userVo.getUser_id());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if (findUser != null) {
                if (findUser.getUser_status() == 1) {
                    if (findUser.getTemp_pw_y() == 0) {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getUser_pwd())) {
                            resultVO.setResult_code(CONSTANT.success + "01");
                            resultVO.setResult_str("로그인 되었습니다.");
                            String _cStr = "user_id=" + findUser.getUser_id() + "; domain=.eura.site; Path=/;";
                            response.addHeader("Set-Cookie", _cStr);
                            findUser.setRemote_ip(getClientIP(req));
                            userMapper.putUserLoginHistory(findUser);
                        }
                    } else if (findUser.getTemp_pw_y() == 1) {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getTemp_pw())) {
                            resultVO.setResult_code(CONSTANT.success + "02");
                            resultVO.setResult_str("임시 비밀번호로 로그인 되었습니다.");
                            ResponseCookie cookie = ResponseCookie.from("user_id", findUser.getUser_id())
                                    // .domain("*")
                                    .sameSite("")
                                    .secure(false)
                                    .httpOnly(true)
                                    // .path("/")
                                    .build();
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
     * 
     * @param req
     * @param session
     * @param userVo
     * @return
     */
    @PostMapping("/join_default")
    public ResultVO join_default(HttpServletRequest req, HttpSession session, @RequestBody UserVO userVo) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        if (userVo.getUser_name() != null &&
                userVo.getUser_id() != null &&
                userVo.getUser_pwd() != null) {

            String regexId = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            Matcher matcherId = Pattern.compile(regexId).matcher(userVo.getUser_id());
            if (!matcherId.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("아이디는 이메일형식이어야 합니다");
                return resultVO;
            }

            Matcher matcherPw = Pattern.compile(CONSTANT.REGEXPW).matcher(userVo.getUser_pwd());
            if (userVo.getUser_pwd().length() < 10 || !matcherPw.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("비밀번호는 영문+숫자+특수문자 10자 이상으로 설정해주세요.");
                return resultVO;
            }

            UserVO findUser = userService.findUserById(userVo.getUser_id());
            if (findUser != null) {
                resultVO.setResult_str("이미 사용중인 아이디입니다.");
                return resultVO;
            }
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("사용 가능한 아이디, 비밀번호 입니다.");
        }
        return resultVO;
    }

    /**
     * 프로필 사진 변경
     * 
     * @param file
     * @param session
     * @param user_id
     * @return
     */
    @PostMapping("/modify_profile")
    public ResultVO modify_profile(@RequestParam("file") MultipartFile file, HttpSession session, @CookieValue("user_id") String user_id) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("사진 저장에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if (file != null) {
            UserVO findUserVO = userService.findUserById(user_id);

            String _path = "/profile/" + findUserVO.getIdx_user() + "/";
            MeetingVO _frs = meetingService.saveFile(file, _path);
            ProfileInfoVO profileInfo = new ProfileInfoVO();
            profileInfo.setIdx_user(findUserVO.getIdx_user());
            profileInfo.setFile_name(_frs.getFile_name());
            profileInfo.setFile_path(_path);

            if (findUserVO.getProfile_y() == 0) {
                profileFileService.uploadProfileFile(profileInfo);
                findUserVO.setProfile_y(1);
                userService.addNewProfile(findUserVO);

                resultVO.setResult_str("프로필이 등록되었습니다.");
                resultVO.setResult_code(CONSTANT.success + "01");

            } else if (findUserVO.getProfile_y() == 1) {
                profileFileService.updateProfileFile(profileInfo);

                resultVO.setResult_str("프로필이 수정되었습니다.");
                resultVO.setResult_code(CONSTANT.success + "02");
            }
        }

        return resultVO;
    }

    /**
     * 회원정보 변경
     * 
     * @param user_id
     * @param userVO
     * @return ResultVO
     * @throws Exception
     */
    @PostMapping("/modify_myinfo")
    public ResultVO modify_myinfo(@CookieValue("user_id") String user_id, @RequestBody UserVO userVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("사진 저장에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        userVO.setUser_id(user_id);
        if (userVO.getUser_pwd() != null) {
            if (userVO.getUser_pwd_origin() == null) {
                resultVO.setResult_str("기존 비밀번호를 입력해주세요.");
                return resultVO;
            }
            Matcher matcherPw = Pattern.compile(CONSTANT.REGEXPW).matcher(userVO.getUser_pwd());
            if (userVO.getUser_pwd().length() < 10 || !matcherPw.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("비밀번호는 영문+숫자+특수문자 10자 이상으로 설정해주세요.");
                return resultVO;
            }

            UserVO rs = userService.findUserById(user_id);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (passwordEncoder.matches(userVO.getUser_pwd_origin(), rs.getUser_pwd())) {
                String change_pwd = passwordEncoder.encode(userVO.getUser_pwd());
                userVO.setUser_pwd(change_pwd);
                userMapper.updateUserInfo(userVO);
                resultVO.setResult_str("프로필이 수정되었습니다.");
                resultVO.setResult_code(CONSTANT.success);
            } else {
                resultVO.setResult_str("프로필을 수정 할 수 없습니다.");
            }
        } else {
            userMapper.updateUserInfo(userVO);
            resultVO.setResult_str("프로필이 수정되었습니다.");
            resultVO.setResult_code(CONSTANT.success);
        }

        return resultVO;
    }

    /**
     * 임시 비밀번호 발급
     * 
     * @param userVO
     * @return
     * @throws Exception
     */
    @PostMapping("/pw_find_mail")
    public ResultVO pw_find_mail(@RequestBody UserVO userVO) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("유저 정보를 다시 확인해주세요.");

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
            resultVO.setResult_str("임시 비밀번호가 발급되었습니다.");
        }
        return resultVO;
    }

    /**
     * 회원가입 인증 메일 다시 받기
     * 
     * @param userVo
     * @return
     * @throws Exception
     */
    @PostMapping("/remail_join")
    public ResultVO remail_join(@RequestBody UserVO userVo) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("메일 발송에 실패했습니다.");

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
            resultVO.setResult_str("메일이 재발송 되었습니다.");
        }

        return resultVO;
    }

    /**
     * 내 정보 가져오기
     * 
     * @param userVo
     * @return ResultVO
     * @throws Exception
     */
    @PostMapping("/myinfo")
    public ResultVO getMyInfo(HttpServletRequest req) throws Exception {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("NO INFO");

        UserVO rs = userService.findUserById(getUserID(req));
        if (rs != null) {
            Map<String, Object> _rs = new HashMap<String, Object>();
            _rs.put("idx_user", rs.getIdx_user());
            _rs.put("user_name", rs.getUser_name());
            _rs.put("user_phone", rs.getUser_phone());
            _rs.put("eq_type01", rs.getEq_type01());
            _rs.put("eq_type02", rs.getEq_type02());
            
            String _upic = "";
            if (!rs.getFile_name().isEmpty() && rs.getFile_name() != null) {
                _upic = domain + "/pic?fnm=" + rs.getFile_path() + rs.getFile_name();
            }
            _rs.put("user_pic", _upic);
            resultVO.setData(_rs);
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("OK");
        }
        return resultVO;
    }
}
