(function() {
    'use strict';

    angular
        .module('frontendApp')
        .controller('SystemCodeTableDetailController', SystemCodeTableDetailController);

    SystemCodeTableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SystemCodeTable'];

    function SystemCodeTableDetailController($scope, $rootScope, $stateParams, previousState, entity, SystemCodeTable) {
        var vm = this;

        vm.systemCodeTable = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('frontendApp:systemCodeTableUpdate', function(event, result) {
            vm.systemCodeTable = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
