var app = angular.module('pushAdmin.services', []);

app.service('applicationService', function ($http, $rootScope, $location) {
    this.saveCreateApp = function (files, content, iosDetails){
        var fd = new FormData();
        if(files != undefined){
            fd.append('file',files);
        }
        fd.append('content', content);
        fd.append('ios-details',iosDetails );

        return $http.post('/push/application/multi', fd, { transformRequest: angular.identity,
            headers: { 'Content-Type': undefined }
        });
    };

    this.update = function(application){
        return $http({method: 'PUT', url: '/push/application',
            headers: {'Content-Type':'application/json'},
            data: getPopulatedApplicationData(application)
        });
    }

    this.deleteApp = function(appid){
        return $http({method: 'DELETE', url: 'push/application/' + appid});
    }

    this.getApps = function(appId) {
        $http({method: 'GET', url:'/push/applications', cache:false, headers: { 'If-Modified-Since': "0" }}).success(function (response) {
            $rootScope.applications = response;
            if (appId == 0){
                $rootScope.currentApplication = $rootScope.applications[appId];
            }
            else if (appId == undefined){
                $rootScope.currentApplication = $rootScope.applications[$rootScope.applications.length - 1];
            }
            else {
                angular.forEach($rootScope.applications, function(app){
                    if (appId == app.id){
                        $rootScope.currentApplication = app;
                    }
                });
            }
            $rootScope.applications.push({'id':-1, 'name' : 'Create Application ...'});
            return $location.path('/app/details');
        });
    }
});

app.service('recipientService', function($http) {
    this.searchRecipients = function(application, recipient){
        return $http({method: 'GET', url: '/push/tags/' + application.id + '/' + recipient.searchKey
        	
        });
    }
});

app.service('messageService', function($http) {
    this.sendMessage = function(application, message){
        return $http({method: 'POST', url: '/push/message/' + application.id,
            data: {"title": message.title, "message" : message.details,
                "tagIds": message.recipientIds, "channelTypes": ["IOS"] }
        });
    }
});

app.service('dialogService', function($dialog){
    this.showDeleteDialog = function(title, message){
        var btns = [{result:false, label: 'No'}, {result:true, label: 'Yes', cssClass: 'btn btn-warning'}];
        return $dialog.messageBox(title, message, btns).open();
    }

    this.showMessageDialog = function(title, message){
        var btns = [{result:true, label: 'Ok', cssClass: 'btn btn-warning'}];
        return $dialog.messageBox(title, message, btns).open();
    }
});

getPopulatedApplicationData = function(application){
    return {"id":application.id, "name": application.name, "mode": application.mode, "token":null, "dateCreated":null, "icon":null, "dateDeleted":null}
}
