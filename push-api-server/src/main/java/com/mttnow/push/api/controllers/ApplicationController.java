package com.mttnow.push.api.controllers;

import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.APNSChannel;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.service.ApplicationService;
import com.mttnow.push.api.service.ChannelService;
import com.mttnow.push.api.utils.FileUploader;
import com.mttnow.push.api.utils.PushObjectMapper;
import com.mttnow.push.api.validators.ApplicationValidator;
import com.mttnow.push.core.component.ApnsCertificateStorageHandler;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;


/**
 * Application Controller which is actually exposed as a restful service.
 * This contains CRUD function of the app
 */
@Controller
public class ApplicationController extends BaseController {

    private static Logger log = LoggerFactory.getLogger(ApplicationController.class);
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ChannelService channelService;
    @Autowired
    ApplicationValidator applicationValidator;
    @Autowired
    ApnsCertificateStorageHandler apnsCertificateStorageHandler;

    @RequestMapping(value = "/application/{id}", method = RequestMethod.GET)
    public
    @ResponseBody
    Application getApplication(@PathVariable String id) {
        //TODO create custom query on repository to findByNameAndDateDeletedNullAndUser RDBFU-119
        return applicationService.findById(id);
    }

    @RequestMapping(value = "/applications", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Application> getApplications(HttpServletRequest request) throws IOException {
        final String userName = getUserName(request);
        //TODO create custom query on repository to findByNameAndDateDeletedNullAndUser RDBFU-100
        return applicationService.findAllByUser(userName);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/application/multi", consumes = "multipart/form-data")
    public
    @ResponseBody
    Application saveApplicationMultipart(@RequestParam(value = "content") String application, @RequestParam(value = "ios-details", required = false) String iosDetails,
                                         @RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) throws PushApplicationException, IOException, ConfigurationException {
        PushObjectMapper mapper = new PushObjectMapper();
        APNSChannel apnsChannel = mapper.readValue(iosDetails, APNSChannel.class);
        Application appForSaving = mapper.readValue(application, Application.class);
        final String userName = getUserName(request);
        applicationValidator.validateApplicationMultipartRequestParams(file, apnsChannel, appForSaving);

        Application savedApp = applicationService.save(appForSaving, userName);
        if (apnsChannel != null) {
            String savedStoragePath = FileUploader.saveUploadedFile(apnsCertificateStorageHandler.getApplicationCertificateStorage(savedApp.getId()), file.getOriginalFilename(), file.getBytes());
            //TODO filename instead of savedStoragePath, enhance implementation in sending message RDBFU-108
            apnsChannel.setCert(savedStoragePath);
            apnsChannel.setApplication(savedApp);
            channelService.saveApnsChannel(apnsChannel);
        }
        return savedApp;
    }

    @RequestMapping(value = "/application/{id}", method = RequestMethod.PUT)
    public
    @ResponseBody
    Application updateApplication(@RequestBody Application application, @PathVariable String id) throws PushApplicationException {
        log.info("################## APPLICATION FOR UPDATE : {}", application);
        application.setId(id);
        return applicationService.update(application);
    }

    @RequestMapping(value = "/application/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteApplication(@PathVariable String id) {
        applicationService.delete(id);
        return "Successfully Deleted!";
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(applicationValidator);
    }

    @ExceptionHandler(PushApplicationException.class)
    @ResponseBody
    public ModelAndView handlePushApplicationException(PushApplicationException ex) {
        log.warn(ex.getMessage());
        return ex.getErrorMessage().asModelAndView();
    }
}
