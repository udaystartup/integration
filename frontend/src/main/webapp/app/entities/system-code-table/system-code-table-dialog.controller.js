(function() {
    'use strict';

    angular
        .module('frontendApp')
        .controller('SystemCodeTableDialogController', SystemCodeTableDialogController);

    SystemCodeTableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SystemCodeTable'];

    function SystemCodeTableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SystemCodeTable) {
        var vm = this;

        vm.systemCodeTable = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.systemCodeTable.id !== null) {
                SystemCodeTable.update(vm.systemCodeTable, onSaveSuccess, onSaveError);
            } else {
                SystemCodeTable.save(vm.systemCodeTable, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('frontendApp:systemCodeTableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
