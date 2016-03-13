package com.mttnow.push.api.service.impl;

import com.mttnow.push.api.builders.ApplicationBuilder;
import com.mttnow.push.api.exceptions.PushApplicationException;
import com.mttnow.push.api.models.Application;
import com.mttnow.push.api.models.User;
import com.mttnow.push.core.persistence.ApplicationRepository;
import com.mttnow.push.core.persistence.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationServiceImplTest {

    public static final String APP_ID = "1L";
    public static final String APP_USER = "chuck";
    public static final String APP_NAME = "appName";

    @Mock
    ApplicationRepository repository;
    
    @Mock
    UserRepository userRepository;

    @InjectMocks
    private ApplicationServiceImpl appService;

    @Before
    public void setup() throws Exception {
    }

    @Test
    public void shouldGetApplicationOnFindById() throws Exception {
        when(repository.findOne(APP_ID)).thenReturn(new Application(APP_ID));

        Application app = appService.findById(APP_ID);
        assertEquals(APP_ID, app.getId());
        verify(repository).findOne(APP_ID);
    }

    @Test
    public void shouldGetApplicationOnFindByUser() throws Exception {
        List<Application> applications = new ArrayList<Application>();
        applications.add(new ApplicationBuilder().buildWithDefaultValues());
        User user = new User();
        user.setApplications(applications);
        when(userRepository.findByUsername(APP_USER)).thenReturn(user);

        appService.findAllByUser(APP_USER);
        verify(userRepository).findByUsername(APP_USER);
    }

    @Test
    public void shouldCreateNewAppOnSaveNoDuplicateNames() throws PushApplicationException {
        when(repository.findByName(APP_NAME)).thenReturn(null);


        Application application = new ApplicationBuilder().buildWithDefaultValues();
        when(repository.save(application)).thenReturn(application);

        User user = new User();
        user.setApplications(new ArrayList<Application>());
        when(userRepository.findByUsername(APP_USER)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        appService.save(application, APP_USER);

        verify(repository).findByName(APP_NAME);
        verify(repository).save(application);
        verify(userRepository).save(user);

    }

    @Test(expected = PushApplicationException.class)
    public void shouldThrowExceptionIfAppNameExists() throws PushApplicationException {
        List<Application> apps = new ArrayList<Application>();
        Application existingApp = new ApplicationBuilder().buildWithDefaultValues();
        apps.add(existingApp);
        when(repository.findByName(APP_NAME)).thenReturn(apps);

        appService.save(existingApp, APP_USER);

        verify(repository).findByName(APP_NAME);
    }

    @Test
    public void shouldSaveIfExistingAppNameIsDeleted() throws PushApplicationException {
        List<Application> apps = new ArrayList<Application>();
        Application existingApp = new ApplicationBuilder().buildWithDefaultValues();
        existingApp.setDeleted();
        apps.add(existingApp);
        when(repository.findByName(APP_NAME)).thenReturn(apps);
        when(repository.save(existingApp)).thenReturn(existingApp);
        
        User user = new User();
        user.setApplications(new ArrayList<Application>());
        when(userRepository.findByUsername(APP_USER)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        appService.save(existingApp, APP_USER);

        verify(repository).findByName(APP_NAME);
        verify(repository).save(existingApp);
    }

    @Test
    public void shouldUpdateApplication() throws PushApplicationException {
        Application app = new ApplicationBuilder().buildWithDefaultValues();

        when(repository.save(app)).thenReturn(app);

        Application returnedApp = appService.update(app);

        verify(repository).save(app);
        assertNotNull(returnedApp);
    }

    @Test
    public void shouldDeleteApplication() throws PushApplicationException {
        Application app = new ApplicationBuilder().buildWithDefaultValues();
        when(repository.findOne(APP_ID)).thenReturn(app);
        when(repository.save(app)).thenReturn(app);

        appService.delete("1L");

        verify(repository).findOne("1L");
        verify(repository).save(app);
    }
}
