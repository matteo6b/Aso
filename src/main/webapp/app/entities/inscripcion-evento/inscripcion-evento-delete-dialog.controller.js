(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('InscripcionEventoDeleteController',InscripcionEventoDeleteController);

    InscripcionEventoDeleteController.$inject = ['$uibModalInstance', 'entity', 'InscripcionEvento'];

    function InscripcionEventoDeleteController($uibModalInstance, entity, InscripcionEvento) {
        var vm = this;
        vm.inscripcionEvento = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InscripcionEvento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
