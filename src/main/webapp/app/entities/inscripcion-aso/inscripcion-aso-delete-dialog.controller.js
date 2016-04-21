(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionAsoDeleteController',InscripcionAsoDeleteController);

    InscripcionAsoDeleteController.$inject = ['$uibModalInstance', 'entity', 'InscripcionAso'];

    function InscripcionAsoDeleteController($uibModalInstance, entity, InscripcionAso) {
        var vm = this;
        vm.inscripcionAso = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InscripcionAso.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
