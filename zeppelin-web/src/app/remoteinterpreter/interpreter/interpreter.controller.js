/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

angular.module('zeppelinWebApp').controller('JobCtrl', JobCtrl);

JobCtrl.$inject = ['$scope', '$http', 'baseUrlSrv'];

function JobCtrl($scope, $http, baseUrlSrv) {
  $scope.init = function(jobInformation) {
    $scope.progressValue = 0;
    console.log($scope);
  };

  $scope.restartRemoteInterpreterServer = function(interpreterGroup) {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Restart the RemoteInterpreter Server?',
      callback: function(result) {
        if (result) {
          $http({
            method: 'PUT',
            url: baseUrlSrv.getRestApiBase() + '/remoteinterpreter/setting/restart/' + interpreterGroup,
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          }).then(function successCallback(response) {
            // success
            BootstrapDialog.alert({
              closable: true,
              title: 'RESTART SUCCESS',
              message: response.data.body,
            });
            $scope.$emit('RESTART');
          }, function errorCallback(errorResponse) {
            let errorText = 'SERVER ERROR';
            if (errorResponse.data.message) {
              errorText = errorResponse.data.message;
            }
            BootstrapDialog.alert({
              closable: true,
              title: 'Execution Failure',
              message: errorText,
            });
          });
        }
      },
    });
  };

  $scope.stopNotebookJob = function(notebookId) {
    BootstrapDialog.confirm({
      closable: true,
      title: '',
      message: 'Stop all paragraphs?',
      callback: function(result) {
        if (result) {
          $http({
            method: 'DELETE',
            url: baseUrlSrv.getRestApiBase() + '/notebook/job/' + notebookId,
            headers: {
              'Content-Type': 'application/x-www-form-urlencoded',
            },
          }).then(function successCallback(response) {
            // success
          }, function errorCallback(errorResponse) {
            let errorText = 'SERVER ERROR';
            if (errorResponse.data.message) {
              errorText = errorResponse.data.message;
            }
            BootstrapDialog.alert({
              closable: true,
              title: 'Stop Failure',
              message: errorText,
            });
          });
        }
      },
    });
  };
}

