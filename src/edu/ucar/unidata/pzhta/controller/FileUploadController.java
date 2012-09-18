package edu.ucar.unidata.pzhta.controller;

import org.grlea.log.SimpleLogger;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import edu.ucar.unidata.pzhta.domain.UploadedFile;

/**
 * Controller to handle file uploads.
 *
 */


@Controller
public class FileUploadController implements HandlerExceptionResolver {

    private static final SimpleLogger log = new SimpleLogger(FileUploadController.class);


/*
    @RequestMapping(method=RequestMethod.GET)
    public String showForm(ModelMap model){
        UploadForm form = new UploadForm();
        model.addAttribute("FORM", form);
        return "upload";
    }
*/

    /*
     * Returns the local (unique, alphanumeric) file name of the 
     * uploaded ASCII file as it is found on disk.
     * 
     * @param file      The UploadedFile form backing object containing the file.
     * @param request   The HttpServletRequest with which to glean the client IP address.
     * @return          A String of the local file name for the ASCII file. 
     */
    @RequestMapping(value="/upload", method=RequestMethod.POST)
    @ResponseBody
    public String processUpload(UploadedFile file, HttpServletRequest request) {
        FileOutputStream outputStream = null;
        String uniqueId = createUniqueId(request);
        String filePath = System.getProperty("java.io.tmpdir") + "/" + uniqueId;
        try {
            File localFileDir = new File(filePath);
            if (!localFileDir.exists()) localFileDir.mkdir();
            outputStream = new FileOutputStream(new File(filePath + "/" + file.getFileName()));
            outputStream.write(file.getFile().getFileItem().get());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("A file upload error has occurred: " + e.getMessage());
            return null;
        }
        return uniqueId;                 
    }


    @Override
    public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception exception) {
        Map<Object, Object> model = new HashMap<Object, Object>();
        if (exception instanceof MaxUploadSizeExceededException){ 
           log.error("File size should be less then "+ ((MaxUploadSizeExceededException)exception).getMaxUploadSize()+" byte.");
        } else{
           log.error("An error has occurred: " + exception.getClass().getName() + ":");
           log.error(exception.getMessage());
        }        
        return null;
    }


    /*
     * Attempts to get the client IP address from the request.
     * 
     * @param request     The HttpServletRequest.
     * @return            The client's IP address.
     */
    private String getIpAddress(HttpServletRequest request) {
        String ipAddress = null;
        if (request.getRemoteAddr() != null) {
            ipAddress = request.getRemoteAddr();
            ipAddress = StringUtils.deleteWhitespace(ipAddress);            
            ipAddress = StringUtils.trimToNull(ipAddress);
            ipAddress = StringUtils.lowerCase(ipAddress);  
            ipAddress = StringUtils.replaceChars(ipAddress, ".", "");
        }
        return ipAddress;
    }


    /*
     * Creates a unique id for the file name from the clients IP address and the date.
     * 
     * @param request     The HttpServletRequest.
     * @return            The unique file name id.
     */
    private String createUniqueId(HttpServletRequest request) {

        String id = new Integer(new Date().hashCode()).toString();
        String ipAddress = getIpAddress(request);
        if (ipAddress != null) {
            id = ipAddress + id;
        } else {
            id = new Integer(new Random().nextInt()).toString() + id;
        }
        return id;
    }


}