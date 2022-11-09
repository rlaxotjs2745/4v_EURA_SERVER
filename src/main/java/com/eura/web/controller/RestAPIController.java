package com.eura.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.eura.web.model.DTO.*;
import com.eura.web.service.*;
import com.eura.web.util.CONSTANT;

import com.eura.web.util.MailSender;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RequiredArgsConstructor
@RestController
public class RestAPIController {
    private final UserService userService;

    @Resource(name = "fileService")
    public FileService fileService;

    @Resource(name = "profileFileService")
    public ProfileFileService profileFileService;

    @Resource(name = "mailService")
    public MailService mailService;

    @Autowired
    private MailSender mailSender;

    @Value("${file.upload-dir}")
    private String filepath;

    @Value("${domain}")
    private String domain;

    public static final String REGEXPW = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$";

    /**
     * 로그인 확인
     * @param request
     * @param response
     * @param user_id
     * @return
     */
    @PostMapping("/index")
    public ResultVO home(HttpServletRequest request, HttpServletResponse response, @CookieValue(name = "user_id", required = false) String user_id){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("유저 정보가 존재합니다.");

        UserVO findUserVo = null;
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if(flashMap!=null) {
            findUserVo =(UserVO)flashMap.get("userVo");
        }
        if(user_id!=null) {
            findUserVo =userService.findUserById(user_id);
        }

        if (findUserVo == null) {//로그인 필요
            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("로그인이 필요합니다");
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

        if(userVo!=null && userVo.getUser_id() !=null && userVo.getUser_name() != null && userVo.getUser_pwd()!=null){
            UserVO findUser = userService.findUserById(userVo.getUser_id());
            if(findUser != null){
                resultVO.setResult_str("이미 사용중인 아이디입니다.");
                return resultVO;
            } else {
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String hashedPassword = passwordEncoder.encode(userVo.getUser_pwd());
                userVo.setUser_pwd(hashedPassword);
                //insert
                userVo.setProfile_y(0);
                userVo.setTemp_pw_y(0);
                long idx_user = userService.join(userVo);


                System.out.println("idx_user = " + idx_user);
                if(file != null){

                    UserVO findUserVO = userService.getUserProfileFile(idx_user);

                    String fileName = fileService.storeFile(file);
                    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/fileStore/")
                            .path(fileName)
                            .toUriString();
                    FileUploadResponseVO fileResponse = new FileUploadResponseVO(fileName, fileDownloadUri, file.getContentType(), file.getSize());

                    ProfileInfoVO profileInfo= new ProfileInfoVO();
                    profileInfo.setIdx_user(idx_user);
                    profileInfo.setFile_name(fileResponse.getFileName());
                    profileInfo.setFile_path(fileResponse.getFileDownloadUri());

                    SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                    String now_date = fm.format(Calendar.getInstance().getTime());
                    profileInfo.setReg_dt(now_date);
                    if(findUserVO.getProfile_y() == 0){
                        profileFileService.uploadProfileFile(profileInfo);
                        findUserVO.setProfile_y(1);
                        userService.addNewProfile(findUserVO);
                    } else if(findUserVO.getProfile_y() == 1) {
                        profileFileService.updateProfileFile(profileInfo);
                    }
                }
                //메일 발송
                String authKey = mailService.generateAuthNo(6);
                String Message = "<h1>[이메일 인증]</h1><br><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
                        + "<a href=\""+ domain +"/signUpConfirm?email=" + userVo.getUser_id()
                        + "&authKey=" + authKey + "\" target=\"_blenk\">이메일 인증 확인</a>";
                String to = userVo.getUser_id();
                String title = "[EURA] 회원가입 인증 메일";

                MailVO mailSendVO = new MailVO();
                mailSendVO.setIdx_user(idx_user);
                mailSendVO.setReceiver(userVo.getUser_id());
                mailSendVO.setTitle(title);
                mailSendVO.setContent(Message);
                mailSendVO.setMail_type(0);

                SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                Calendar time = Calendar.getInstance();
                String now_date = fm.format(time.getTime());
                mailSendVO.setSendTime(now_date);

                mailService.insertJoinEmail(mailSendVO);

                userVo.setAuthKey(authKey);
                userService.updateAuthKey(userVo);

                mailSender.sender(to, title, Message);

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
     */
    @GetMapping("/signUpConfirm")
    public ResultVO signUpConfirm(@RequestParam("email") String email, @RequestParam("authKey") String authKey){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("메일 인증에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if(email != null && authKey != null){
            UserVO userVO = new UserVO();
            userVO.setAuthKey(authKey);
            userVO.setUser_id(email);
            userService.updateAuthStatus(userVO);

            UserVO findUser = userService.findUserById(email);
            if(findUser.getUser_status() == 1){
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
    @PostMapping("/api_post_login")
    public ResultVO api_post_login(HttpServletResponse response, @RequestBody UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail+"01");
        resultVO.setResult_str("로그인 정보를 다시 확인해주세요.");

        if(userVo.getUser_id() != null || userVo.getUser_pwd() != null){
            UserVO findUser = userService.findUserById(userVo.getUser_id());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if(findUser!=null){
                if(findUser.getUser_status() == 1) {
                    if (findUser.getTemp_pw_y() == 0) {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getUser_pwd())) {
                            resultVO.setResult_code(CONSTANT.success+"01");
                            resultVO.setResult_str("로그인 되었습니다.");

                            Cookie myCookie = new Cookie("user_id", findUser.getUser_id());
                            if(userVo.isAutoLogin() == true){
                                myCookie.setMaxAge(60*60*24*365);
                            } else {
                                myCookie.setMaxAge(-1);
                            }
                            myCookie.setPath("/");
                            response.addCookie(myCookie);
                        }
                    }
                    else if (findUser.getTemp_pw_y() == 1) {
                        if (passwordEncoder.matches(userVo.getUser_pwd(), findUser.getTemp_pw())) {
                            resultVO.setResult_code(CONSTANT.success+"02");
                            resultVO.setResult_str("임시 비밀번호로 로그인 되었습니다.");

                            Cookie myCookie = new Cookie("user_id", findUser.getUser_id());
                            if(userVo.isAutoLogin() == true){
                                myCookie.setMaxAge(60*60*24*365);
                            } else {
                                myCookie.setMaxAge(-1);
                            }
                            myCookie.setPath("/");
                            response.addCookie(myCookie);
                        }
                    }
                } else {
                    resultVO.setResult_code(CONSTANT.fail+"02");
                    resultVO.setResult_str("이메일 인증을 진행해주세요");
                }
            }
        }
        return resultVO;
    }

    /**
     * 회원 가입 검증
     * @param req
     * @param session
     * @param userVo
     * @return
     */
    @PostMapping("/join_default")
    public ResultVO join_default(HttpServletRequest req, HttpSession session, @RequestBody UserVO userVo){
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_code(CONSTANT.fail);
        resultVO.setResult_str("Data error");

        if(userVo.getUser_name()!=null &&
            userVo.getUser_id()!=null&&
            userVo.getUser_pwd()!=null){

            String regexId = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            Matcher matcherId = Pattern.compile(regexId).matcher(userVo.getUser_id());
            if (!matcherId.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("아이디는 이메일형식이어야 합니다");
                return resultVO;
            }

            Matcher matcherPw = Pattern.compile(REGEXPW).matcher(userVo.getUser_pwd());
            if (userVo.getUser_pwd().length() < 10 || !matcherPw.find()) {
                resultVO.setResult_code(CONSTANT.fail);
                resultVO.setResult_str("비밀번호는 영문+숫자+특수문자 10자 이상으로 설정해주세요.");
                return resultVO;
            }

            UserVO findUser = userService.findUserById(userVo.getUser_id());
            if(findUser != null){
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
     * @param file
     * @param session
     * @param user_id
     * @return
     */
    @PostMapping("/modify_profile")
    public ResultVO modify_profile(@RequestParam("file") MultipartFile file, HttpSession session, @CookieValue("user_id") String user_id) {
        ResultVO resultVO = new ResultVO();
        resultVO.setResult_str("사진 저장에 실패했습니다.");
        resultVO.setResult_code(CONSTANT.fail);

        if(file != null){

            UserVO findUserVO = userService.findUserById(user_id);

            String fileName = fileService.storeFile(file);
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/fileStore/")
                    .path(fileName)
                    .toUriString();
            FileUploadResponseVO fileResponse = new FileUploadResponseVO(fileName, fileDownloadUri, file.getContentType(), file.getSize());

            ProfileInfoVO profileInfo= new ProfileInfoVO();
            profileInfo.setIdx_user(findUserVO.getIdx_user());
            profileInfo.setFile_name(fileResponse.getFileName());
            profileInfo.setFile_path(fileResponse.getFileDownloadUri());

            SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            String now_date = fm.format(Calendar.getInstance().getTime());
            profileInfo.setReg_dt(now_date);

            if(findUserVO.getProfile_y() == 0){
                profileFileService.uploadProfileFile(profileInfo);
                findUserVO.setProfile_y(1);
                userService.addNewProfile(findUserVO);

                resultVO.setResult_str("프로필이 등록되었습니다.");
                resultVO.setResult_code(CONSTANT.success+"01");

            } else if(findUserVO.getProfile_y() == 1) {
                profileFileService.updateProfileFile(profileInfo);

                resultVO.setResult_str("프로필이 수정되었습니다.");
                resultVO.setResult_code(CONSTANT.success+"02");
            }
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
        resultVO.setResult_str("유저 정보를 다시 확인해주세요.");

        UserVO finduser = userService.findUserById(userVO.getUser_id());

        if(finduser!=null){
            String tempPW = mailService.getRamdomPassword();
            String Message = "<h1>[임시 비밀번호]</h1><br><p>아래의 비밀번호로 로그인 후 비밀번호를 변경해 주세요.</p>"
                    + "임시 비밀번호 : " + tempPW;
            String to = finduser.getUser_id();
            String title = "[EURA] 임시 비밀번호 발급 메일";

            MailVO mailSendVO = new MailVO();
            mailSendVO.setIdx_user(finduser.getIdx_user());
            mailSendVO.setReceiver(finduser.getUser_id());
            mailSendVO.setTitle(title);
            mailSendVO.setContent(Message);
            mailSendVO.setMail_type(1);

            SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
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

            mailSender.sender(to, title, Message);

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

        if(findUser != null){
            String authKey = mailService.generateAuthNo(6);
            String Message = "<h1>[이메일 인증]</h1><br><p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>"
                    + "<a href=\""+ domain +"/signUpConfirm?email=" + findUser.getUser_id()
                    + "&authKey=" + authKey + "\" target=\"_blenk\">이메일 인증 확인</a>";
            String to = findUser.getUser_id();
            String title = "[EURA] 회원가입 인증 메일";

            MailVO mailSendVO = new MailVO();
            mailSendVO.setIdx_user(findUser.getIdx_user());
            mailSendVO.setReceiver(findUser.getUser_id());
            mailSendVO.setTitle(title);
            mailSendVO.setContent(Message);
            mailSendVO.setMail_type(0);

            SimpleDateFormat fm = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            Calendar time = Calendar.getInstance();
            String now_date = fm.format(time.getTime());
            mailSendVO.setSendTime(now_date);

            mailService.insertJoinEmail(mailSendVO);

            findUser.setAuthKey(authKey);
            findUser.setUser_status(0);
            userService.updateAuthKey(findUser);

            mailSender.sender(to, title, Message);

            resultVO.setResult_code(CONSTANT.success);
            resultVO.setResult_str("메일이 재발송 되었습니다.");
        }

        return resultVO;
    }

}