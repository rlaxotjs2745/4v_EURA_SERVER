package com.eura.web.controller;

import com.eura.web.base.BaseController;
import com.eura.web.model.DTO.FileUploadResponseVO;
import com.eura.web.model.DTO.RecieveFilesVO;
import com.eura.web.service.FileService;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
@Controller
public class FileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    
    // @Autowired
    private FileService fileService;
    
    @Value("${file.upload-dir}")
    private String filepath;

    public FileUploadResponseVO storeFile(@RequestParam("file") MultipartFile file) {
        String fileName = fileService.storeFile(file);
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/fileStore/")
                .path(fileName)
                .toUriString();

        return new FileUploadResponseVO(fileName, fileDownloadUri, file.getContentType(), file.getSize());
    }

    @PostMapping("/uploadFile")
    public FileUploadResponseVO uploadFile(@RequestParam("file") MultipartFile file) {

        //DB 저장해야 한다
        return storeFile(file);
        /*String fileName = fileService.storeFile(file);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/fileStore/")
                .path(fileName)
                .toUriString();

        return new FileUploadResponseVO(fileName, fileDownloadUri, file.getContentType(), file.getSize());*/
    }

    @PostMapping("/uploadMultipleFiles")
    public String uploadMultipleFiles(@ModelAttribute RecieveFilesVO recieveFilesVo){
        //List<FileUploadResponseVO> respList = new ArrayList<FileUploadResponseVO>();
        //List<MultipartFile> files = recieveFilesVo.getFile();
        if(recieveFilesVo!=null)
        {
            //DB 저장
            if(recieveFilesVo.getFile1()!=null && !recieveFilesVo.getFile1().isEmpty()) storeFile(recieveFilesVo.getFile1());
            if(recieveFilesVo.getFile2()!=null && !recieveFilesVo.getFile2().isEmpty()) storeFile(recieveFilesVo.getFile2());
            if(recieveFilesVo.getFile3()!=null && !recieveFilesVo.getFile3().isEmpty()) storeFile(recieveFilesVo.getFile3());
            if(recieveFilesVo.getFile4()!=null && !recieveFilesVo.getFile4().isEmpty()) storeFile(recieveFilesVo.getFile4());
            if(recieveFilesVo.getFile5()!=null && !recieveFilesVo.getFile5().isEmpty()) storeFile(recieveFilesVo.getFile5());
            if(recieveFilesVo.getFile6()!=null && !recieveFilesVo.getFile6().isEmpty()) storeFile(recieveFilesVo.getFile6());
            if(recieveFilesVo.getFile7()!=null && !recieveFilesVo.getFile7().isEmpty()) storeFile(recieveFilesVo.getFile7());
            if(recieveFilesVo.getFile8()!=null && !recieveFilesVo.getFile8().isEmpty()) storeFile(recieveFilesVo.getFile8());
            if(recieveFilesVo.getFile9()!=null && !recieveFilesVo.getFile9().isEmpty()) storeFile(recieveFilesVo.getFile9());
        }
        return "Success";
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){

        // Load file as Resource
        Resource resource = fileService.loadFileAsResource(fileName);

        //String originalFileName = URLDecoder.decode(fileName,"UTF-8");
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream; charset=UTF-8";
        }
 /*       String fileNm = "한글 파일명";
        String browser = getBrowser(req);
        res.setContentType("application/octet-stream; charset=UTF-8");
        res.setHeader("Content-Description", "file download");
        res.setHeader("Content-Disposition", "attachment; filename=\"".concat(getFileNm(browser, resource.getFilename())).concat("\""));
        res.setHeader("Content-Transfer-Encoding", "binary");*/
        String browser = getBrowser(request);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"".concat(getFileNm(browser, resource.getFilename())).concat("\""))
                .header(HttpHeaders.TRANSFER_ENCODING, "binary")
                .body(resource);
    }

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
}
