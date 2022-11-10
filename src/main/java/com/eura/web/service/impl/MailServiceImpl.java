package com.eura.web.service.impl;

import com.eura.web.model.DTO.MailVO;
import com.eura.web.model.MailMapper;
import com.eura.web.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service("mailService")
public class MailServiceImpl implements MailService {

    @Autowired
    private MailMapper mailMapper;

    //이메일 추가
    @Override
    public void insertJoinEmail(MailVO mailSendVO) {
        mailMapper.insertJoinEmail(mailSendVO);
    }


    //난수 발생(회원가입 인증번호)
    public String generateAuthNo(int num) {
        Random rand = new Random();
        String authKey = "";

        for (int i = 0; i < num; i++) {
            String random = Integer.toString(rand.nextInt(10));
            authKey += random;
        }
        return authKey;
    }

    //난수+문자열 발생(임시비밀번호)
    public String getRamdomPassword() {
        char pwCollectionSpCha[] = new char[] {'!','@','#','$','%','^','&','*','(',')'};
        char pwCollectionNum[] = new char[] {'1','2','3','4','5','6','7','8','9','0',};
        char pwCollectionAll[] = new char[] {'1','2','3','4','5','6','7','8','9','0',
                                             'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                                             'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                                             '!','@','#','$','%','^','&','*','(',')'};

        return getRandPw(1, pwCollectionSpCha) + getRandPw(8, pwCollectionAll) + getRandPw(1, pwCollectionNum);
    }

    public String getRandPw(int size, char[] pwCollection){
         String ranPw = "";
         for (int i = 0; i<size; i++) {
             int selectRandomPw = (int) (Math.random() * (pwCollection.length));
             ranPw += pwCollection[selectRandomPw];
         }
     return ranPw;
    }
}
