package com.yuandong.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yuandong.util.ResultGenerator;
import com.yuandong.vo.RestResult;

@Controller
@RequestMapping("/file")
public class UploadController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    //Save the uploaded file to this folder
    @Value("${http.multipart.uploadPathPrefix}")
    private String UPLOADED_FOLDER;
    @Value("${http.multipart.fileSystemBaseUrl}")
    private String fileSystemBaseUrl;
    
    @Autowired
    private ResultGenerator generator;
    
    private DateFormat dft = new SimpleDateFormat("yyyyMMdd");
    
    @RequestMapping("/upload.html")
    public String upload(){
    	return "upload";
    }
    
    @RequestMapping("/testCkeditor.html")
    public String testCkeditor(){
    	return "testCkeditor";
    }
    
    @RequestMapping("/ueditor.html")
    public String ueditor(){
    	return "ueditor";
    }
    
    /**
     * 读取ueeditor配置
     * @return
     */
    @RequestMapping(value = "/uploadConfig.json")
    @ResponseBody
    public String imgUpload() {
    	InputStream is = null;
    	InputStreamReader ir = null;
    	BufferedReader br = null;
    	try {
    		is = getClass().getClassLoader().getResourceAsStream("static/scripts/ueditor/jsp/config.json");
        	ir = new InputStreamReader(is);
        	br = new BufferedReader(ir);
        	String str = null;
        	StringBuffer sb = new StringBuffer();
        	while((str=br.readLine())!=null){
        		sb.append(str.trim());
        	}
        	return sb.toString().replaceAll("\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "");
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "{}";
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ir != null){
				try {
					ir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
    }

    //Single file upload
    @RequestMapping(value="/upload.json",method=RequestMethod.POST)
    @ResponseBody
    public Map uploadFile(@RequestParam("upfile") MultipartFile uploadfile) {
        Map<String,String> rs = new HashMap<>();
        if (uploadfile.isEmpty()) {
        	rs.put("state", "请选择上传的文件");
        	rs.put("url","");
            rs.put("title", "");
            rs.put("original", "");
        	return rs;
        }
        try {
            List<String> subPaths = saveUploadedFiles(Arrays.asList(uploadfile));
            String config =
                    "{\n" +
                    "            \"state\": \"SUCCESS\",\n" +
                    "                \"url\": \""+subPaths.get(0)+"\",\n" +
                    "                \"title\": \""+subPaths.get(0)+"\"\n" +
                    "        }";
            rs.put("state", "SUCCESS");
            rs.put("url", fileSystemBaseUrl + subPaths.get(0));
            rs.put("title", uploadfile.getOriginalFilename());
            rs.put("original", uploadfile.getOriginalFilename());
            return rs;
        } catch (IOException e) {
        	logger.error(e.getMessage(),e);
        	rs.put("state", "上传文件失败！");
        	rs.put("url","");
            rs.put("title", "");
            rs.put("original", "");
        	return rs;
        }
    }

    // Multiple file upload
    @RequestMapping(value="/multiUpload.json",method=RequestMethod.POST)
    @ResponseBody
    public RestResult uploadFileMulti(@RequestParam("files") MultipartFile[] uploadfiles) {
    	
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));

        if (StringUtils.isEmpty(uploadedFileName)) {
        	generator.getSuccessResult("请选择上传的文件",null);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfiles));
        } catch (IOException e) {
        	logger.error(e.getMessage(),e);
        	return generator.getFailResult(e.getMessage());
        }
        return generator.getSuccessResult();
    }


    //save file
    private List<String> saveUploadedFiles(List<MultipartFile> files) throws IOException {
    	List<String> subPaths = new LinkedList<String>();
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; //next pls
            }
            String yyyMMdd = dft.format(new Date());
            int index = file.getOriginalFilename().lastIndexOf(".");
            StringBuffer filePath = new StringBuffer();//存储文件相对路径：/2018/0803/jfhjkjngd.jpg
            filePath.append(UPLOADED_FOLDER).append(yyyMMdd.substring(0,4)).append("/").append(yyyMMdd.substring(4)).append("/");
            File dir = new File(filePath.toString());
            if(!dir.exists()){
        	   dir.mkdirs();
            }
            String fileName = UUID.randomUUID()+(index>-1?file.getOriginalFilename().substring(index):"");
            filePath.append(fileName);
            Path path = Paths.get(filePath.toString());
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
            subPaths.add(filePath.toString().replace(UPLOADED_FOLDER, ""));
        }
        return subPaths;
    }
    
}
