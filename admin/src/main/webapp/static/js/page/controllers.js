function initialController(applicationService, $cookieStore) {
    var cookie = $cookieStore.get('push');
    if (cookie != undefined && cookie.app != undefined){
        applicationService.getApps(cookie.app);
    }
    else {
        applicationService.getApps(0);
    }
}

function loginController($rootScope, $scope) {
    $scope.loginPanel = true;
    $scope.user = {name:'', password:''};

    $scope.login = function() {
        $scope.$emit('event:user-request', $scope.user);
    };

    $scope.logout = function() {
        $scope.$emit('event:forget-user');
        $scope.loginPanel = true;
    }

    $rootScope.$on('event:user-confirmed', function () {
        $scope.loginPanel = false;
    });
}

function dashboardController($scope, $location, $rootScope) {
    $scope.navClass = function (page) {
        var currentRoute = $location.path().substring(1) || 'app/details';
        return page === currentRoute ? 'active' : '';
    };

    $scope.closeNotification = function () {
        if ($rootScope.showCreatedAppAlert) {
            $rootScope.showCreatedAppAlert = false;
        }
    }
}

function messageController($rootScope, $scope, messageService, $timeout,$location){
    $scope.message = {details:'', title: '', channels:'iOS', recipientType:'Broadcast', deliveryType:'Now', recipientIds: []};
    $scope.response = {status: '', msg: ''};

    $scope.showRecipientsPanel = function(){
        $scope.chooseRecipientsPanel = true;
    }

    $scope.hideRecipientsPanel = function(){
        $scope.chooseRecipientsPanel = false;
    }

    clearMessage = function() {
        $scope.message = {details:'', title: '', channels:'iOS', recipientType:'Broadcast', deliveryType:'Now', recipientIds: []};
        $scope.clearRecipients();
    }

    $scope.clearRecipients = function() {
        $scope.message.recipientIds = [];
        $scope.selectedTags = [];
    }

    $scope.sendMessage = function () {
        messageService.sendMessage($rootScope.currentApplication, $scope.message)
            .success(function (response) {
                if(response.error != undefined){
                    $scope.response.status = 'error';
                    $scope.response.msg = response.error;
                }else{
                    $scope.response.status = 'success';
                    $scope.response.msg = response;
                    startAlertTimer();
                    clearMessage();
                }
            }).error(function (response) {
                $scope.response.status = 'error';
                $scope.response.msg = "An error has occurred while sending message.";
            });
    }


    startAlertTimer = function(){
        var timeout, num = 0;

        var startTimer = function () {
            num++;

            timeout = $timeout(startTimer, 1000);
            if (num > 5) {
                $timeout.cancel(timeout);
                $scope.response.msg = '';
            }
        };
        startTimer();
    }

    $scope.onCancel = function(){
       return $location.path('/app/details');
    }
}


function AlertCtrl($scope, $rootScope) {
    $rootScope.alerts = [{ type: 'success', msg: 'Application successfully created.'}];
    $scope.closeAlert = function (index) {
        $rootScope.alerts.splice(index, 1);
    };
}

function createAppController($scope, applicationService, $rootScope, $location) {
    $scope.notificationModal = false;
    $scope.createAppModal = true;
    $scope.application = {name: '',channels:'iOS'};
    $scope.pass = {password:'', confirmpassword: ''}
    $scope.error = {pageerror: ''}
    $scope.modeId = 0;
    $scope.modeItems = [
        {modeId: 0, modeName: 'DEVELOPMENT' },
        {modeId: 1, modeName: 'PRODUCTION' }
    ];

    $scope.saveUpload = function() {
        $scope.showPageError = false;
        $scope.passwordError = "";
        $scope.error.pageerror = "";

        if (!isFormValid()) { return;}

        $scope.modeName = $scope.modeItems[$scope.modeId].modeName;
        var files = document.getElementById('certification').files[0];
        if (files == undefined) {return;}

        $scope.content = '{"name": "'+ $scope.application.name +'", "mode": "'+ $scope.modeName +'"}';
        $scope.iosDetails = '{"password":"'+ $scope.pass.password +'"}';

        applicationService.saveCreateApp(files,$scope.content, $scope.iosDetails).
            success(function (data) {
                $scope.data = data;
                if (data.error != undefined) {
                    $scope.error.pageerror = data.error;
                    $scope.showPageError = true;
                } else {
                    $scope.createAppModal = false;
                    $rootScope.showCreatedAppAlert = true;
                    applicationService.getApps();
                }
            }).
            error(function (data) {
                $scope.data = data || "Request failed";
            });
    }

    isFormValid = function () {
        var isValid = false;
        if ($scope.createAppForm.$invalid) {
            $scope.error.pageerror = "Invalid input.";
            $scope.showPageError = true;
        }

        if ($scope.pass.password != $scope.pass.confirmpassword) {
            $scope.passwordError = "Password mismatch.";
        }

        if ($scope.passwordError == '' && $scope.error.pageerror == '') {
            isValid = true;
        }
        return isValid;
    }

    $scope.onclose = function () {
        $rootScope.showCreatedAppAlert = false;
        $scope.createAppModal = false;
        return $location.path('/app/details');
    }
}

function appDetailsController($scope, $rootScope, applicationService, $location, dialogService, $timeout) {


    var timeout;
    var num = 0;

    var startTimer = function () {
        num++;

        timeout = $timeout(startTimer, 1000);
        if (num > 5) {
            $timeout.cancel(timeout);
            $rootScope.showCreatedAppAlert = false;
        }
    };

    if ($rootScope.showCreatedAppAlert) {
        startTimer();
    }

    $scope.deleteApp = function () {
        dialogService.showDeleteDialog('Delete application', 'do you want to delete this app?').
            then(function (result) {
                if (result) {
                    applicationService.deleteApp($rootScope.currentapplication.id).
                        success(function (data, status) {
                            $scope.status = status;
                            $scope.data = data;
                            return $location.path('/app/details')
                        }).
                        error(function (data, status) {
                            $scope.data = data || "Request failed";
                            $scope.status = status;
                        });
                }
            });
    }
}

function recipientsController($rootScope, $scope, recipientService) {
    $scope.recipient = {searchKey: ''};
    $scope.availableTags = {};
    $scope.selectedTags = [];

    $scope.searchRecipients = function() {
        recipientService.searchRecipients($rootScope.currentApplication, $scope.recipient).
            success(function (data, status) {
                $scope.availableTags = data;
            }).
            error(function (data, status) {
                $scope.availableTags = {};
            });
    }

    $scope.addRecipients = function(){
        angular.forEach($scope.recipient.availableTags, function(value){
            var isIncluded = $scope.isRecipientIncluded(value);
            if (isIncluded == false){
                var selVal = $scope.getAvailableRecipient(value);
                if (selVal != ''){
                    $scope.selectedTags.push(selVal);
                }
            }
        });
    }

    $scope.addAllRecipients = function(){
        if ($scope.selectedTags.length != $scope.availableTags){
            $scope.selectedTags = [];
            angular.forEach($scope.availableTags, function(value){
                if (value.id != 0){
                   $scope.selectedTags.push(value);
                }
            });
        }
    }

    $scope.removeRecipients = function(){
        angular.forEach($scope.recipient.selectedTags, function(value){
            angular.forEach($scope.selectedTags, function(selValue, key){
                if (value == selValue.id){
                    $scope.selectedTags.splice(key,1);
                }
            });
        });
    }

    $scope.removeAllRecipients = function(){
        $scope.selectedTags = [];
    }

    $scope.isRecipientIncluded = function(valueId) {
        var isIncluded = false;
        angular.forEach($scope.selectedTags, function(selValue){
            if (valueId == selValue.id){
                isIncluded = true;
                return isIncluded;
            }
        });
        return isIncluded;
    }

    $scope.getAvailableRecipient = function(valueId) {
        var availVal = [];
        angular.forEach($scope.availableTags, function(selValue){
            if (valueId == selValue.id){
                availVal = selValue;
                return availVal;
            }
        });
        return availVal;
    }

    $scope.cancelSelection = function(){
        $scope.selectedTags = [];
        $scope.recipient.availableTags = [];
        $scope.recipient.selectedTags = [];
    }

    $scope.saveRecipients = function() {
        $scope.message.recipientIds = [];
        angular.forEach($scope.selectedTags, function(value){
            $scope.message.recipientIds.push(value.id);
        });
    }
}