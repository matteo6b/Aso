(function() {
    'use strict';

    angular
        .module('asoApp')
        .controller('EventoDeleteController',EventoDeleteController);

    EventoDeleteController.$inject = ['$uibModalInstance', 'entity', 'Evento'];

    function EventoDeleteController($uibModalInstance, entity, Evento) {
        var vm = this;
        vm.evento = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            Evento.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
