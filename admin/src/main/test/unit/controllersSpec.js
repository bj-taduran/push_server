describe('pushAdmin-controllers', function() {
    var controller, mockBackend, rootScope, scope, cookieStore;

    beforeEach(module('pushAdmin'));
    beforeEach(inject(function($rootScope, $controller, $httpBackend, $cookieStore) {
        rootScope = $rootScope;
        scope = $rootScope.$new();
        mockBackend = $httpBackend;
        cookieStore = $cookieStore;
        controller = $controller;
    }));

    describe('initialController', function() {
        var devApp, prodApp, applications = [];

        beforeEach(inject(function() {
            devApp = {id: '4471dc68-9ee9-47bb-93a7-c058f743f9dd', mode: 'DEVELOPMENT', name: 'Development Admin App'};
            prodApp = {id: 'fa19dfc6-cc03-45a7-8b62-41b982adca20', mode: 'DEVELOPMENT', name: 'Production Admin App'};

            applications.push(devApp);
            applications.push(prodApp);
            mockBackend.expectGET('/push/applications').respond(applications);
            applications.push({ id : -1, name : 'Create Application ...' });
        }));

        afterEach(function() {
            mockBackend.verifyNoOutstandingExpectation();
            mockBackend.verifyNoOutstandingRequest();
        });

        it('should return applications and currentApplication is set based on the data from the cookie', function() {
            cookieStore.put('push', {'user':'YWRtaW46YWRtaW4=', 'app':prodApp.id});
            controller(initialController);
            mockBackend.flush();

            expect(rootScope.applications).toBeDefined();
            expect(rootScope.applications).toEqual(applications);

            expect(rootScope.currentApplication).toBeDefined();
            expect(rootScope.currentApplication.id).toBe(prodApp.id);
        });


       it('should return applications and currentApplication is set to default when cookie is not present', function() {
            cookieStore.remove('push');
            controller(initialController);
            mockBackend.flush();

            expect(rootScope.applications).toBeDefined();
            expect(rootScope.applications).toEqual(applications);

            expect(rootScope.currentApplication).toBeDefined();
            expect(rootScope.currentApplication.id).toBe(devApp.id);
        });
    });

    describe('loginController', function() {

        beforeEach(inject(function() {
            controller(loginController, {$rootScope: rootScope, $scope: scope});
        }));

        afterEach(function() {
            mockBackend.verifyNoOutstandingExpectation();
            mockBackend.verifyNoOutstandingRequest();
        });

        it('should show login panel onLoad and input should have default values', function() {
            expect(scope.loginPanel).toBeTruthy();

            expect(scope.user).toBeDefined();
            expect(scope.user.name).toBe('');
            expect(scope.user.password).toBe('');
        });

        it('should return user details upon successful login and store cookie', function() {
            scope.user.name = 'admin';
            scope.user.password = 'admin';
            mockBackend.expectGET('/push/user').respond({username: "admin", roles:'ROLE_ADMIN'});

            scope.login();
            mockBackend.flush();

            expect(rootScope.authenticatedUser).toBeDefined();
            expect(rootScope.authenticatedUser.name).toMatch('admin');
            expect(cookieStore.get("push")).toBeDefined();
            expect(cookieStore.get("push").user).toMatch('YWRtaW46YWRtaW4=');

            expect(scope.loginPanel).toBeFalsy();
        });

        it('should return invalid credentials when error is encountered upon login', function() {
            mockBackend.expectGET('/push/user').respond(function(method, url, data) {return [401, {}];});

            scope.login();
            mockBackend.flush();

            expect(rootScope.authenticatedUser).toBeUndefined();
            expect(cookieStore.get("push")).toBeUndefined();

            expect(rootScope.loginError).toBeDefined();
            expect(rootScope.loginError).toMatch('Invalid credentials.');
            expect(scope.loginPanel).toBeTruthy();
        });

        it('should invalidate user and remove cookie upon logout', function() {
            cookieStore.put('push', {'user':'YWRtaW46YWRtaW4='});
            expect(cookieStore.get("push")).toBeDefined();

            scope.logout();

            expect(rootScope.authenticatedUser).toBeUndefined();
            expect(rootScope.currentApplication).toBeUndefined();
            expect(rootScope.applications).toBeUndefined();
            expect(cookieStore.get("push")).toBeUndefined();
            expect(scope.loginPanel).toBeTruthy();
        });

    });

    describe('messageController', function(){
        beforeEach(inject(function() {
            controller(messageController, {$rootScope: rootScope, $scope: scope});
            rootScope.currentApplication = {id: '4471dc68-9ee9-47bb-93a7-c058f743f9dd'};
        }));

        it('should show compose page and input should have default values', function() {
            expect(scope.message).toBeDefined();
            expect(scope.message.details).toBe('');
            expect(scope.message.title).toBe('');
            expect(scope.message.channels).toBe('iOS');
            expect(scope.message.recipientType).toBe('Broadcast');
            expect(scope.message.deliveryType).toBe('Now');
            expect(scope.message.recipientIds).toMatch([]);

            expect(scope.response).toBeDefined();
            expect(scope.response.status).toBe('');
            expect(scope.response.msg).toBe('');

        });


        it('should send message and received response, Message Successfully Sent!', function(){
            scope.message.details = 'message details';
            scope.message.title = 'message title';
            scope.message.recipientIds = [];

            mockBackend.expectPOST('/push/message/' + rootScope.currentApplication.id).respond('Message Successfully Sent!');

            scope.sendMessage();
            mockBackend.flush();
            expect(scope.response.status).toBe('success');
            expect(scope.response.msg).toBe('Message Successfully Sent!');

        });

    });
});
