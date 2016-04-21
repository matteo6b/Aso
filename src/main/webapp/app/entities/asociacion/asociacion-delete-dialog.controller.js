(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('AsociacionDeleteController',AsociacionDeleteController);

    AsociacionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Asociacion'];

    function AsociacionDeleteController($uibModalInstance, entity, Asociacion) {
        var vm = this;
        vm.asociacion = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Asociacion.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
