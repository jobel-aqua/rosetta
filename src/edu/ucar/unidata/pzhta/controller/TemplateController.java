package edu.ucar.unidata.pzhta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller for template selection.
 *
 * This controller returns the correct view based on the user's template
 * choice. If the user needs to create a template, the 'create' view is 
 * returned. If the user already has a template, the 'upload' view is returned.
 */

@Controller
public class TemplateController {
   
    /*
     * Returns the 'create' view for the UrlBasedViewResolver. 
     * This view contains the web form that walks the user 
     * throught the steps of cf type template creation.
     * 
     * @return  The 'create' ModelAndView.
     */
    @RequestMapping("/create")
    public ModelAndView createTemplate() {
        return new ModelAndView("create");
    }

   
    /*
     * Returns the 'upload' view for the UrlBasedViewResolver. 
     * This view contains the web form that allows the user 
     * to upload and use an existing cf type template.
     * 
     * @return  The 'upload' ModelAndView.
     */
/*
    @RequestMapping("/upload")
    public ModelAndView createTemplate() {
        return new ModelAndView("upload");
    }
*/

} 