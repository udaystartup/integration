(function() {
    'use strict';

    angular
        .module('frontendApp')
        .controller('SystemCodeTableDeleteController',SystemCodeTableDeleteController);

    SystemCodeTableDeleteController.$inject = ['$uibModalInstance', 'entity', 'SystemCodeTable'];

    function SystemCodeTableDeleteController($uibModalInstance, entity, SystemCodeTable) {
        var vm = this;

        vm.systemCodeTable = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SystemCodeTable.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
