'use strict';

function createBucketController($scope, $http) {
    $scope.bucketName = "";
    $scope.bucketRegion = "";
    $scope.appAlert = {};

    $scope.regions = [
        "ap-southeast-1",
        "eu-central-1",
        "ca-central-1"
    ];
    
    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'name': $scope.bucketName,
            'region': $scope.bucketRegion,
            'creationDate': ''
        };

        $http.post('/storage/create-bucket', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function uploadObjectController($scope, $http) {
    $scope.appAlert = {};
    $scope.bucketName = "";
    $scope.uploadFile = function(files) {
        $scope.appAlert = {};
        var fd = new FormData();
        var bucketName = $scope.bucketName;
        //Take the first selected file
        fd.append("file", files[0]);
        fd.append("bucketName", $scope.bucketName);

        $http.post('/storage/uploadFile', fd, {
            withCredentials: true,
            headers: {'Content-Type': undefined },
            transformRequest: angular.identity
        }).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });

    };
}

function deleteObjectController($scope, $http) {
    $scope.bucketName = "";
    $scope.fileName = "";
    $scope.appAlert = {};

    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'bucketName': $scope.bucketName,
            'fileName': $scope.fileName
        };

        $http.post('/storage/deleteFile', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
            $scope.fileName = "";
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function downloadObjectController($scope, $http) {
    $scope.bucketName = "";
    $scope.fileName = "";
    $scope.appAlert = {};

    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'bucketName': $scope.bucketName,
            'fileName': $scope.fileName
        };

        var date = new Date();
        var downloadStarts = date.getTime();
        $http.post('/storage/download', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            //$scope.appAlert.message = response.data;

            var file = new Blob([response.data], { type: 'application/binary' });
            var downloadFinishes = date.getTime();

            var fileURL = URL.createObjectURL(file);
            window.open(fileURL);
            var link = document.createElement('a');
            link.href = fileURL;
            link.download = $scope.fileName;
            link.click();
            //window.open(objectUrl);
            $scope.appAlert.message = 'Successfully downloaded file \"'+ $scope.fileName+ '\" (download time: '+ (downloadFinishes - downloadStarts) + 'ms)';
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function indexController($scope, $http) {

}

function bucketsController($scope, $http) {
    $scope.buckets = [];
    $http.get('/storage/buckets').then(function (response) {

        for (var i = 0; i < response.data.length; i++) {
            var bucket = {};
            bucket = response.data[i];
            $scope.buckets.push(bucket);
        }
    });
}


function securityGroupsController($scope, $http) {
    $scope.securityGroups = [];
    $http.get('/ec2/security-groups').then(function (response) {
        for (var i = 0; i < response.data.length; i++) {
            var group = {};
            group = response.data[i];
            $scope.securityGroups.push(group);
        }
    });
}

function keyPairsController($scope, $http) {
    $scope.keys = [];
    $http.get('/ec2/key-pairs').then(function (response) {
        for (var i = 0; i < response.data.length; i++) {
            var key = {};
            key = response.data[i];
            $scope.keys.push(key);
        }
    });
}

function createSecurityGroupController($scope, $http) {
    $scope.name = "";
    $scope.description = "";
    $scope.appAlert = {};

    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'name': $scope.name,
            'description': $scope.description
        };

        $http.post('/ec2/create-security-group', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function createInstanceController($scope, $http) {
    $scope.appAlert = {};

    $scope.imageId;
    $scope.instanceType = 't2.micro';
    $scope.keyName;
    $scope.securityGroupName;
    $scope.count = 1;

    $scope.displayKeyPairs = [];
    $scope.displaySecurityGroups = [];

    var loadKeyPairs = function () {
        $http.get('/ec2/key-pairs').then(function (response) {
            for (var i = 0; i < response.data.length; i++) {
                var key = {};
                key = response.data[i];
                $scope.displayKeyPairs.push(key.keyName);
            }
        });

    };

    var loadSecurityGroups = function () {
        $http.get('/ec2/security-groups').then(function (response) {
            for (var i = 0; i < response.data.length; i++) {
                var group = {};
                group = response.data[i];
                $scope.displaySecurityGroups.push(group.groupName);
            }
        });
    };

    loadKeyPairs();
    loadSecurityGroups();

    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'imageId' : $scope.imageId,
            'instanceType' : $scope.instanceType,
            'keyName' : $scope.keyName,
            'count' : $scope.count,
            'tagName' : 'Name',
            'tagValue' : 'anisun',
            'securityGroupName' : $scope.securityGroupName
        };

        $http.post('/ec2/create-instance', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function instancesController($scope, $http, $timeout) {
    $scope.appAlert = {};
    $scope.instances = [];
    var data = {
        'keyName': 'Name',
        'keyValue': 'anisun'
    };

    var loadPage = function () {
        $scope.instances = [];
        $http.post('/ec2/instances', data).then(function (response) {
            for (var i = 0; i < response.data.instances.length; i++) {
                var instance = {};
                instance = response.data.instances[i];
                $scope.instances.push(instance);
            }
        });
    };

    loadPage();

    $scope.startInstance = function (instanceId) {
        $scope.appAlert = {};
        var data = {
            'instanceId': instanceId
        };
        $http.post('/ec2/start-instance', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });

        $timeout(loadPage, 3000);
    };

    $scope.stopInstance = function (instanceId) {
        $scope.appAlert = {};
        var data = {
            'instanceId': instanceId
        };
        $http.post('/ec2/stop-instance', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });

        $timeout(loadPage, 3000);
    };

    $scope.rebootInstance = function (instanceId) {
        $scope.appAlert = {};
        var data = {
            'instanceId': instanceId
        };
        $http.post('/ec2/reboot-instance', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });

        $timeout(loadPage, 3000);
    };
}

function createKeyPairController($scope, $http) {
    $scope.keyName;

    $scope.submit = function () {
        $scope.appAlert = {};
        var data = {
            'keyName': $scope.keyName
        };

        $http.post('/ec2/create-keypair', data).then(function successCallback(response) {
            $scope.appAlert.type = "success";
            $scope.appAlert.message = response.data.message;
        }, function errorCallback(response) {
            $scope.appAlert.type = "error";
            $scope.appAlert.message = response.data.message;
        });
    }
}

function cloudWatchController($scope, $http, $routeParams) {
    $scope.instanceId = $routeParams.instanceId;
    $scope.avgCpuUsage;
    $scope.numberOfPacketIn;
    $scope.numberOfPacketOut;

    $scope.chartReady = "false";

    var data = {
        'instanceId': $scope.instanceId
    };

    $http.post('/ec2/cloud-watch', data).then(function successCallback(response) {
        $scope.avgCpuUsage = response.data.avgCPU;
        $scope.numberOfPacketIn = response.data.netIn;
        $scope.numberOfPacketOut = response.data.netOut;
    }, function errorCallback(response) {
        //handle error
        console.log(response);
    });
}