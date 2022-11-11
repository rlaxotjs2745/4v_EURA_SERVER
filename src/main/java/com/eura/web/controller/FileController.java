package com.eura.web.controller;

import com.eura.web.base.BaseController;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

@Slf4j
@Controller
public class FileController extends BaseController {
    @Value("${file.upload-dir}")
    private String filepath;

    /**
     * 파일 다운로드
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping("/download")
    public void getDownloadFile(HttpServletRequest req, HttpServletResponse res) throws Exception {
        try{
            String header = req.getHeader("User-Agent");
            String fnm = req.getParameter("fnm");
            File _df = new File(filepath + fnm);
            String originFilenm = _df.getName();
            String fileName;
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(_df));
            if ((header.contains("MSIE")) || (header.contains("Trident")) || (header.contains("Edge"))) {
                fileName = URLEncoder.encode(originFilenm, "UTF-8");
            } else {
                fileName = new String(originFilenm.getBytes("UTF-8"), "iso-8859-1");
            }
            res.setContentType("application/octet-stream");
            res.setHeader("Content-Disposition", "attachment; filename=\""+ fileName + "\"");
            FileCopyUtils.copy(in, res.getOutputStream());
            in.close();
            res.getOutputStream().flush();
            res.getOutputStream().close();
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }

    /**
     * 이미지 연결
     * @param req
     * @param res
     * @return ResponseBody byte
     * @throws IOException
     */
    @RequestMapping(value = "/pic", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getFileStream(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String fnm = req.getParameter("fnm");
        File _df = new File(filepath + fnm);
		InputStream in = new FileInputStream(_df);
		return IOUtils.toByteArray(in);
    }
}
