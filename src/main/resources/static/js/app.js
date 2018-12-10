'use strict';

var app = angular.module('myApp', ['myApp.directives', 'ngRoute', 'ngResource']);

app.config(function($routeProvider, $locationProvider){
    $routeProvider
        .when('/create-bucket',{
            templateUrl: '/views/create-bucket.html',
            controller: createBucketController
        })
        .when('/buckets',{
            templateUrl: '/views/buckets.html',
            controller: bucketsController
        })
        .when('/upload-object',{
            templateUrl: '/views/upload-object.html',
            controller: uploadObjectController
        })
        .when('/download-object',{
            templateUrl: '/views/download-object.html',
            controller: downloadObjectController
        })
        .when('/delete-object',{
            templateUrl: '/views/delete-object.html',
            controller: deleteObjectController
        })
        .when('/create-security-group',{
            templateUrl: '/views/create-security-group.html',
            controller: createSecurityGroupController
        })
        .when('/security-groups',{
            templateUrl: '/views/security-groups.html',
            controller: securityGroupsController
        })
        .when('/instances',{
            templateUrl: '/views/instances.html',
            controller: instancesController
        })
        .when('/create-instance',{
            templateUrl: 'views/create-instance.html',
            controller: createInstanceController
        })
        .when('/key-pairs',{
            templateUrl: 'views/key-pairs.html',
            controller: keyPairsController
        })
        .when('/create-key-pair',{
            templateUrl: 'views/create-key-pair.html',
            controller: createKeyPairController
        })
        .when('/cloud-watch/:instanceId',{
            templateUrl: 'views/cloud-watch.html',
            controller: cloudWatchController
        })
        .otherwise(
            { redirectTo: '/'}
        );
});

app.controller('mainController', function ($scope, $http, $window) {
    $scope.appName = 'pAWS';
    $scope.currentRegion;
    $scope.regions = [];


    var loadCurrentRegion = function () {
        $http.get('/ec2/current-region').then(function (response) {
            $scope.currentRegion = response.data;
        });
    };

    var loadRegions = function () {
        $http.get('/ec2/regions').then(function (response) {
            for (var i = 0; i < response.data.length; i++) {
                $scope.regions.push(response.data[i].regionName) ;
            }
        });
    };

    $scope.selectRegion = function (region) {
        $scope.appAlert = {};
        var data = {
            'region' : region
        };

        $http.post('/ec2/change-region', data).then(function successCallback(response) {
            loadCurrentRegion();
            $scope.currentRegion = data.region;
            $scope.$apply(); // launch digest;
        }, function errorCallback(response) {
        });

        $window.location.reload();
    };

    loadRegions();
    loadCurrentRegion();
});
